package com.example.vendingmachine;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface VendingMachineApi {

    @GET("items")
    Call<List<Item>> getItems();

    @POST("money/{money}/item/{item}")
    Call<Change> getChange(@Path("money") String money, @Path("item") int itemId);

}
