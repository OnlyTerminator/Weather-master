package com.aotuman.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aotuman.adapter.CityWeatherAdapter;
import com.aotuman.database.WeatherInfoDataManager;
import com.aotuman.http.cityinfo.CityInfo;
import com.aotuman.http.weatherinfo.data.NowWeather;
import com.aotuman.http.weatherinfo.data.Weather;
import com.aotuman.weather.R;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;


public class CityWeatherFragment extends Fragment {
    private View mView;
    private TextView tv_city_name;
    private TextView tv_city_weather;
    private TextView tv_city_temp;
    private RecyclerView rc_city_weather;
    private Weather mWeather;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(null == mView){
            mView = inflater.inflate(R.layout.one_frag_layout,container,false);

            initData();

            initView(mView);
        }
        return mView;
    }

    private void initView(View view) {
        tv_city_name = (TextView) view.findViewById(R.id.tv_city_name);
        tv_city_weather = (TextView) view.findViewById(R.id.tv_city_weather);
        tv_city_temp = (TextView) view.findViewById(R.id.tv_city_temp);
        rc_city_weather = (RecyclerView) view.findViewById(R.id.recyclerview);
        if(null != mWeather) {
            tv_city_name.setText(mWeather.citynm);
            NowWeather nowWeather = mWeather.nowWeather;
            if(null != nowWeather) {
                tv_city_weather.setText(nowWeather.weather);
                tv_city_temp.setText(nowWeather.temperature_curr);
            }
        }

        TwinklingRefreshLayout refreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.refresh);
        BezierLayout headerView = new BezierLayout(this.getContext());
        refreshLayout.setHeaderView(headerView);
        View exHeader = View.inflate(this.getContext(), R.layout.item_city_weather_title, null);
        refreshLayout.addFixedExHeader(exHeader);
        refreshLayout.setPureScrollModeOn(true);

        rc_city_weather.setLayoutManager(new LinearLayoutManager(this.getContext()));
        CityWeatherAdapter cityWeatherAdapter = new CityWeatherAdapter(this.getContext());
        rc_city_weather.setAdapter(cityWeatherAdapter);
    }

    private void initData() {
        Bundle bundle = getArguments();
        CityInfo cityInfo = (CityInfo) bundle.get("key_weather_page_data");
        String  cityId = cityInfo.cityid;
        mWeather = WeatherInfoDataManager.getInstance(this.getContext()).findWeatherByCityID(cityId);
    }

}
