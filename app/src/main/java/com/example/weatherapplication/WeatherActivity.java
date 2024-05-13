package com.example.weatherapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

public class WeatherActivity extends AppCompatActivity {
    private TextView city2,temperature2,weatherCondition2,humidity2,maxTemp2,minTemp2,pressure2,wind2,rain3;
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
        rain3=findViewById(R.id.rain3);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName= editTextWeather.getText().toString();
                editTextWeather.setText("");
                getWeatherData(cityName);
            }
        });

    }
    //method to get weather data by city name getting data using retrofit call


    public void getWeatherData(String name){
        if (isNetworkAvailable()) {
            WeatherAPI weatherAPI = RetrofitWeather.getRetrofitInstance().create(WeatherAPI.class);
            Call<OpenWeatherMap> call = weatherAPI.getWeatherWithCityName(name);
            call.enqueue(new Callback<OpenWeatherMap>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<OpenWeatherMap> call, @NonNull Response<OpenWeatherMap> response) {
                    if (response.isSuccessful()) {
                        //null check is missing//sir maine abhi apke samne lgaya tha new mei isme reh gya
                        city2.setText(response.body().getName() + " , " + response.body().getSys().getCountry());
                        temperature2.setText(response.body().getMain().getTemp() + " °C");
                        weatherCondition2.setText(response.body().getWeather().get(0).getDescription());
                        humidity2.setText( response.body().getMain().getHumidity() + " %");
                        maxTemp2.setText( response.body().getMain().getTempMax() + "°C");
                        minTemp2.setText( response.body().getMain().getTempMin() + " °C");
                        pressure2.setText( response.body().getMain().getPressure() + "Pa");
                        wind2.setText(  response.body().getWind().getSpeed() + "Km/hr");
                        rain3.setText(String.valueOf(response.body().getMain().getFeelsLike() + "%"));
//                        String iconCode = response.body().getWeather().get(0).getIcon();
                        String iconCodeSearch = response.body().getWeather().get(0).getIcon();
                        Picasso.get().load("  https://openweathermap.org/img/wn/" +iconCodeSearch+ ".png")
                                .placeholder(R.drawable.iconw2).into(imageview2);

                    } else {
                        Toast.makeText(WeatherActivity.this, "city not found please try again", Toast.LENGTH_LONG).show();
                    }
                }


                @Override
                public void onFailure(@NonNull Call<OpenWeatherMap> call, @NonNull Throwable throwable) {

                }

            });
        }
        else {
            Toast.makeText(this, "Network Not Available Turn On Network To Continue", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isNetworkAvailable () {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
