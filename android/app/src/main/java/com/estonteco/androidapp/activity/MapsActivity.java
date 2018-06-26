package com.estonteco.androidapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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

@SuppressWarnings("NullableProblems")
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    Map<Marker, ParkingSlot> markerMap;
    ReservationService reservationService = NetworkServiceFactory.createReservationService();
    SlotService slotService = NetworkServiceFactory.createSlotService();
    ReservationAdded mReservationAdded;
    View l_main;
    CardView cardView;
    TextView tv_card_time;
    GoogleMap mMap;
    List<ParkingSlot> mParkingSlots;
    Context context;
    Marker lastMarkerClicked = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        bindViews();
        context = this;
    }

    void bindViews() {
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
                if (mParkingSlots == null)
                    return;

                for (ParkingSlot r : mParkingSlots) {
                    Marker marker = mMap.addMarker(createMarkerFrom(r));
                    markerMap.put(marker, r);
                }

                if (mParkingSlots.isEmpty())
                    return;

                // animate camera to last entry
                ParkingSlot last = mParkingSlots.get(mParkingSlots.size() - 1);
                LatLng latlong = new LatLng(last.DlugoscGeo, last.SzerokoscGeo);
                CameraPosition position = CameraPosition.builder().target(latlong).zoom(13).build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(position);
                mMap.animateCamera(cameraUpdate);
            }

            @Override
            public void onFailure(Call<List<ParkingSlot>> call, Throwable t) {
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
        mMap.setOnMarkerClickListener(this);
    }

    @NonNull
    private MarkerOptions createMarkerFrom(ParkingSlot r) {
        String title = getTitleFor(r);
        String snippet = String.format(getString(R.string.parking_no), r.IdParkingu);
        if (!r.CzyZarezerwowane && !r.CzyZajete) {
            snippet = "Tapnij, aby zarezerwować";
        }
        return new MarkerOptions()
                .position(new LatLng(r.DlugoscGeo, r.SzerokoscGeo))
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker(getColorFor(r)));
    }

    private float getColorFor(ParkingSlot r) {
        if (r.CzyZajete) return BitmapDescriptorFactory.HUE_ROSE;
        else if (r.CzyZarezerwowane) return BitmapDescriptorFactory.HUE_ROSE;
        return BitmapDescriptorFactory.HUE_GREEN;
    }

    @NonNull
    private String getTitleFor(ParkingSlot r) {
        if (r.CzyZajete) return "Zajęte miejsce";
        else if (r.CzyZarezerwowane) return "Zarezerwowane";
        return "Wolne miejsce";
    }

    public AlertDialog.Builder getAlertBuilder() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        return builder;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        final ParkingSlot selectedParkingSlot = markerMap.get(marker);

        if (selectedParkingSlot.CzyZajete || selectedParkingSlot.CzyZarezerwowane) {
            showSnackbar(l_main, "To miejsce jest już zajęte", Snackbar.LENGTH_SHORT);
            return false;
        }
        if (lastMarkerClicked == null) {
            lastMarkerClicked = marker;
            return false;
        }
        lastMarkerClicked = marker;

        getAlertBuilder().setTitle("Rezerwacja miejsca")
                .setMessage("Czy chcesz zarezerwować miejsce parkingowe #" + selectedParkingSlot.Id)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ReservationRequest request = new ReservationRequest();
                        request.slotId = selectedParkingSlot.Id;
                        reservationService.book(request).enqueue(new Callback<ReservationAdded>() {
                            @Override
                            public void onResponse(Call<ReservationAdded> call, Response<ReservationAdded> response) {
                                mReservationAdded = response.body();
                                if (mReservationAdded != null) {
                                    showSnackbar(l_main, getString(R.string.reservation_successfull, mReservationAdded.reservationId),
                                            Snackbar.LENGTH_LONG);
                                    cardView.setVisibility(View.VISIBLE);
                                    startReservationTimer(selectedParkingSlot);
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

    private long limit(long value, long min) {
        if (value < min)
            return min;
        return value;
    }


    private void startReservationTimer(final ParkingSlot selectedParkingSlot) {
        final Handler handler = new Handler();

        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        long expirationDate = mReservationAdded.expirationDate.getTime();
                        long timeLeft = limit((expirationDate - (new Date()).getTime()), 0);
                        Date date = new Date();
                        date.setTime(timeLeft);
                        tv_card_time.setText(date.getMinutes() + " : " + date.getSeconds());
                        if (timeLeft == 0) {
                            getExpiredReservationAlertDialog(selectedParkingSlot);
                        }
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timertask, 0, 1000);
    }

    private void getExpiredReservationAlertDialog(ParkingSlot selectedParkingSlot) {
        getAlertBuilder().setTitle("Rezerwacja wygasła!")
                .setMessage("Twoja rezerwacja miejsca #" + selectedParkingSlot.Id + "już wygasła")
                .setPositiveButton(android.R.string.ok, null).show();
    }

    void showSnackbar(View view, String message, int duration) {
        Snackbar.make(view, message, duration).show();
    }
}
