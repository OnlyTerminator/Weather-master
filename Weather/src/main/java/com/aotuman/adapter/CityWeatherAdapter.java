package com.aotuman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aotuman.adapter.clicklistener.OnItemClickListener;
import com.aotuman.database.WeatherInfoDataManager;
import com.aotuman.http.cityinfo.CityInfo;
import com.aotuman.http.weatherinfo.data.NowWeather;
import com.aotuman.http.weatherinfo.data.Weather;
import com.aotuman.weather.R;
import com.aotuman.weather.TTApplication;

import java.util.List;

/**
 * Created by 凹凸曼 on 2016/12/6.
 */

public class CityWeatherAdapter extends RecyclerView.Adapter<CityWeatherAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater = null;

    public CityWeatherAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = null;
        View view = layoutInflater.inflate(R.layout.item_city_weather, parent, false);
        myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_city_name;
        private LinearLayout ll_ba;
        private ImageView iv;
        private TextView tv_city_des;
        private TextView tv_city_temp;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_city_name = (TextView) itemView.findViewById(R.id.tv_city_name);
            ll_ba = (LinearLayout) itemView.findViewById(R.id.ll_ba);
            iv = (ImageView) itemView.findViewById(R.id.iv_city_icon);
            tv_city_des = (TextView) itemView.findViewById(R.id.tv_city_des);
            tv_city_temp = (TextView) itemView.findViewById(R.id.tv_city_temp);
        }
    }
}
