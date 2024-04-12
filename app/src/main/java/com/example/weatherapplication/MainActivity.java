package com.example.weatherapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView city, temperature, weatherCondition, humidity, maxTemp, minTemp, pressure, wind, rain;
    private ImageView imageview;
    RecyclerView recyclerView;

    private FloatingActionButton fab;
    Button next;
    private FastAdapter<Item> fastAdapter;

    private ItemAdapter<Item> itemAdapter;

    LocationManager locationManager;

    LocationListener locationListener;
    LocationInfo currentLocationInfo;
    //Button buttonSearch;
//private EditText editText;
    DatabaseHelper databaseHelper;
    public static final String FIRST_TIME_OPEN_KEY = "first_time_open_key";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("my_pref", 0);
        boolean isFirstTime = pref.getBoolean(FIRST_TIME_OPEN_KEY,true);
        if(isFirstTime) { // TODO only one time running code
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(FIRST_TIME_OPEN_KEY, false);
            ArrayList<LocationInfo> locations = new ArrayList<>();

            LocationInfo mumbaiLocation = new LocationInfo(19.03681, 73.01582);
            LocationInfo bengalLocation = new LocationInfo(22.978624, 87.747803);
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


//for(LocationInfo locationInfo: locations) {
//    databaseHelper.insertIntoLocationTable(this, locationInfo);
//  ArrayList<LocationInfo> locations)




        }

        initIds();
        try {
          getListOfLocations();
        } catch (Exception e) {
            //ignored
        }
        setUpListeners();

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
                currentLocationInfo = new LocationInfo(location.getLatitude(),location.getLongitude());

                if (currentLocationInfo.latitude != 0 && currentLocationInfo.longitude != 0) {
                    databaseHelper.insertIntoLocationTable(MainActivity.this, currentLocationInfo);

                }

               WeatherInfoModel weatherInfoModel =  getWeatherData(currentLocationInfo);
//                Log.e("lat :", String.valueOf(lat));
//                Log.e("lon :", String.valueOf(lon));
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


    private void setUpListeners() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, weatherActivity.class);
                startActivity(intent);

            }
        });
    }

    private void initIds() {
        next = findViewById(R.id.button);
        city = findViewById(R.id.textViewcity);
        temperature = findViewById(R.id.textViewTemp);
        recyclerView = findViewById(R.id.recyclerId);
//        ItemAdapter itemAdapter = new ItemAdapter();
        itemAdapter = ItemAdapter.items();
//        FastAdapter fastAdapter = FastAdapter.with(itemAdapter);
        fastAdapter = FastAdapter.with(itemAdapter);
        recyclerView.setAdapter(fastAdapter);
        weatherCondition = findViewById(R.id.textViewWeathCondition);
        humidity = findViewById(R.id.Humi);
        maxTemp = findViewById(R.id.MaxTemp);
        rain = findViewById(R.id.Rain);
        minTemp = findViewById(R.id.MinTemp);
        pressure = findViewById(R.id.Press);
        wind = findViewById(R.id.Wind);
        imageview = findViewById(R.id.imageViewWeather);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        itemAdapter.add(ITEMS);




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

            LocationInfo locationInfo = new LocationInfo(latitude,longitude);

            locationInfos.add(locationInfo);

//            getWeatherData(locationInfo);
        }

        res.close();
        return locationInfos;

    }
//        while (res.moveToNext()) {
//           double lat = res.getDouble(res.getColumnIndex("lat"));
//            double lon = res.getDouble(res.getColumnIndex("lon"));
//            getWeatherData(lat, lon);
//        }
//        res.close();
//    }

    public WeatherInfoModel getWeatherData(LocationInfo locationInfo) {
        WeatherAPI weatherAPI = RetrofitWeather.getRetrofitInstance().create(WeatherAPI.class);
        Call<OpenWeatherMap> call = weatherAPI.getWeatherWithLocation(locationInfo.latitude, locationInfo.longitude);
        WeatherInfoModel WeatherInfoModel = new WeatherInfoModel();
        call.enqueue(new Callback<OpenWeatherMap>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<OpenWeatherMap> call, @NonNull Response<OpenWeatherMap> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OpenWeatherMap weatherMap = response.body();
                    WeatherInfoModel weatherInfoModel = new WeatherInfoModel();
                    weatherInfoModel.setCity(weatherMap.getName());
                    weatherInfoModel.setTemperature(weatherMap.getMain().getTemp());
                    weatherInfoModel.setWeatherCondition(weatherMap.getWeather().toString());
                    weatherInfoModel.setHumidity(weatherMap.getMain().getHumidity());
                    weatherInfoModel.setMaxTep(weatherMap.getMain().getTempMax());
                    weatherInfoModel.setMinTemp(weatherMap.getMain().getTempMin());
                    weatherInfoModel.setWind(weatherMap.getWind().getSpeed());

                    Log.d("API_RESPONSE", "Response: " + weatherMap.toString());
                    city.setText(weatherMap.getName() + " , " + response.body().getSys().getCountry());
                    temperature.setText(weatherMap.getMain().getTemp() + " °C");
                    weatherCondition.setText(weatherMap.getWeather().get(0).getDescription());
                    humidity.setText(weatherMap.getMain().getHumidity() + " %");
                    maxTemp.setText(weatherMap.getMain().getTempMax() + "°C");
                    minTemp.setText(weatherMap.getMain().getTempMin() + " °C");
                    pressure.setText(weatherMap.getMain().getPressure() + " %");
                    wind.setText(weatherMap.getWind().getSpeed().toString());

                    String iconCode = response.body().getWeather().get(0).getIcon();

                    Picasso.get().load("  https://openweathermap.org/img/wn/" + iconCode + " @2x.png")
                            .placeholder(R.drawable.iconw2).into(imageview);
                }

            }

            @Override
            public void onFailure(@NonNull Call<OpenWeatherMap> call, @NonNull Throwable throwable) {
                Toast.makeText(MainActivity.this, "failed to load", Toast.LENGTH_SHORT).show();

            }

        });

        return WeatherInfoModel;
    }
    }
