package com.example.weatherapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("weather?&appid=31de9191de02ad2d3f1b1e943eb2245a&units=metric")
    Call<OpenWeatherMap>getWeatherWithLocation(@Query("lat")double lat,@Query("lon")double lon);

    @GET("weather?&appid=31de9191de02ad2d3f1b1e943eb2245a&units=metric")
    Call<OpenWeatherMap>getWeatherWithCityName(@Query("q")String name);
}
