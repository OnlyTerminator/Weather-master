package com.aotuman.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aotuman.http.cityinfo.CityInfo;
import com.aotuman.weather.R;
import com.aotuman.weather.WeatherContext;


public class CityWeatherFragment extends Fragment {
    private View mView;
    private TextView tv_city_name;
    private CityInfo cityInfo;

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
        if(null != cityInfo) {
            tv_city_name.setText(cityInfo.citynm);
        }
    }

    private void initData() {
        int size = WeatherContext.cityList.size();
        if(WeatherContext.currentIndex < size) {
            cityInfo = WeatherContext.cityList.get(WeatherContext.currentIndex);
        }
    }

}
