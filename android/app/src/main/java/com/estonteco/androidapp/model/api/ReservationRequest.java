package com.estonteco.androidapp.model.api;

import com.google.gson.annotations.SerializedName;

public class ReservationRequest {
    @SerializedName("slotId")
    public int slotId;
    @SerializedName("userId")
    public int userId = 0;
}
