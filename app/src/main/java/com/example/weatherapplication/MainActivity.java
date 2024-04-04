package com.example.weatherapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import kotlin.ranges.ClosedFloatingPointRange;

public class MainActivity extends AppCompatActivity {
private TextView city,temperature,weatherCondition,humidity,maxTemp,minTemp,pressure,wind;
private ImageView imageview;
private FloatingActionButton fab;

  LocationManager locationManager;

  LocationListener locationListener;
  double lat,lon;
Button buttonSearch;
private EditText editText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.textViewcity);
       temperature = findViewById(R.id.textViewTemp);
       weatherCondition = findViewById(R.id.textViewWeathCondition);
        humidity = findViewById(R.id.humidity);
        maxTemp= findViewById(R.id.maxTemp);
      minTemp = findViewById(R.id.minTemp);
        pressure = findViewById(R.id.pressure);
        wind = findViewById(R.id.wind);
        imageview = findViewById(R.id.imageViewWeather);
        editText = findViewById(R.id.EditTextCityName);
        buttonSearch = findViewById(R.id.search);
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
       locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
       locationListener = new LocationListener() {
           @Override
           public void onProviderDisabled(@NonNull String provider) {
//               LocationListener.super.onProviderDisabled(provider);
           }

           @Override
           public void onLocationChanged(@NonNull Location location) {
         lat = location.getLatitude();
         lon = location.getLongitude();
         Log.e("lat :",String.valueOf(lat));
         Log.e("lon :",String.valueOf(lon));
           }
       };
       if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
       != PackageManager.PERMISSION_GRANTED)
       {
           ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                   ,1);
       }
       else {
           locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,50,locationListener);
       }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1  && permissions.length > 0 && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,50,locationListener);
        }
    }


}