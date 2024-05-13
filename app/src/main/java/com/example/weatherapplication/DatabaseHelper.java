package com.example.weatherapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "weatherInfo.db";

    public static final String TABLE_WEATHER_DATA = "weather_data";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LATITUDE = "latitude_item";
    public static final String COLUMN_LONGITUDE = "longitude_item";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_TEMPERATURE = "temperature";
    public static final String COLUMN_WEATHER_CONDITION = "weather_condition";
    public static final String COLUMN_HUMIDITY = "humidity";
    public static final String COLUMN_MAX_TEMP = "max_temp";
    public static final String COLUMN_MIN_TEMP = "min_temp";
    public static final String COLUMN_PRESSURE = "pressure";
    public static final String COLUMN_WIND = "wind";
    public static final String COLUMN_ICON = "icon";


    protected DatabaseHelper(@Nullable Context context) {

        super(context, DATABASE_NAME ,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query ="CREATE TABLE location_info (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "lat  TEXT UNIQUE " +
                ",lon TEXT UNIQUE" +
                ")";
        db.execSQL(query);


        String CREATE_TABLE_WEATHER_DATA = "CREATE TABLE weather_data ("
               + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LATITUDE+ " TEXT UNIQUE, " +
                COLUMN_LONGITUDE+ " TEXT UNIQUE, " +
                COLUMN_LOCATION+" TEXT, " +
                COLUMN_TEMPERATURE+ " TEXT, " +
                COLUMN_WEATHER_CONDITION+ " TEXT, " +
                COLUMN_HUMIDITY+ " TEXT, " +
                COLUMN_MAX_TEMP+ " TEXT, " +
                COLUMN_MIN_TEMP+ " TEXT, " +
                COLUMN_PRESSURE+ " TEXT, " +
                COLUMN_WIND+ " TEXT, " +
                COLUMN_ICON+ " TEXT" +
                ")";

        db.execSQL(CREATE_TABLE_WEATHER_DATA);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void insertIntoLocationTable(Context context ,LocationInfo locationInfo){
        try {
            ContentValues values = new ContentValues();
            values.put("lat", String.valueOf(locationInfo.latitude));
            values.put("lon", String.valueOf(locationInfo.longitude));
            this.getWritableDatabase().insert("location_info", null, values);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
   public Cursor getAllData(){
      SQLiteDatabase db= this.getWritableDatabase();
      Cursor res = db.rawQuery("select * from " +" location_info",null);
      return res;
   }

    public void insertWeatherData(Context context, WeatherInfoModel weatherData,LocationInfo locationInfo) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_LATITUDE,locationInfo.getLatitude());
            values.put(COLUMN_LONGITUDE,locationInfo.getLongitude());
            values.put(COLUMN_LOCATION, weatherData.getCity());
            values.put(COLUMN_TEMPERATURE, weatherData.getTemperature());
            values.put(COLUMN_WEATHER_CONDITION, weatherData.getWeatherCondition());
            values.put(COLUMN_HUMIDITY, weatherData.getHumidity());
            values.put(COLUMN_MAX_TEMP, weatherData.getTempMax());
            values.put(COLUMN_MIN_TEMP, weatherData.getMinTemp());
            values.put(COLUMN_PRESSURE, weatherData.getPressure());
            values.put(COLUMN_WIND, weatherData.getWind());
            values.put(COLUMN_ICON, weatherData.getIcon());

            db.insert(TABLE_WEATHER_DATA, null, values);
            db.close();

//            Toast.makeText(context, "Weather data inserted successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error inserting weather data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    public Cursor getWeatherByLocation(LocationInfo locationInfo){
        SQLiteDatabase db= this.getReadableDatabase();
        String[] args = {
               String.valueOf(locationInfo.getLatitude()),
               String.valueOf(locationInfo.getLongitude())
        };
        return  db.rawQuery("select * from " + TABLE_WEATHER_DATA + " where "+ COLUMN_LATITUDE + " = ? and "+COLUMN_LONGITUDE+ "= ?",args);
    }

    @SuppressLint("Range")
    public WeatherInfoModel getWeatherInfoFromTable(Context context, LocationInfo locationInfo) {
        Cursor weatherCursor = getWeatherByLocation(locationInfo);
        WeatherInfoModel weatherInfoModel1;
        if (weatherCursor.getCount() == 0) {
            return null;
        }
        if (weatherCursor.moveToFirst()) {
            do {
                weatherInfoModel1 = new WeatherInfoModel();
                weatherInfoModel1.setCity(weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseHelper.COLUMN_LOCATION)));
                weatherInfoModel1.setTemperature(weatherCursor.getDouble(weatherCursor.getColumnIndex(DatabaseHelper.COLUMN_TEMPERATURE)));
                weatherInfoModel1.setWeatherCondition(weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseHelper.COLUMN_WEATHER_CONDITION)));
                weatherInfoModel1.setMaxTep(weatherCursor.getDouble(weatherCursor.getColumnIndex(DatabaseHelper.COLUMN_MAX_TEMP)));
                weatherInfoModel1.setMinTemp(weatherCursor.getDouble(weatherCursor.getColumnIndex(DatabaseHelper.COLUMN_MIN_TEMP)));
                weatherInfoModel1.setHumidity(weatherCursor.getInt(weatherCursor.getColumnIndex(DatabaseHelper.COLUMN_HUMIDITY)));
                weatherInfoModel1.setPressure(weatherCursor.getInt(weatherCursor.getColumnIndex(DatabaseHelper.COLUMN_PRESSURE)));
                weatherInfoModel1.setWind(weatherCursor.getDouble(weatherCursor.getColumnIndex(DatabaseHelper.COLUMN_WIND)));
                weatherInfoModel1.setIcon(weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseHelper.COLUMN_ICON)));
            } while (weatherCursor.moveToNext());
            weatherCursor.close();

            return weatherInfoModel1;
        }


        return null;
    }
}
