package com.aotuman.http.cityinfo;

import android.util.Log;

import com.aotuman.basetools.L;
import com.aotuman.database.CityInfoDataManager;
import com.aotuman.http.callback.HttpCallBack;
import com.aotuman.http.okhttp.OkHttpUtils;
import com.aotuman.http.okhttp.callback.Callback;
import com.aotuman.http.okhttp.callback.StringCallback;
import com.aotuman.weather.TTApplication;
import com.aotuman.weather.WeatherContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 凹凸曼 on 2016/12/6.
 */

public class GetWeatherCityInfo {

    public void getWeatherInfo(){
        OkHttpUtils.post()
                .url(WeatherContext.where)
                .addParams("app", "weather.city")
                .build()
                .execute(new Callback(){
                    @Override
                    public Object parseNetworkResponse(Response response1, int id) throws Exception {
                        String response = response1.body().string();
                        Log.i("weather info",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String str = jsonObject.getString("success");
                            if("1".equals(str)){
                                String result = jsonObject.getString("result");
                                Type type = new TypeToken<HashMap<String,CityInfo>>(){}.getType();
                                final HashMap<String,CityInfo> list = new Gson().fromJson(result,type);
                                L.i("aotuman",list.size()+""+TTApplication.getInstance());
                                CityInfoDataManager.getInstance(TTApplication.getInstance()).insertCityInfo(list);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        callBack.callBackRequest();
                    }

                    @Override
                    public void onResponse(Object response, int id) {
//                        callBack.callBackRequest();
                    }

                });
    }
}
