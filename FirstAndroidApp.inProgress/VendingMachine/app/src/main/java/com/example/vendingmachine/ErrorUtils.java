package com.example.vendingmachine;

import android.graphics.ColorSpace;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ErrorUtils {

    public static ModelError parseError (Response<?> response) {
        Converter<ResponseBody, ModelError> converter = ServiceGenerator.retrofit.responseBodyConverter(ModelError.class, new Annotation[0] );
;
        ModelError error;
        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ModelError();
        }

        return error;
    }
}
