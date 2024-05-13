package com.example.weatherapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class WeatherSharedPreference{

    private static final String PREF_NAME = "WeatherPref";
    private static final String KEY_LAST_WEATHER = "lastWeather";

    public static void saveLastWeather(Context context, String lastWeather) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LAST_WEATHER, lastWeather);
        editor.apply();
    }

    public static String getLastWeather(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LAST_WEATHER, "");
    }
}

