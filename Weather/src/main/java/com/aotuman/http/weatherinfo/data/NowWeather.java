package com.aotuman.http.weatherinfo.data;

/**
 * Created by 凹凸曼 on 2016/12/14.
 */

public class NowWeather extends WeatherBase {
    public String days;
    public String week;
    public String updatetime;/*历史天气用到，更新时间*/
    public String temperature;/*当日温度区间*/
    public String temperature_curr;/*当前温度*/
    public String humidity;/*湿度*/
    public String weather; /*天气*/
    public String weather_icon; /*气象图标 下载*/
    public String weather_icon1; /*气象图标 下载*/
    public String wind;/*风向*/
    public String winp; /*风力*/
    public String temp_high; /*最高温度*/
    public String temp_low; /*最低温度*/
    public String humi_high; /*最大湿度*/
    public String humi_low; /*最小湿度*/
    public String weatid;/*白天天气ID*/
    public String weatid1; /*夜间天气ID*/
    public String windid; /*风向ID*/
    public String winpid;/*风力ID*/
}
