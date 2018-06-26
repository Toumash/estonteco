package com.estonteco.androidapp.model.api;

import com.google.gson.annotations.SerializedName;

public class ParkingSlot {
    @SerializedName("id")
    public int Id;
    @SerializedName("idParkingu")
    public int IdParkingu;
    @SerializedName("miejsce")
    public int Miejsce;
    @SerializedName("czyZarezerwowane")
    public boolean CzyZarezerwowane;
    @SerializedName("czyZajete")
    public boolean CzyZajete;
    @SerializedName("dlugoscGeo")
    public double DlugoscGeo;
    @SerializedName("szerokoscGeo")
    public double SzerokoscGeo;
}
