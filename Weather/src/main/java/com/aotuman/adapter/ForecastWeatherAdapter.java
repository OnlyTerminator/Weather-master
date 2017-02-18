package com.aotuman.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.aotuman.http.weatherinfo.data.ForecastWeather;
import com.aotuman.weather.R;
import com.aotuman.weather.TTApplication;

import java.util.List;

/**
 * Created by 凹凸曼 on 2017/2/18.
 */

public class ForecastWeatherAdapter extends BaseAdapter {
    public List<ForecastWeather> mForecast;
    public ForecastWeatherAdapter(List<ForecastWeather> forecastWeathers){
        mForecast = forecastWeathers;
    }
    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return 5;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView){
            convertView = LayoutInflater.from(TTApplication.getInstance()).inflate(R.layout.item_forecast,null);
        }
        return convertView;
    }
}
