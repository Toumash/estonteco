package com.estonteco.androidapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.estonteco.androidapp.R;
import com.estonteco.androidapp.model.api.Reservation;
import com.estonteco.androidapp.network.ReservationService;
import com.estonteco.androidapp.network.RetrofitClientInstance;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ReservationService service = RetrofitClientInstance.getRetrofitInstance().create(ReservationService.class);
        Call<List<Reservation>> call = service.getAllReservations();
        call.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.body() != null) {
                    List<Reservation> reservations = response.body();
                    for (Reservation r : reservations) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(r.DlugoscGeo, r.SzerokoscGeo)).title("xd"));
                    }

                    // animate camera to last entry
                    Reservation last = reservations.get(reservations.size() - 1);
                    LatLng latlong = new LatLng(last.DlugoscGeo, last.SzerokoscGeo);
                    CameraPosition position = CameraPosition.builder().target(latlong).zoom(13).build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(position);
                    mMap.animateCamera(cameraUpdate);
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                //progressDoalog.dismiss();
                Toast.makeText(MapsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
