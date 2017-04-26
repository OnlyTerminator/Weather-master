package com.aotuman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.aotuman.basetools.L;
import com.aotuman.http.weatherinfo.data.AQIWeather;
import com.aotuman.http.weatherinfo.data.ForecastWeather;
import com.aotuman.http.weatherinfo.data.NowWeather;
import com.aotuman.http.weatherinfo.data.Weather;
import com.aotuman.weather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 凹凸曼 on 2016/12/6.
 */

public class CityWeatherAdapter extends RecyclerView.Adapter<CityWeatherAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater = null;
    private Weather mWeather;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_AQI = 1;
    private static final int TYPE_FORECAST = 2;
    private static final int TYPE_TODAY_DES = 3;
    private static final int TYPE_TODAY_CONTENT = 4;

    public CityWeatherAdapter(Context context, Weather weather) {
        this.layoutInflater = LayoutInflater.from(context);
        this.mWeather = weather;
    }

    @Override
    public int getItemViewType(int position) {
        L.i("CityAdapter", position + "-------------");
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = null;
        View view = null;
        ForecastWeatherAdapter forecastWeatherAdapter = null;
        L.i("CityAdapter", viewType + "============");
        if (viewType == 0) {
//            view = layoutInflater.inflate(R.layout.item_city_weather_title, parent, false);
//        }else {
            view = layoutInflater.inflate(R.layout.item_city_weather_forecast, parent, false);
            forecastWeatherAdapter = new ForecastWeatherAdapter(mWeather.forecastWeather);
        }
        myViewHolder = new MyViewHolder(view, forecastWeatherAdapter);
        return myViewHolder;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        L.i("CityAdapter", position + "++++++++++++++" + getItemViewType(position));
        if (getItemViewType(position) == 0) {
            holder.forecastListView.setAdapter(holder.forecastWeather);
            holder.forecastWeather.notifyDataSetChanged();
            holder.setData();
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private ListView forecastListView;
        private ForecastWeatherAdapter forecastWeather;
        private TextView tv_weather_des;
        private TextView tv_content_city;
        private TextView tv_weather_temp;
        private TextView tv_content_up_humidity;
        private TextView tv_content_low_humidity;
        private TextView tv_content_wind;
        private TextView tv_content_winp;
        private TextView tv_content_aqi;
        private TextView tv_content_aqi_des;

        public MyViewHolder(View itemView, ForecastWeatherAdapter forecastWeather) {
            super(itemView);
            this.forecastWeather = forecastWeather;
            initView(itemView);
        }

        private void initView(View itemView) {
            forecastListView = (ListView) itemView.findViewById(R.id.lv_forecast);
            tv_weather_des = (TextView) itemView.findViewById(R.id.tv_weather_des);
            tv_content_city = (TextView) itemView.findViewById(R.id.tv_content_city);
            tv_weather_temp = (TextView) itemView.findViewById(R.id.tv_weather_temp);
            tv_content_up_humidity = (TextView) itemView.findViewById(R.id.tv_content_up_humidity);
            tv_content_low_humidity = (TextView) itemView.findViewById(R.id.tv_content_low_humidity);
            tv_content_wind = (TextView) itemView.findViewById(R.id.tv_content_wind);
            tv_content_winp = (TextView) itemView.findViewById(R.id.tv_content_winp);
            tv_content_aqi = (TextView) itemView.findViewById(R.id.tv_content_aqi);
            tv_content_aqi_des = (TextView) itemView.findViewById(R.id.tv_content_aqi_des);
        }

        public void setData() {
            if (null != mWeather) {
                NowWeather nowWeather = mWeather.nowWeather;
                AQIWeather aqiWeather = mWeather.aqiWeather;
                if (null != nowWeather) {
                    tv_content_city.setText(getTextViewContent(tv_content_city) + mWeather.citynm);
                    tv_weather_temp.setText(getTextViewContent(tv_weather_temp) + nowWeather.temperature_curr);
                    tv_content_up_humidity.setText(getTextViewContent(tv_content_up_humidity) + nowWeather.humidity);
                    tv_content_low_humidity.setText(getTextViewContent(tv_content_low_humidity) + nowWeather.humi_low);
                    tv_content_wind.setText(getTextViewContent(tv_content_wind) + nowWeather.wind);
                    tv_content_winp.setText(getTextViewContent(tv_content_winp) + nowWeather.winp);
                }
                if (null != aqiWeather) {
                    tv_content_aqi.setText(getTextViewContent(tv_content_aqi) + aqiWeather.aqi);
                    tv_content_aqi_des.setText(getTextViewContent(tv_content_aqi_des) + aqiWeather.aqi_remark);
                }
            }
        }

        private String getTextViewContent(TextView tv) {
            return tv.getText().toString().trim();
        }
    }
}
