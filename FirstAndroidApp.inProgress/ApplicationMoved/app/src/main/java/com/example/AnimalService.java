package com.example;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AnimalService {
    @GET("/RandomAnimal")
    public Call<Animal> getAnimal(

    );
}
