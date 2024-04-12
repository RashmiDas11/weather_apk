package com.example.weatherapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class weatherActivity extends AppCompatActivity {
    private TextView city2,temperature2,weatherCondition2,humidity2,maxTemp2,minTemp2,pressure2,wind2;
    private ImageView imageview2;
    Button buttonSearch;
    private EditText editTextWeather;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        city2 = findViewById(R.id.textView2);
        temperature2 = findViewById(R.id.textViewTemp2);
        weatherCondition2 = findViewById(R.id.textViewWeathCondition2);
        humidity2 = findViewById(R.id.humidity2);
        maxTemp2 = findViewById(R.id.maxTemp2);
        minTemp2 = findViewById(R.id.minTemp2);
        pressure2 = findViewById(R.id.pressure2);
        wind2 = findViewById(R.id.wind2);
        imageview2 = findViewById(R.id.imageview2);
        editTextWeather = findViewById(R.id.EditTextCityName);
        buttonSearch = findViewById(R.id.search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName= editTextWeather.getText().toString();
                editTextWeather.setText("");
                getWeatherData(cityName);
            }
        });

    }
    public void getWeatherData(String name){
        WeatherAPI weatherAPI = RetrofitWeather.getRetrofitInstance().create(WeatherAPI.class);
        Call<OpenWeatherMap> call = weatherAPI.getWeatherWithCityName(name);
        call.enqueue(new Callback<OpenWeatherMap>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<OpenWeatherMap> call, @NonNull Response<OpenWeatherMap> response) {
//                assert response.body() != null;
                if (response.isSuccessful() ){
                    city2.setText(response.body().getName() + " , " + response.body().getSys().getCountry());
                    temperature2.setText(response.body().getMain().getTemp() + " °C");
                    weatherCondition2.setText(response.body().getWeather().get(0).getDescription());
                    humidity2.setText(" : " + response.body().getMain().getHumidity() + " %");
                    maxTemp2.setText(": " + response.body().getMain().getTempMax() + "°C");
                    minTemp2.setText(": " + response.body().getMain().getTempMin() + " °C");
                    pressure2.setText(": " + response.body().getMain().getPressure());
                    wind2.setText(": " + response.body().getWind().getSpeed());


                String iconCode =response.body().getWeather().get(0).getIcon();
//                String iconCode = null;
//                if (response != null && response.body() != null && !response.body().getWeather().isEmpty()) {
//                    iconCode = response.body().getWeather().get(0).getIcon();
//                }

                Picasso.get().load(" https://openweathermap.org/img/wn/"+iconCode+"@2x.png")
                        .placeholder(R.drawable.weather).into(imageview2);
            }
            else{
                    Toast.makeText(weatherActivity.this, "city not found please try again", Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(@NonNull Call<OpenWeatherMap> call, @NonNull Throwable throwable) {

            }
        });
    }

}
