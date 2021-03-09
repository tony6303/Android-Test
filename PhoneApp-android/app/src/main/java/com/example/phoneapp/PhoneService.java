package com.example.phoneapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PhoneService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://113.198.238.82:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("phone")
    Call<CMRespDto<List<Phone>>> findAll();

    @POST("phone")
    Call<CMRespDto<Phone>> save(@Body Phone phone);

    @DELETE("phone/{id}")
    Call<Void> deleteById(@Path("id") long id);

    @PUT("phone/{id}")
    Call<CMRespDto<Phone>> update(@Path("id") long id,@Body Phone phone);
}
