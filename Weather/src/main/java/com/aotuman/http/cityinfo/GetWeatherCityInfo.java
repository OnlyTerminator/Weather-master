package com.aotuman.http.cityinfo;

import android.content.Context;
import android.util.Log;

import com.aotuman.basetools.L;
import com.aotuman.database.CityInfoDataManager;
import com.aotuman.http.callback.HttpCallBack;
import com.aotuman.http.okhttp.OkHttpUtils;
import com.aotuman.http.okhttp.callback.Callback;
import com.aotuman.http.okhttp.callback.StringCallback;
import com.aotuman.weather.R;
import com.aotuman.weather.TTApplication;
import com.aotuman.weather.WeatherContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
//        OkHttpUtils.post()
//                .url(WeatherContext.where)
//                .addParams("app", "weather.city")
//                .build()
//                .execute(new Callback(){
//                    @Override
//                    public Object parseNetworkResponse(Response response1, int id) throws Exception {
//                        String response = response1.body().string();
//                        Log.i("weather info",response);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String str = jsonObject.getString("success");
//                            if("1".equals(str)){
//                                String result = jsonObject.getString("result");
//                                Type type = new TypeToken<HashMap<String,CityInfo>>(){}.getType();
//                                final HashMap<String,CityInfo> list = new Gson().fromJson(result,type);
//                                L.i("aotuman",list.size()+""+TTApplication.getInstance());
//                                CityInfoDataManager.getInstance(TTApplication.getInstance()).insertCityInfo(list);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        return null;
//                    }
//
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
////                        callBack.callBackRequest();
//                    }
//
//                    @Override
//                    public void onResponse(Object response, int id) {
////                        callBack.callBackRequest();
//                    }
//
//                });

    }

    /**
     * 读取raw目录下的城市数据库文件，只load一次
     */
    private void loadCityDB(Context mContext) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            File localFile = mContext.getDatabasePath("cityinfo.db");
            if (!localFile.exists()) {
                is = mContext.getResources().openRawResource(R.raw.city_info);
                fos = new FileOutputStream(localFile);
                byte[] buffer = new byte[8192];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
        } catch (Exception e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
