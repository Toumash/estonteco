package com.estonteco.androidapp.network;

import com.estonteco.androidapp.model.api.ParkingSlot;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SlotService {

    @GET("status/get")
    Call<List<ParkingSlot>> getAllParkingSlots();
}
