package com.estonteco.androidapp.network;

import com.estonteco.androidapp.model.api.ReservationAdded;
import com.estonteco.androidapp.model.api.ReservationRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ReservationService {

    @POST("reservation/add")
    @Headers( "Content-Type: application/json" )
    Call<ReservationAdded> book(@Body ReservationRequest request);
}