package com.example.vendingmachine;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface VendingMachineApi {

    @GET("items")
    Call<List<Item>> getItems();

}
