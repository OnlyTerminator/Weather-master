package com.aotuman.http.weatherinfo;

import android.text.TextUtils;

import com.aotuman.basetools.L;
import com.aotuman.database.WeatherInfoDataBaseHelp;
import com.aotuman.database.WeatherInfoDataManager;
import com.aotuman.http.callback.HttpCallBack;
import com.aotuman.http.okhttp.OkHttpUtils;
import com.aotuman.http.okhttp.callback.StringCallback;
import com.aotuman.http.weatherinfo.data.NowWeather;
import com.aotuman.http.weatherinfo.data.Weather;
import com.aotuman.weather.TTApplication;
import com.aotuman.weather.WeatherContext;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by 凹凸曼 on 2016/12/16.
 */

public class GetNowWeather {

    public void getNowWeather(String cityname, final HttpCallBack<NowWeather> callBack){
        OkHttpUtils.post()
                .url(WeatherContext.nowweather)
                .addParams("app","weather.today")
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
                        L.i("GetNowWeathwe",response);
                        callBack.callBackRequest();
                        if(!TextUtils.isEmpty(response)){
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.optString("success");
                                if("1".equalsIgnoreCase(success)){
                                    String result = jsonObject.optString("result");
                                    if(!TextUtils.isEmpty(result)){
                                        NowWeather nowWeather = new Gson().fromJson(result,NowWeather.class);
                                        callBack.callBackEntity(nowWeather);
                                        Weather weather = new Weather(nowWeather.cityid,nowWeather.citynm,nowWeather.cityno);
                                        weather.setWeatherInfo(nowWeather,null,null,null);
                                        WeatherInfoDataManager.getInstance(TTApplication.getInstance()).insertWeatherInfo(weather);
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
