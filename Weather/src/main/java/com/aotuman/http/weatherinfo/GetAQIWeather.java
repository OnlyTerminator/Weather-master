package com.aotuman.http.weatherinfo;

import android.text.TextUtils;

import com.aotuman.basetools.L;
import com.aotuman.http.callback.HttpCallBack;
import com.aotuman.http.okhttp.OkHttpUtils;
import com.aotuman.http.okhttp.callback.StringCallback;
import com.aotuman.http.weatherinfo.data.AQIWeather;
import com.aotuman.weather.WeatherContext;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by 凹凸曼 on 2016/12/16.
 */

public class GetAQIWeather {

    public void getAQIWeather(String cityname, final HttpCallBack<AQIWeather> callBack){
        OkHttpUtils.post()
                .url(WeatherContext.aqiweather)
                .addParams("app","weather.pm25")
                .addParams("weaid",cityname)
                .addParams("appkey",WeatherContext.apppkey)
                .addParams("sign",WeatherContext.Sign)
                .addParams("format","json")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.callBackRequest();
                        callBack.callBackError(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        callBack.callBackRequest();
                        L.i("GetNowWeathwe",response);
                        if(!TextUtils.isEmpty(response)){
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.optString("success");
                                if("1".equalsIgnoreCase(success)){
                                    String result = jsonObject.optString("result");
                                    if(!TextUtils.isEmpty(result)){
                                        AQIWeather apiWeather = new Gson().fromJson(result,AQIWeather.class);
                                        callBack.callBackEntity(apiWeather);
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
