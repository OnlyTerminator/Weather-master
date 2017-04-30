package com.aotuman.weather;

import android.app.Application;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;

/**
 * Created by 凹凸曼 on 2016/12/7.
 */

public class TTApplication extends Application{
    private static TTApplication instance;
    public static TTApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        PlatformConfig.setWeixin("wxafc51db4b3b38eec", "0ecaa8a9c7be6331539ee7680de796e8");
//        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        PlatformConfig.setQQZone("1106131540", "anulcPI05GA0s5im");
        Config.DEBUG = true;
        instance = this;
    }
}
