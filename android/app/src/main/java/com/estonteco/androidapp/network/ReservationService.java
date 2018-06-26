package com.estonteco.androidapp.network;

import com.estonteco.androidapp.model.api.ParkingSlot;
import com.estonteco.androidapp.model.api.ReservationAdded;
import com.estonteco.androidapp.model.api.ReservationRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ReservationService {

    @GET("/reservation/get")
    Call<List<ParkingSlot>> getAllParkingSlots();

    @GET("/reservation/add")
    Call<ReservationAdded> book(ReservationRequest request);
}