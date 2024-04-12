package com.example.weatherapplication;

public class WeatherInfoModel {
    public WeatherInfoModel(){

    }
    static String city;
    static double temperature;
    static String WeatherCondition;
    Integer humidity;
    double maxTep;
    double minTemp;
    int pressure;

    public static String getWeatherCondition() {
        return WeatherCondition;
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

    public double getMaxTep() {
        return maxTep;
    }

    public void setMaxTep(double maxTep) {
        this.maxTep = maxTep;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public Double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    double wind;
    public static String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static Double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
