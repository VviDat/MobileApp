package com.example.bt4.API;

import com.example.bt4.Model.Civic;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
//https://civicinfo.googleapis.com/civicinfo/v2/representatives?address=Chicago&key=AIzaSyBbygnIEHrjTp90nMXmpx32iWPj868Ko5A

public interface GoogleAPI {
    final static String APIKey = "AIzaSyBbygnIEHrjTp90nMXmpx32iWPj868Ko5A";
    String address = null;

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    GoogleAPI googleApi = new Retrofit.Builder()
            .baseUrl("https://civicinfo.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(GoogleAPI.class);

    @GET("civicinfo/v2/representatives")
    Call<Civic> getCivic(@Query("address") String address, @Query("key") String key);


}
