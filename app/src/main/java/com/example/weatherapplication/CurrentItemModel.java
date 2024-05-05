
package com.example.weatherapplication;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;

import java.util.List;


import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CurrentItemModel extends AbstractItem<CurrentItemModel, CurrentItemModel.ViewHolder> {

    private SharedPreferences sharedPreferences;

    public LocationInfo locationInfo;

    public CurrentItemModel(LocationInfo locationInfo, SharedPreferences sharedPreferences) {
        this.locationInfo = locationInfo;
        this.sharedPreferences = sharedPreferences;
    }


    @NonNull
    @Override
    public CurrentItemModel.ViewHolder getViewHolder(View v) {
        return new CurrentItemModel.ViewHolder(v);

    }

    @Override
    public int getType() {
        return R.id.parentCurrent;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.current_item;
    }


    protected static class ViewHolder extends FastAdapter.ViewHolder<CurrentItemModel> {

        private TextView city, temperature, weatherCondition, humidity, maxTemp, minTemp, pressure, wind, feels_like;
        private ImageView imageviewCurrent;
        private ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.textViewcity);
            temperature = itemView.findViewById(R.id.textViewTemp);
            weatherCondition = itemView.findViewById(R.id.textViewWeathCondition);
            humidity = itemView.findViewById(R.id.humi);
            maxTemp = itemView.findViewById(R.id.MaxTemp);
            feels_like = itemView.findViewById(R.id.rain);
            minTemp = itemView.findViewById(R.id.MinTemp);
            pressure = itemView.findViewById(R.id.Press);
            wind = itemView.findViewById(R.id.Wind);
            imageviewCurrent = itemView.findViewById(R.id.imageViewWeather);
            progressBar = itemView.findViewById(R.id.progressBar);
        }

        @Override
        public void bindView(CurrentItemModel item, List<Object> payloads) {
            String  cachedWeatherJson = item.sharedPreferences.getString("cached_weather_data", null);
            if (cachedWeatherJson != null) {
                WeatherInfoModel cachedWeatherInfo = WeatherInfoModel.fromJson(cachedWeatherJson);
                MainActivity.weatherInfoModel =cachedWeatherInfo;
                bindWeatherData(cachedWeatherInfo);
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
            }

            if (item.locationInfo.getLatitude() != 0 && item.locationInfo.getLongitude() != 0) {
                    progressBar.setVisibility(View.VISIBLE);
                MainActivity.getWeatherData(item.locationInfo, weatherInfoModel -> {
                    if (weatherInfoModel != null) {
                        progressBar.setVisibility(View.GONE);
                        bindWeatherData(weatherInfoModel);
                        MainActivity.weatherInfoModel=weatherInfoModel;
                        showWeatherDataLoadedSnackbar(itemView);
                        SharedPreferences.Editor editor = item.sharedPreferences.edit();
                        editor.putString("cached_weather_data", weatherInfoModel.toJson());
                        editor.apply();
                    } else {
                        //  where weather data is null
                        progressBar.setVisibility(View.GONE);
                        Snackbar.make(itemView, "Failed to load weather data", Snackbar.LENGTH_SHORT).show();
                        Log.e("WeatherAPI", "Failed to fetch weather data");
                    }
                });
            }
        }

        private void showWeatherDataLoadedSnackbar(View view) {
            Snackbar.make(view, "Current Weather loaded", Snackbar.LENGTH_SHORT).show();
        }

        private void bindWeatherData(WeatherInfoModel weatherInfoModel) {
            if (weatherInfoModel != null){
                city.setText(weatherInfoModel.getCity());
            temperature.setText(weatherInfoModel.getTemperature() + " °C");
            weatherCondition.setText(weatherInfoModel.getWeatherCondition());
            humidity.setText(weatherInfoModel.getHumidity() + " %");
            maxTemp.setText(weatherInfoModel.getMaxTep() + "°C");
            minTemp.setText(weatherInfoModel.getMinTemp() + " °C");
            pressure.setText(weatherInfoModel.getPressure() + "pa");
            wind.setText(String.valueOf(weatherInfoModel.getWind() + "Km/hr"));
            feels_like.setText(String.valueOf(weatherInfoModel.getFeels_like() + "%"));
                String  iconcodeCurrent = weatherInfoModel.getIcon();
                Picasso.get().load("https://openweathermap.org/img/wn/" + iconcodeCurrent + ".png")
                        .placeholder(R.drawable.iconw2).into(imageviewCurrent);

        }
    }

        @Override
        public void unbindView(CurrentItemModel item) {

        }
    }
}

