package com.example.weatherapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FastItemAdapter<AbstractItem> fastItemAdapter;
    LocationManager locationManager;
    LocationListener locationListener;
    LocationInfo currentLocationInfo;
    CurrentItemModel currentItemModel;
    public static WeatherInfoModel weatherInfoModel;


    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public static final String FIRST_TIME_OPEN_KEY = "first_time_open_key";
    public static final String LAST_SAVED_LATITUDE_KEY = "last_saved_latitude_key";
    public static final String LAST_SAVED_LONGITUDE_KEY = "last_saved_longitude_key";
    public static final String LOCATION_INTENT_KEY = "location_intent_key";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        pref = getApplicationContext().getSharedPreferences("my_pref", 0);
        editor = pref.edit();
        if (isNetworkAvailable()) {
            getWeatherData(currentLocationInfo, weatherInfoModel1 -> {
            });


        } else {
            Toast.makeText(this, "Netwok Not Available", Toast.LENGTH_SHORT).show();
        }

//    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//    builder.setTitle("Network Not Available");
//    builder.setMessage("Please enable network to fetch weather data.");
//    builder.setPositiveButton("Open Settings", (dialog, which) -> {
//        Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
//        startActivity(intent);
//    });
//    builder.setNegativeButton("Cancel", (dialog, which) -> {
//        Toast.makeText(this, "Network is required", Toast.LENGTH_SHORT).show();
//        finish();
//    });
//            builder.setOnCancelListener(dialog -> {
//                finish();
//            });
//    builder.show();


        boolean isFirstTime = pref.getBoolean(FIRST_TIME_OPEN_KEY, true);

        double longitude = Double.parseDouble(pref.getString(LAST_SAVED_LONGITUDE_KEY, "0"));
        double latitude = Double.parseDouble(pref.getString(LAST_SAVED_LATITUDE_KEY, "0"));


        if (isFirstTime) {
            editor.putBoolean(FIRST_TIME_OPEN_KEY, false);

            ArrayList<LocationInfo> locations = new ArrayList<>();
            LocationInfo mumbaiLocation = new LocationInfo(19.03681, 73.01582);
            LocationInfo bengalLocation = new LocationInfo(11.059821, 78.387451);
            LocationInfo delhiLocation = new LocationInfo(28.7041, 77.1025);
            LocationInfo odishaLocation = new LocationInfo(20.940920, 84.803467);
            LocationInfo haryanaLocation = new LocationInfo(29.238478, 76.431885);
            LocationInfo uttarPradeshLocation = new LocationInfo(28.207609, 79.826660);
            LocationInfo rajasthanLocation = new LocationInfo(27.391277, 73.432617);
            LocationInfo gujaratLocation = new LocationInfo(22.309425, 72.136230);
            LocationInfo himachalPradeshLocation = new LocationInfo(32.084206, 77.571167);

            locations.add(mumbaiLocation);
            locations.add(delhiLocation);
            locations.add(bengalLocation);
            locations.add(odishaLocation);
            locations.add(haryanaLocation);
            locations.add(uttarPradeshLocation);
            locations.add(rajasthanLocation);
            locations.add(gujaratLocation);
            locations.add(himachalPradeshLocation);


            for (LocationInfo locationInfo : locations) {

                databaseHelper.insertIntoLocationTable(this, locationInfo);
            }

        }
        if (latitude != 0 && longitude != 0) {
            currentLocationInfo = new LocationInfo(longitude, latitude);


        } else {
            currentLocationInfo = new LocationInfo(0.0, 0.0);
        }


        initIds();


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        locationListener = new LocationListener() {
            @Override
            public void onProviderDisabled(@NonNull String provider) {
                LocationListener.super.onProviderDisabled(provider);
            }


            @Override
            public void onProviderEnabled(@NonNull String provider) {
                LocationListener.super.onProviderEnabled(provider);
            }

            @Override
            public void onLocationChanged(@NonNull Location location) {
                currentLocationInfo = new LocationInfo(location.getLatitude(), location.getLongitude());

                if (currentLocationInfo.latitude != 0 && currentLocationInfo.longitude != 0) {
                    editor.putString(LAST_SAVED_LONGITUDE_KEY, String.valueOf(currentLocationInfo.longitude));
                    editor.putString(LAST_SAVED_LATITUDE_KEY, String.valueOf(currentLocationInfo.latitude));

                    CurrentItemModel currentItemModel = (CurrentItemModel) fastItemAdapter.getAdapterItem(0);
                    currentItemModel.locationInfo = currentLocationInfo;
                    fastItemAdapter.notifyAdapterItemChanged(0);

                }

            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                LocationListener.super.onStatusChanged(provider, status, extras);
            }
        };


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, locationListener);

        }
    }


    private void saveWeatherData(WeatherInfoModel weatherInfoModel) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("city", weatherInfoModel.getCity());
        editor.putString("temperature", String.valueOf(weatherInfoModel.getTemperature()));
        editor.putString("weatherCondition", weatherInfoModel.getWeatherCondition());
        editor.putString("humidity", String.valueOf(weatherInfoModel.getHumidity()));
        editor.putString("maxTemp", String.valueOf(weatherInfoModel.getMaxTep()));
        editor.putString("minTemp", String.valueOf(weatherInfoModel.getMinTemp()));
        editor.putString("pressure", String.valueOf(weatherInfoModel.getPressure()));
        editor.putString("wind", String.valueOf(weatherInfoModel.getWind()));
        editor.putString("feels_like", String.valueOf(weatherInfoModel.getFeels_like()));

        editor.apply();
    }


    private void initIds() {

        recyclerView = findViewById(R.id.recyclerId);
        fastItemAdapter = new FastItemAdapter<>();

        recyclerView.setAdapter(fastItemAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        fastItemAdapter.clear();
        ArrayList<LocationInfo> locationInfos = getListOfLocations();
        fastItemAdapter.clear();
        //for current weather

        fastItemAdapter.add(new CurrentItemModel(currentLocationInfo, pref));
        if (locationInfos != null) {
            for (LocationInfo locationInfo : locationInfos) {
                fastItemAdapter.add(new WeatherItemModel(locationInfo));

            }
        }

        fastItemAdapter.notifyAdapterDataSetChanged();


        fastItemAdapter.withOnClickListener((v, adapter, item, position) -> {
//           if (isNetworkAvailable()) {
            if (item instanceof WeatherItemModel)  {
//                Toast.makeText(MainActivity.this, "Clicked item at position: " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ItemInfo.class);
                intent.putExtra(LOCATION_INTENT_KEY, ((WeatherItemModel) item).locationInfo);
                startActivity(intent);
            }

            return true;
        });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && permissions.length > 0 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, locationListener);

        }

    }

    public ArrayList<LocationInfo> getListOfLocations() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor res = databaseHelper.getAllData();
        if (res.getCount() == 0) {
            return null;
        }
        ArrayList<LocationInfo> locationInfos = new ArrayList<>();
        while (res.moveToNext()) {
            @SuppressLint("Range") String latitudeStr = res.getString(res.getColumnIndex("lat"));
            @SuppressLint("Range") String longitudeStr = res.getString(res.getColumnIndex("lon"));

            double latitude = Double.parseDouble(latitudeStr);
            double longitude = Double.parseDouble(longitudeStr);

            LocationInfo locationInfo = new LocationInfo(latitude, longitude);

            locationInfos.add(locationInfo);
        }

        res.close();
        return locationInfos;

    }


    public static void getWeatherData(LocationInfo locationInfo, WeatherInfoCallBack weatherInfoCallBack) {
//getting wether data fron api and calling it throw retrofit instance method
        try {
            WeatherAPI weatherAPI = RetrofitWeather.getRetrofitInstance().create(WeatherAPI.class);
            Call<OpenWeatherMap> call = weatherAPI.getWeatherWithLocation(locationInfo.latitude, locationInfo.longitude);

            call.enqueue(new Callback<OpenWeatherMap>() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<OpenWeatherMap> call, @NonNull Response<OpenWeatherMap> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        OpenWeatherMap weatherMap = response.body();
                        WeatherInfoModel WeatherInfoModel = new WeatherInfoModel();
                        WeatherInfoModel.setCity(weatherMap.getName() + " , " + response.body().getSys().getCountry());
                        WeatherInfoModel.setTemperature(weatherMap.getMain().getTemp());
                        WeatherInfoModel.setWeatherCondition(weatherMap.getWeather().get(0).getDescription());
                        WeatherInfoModel.setHumidity(weatherMap.getMain().getHumidity());
                        WeatherInfoModel.setMaxTep(weatherMap.getMain().getTempMax());
                        WeatherInfoModel.setMinTemp(weatherMap.getMain().getTempMin());
                        WeatherInfoModel.setPressure(weatherMap.getMain().getPressure());
                        WeatherInfoModel.setWind(weatherMap.getWind().getSpeed());
                        WeatherInfoModel.setFeels_like(weatherMap.getMain().getFeelsLike());
                        String iconCode = weatherMap.getWeather().get(0).getIcon();
                        WeatherInfoModel.setIcon(iconCode);
//
                        weatherInfoCallBack.onWeatherInfoAvailable(WeatherInfoModel);

                    }

                }

                @Override
                public void onFailure(@NonNull Call<OpenWeatherMap> call, @NonNull Throwable throwable) {
//                Toast.makeText(this, "failed to load", Toast.LENGTH_SHORT).show();

                }

            });
        } catch (Exception e) {
            Log.e("TAG", "getWeatherData: " + e.getMessage());
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.item_ref) {
            refreshWeatherInfo();
        }
        if (id == R.id.item_dark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }
        if (id == R.id.item_light) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        if (id == R.id.item_share) {
            shareCurrentLocation();
        }


        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    private void shareCurrentLocation() {

        if (weatherInfoModel != null) {
            String location = "city :" + weatherInfoModel.getCity() + ", Temperature:" + weatherInfoModel.getTemperature();
//            String location = "city: " + weatherInfoModel.getCity() + ", Temperature: " + weatherInfoModel.getTemperature()  ;
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Current Location");
            shareIntent.putExtra(Intent.EXTRA_TEXT, location);
            startActivity(Intent.createChooser(shareIntent, "Share Location"));
        } else {

            Toast.makeText(this, "Weather not loaded yet ", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshWeatherInfo() {
//
        if (isNetworkAvailable()) {
            if (currentLocationInfo != null) {
              // method to get weather data for the current location
                getWeatherData(currentLocationInfo, new WeatherInfoCallBack() {
                    @Override
                    public void onWeatherInfoAvailable(WeatherInfoModel newWeatherInfoModel) {
                        weatherInfoModel = newWeatherInfoModel;
                        saveWeatherData(newWeatherInfoModel);
                        fastItemAdapter.notifyAdapterItemChanged(0);
                        Toast.makeText(MainActivity.this, "Weather refreshed", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {

                Toast.makeText(this, "Current location information not available", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Network Not Available", Toast.LENGTH_SHORT).show();
        }
    }


}

interface WeatherInfoCallBack {
    void onWeatherInfoAvailable(WeatherInfoModel weatherInfoModel);
}

