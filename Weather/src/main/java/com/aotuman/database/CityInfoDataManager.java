package com.aotuman.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.aotuman.http.cityinfo.CityInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 凹凸曼 on 2016/12/6.
 */

public class CityInfoDataManager {
    private static CityInfoDataBaseHelp cityInfoDataBaseHelp = null;
    private static CityInfoDataManager instance;
    private Context context;
    public CityInfoDataManager(Context context){
        this.context = context;
        cityInfoDataBaseHelp = new CityInfoDataBaseHelp(context);
    }

    public static CityInfoDataManager getInstance(Context context){
        if(null == instance){
            instance = new CityInfoDataManager(context);
        }
        return instance;
    }

    public List<CityInfo> findCitysByName(String cityName){
        List<CityInfo> cityInfoList = new ArrayList<>();
        String sql = "select * from cityinfo where citynm like ? or area_1 like ?";
        SQLiteDatabase db = cityInfoDataBaseHelp.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery(sql,new String[] {"%" + cityName + "%","%" + cityName + "%"});
        if(null != cursor) {
            while (cursor.moveToNext()) {
                CityInfo cityInfo = new CityInfo();
                cityInfo.weaid = cursor.getInt(1)+""; //获取第一列的值,第一列的索引从0开始
                cityInfo.citynm = cursor.getString(2);//获取第二列的值
                cityInfo.cityno = cursor.getString(3);//获取第三列的值
                cityInfo.cityid = cursor.getString(4);
                cityInfo.area_1 = cursor.getString(5);
                cityInfo.area_2 = cursor.getString(6);
                cityInfo.area_3 = cursor.getString(7);
//                cityInfo.simcode = cursor.getString(9);
                cityInfoList.add(cityInfo);
            }
            cursor.close();
        }
        db.close();
        return cityInfoList;
    }

    public List<CityInfo> findAllCitys(){
        List<CityInfo> cityInfoList = new ArrayList<>();
        String sql = "select * from cityinfo";
        SQLiteDatabase db = cityInfoDataBaseHelp.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery(sql,new String[] {});
        if(null != cursor) {
            while (cursor.moveToNext()) {
                CityInfo cityInfo = new CityInfo();
                cityInfo.weaid = cursor.getInt(1)+""; //获取第一列的值,第一列的索引从0开始
                cityInfo.citynm = cursor.getString(2);//获取第二列的值
                cityInfo.cityno = cursor.getString(3);//获取第三列的值
                cityInfo.cityid = cursor.getString(4);
                cityInfo.area_1 = cursor.getString(5);
                cityInfo.area_2 = cursor.getString(6);
                cityInfo.area_3 = cursor.getString(7);
//                cityInfo.simcode = cursor.getString(9);
                if(TextUtils.isEmpty(cityInfo.area_3)) {
                    cityInfoList.add(cityInfo);
                }
            }
            cursor.close();
        }
        db.close();
        return cityInfoList;
    }

}
