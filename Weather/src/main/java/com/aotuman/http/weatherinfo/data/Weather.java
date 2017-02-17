package com.aotuman.http.weatherinfo.data;

/**
 * Created by 凹凸曼 on 2016/12/16.
 */

public class Weather extends WeatherBase {
    public NowWeather nowWeather;
    public AQIWeather aqiWeather;
    public ForecastWeather forecastWeather;
    public HistoryWeather historyWeather;

    public Weather() {
    }

    public Weather(String cityid, String citynm, String cityno) {
        this.cityid = cityid;
        this.citynm = citynm;
        this.cityno = cityno;
    }

    public void setWeatherInfo(NowWeather nowWeather, AQIWeather aqiWeather, HistoryWeather historyWeather, ForecastWeather forecastWeather) {
        this.nowWeather = nowWeather;
        this.aqiWeather = aqiWeather;
        this.forecastWeather = forecastWeather;
        this.historyWeather = historyWeather;
    }
}
