package com.aotuman.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aotuman.adapter.CityWeatherAdapter;
import com.aotuman.database.WeatherInfoDataManager;
import com.aotuman.http.cityinfo.CityInfo;
import com.aotuman.http.weatherinfo.data.AQIWeather;
import com.aotuman.http.weatherinfo.data.NowWeather;
import com.aotuman.http.weatherinfo.data.Weather;
import com.aotuman.weather.R;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CityWeatherFragment extends Fragment {
    private View mView;
    private TextView tv_city_name;
    private TextView tv_city_weather;
    private TextView tv_city_temp;
    private RecyclerView rc_city_weather;
    private Weather mWeather;
    private LinearLayout mllWeatherBack;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.one_frag_layout, container, false);

            initData();

            initView(mView);
        }
        return mView;
    }

    private void initView(View view) {
        NowWeather nowWeather = null;
        tv_city_name = (TextView) view.findViewById(R.id.tv_city_name);
        tv_city_weather = (TextView) view.findViewById(R.id.tv_city_weather);
        tv_city_temp = (TextView) view.findViewById(R.id.tv_city_temp);
        rc_city_weather = (RecyclerView) view.findViewById(R.id.recyclerview);
        mllWeatherBack = (LinearLayout) view.findViewById(R.id.ll_weather_back);
        if (null != mWeather) {
            tv_city_name.setText(mWeather.citynm);
            nowWeather = mWeather.nowWeather;
            if (null != nowWeather) {
                tv_city_weather.setText(nowWeather.weather);
                tv_city_temp.setText(nowWeather.temperature_curr);
            }
        }

        TwinklingRefreshLayout refreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.refresh);
        BezierLayout headerView = new BezierLayout(this.getContext());
        refreshLayout.setHeaderView(headerView);
        View exHeader = View.inflate(this.getContext(), R.layout.item_city_weather_title, null);
        initWeatherTitle(exHeader, nowWeather);
        refreshLayout.addFixedExHeader(exHeader);
        refreshLayout.setPureScrollModeOn(true);

        rc_city_weather.setLayoutManager(new LinearLayoutManager(getContext()));
        CityWeatherAdapter cityWeatherAdapter = new CityWeatherAdapter(getContext(), mWeather);
        rc_city_weather.setAdapter(cityWeatherAdapter);
    }

    private void initWeatherTitle(View view, NowWeather nowWeather) {
        TextView tv_low_temp = (TextView) view.findViewById(R.id.tv_low_temp);
        TextView tv_up_temp = (TextView) view.findViewById(R.id.tv_up_temp);
        TextView tv_week = (TextView) view.findViewById(R.id.tv_week);
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        tv_week.setText(simpleDateFormat.format(date));
        if (null != nowWeather) {
            tv_low_temp.setText(nowWeather.temp_low);
            tv_up_temp.setText(nowWeather.temp_high);
        }

        TextView tv_aqi_num = (TextView) view.findViewById(R.id.tv_aqi_num);
        TextView tv_aqi_level = (TextView) view.findViewById(R.id.tv_aqi_level);
        TextView tv_aqi_des = (TextView) view.findViewById(R.id.tv_aqi_des);
        TextView tv_aqi_message = (TextView) view.findViewById(R.id.tv_aqi_message);
        AQIWeather aqiWeather = mWeather == null ? null : mWeather.aqiWeather;
        if (null != aqiWeather) {
            tv_aqi_num.setText("空气质量：" + aqiWeather.aqi);
            tv_aqi_level.setText("空气等级：" + aqiWeather.aqi_levid);
            tv_aqi_des.setText("空气描述：" + aqiWeather.aqi_levnm);
            tv_aqi_message.setText("健康提示：" + aqiWeather.aqi_remark);
        }

    }

    private void initData() {
        Bundle bundle = getArguments();
        CityInfo cityInfo = (CityInfo) bundle.get("key_weather_page_data");
        String cityId = cityInfo.cityid;
        mWeather = WeatherInfoDataManager.getInstance(this.getContext()).findWeatherByCityID(cityId);
    }

}
