package com.aotuman.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aotuman.http.weatherinfo.data.ForecastWeather;
import com.aotuman.http.weatherinfo.data.NowWeather;
import com.aotuman.weather.R;
import com.aotuman.weather.TTApplication;

import java.util.List;

/**
 * Created by 凹凸曼 on 2017/2/18.
 */

public class ForecastWeatherAdapter extends BaseAdapter {
    private List<NowWeather> mNowWeathers;
    public ForecastWeatherAdapter(ForecastWeather forecastWeathers){
        mNowWeathers = null == forecastWeathers ? null : forecastWeathers.result;
    }
    @Override
    public int getCount() {
        return mNowWeathers == null ? 0 : mNowWeathers.size();
    }

    @Override
    public Object getItem(int position) {
        return mNowWeathers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView){
            convertView = LayoutInflater.from(TTApplication.getInstance()).inflate(R.layout.item_forecast,null);
            holder = new ViewHolder();
            holder.tv_week = (TextView) convertView.findViewById(R.id.tv_week);
            holder.tv_low_temp = (TextView) convertView.findViewById(R.id.tv_low_temp);
            holder.tv_up_temp = (TextView) convertView.findViewById(R.id.tv_up_temp);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
            if(null != mNowWeathers && position < mNowWeathers.size()){
                NowWeather nowWeather = mNowWeathers.get(position);
                if(null != nowWeather){
                    holder.tv_week.setText(nowWeather.week);
                    holder.tv_up_temp.setText(nowWeather.temp_high);
                    holder.tv_low_temp.setText(nowWeather.temp_low);
                }
            }
        return convertView;
    }

    class ViewHolder{
        private TextView tv_week;
        private TextView tv_low_temp;
        private TextView tv_up_temp;
    }
}
