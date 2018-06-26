package com.estonteco.androidapp.network;

public class NetworkServiceFactory {
    public static SlotService createSlotService() {
        return RetrofitClientInstance.getRetrofitInstance().create(SlotService.class);
    }
}
