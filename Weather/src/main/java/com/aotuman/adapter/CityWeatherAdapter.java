package com.aotuman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.aotuman.basetools.L;
import com.aotuman.http.weatherinfo.data.ForecastWeather;
import com.aotuman.weather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 凹凸曼 on 2016/12/6.
 */

public class CityWeatherAdapter extends RecyclerView.Adapter<CityWeatherAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater = null;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_AQI = 1;
    private static final int TYPE_FORECAST = 2;
    private static final int TYPE_TODAY_DES = 3;
    private static final int TYPE_TODAY_CONTENT = 4;

    public CityWeatherAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
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
            List<ForecastWeather> list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                list.add(new ForecastWeather());
            }
            forecastWeatherAdapter = new ForecastWeatherAdapter(list);
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
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private ListView forecastListView;
        private ForecastWeatherAdapter forecastWeather;

        public MyViewHolder(View itemView, ForecastWeatherAdapter forecastWeather) {
            super(itemView);
            this.forecastWeather = forecastWeather;
            initView(itemView);
        }

        private void initView(View itemView) {
            forecastListView = (ListView) itemView.findViewById(R.id.lv_forecast);
        }
    }
}
