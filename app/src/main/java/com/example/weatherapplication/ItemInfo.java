package com.example.weatherapplication;
import static com.example.weatherapplication.MainActivity.LOCATION_INTENT_KEY;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ItemInfo extends AppCompatActivity {

    private TextView cityItem, temperatureItem, weatherConditionItem, humidityItem,rain, maxTempItem, minTempItem, pressureItem, windItem;
    private ImageView imageViewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_info);

        cityItem = findViewById(R.id.textViewitem);
        temperatureItem = findViewById(R.id.textViewTempItem);
        weatherConditionItem = findViewById(R.id.textViewWeathConditionitem);
        humidityItem = findViewById(R.id.humidityitem);
        maxTempItem = findViewById(R.id.maxTempitem);
        minTempItem = findViewById(R.id.minTempitem);
        pressureItem = findViewById(R.id.pressureitem);
        windItem = findViewById(R.id.winditem);
        imageViewItem = findViewById(R.id.imageviewitem);
        rain= findViewById(R.id.rain2);

        LocationInfo locationInfo = getIntent().getParcelableExtra(LOCATION_INTENT_KEY);

        if (locationInfo != null) {
            if (isNetworkAvailable()) {
                // Fetch data from the network
                MainActivity.getWeatherData(locationInfo, new WeatherInfoCallBack() {
                    @Override
                    public void onWeatherInfoAvailable(WeatherInfoModel weatherInfoModel) {
                        // Populate UI with data from the network
                        populateUI(weatherInfoModel);
                    }
                });
            } else {
                // Fetch data from the local database
                DatabaseHelper databaseHelper = new DatabaseHelper(this);
                WeatherInfoModel weatherInfoModel = databaseHelper.getWeatherInfoFromTable(this, locationInfo);
                if (weatherInfoModel != null) {
                    // Populate UI with data from the local database
                    populateUI(weatherInfoModel);
                } else {
                    // Handle the scenario where data is not available in the local database
                    Toast.makeText(ItemInfo.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void populateUI(WeatherInfoModel weatherInfoModel) {
        cityItem.setText(weatherInfoModel.getCity());
        temperatureItem.setText(weatherInfoModel.getTemperature() + " °C");
        weatherConditionItem.setText(weatherInfoModel.getWeatherCondition());
        humidityItem.setText( weatherInfoModel.getHumidity() + " %");
        maxTempItem.setText( weatherInfoModel.getMaxTep() + "°C");
        minTempItem.setText(weatherInfoModel.getMinTemp() + " °C");
        pressureItem.setText( weatherInfoModel.getPressure() + "Pa");
        windItem.setText(String.valueOf(weatherInfoModel.getWind() +"Km/Hr"));
                    String  iconcodeItem = weatherInfoModel.getIcon();
                    Picasso.get().load("https://openweathermap.org/img/wn/" + iconcodeItem + ".png")
                            .placeholder(R.drawable.iconw2).into(imageViewItem);
                    rain.setText(String.valueOf(weatherInfoModel.getFeels_like() + "%"));
//                    .setText(String.valueOf(weatherInfoModel.getFeels_like()));
    }
}
//        windItem.setText(":"+WeatherInfoModel.getMain.);

//
//import static com.example.weatherapplication.MainActivity.LOCATION_INTENT_KEY;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.squareup.picasso.Picasso;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class ItemInfo extends AppCompatActivity {
//
//    private TextView cityitem, temperatureItem, weatherConditionItem, humidityItem, maxTempItem, minTempItem, pressureItem, windItem;
//    private ImageView imageviewItem;
//
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.item_info);
//
//
//        cityitem = findViewById(R.id.textViewitem);
//        temperatureItem = findViewById(R.id.textViewTempItem);
//        weatherConditionItem = findViewById(R.id.textViewWeathConditionitem);
//        humidityItem = findViewById(R.id.humidityitem);
//        maxTempItem = findViewById(R.id.maxTempitem);
//        minTempItem = findViewById(R.id.minTempitem);
//        pressureItem = findViewById(R.id.pressureitem);
//        windItem = findViewById(R.id.winditem);
//        imageviewItem = findViewById(R.id.imageviewitem);
//
//        LocationInfo locationInfo = getIntent().getParcelableExtra(LOCATION_INTENT_KEY);
//
//        if (locationInfo != null) {
//            MainActivity.getWeatherData(locationInfo, new WeatherInfoCallBack() {
//                @Override
//                public void onWeatherInfoAvailable(WeatherInfoModel weatherInfoModel) {
//                   cityitem.setText(weatherInfoModel.city);
//               temperatureItem.setText( +weatherInfoModel.getTemperature() + " °C");
//                    weatherConditionItem.setText(weatherInfoModel.getWeatherCondition());
//                    humidityItem.setText(":  "+ weatherInfoModel.getHumidity() + " %");
//                    maxTempItem.setText(":  "+weatherInfoModel.getMaxTep() + "°C");
//                    minTempItem.setText(":  "+weatherInfoModel.getMinTemp() + " °C");
//                    pressureItem.setText(":  "+weatherInfoModel.getPressure() + " Pa");
//                    windItem.setText(":  "+String.valueOf(weatherInfoModel.getWind() +"Km/Hr"));
//                    String  iconcodeItem = weatherInfoModel.getIcon();
//                    Picasso.get().load("https://openweathermap.org/img/wn/" + iconcodeItem + ".png")
//                            .placeholder(R.drawable.iconw2).into(imageviewItem);
////                    feels_likeItem.setText(String.valueOf(weatherInfoModel.getFeels_like()));
//                }
//            });
//        }
//
//
//    }
//}
//
//
//
//
//
//
