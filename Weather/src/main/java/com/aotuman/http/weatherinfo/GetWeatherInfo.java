package com.aotuman.http.weatherinfo;

import com.aotuman.http.callback.HttpCallBack;
import com.aotuman.http.callback.WeatherCallBack;
import com.aotuman.http.weatherinfo.data.AQIWeather;
import com.aotuman.http.weatherinfo.data.ForecastWeather;
import com.aotuman.http.weatherinfo.data.NowWeather;
import com.aotuman.http.weatherinfo.data.Weather;

/**
 * Created by 凹凸曼 on 2017/2/19.
 */

public class GetWeatherInfo {
    private Weather weather = new Weather();

    public void getWeather(final String cityName, final WeatherCallBack weatherCallBack) {

        new GetNowWeather().getNowWeather(cityName, new HttpCallBack<NowWeather>() {
            @Override
            public void callBackRequest() {

            }

            @Override
            public void callBackEntity(NowWeather object) {
                weather.nowWeather = object;
                new GetAQIWeather().getAQIWeather(cityName, new HttpCallBack<AQIWeather>() {
                    @Override
                    public void callBackRequest() {
                    }

                    @Override
                    public void callBackEntity(AQIWeather object) {
                        weather.aqiWeather = object;
                        new GetForecastWeather().getForecastWeather(cityName, new HttpCallBack<ForecastWeather>() {
                            @Override
                            public void callBackRequest() {

                            }

                            @Override
                            public void callBackEntity(ForecastWeather object) {
                                weather.forecastWeather = object;
                                weatherCallBack.success(weather);
                            }

                            @Override
                            public void callBackError(Exception e) {
                            }
                        });
                    }

                    @Override
                    public void callBackError(Exception e) {
                    }
                });
            }

            @Override
            public void callBackError(Exception e) {
                weatherCallBack.failed();
            }
        });
    }

}
