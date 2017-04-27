package com.aotuman.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aotuman.adapter.ForecastWeatherAdapter;
import com.aotuman.database.WeatherInfoDataManager;
import com.aotuman.event.RefreshListener;
import com.aotuman.http.callback.WeatherCallBack;
import com.aotuman.http.cityinfo.CityInfo;
import com.aotuman.http.weatherinfo.GetWeatherInfo;
import com.aotuman.http.weatherinfo.data.AQIWeather;
import com.aotuman.http.weatherinfo.data.NowWeather;
import com.aotuman.http.weatherinfo.data.Weather;
import com.aotuman.view.MainScrollView;
import com.aotuman.view.loadview.LoadingDialog;
import com.aotuman.weather.R;
import com.aotuman.weather.TTApplication;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CityWeatherFragment extends Fragment {
    private View mView;
    private TextView tv_city_name;
    private TextView tv_city_weather;
    private TextView tv_city_temp;
    private Weather mWeather;
    private LinearLayout mllWeatherBack;

    private TextView tv_low_temp;
    private TextView tv_up_temp;
    private TextView tv_week;

    private TextView tv_aqi_num;
    private TextView tv_aqi_level;
    private TextView tv_aqi_des;
    private TextView tv_aqi_message;

    private MainScrollView mMainScrollView;

    private LoadingDialog mLoadingDialog;

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
        mLoadingDialog = new LoadingDialog(getContext(), "玩命加载中...");
        mMainScrollView = (MainScrollView) view.findViewById(R.id.refresh);
        tv_city_name = (TextView) view.findViewById(R.id.tv_city_name);
        tv_city_weather = (TextView) view.findViewById(R.id.tv_city_weather);
        tv_city_temp = (TextView) view.findViewById(R.id.tv_city_temp);

        tv_low_temp = (TextView) view.findViewById(R.id.tv_low_temp);
        tv_up_temp = (TextView) view.findViewById(R.id.tv_up_temp);
        tv_week = (TextView) view.findViewById(R.id.tv_week);

        tv_aqi_num = (TextView) view.findViewById(R.id.tv_aqi_num);
        tv_aqi_level = (TextView) view.findViewById(R.id.tv_aqi_level);
        tv_aqi_des = (TextView) view.findViewById(R.id.tv_aqi_des);
        tv_aqi_message = (TextView) view.findViewById(R.id.tv_aqi_message);

        mllWeatherBack = (LinearLayout) view.findViewById(R.id.ll_weather_back);

        mMainScrollView.setOnRefreshListener(new RefreshListener() {
            @Override
            public void refreshWeather() {
                mLoadingDialog.show();
                new GetWeatherInfo().getWeather(mWeather.citynm, new WeatherCallBack() {
                    @Override
                    public void success(Weather weather) {
                        mLoadingDialog.close();
                        if (null != weather) {
                            weather.citynm = mWeather.citynm;
                            weather.cityid = mWeather.cityid;
                            weather.cityno = mWeather.cityno;
                            mWeather = weather;
                            WeatherInfoDataManager.getInstance(TTApplication.getInstance()).insertWeatherInfo(weather);
                            setData();
                        }
                    }

                    @Override
                    public void failed() {
                        mLoadingDialog.close();
                    }
                });
            }
        });
        initTwinkView(view);

        setData();

    }

    private ListView forecastListView;
    private ForecastWeatherAdapter forecastWeatherAdapter;
    private TextView tv_weather_des;
    private TextView tv_content_city;
    private TextView tv_weather_temp;
    private TextView tv_content_up_humidity;
    private TextView tv_content_low_humidity;
    private TextView tv_content_wind;
    private TextView tv_content_winp;
    private TextView tv_content_aqi;
    private TextView tv_content_aqi_des;

    private void initTwinkView(View itemView) {
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

            tv_city_name.setText(mWeather.citynm);

            if (null != nowWeather) {
                tv_city_weather.setText(nowWeather.weather);
                tv_city_temp.setText(nowWeather.temperature_curr);
                tv_city_name.setText(nowWeather.citynm);

                tv_low_temp.setText(nowWeather.temp_low);
                tv_up_temp.setText(nowWeather.temp_high);
                tv_week.setText(nowWeather.week);

                tv_content_city.setText("当前城市：  " + mWeather.citynm);
                tv_weather_temp.setText("当前温度：  " + nowWeather.temperature_curr);
                tv_content_up_humidity.setText("当前湿度:   " + nowWeather.humidity);
//                tv_content_low_humidity.setText(getTextViewContent(tv_content_low_humidity) + nowWeather.humi_low);
                tv_content_wind.setText("风向：   " + nowWeather.wind);
                tv_content_winp.setText("风力：   " + nowWeather.winp);
            }
            if (null != aqiWeather) {
                tv_aqi_num.setText("空气质量：" + aqiWeather.aqi);
                tv_aqi_level.setText("空气等级：" + aqiWeather.aqi_levid);
                tv_aqi_des.setText("空气描述：" + aqiWeather.aqi_levnm);
                tv_aqi_message.setText("健康提示：" + aqiWeather.aqi_remark);

                tv_content_aqi.setText("空气质量指数：   " + aqiWeather.aqi);
                tv_content_aqi_des.setText("空气质量：   " + aqiWeather.aqi_remark);
            }
            forecastWeatherAdapter = new ForecastWeatherAdapter(mWeather.forecastWeather);
            forecastListView.setAdapter(forecastWeatherAdapter);
            setListViewHeightBasedOnChildren(forecastListView);
            forecastWeatherAdapter.notifyDataSetChanged();
        }
    }

    private String getTextViewContent(TextView tv) {
        return tv.getText().toString().trim();
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int
             i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i,
                    null, listView);
            // 计算子项View 的宽高
            listItem.measure(0,
                    0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() -
                1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    private void initData() {
        Bundle bundle = getArguments();
        CityInfo cityInfo = (CityInfo) bundle.get("key_weather_page_data");
        String cityId = cityInfo.cityid;
        mWeather = WeatherInfoDataManager.getInstance(this.getContext()).findWeatherByCityID(cityId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLoadingDialog = null;
    }
}
