package com.example.weatherapplication;

import com.google.gson.Gson;

public class WeatherInfoModel {


    String city;
    double temperature;
    double feels_like;
    String WeatherCondition;
    Integer humidity;

    public double getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(double feels_like) {
        this.feels_like = feels_like;
    }

    String icon;
    Double wind;
    Double maxTep;
    Double minTemp;
    Integer pressure;


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWeatherCondition() {
        return WeatherCondition;
    }

    public Double getTempMax() {
        return maxTep;
    }

    public void setWeatherCondition(String weatherCondition) {
        WeatherCondition = weatherCondition;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Double getMaxTep() {
        return maxTep;
    }

    public void setMaxTep(Double maxTep) {
        this.maxTep = maxTep;
    }

    public Double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Double minTemp) {
        this.minTemp = minTemp;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Double getWind() {
        return wind;
    }

    public void setWind(Double wind) {
        this.wind = wind;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public static WeatherInfoModel fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, WeatherInfoModel.class);
    }
    // Method to serialize WeatherInfoModel object into JSON
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }



}