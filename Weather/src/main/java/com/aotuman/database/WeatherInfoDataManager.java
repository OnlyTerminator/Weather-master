package com.aotuman.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aotuman.http.cityinfo.CityInfo;
import com.aotuman.http.weatherinfo.data.AQIWeather;
import com.aotuman.http.weatherinfo.data.ForecastWeather;
import com.aotuman.http.weatherinfo.data.HistoryWeather;
import com.aotuman.http.weatherinfo.data.NowWeather;
import com.aotuman.http.weatherinfo.data.Weather;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 凹凸曼 on 2016/12/6.
 */

public class WeatherInfoDataManager {
    private static WeatherInfoDataBaseHelp weatherInfoDataBaseHelp = null;
    private static WeatherInfoDataManager instance;
    private Context context;
    public WeatherInfoDataManager(Context context){
        this.context = context;
        weatherInfoDataBaseHelp = new WeatherInfoDataBaseHelp(context);
    }

    public static WeatherInfoDataManager getInstance(Context context){
        if(null == instance){
            instance = new WeatherInfoDataManager(context);
        }
        return instance;
    }

    public void insertWeatherInfo(Weather weather){
        String sql = "insert into weatherinfo (cityid,citynm,cityno,nowweather,aqi,historyweather,forecastweather)values(?,?,?,?,?,?,?)";
        SQLiteDatabase db = weatherInfoDataBaseHelp.getWritableDatabase();
        Gson gson = new Gson();
        String nowWeather = gson.toJson(weather.nowWeather);
        String aqiWeather = gson.toJson(weather.aqiWeather);
        String historyWeather = gson.toJson(weather.historyWeather);
        String forecastWeather = gson.toJson(weather.forecastWeather);
        db.execSQL(sql, new Object[]{weather.cityid, weather.citynm, weather.cityno, nowWeather, "", "", ""});
        db.close();
    }

    public Weather findWeatherByCityID(String cityID){
        List<CityInfo> cityInfoList = new ArrayList<>();
        String sql = "select * from weatherinfo where cityid like ?";
        SQLiteDatabase db = weatherInfoDataBaseHelp.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery(sql,new String[] {"%" + cityID + "%"});
        Weather weather = new Weather();
        if(null != cursor) {
            Gson gson = new Gson();
            while (cursor.moveToNext()) {
                weather.cityid = cursor.getString(1); //获取第一列的值,第一列的索引从0开始
                weather.citynm = cursor.getString(2);//获取第二列的值
                weather.cityno = cursor.getString(3);//获取第三列的值
                weather.nowWeather = gson.fromJson(cursor.getString(4),NowWeather.class);
                weather.aqiWeather = gson.fromJson(cursor.getString(5), AQIWeather.class);
                weather.historyWeather = gson.fromJson(cursor.getString(6), HistoryWeather.class);
                weather.forecastWeather = gson.fromJson(cursor.getString(7), ForecastWeather.class);
            }
            cursor.close();
        }
        db.close();
        return weather;
    }

    public List<Weather> findAllWeathers(){
        List<Weather> weatherInfoList = new ArrayList<>();
        String sql = "select * from weatherinfo";
        SQLiteDatabase db = weatherInfoDataBaseHelp.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery(sql,new String[] {});
        if(null != cursor) {
            Gson gson = new Gson();
            while (cursor.moveToNext()) {
                Weather weather = new Weather();
                weather.cityid = cursor.getString(1); //获取第一列的值,第一列的索引从0开始
                weather.citynm = cursor.getString(2);//获取第二列的值
                weather.cityno = cursor.getString(3);//获取第三列的值
                weather.nowWeather = gson.fromJson(cursor.getString(4),NowWeather.class);
                weather.aqiWeather = gson.fromJson(cursor.getString(5), AQIWeather.class);
                weather.historyWeather = gson.fromJson(cursor.getString(6), HistoryWeather.class);
                weather.forecastWeather = gson.fromJson(cursor.getString(7), ForecastWeather.class);
                weatherInfoList.add(weather);
            }
            cursor.close();
        }
        db.close();
        return weatherInfoList;
    }

}
