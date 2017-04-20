package com.aotuman.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 凹凸曼 on 2016/12/6.
 */

public class CityInfoDataBaseHelp extends SQLiteOpenHelper{
    //类没有实例化,是不能用作父类构造器的参数,必须声明为静态

    private static final String name = "city_info.db"; //数据库名称

    private static final int version = 1; //数据库版本

    public CityInfoDataBaseHelp(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("CityInfoDataBaseHelp", "onCreate: ");
//        db.execSQL("CREATE TABLE IF NOT EXISTS cityinfo (id integer primary key autoincrement, weaid INTEGER, citynm varchar(20), cityno varchar(20), cityid varchar(20), area_1 varchar(20), area_2 varchar(20), area_3 varchar(20), simcode varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("CityInfoDataBaseHelp", "onUpgrade: "+oldVersion+newVersion);
//        if(newVersion > oldVersion){
//            // Drop tables
//            db.execSQL("DROP TABLE IF EXISTS " + name);
//            // Create tables
//            onCreate(db);
//        }
    }
}
