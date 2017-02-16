package com.aotuman.http.weatherinfo;

import android.text.TextUtils;

import com.aotuman.basetools.L;
import com.aotuman.http.okhttp.OkHttpUtils;
import com.aotuman.http.okhttp.callback.StringCallback;
import com.aotuman.http.weatherinfo.data.ForecastWeather;
import com.aotuman.weather.WeatherContext;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by 凹凸曼 on 2016/12/16.
 */

public class GetForecastWeather {

    public void getForecastWeather(String cityname){
        OkHttpUtils.post()
                .url(WeatherContext.forecastweather)
                .addParams("app","weather.future")
                .addParams("weaid",cityname)
                .addParams("appkey",WeatherContext.apppkey)
                .addParams("sign",WeatherContext.Sign)
                .addParams("format","json")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        L.i("GetNowWeathwe",response);
                        if(!TextUtils.isEmpty(response)){
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.optString("success");
                                if("1".equalsIgnoreCase(success)){
                                    String result = jsonObject.optString("result");
                                    if(!TextUtils.isEmpty(result)){
                                        ForecastWeather nowWeather = new Gson().fromJson(response,ForecastWeather.class);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
