package com.estonteco.androidapp.network;

import com.estonteco.androidapp.model.api.Reservation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ReservationService {

    @GET("/reservation/get")
    Call<List<Reservation>> getAllReservations();
}