package com.aotuman.http.callback;

import com.aotuman.http.weatherinfo.data.Weather;

/**
 * Created by 凹凸曼 on 2017/2/19.
 */

public interface WeatherCallBack {
    void success(Weather weather);

    void failed();
}
