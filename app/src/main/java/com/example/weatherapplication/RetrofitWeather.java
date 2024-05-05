package com.example.weatherapplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitWeather  {//write all codes again

    private static Retrofit retrofit;
    //search in google
    //making it singleton
    //sir ye ho gya
    public static Retrofit getRetrofitInstance(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}











