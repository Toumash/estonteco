package com.estonteco.androidapp.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ReservationAdded {
    @SerializedName("slotId")
    public int reservationId;
    @SerializedName("expirationDate")
    public Date expirationDate;
}
