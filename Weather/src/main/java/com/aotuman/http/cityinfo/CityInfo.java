package com.aotuman.http.cityinfo;

import android.text.TextUtils;

import com.aotuman.http.BaseInfo;
import com.aotuman.http.okhttp.callback.StringCallback;

import java.util.List;

/**
 * Created by 凹凸曼 on 2016/12/6.
 */

public class CityInfo extends BaseInfo {
    public String weaid = "";
    public String citynm = "";
    public String cityno = "";
    public String cityid = "";
    public String area_1 = "";
    public String area_2 = "";
    public String area_3 = "";
    public String type = "";
    public String simcode = "";

    public CityInfo() {
    }

    public CityInfo(String type) {
        this.type = type;
    }

    public String getSortLetters() {
        // 正则表达式，判断首字母是否是英文字母
        String pinyin = TextUtils.isEmpty(cityno) ? simcode : cityno;
        if (!TextUtils.isEmpty(pinyin)) {
            String shortP = pinyin.substring(0,1).toUpperCase();
            if(shortP.matches("[A-Z]")) {
                return shortP;
            }
        }
        return "#";
    }

    public String getRealCityName(){
        if(!TextUtils.isEmpty(area_3) && !area_3.equals("城区")){
            return area_3;
        }else if(!TextUtils.isEmpty(area_2) && !area_2.equals("城区")){
            return area_2;
        }else if(!TextUtils.isEmpty(area_1) && !area_1.equals("城区")){
            return area_1;
        }else {
            return citynm;
        }
    }
}
