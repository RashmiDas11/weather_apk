package com.example.weatherapplication;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;
//
import java.util.List;
public class WeatherItemModel extends AbstractItem<WeatherItemModel, WeatherItemModel.ViewHolder>{
   LocationInfo locationInfo ;

    public WeatherItemModel(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
    }

    @NonNull
    @Override
    public WeatherItemModel.ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.parent_item ;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item;
    }
    protected static class ViewHolder extends FastAdapter.ViewHolder<WeatherItemModel> {
        private TextView itemNameTextView, weatherInfo, location;
        private ImageView imageView;
        private DatabaseHelper databaseHelper;

        public ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.degree);
            weatherInfo = itemView.findViewById(R.id.weatherinfo);
            location = itemView.findViewById(R.id.location);
            imageView = itemView.findViewById(R.id.icon);

            databaseHelper = new DatabaseHelper(itemView.getContext());
        }

        @Override
        public void bindView(WeatherItemModel item, List<Object> payloads) {
            if (isNetworkAvailable(itemView.getContext())) {
                // Network is available, fetch data from API
                MainActivity.getWeatherData(item.locationInfo, weatherInfoModel -> {
                    databaseHelper.insertWeatherData(itemView.getContext(), weatherInfoModel, item.locationInfo);
                    updateUI(weatherInfoModel);
                });
            } else {
                // Network is not available, retrieve data from the database
                WeatherInfoModel weatherInfoModel = databaseHelper.getWeatherInfoFromTable(itemView.getContext(), item.locationInfo);
                if (weatherInfoModel != null) {
                    updateUI(weatherInfoModel);
                } else {
                    // No data available in the database, handle this scenario if needed
                    Log.e("ViewHolder", "No data available in the database");
                }
            }
        }

        private void updateUI(WeatherInfoModel weatherInfoModel) {
            location.setText(weatherInfoModel.getCity());
            weatherInfo.setText(weatherInfoModel.getWeatherCondition());
            itemNameTextView.setText(weatherInfoModel.getTemperature() + " °C");
            String iconcode = weatherInfoModel.getIcon();
            Picasso.get().load("https://openweathermap.org/img/wn/" + iconcode + ".png")
                    .placeholder(R.drawable.iconw2).into(imageView);
        }


        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        @Override
        public void unbindView(WeatherItemModel item) {
            // Perform cleanup if needed
        }
    }


//    protected static class ViewHolder extends FastAdapter.ViewHolder<WeatherItemModel> {
//        private TextView itemNameTextView,weatherInfo,location;
//        DatabaseHelper databaseHelper = new DatabaseHelper(itemView.getContext());
//        ImageView imageView;
//        public ViewHolder(View itemView) {
//            super(itemView);
//            itemNameTextView = itemView.findViewById(R.id.degree);
//           weatherInfo = itemView.findViewById(R.id.weatherinfo);
//           location = itemView.findViewById(R.id.location);
//           imageView = itemView.findViewById(R.id.icon);
//
//        }
//
//        @Override
//        public void bindView(WeatherItemModel item, List<Object> payloads) {
//            DatabaseHelper dbhelper;
//            try {
//                 dbhelper = new DatabaseHelper(itemView.getContext());
//
//            WeatherInfoModel weatherInfoModel = dbhelper.getWeatherInfoFromTable(itemView.getContext(),item.locationInfo);
//
//              if (weatherInfoModel != null) {
//                  location.setText(weatherInfoModel.getCity());
//                  weatherInfo.setText(weatherInfoModel.getWeatherCondition());
//                  itemNameTextView.setText(weatherInfoModel.getTemperature() + " °C");
//                  String iconcode = weatherInfoModel.getIcon();
//                  Picasso.get().load("https://openweathermap.org/img/wn/" + iconcode + ".png")
//                          .placeholder(R.drawable.iconw2).into(imageView);
//              } else {
//
//
//                  MainActivity.getWeatherData(item.locationInfo, weatherInfoModel1 -> {
//                      databaseHelper.insertWeatherData(itemView.getContext(), weatherInfoModel1, item.locationInfo);
//                      location.setText(weatherInfoModel1.getCity());
//                      weatherInfo.setText(weatherInfoModel1.getWeatherCondition());
//
//                      // Set temperaturet
//                      itemNameTextView.setText(weatherInfoModel1.getTemperature() + " °C");
//                      String iconcode = weatherInfoModel1.getIcon();
//                      Picasso.get().load("https://openweathermap.org/img/wn/" + iconcode + ".png")
//                              .placeholder(R.drawable.iconw2).into(imageView);
//
//
//                  });
//              }
//
//            }catch(Exception exception){
//                MainActivity.getWeatherData(item.locationInfo, weatherInfoModel1 -> {
//                    databaseHelper.insertWeatherData(itemView.getContext(), weatherInfoModel1,item.locationInfo);
//                    location.setText(weatherInfoModel1.getCity());
//                    weatherInfo.setText(weatherInfoModel1.getWeatherCondition());
//                    itemNameTextView.setText(weatherInfoModel1.getTemperature() + " °C");
//                    String iconcode = weatherInfoModel1.getIcon();
//                    Picasso.get().load("https://openweathermap.org/img/wn/" + iconcode + ".png")
//                            .placeholder(R.drawable.iconw2).into(imageView);
//
//                });
//                Log.e("TAG", "bindView: ");
//            }
//
//
//        }
//
//        @Override
//        public void unbindView(WeatherItemModel item) {
//
//        }
//
//        }
//
//
//    }



}