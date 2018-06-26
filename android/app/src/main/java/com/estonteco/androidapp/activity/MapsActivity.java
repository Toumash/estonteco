package com.estonteco.androidapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.estonteco.androidapp.R;
import com.estonteco.androidapp.model.api.ParkingSlot;
import com.estonteco.androidapp.model.api.ReservationAdded;
import com.estonteco.androidapp.model.api.ReservationRequest;
import com.estonteco.androidapp.network.ReservationService;
import com.estonteco.androidapp.network.RetrofitClientInstance;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    Map<Marker, ParkingSlot> markerMap;
    ReservationService reservationService = RetrofitClientInstance.getRetrofitInstance().create(ReservationService.class);
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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Call<List<ParkingSlot>> call = reservationService.getAllParkingSlots();
        call.enqueue(new Callback<List<ParkingSlot>>() {
            @Override
            public void onResponse(Call<List<ParkingSlot>> call, Response<List<ParkingSlot>> response) {
                mParkingSlots = response.body();
                if (mParkingSlots != null) {
                    for (ParkingSlot r : mParkingSlots) {
                        Marker marker = mMap.addMarker(
                                new MarkerOptions()
                                        .position(new LatLng(r.DlugoscGeo, r.SzerokoscGeo))
                                        .title("xd")
                        );
                        markerMap.put(marker, r);
                    }

                    // animate camera to last entry
                    ParkingSlot last = mParkingSlots.get(mParkingSlots.size() - 1);
                    LatLng latlong = new LatLng(last.DlugoscGeo, last.SzerokoscGeo);
                    CameraPosition position = CameraPosition.builder().target(latlong).zoom(13).build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(position);
                    mMap.animateCamera(cameraUpdate);
                }
            }

            @Override
            public void onFailure(Call<List<ParkingSlot>> call, Throwable t) {
                //progressDoalog.dismiss();
                Toast.makeText(MapsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        ParkingSlot selectedParkingSlot = markerMap.get(marker);
        ReservationRequest request = new ReservationRequest();
        request.reservationId = selectedParkingSlot.Id;
        reservationService.book(request)
                .enqueue(new Callback<ReservationAdded>() {
                    @Override
                    public void onResponse(Call<ReservationAdded> call, Response<ReservationAdded> response) {
                        Toast.makeText(MapsActivity.this, "Reservation made successfully. Reservation ID=" + response.body().reservationId, Toast.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onFailure(Call<ReservationAdded> call, Throwable t) {
                        Toast.makeText(MapsActivity.this, "ERROR", Toast.LENGTH_LONG)
                                .show();
                    }
                });
        return false;
    }
}
