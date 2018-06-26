package com.estonteco.androidapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.estonteco.androidapp.R;
import com.estonteco.androidapp.model.api.ParkingSlot;
import com.estonteco.androidapp.model.api.ReservationAdded;
import com.estonteco.androidapp.model.api.ReservationRequest;
import com.estonteco.androidapp.network.NetworkServiceFactory;
import com.estonteco.androidapp.network.ReservationService;
import com.estonteco.androidapp.network.RetrofitClientInstance;
import com.estonteco.androidapp.network.SlotService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    Map<Marker, ParkingSlot> markerMap;
    ReservationService reservationService = RetrofitClientInstance.getRetrofitInstance().create(ReservationService.class);
    SlotService slotService = NetworkServiceFactory.createSlotService();
    ReservationAdded mReservationAdded;
    View l_main;
    Snackbar xd;
    CardView cardView;
    TextView tv_card_time;
    private GoogleMap mMap;
    private List<ParkingSlot> mParkingSlots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        markerMap = new HashMap<>();
        l_main = findViewById(R.id.drawer_layout);
        cardView = findViewById(R.id.maps_cv);
        tv_card_time = findViewById(R.id.tv_card_time);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Call<List<ParkingSlot>> call = slotService.getAllParkingSlots();
        call.enqueue(new Callback<List<ParkingSlot>>() {
            @Override
            public void onResponse(Call<List<ParkingSlot>> call, Response<List<ParkingSlot>> response) {
                mParkingSlots = response.body();
                if (mParkingSlots != null) {
                    for (ParkingSlot r : mParkingSlots) {
                        Marker marker = mMap.addMarker(
                                new MarkerOptions()
                                        .position(new LatLng(r.DlugoscGeo, r.SzerokoscGeo))
                                        .title(String.valueOf(r.Miejsce))
                                        .snippet(r.CzyZajete ? "Zajęte miejsce" : "Wolne miejsce")
                                        .icon(BitmapDescriptorFactory.defaultMarker(r.CzyZajete ? BitmapDescriptorFactory.HUE_RED : BitmapDescriptorFactory.HUE_AZURE))
                        );
                        markerMap.put(marker, r);
                    }

                    if (!mParkingSlots.isEmpty()) {
                        // animate camera to last entry
                        ParkingSlot last = mParkingSlots.get(mParkingSlots.size() - 1);
                        LatLng latlong = new LatLng(last.DlugoscGeo, last.SzerokoscGeo);
                        CameraPosition position = CameraPosition.builder().target(latlong).zoom(13).build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(position);
                        mMap.animateCamera(cameraUpdate);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ParkingSlot>> call, Throwable t) {
                //progressDialog.dismiss();
                Toast.makeText(MapsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        final ParkingSlot selectedParkingSlot = markerMap.get(marker);
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(MapsActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(MapsActivity.this);
        }
        builder.setTitle("Rezerwacja miejsca")
                .setMessage("Czy chcesz zarezerwować miejsce parkingowe #" + selectedParkingSlot.Id)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ReservationRequest request = new ReservationRequest();
                        request.slotId = selectedParkingSlot.Id;
                        reservationService.book(request)
                                .enqueue(new Callback<ReservationAdded>() {
                                    @Override
                                    public void onResponse(Call<ReservationAdded> call, Response<ReservationAdded> response) {
                                        mReservationAdded = response.body();
                                        if (mReservationAdded != null) {
                                            showSnackbar(l_main, "Reservation made successfully. Reservation ID=" + mReservationAdded.reservationId, Snackbar.LENGTH_LONG);

                                            cardView.setVisibility(View.VISIBLE);

                                            final Handler handler = new Handler();

                                            TimerTask timertask = new TimerTask() {
                                                @Override
                                                public void run() {
                                                    handler.post(new Runnable() {
                                                        public void run() {
                                                            long timeLeft = mReservationAdded.expirationDate.getTime() - (new java.util.Date()).getTime();
                                                            Date date = new Date();
                                                            date.setTime(timeLeft);
                                                            tv_card_time.setText(date.getMinutes() + " : " + date.getSeconds());
                                                        }
                                                    });
                                                }
                                            };
                                            Timer timer = new Timer();
                                            timer.schedule(timertask, 0, 1000);
                                        } else {
                                            showSnackbar(l_main, "ERROR", Snackbar.LENGTH_SHORT);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ReservationAdded> call, Throwable t) {
                                        showSnackbar(l_main, "ERROR", Snackbar.LENGTH_SHORT);
                                    }
                                });
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info).show();

        return false;
    }

    public void showSnackbar(View view, String message, int duration) {
        Snackbar.make(view, message, duration).show();
    }
}
