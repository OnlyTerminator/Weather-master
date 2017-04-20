package com.aotuman.database;

import android.content.Context;

import com.aotuman.weather.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 凹凸曼 on 2016/12/6.
 */

public class CityInfoDBCreat {

    /**
     * 读取raw目录下的城市数据库文件，只load一次
     */
    public void loadCityDB(Context mContext) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            String path = "/data/data/" + mContext.getPackageName() + "/databases";
            File file_ = new File(path);
            if (!file_.exists()) {
                file_.mkdirs();
            }
            File localFile = mContext.getDatabasePath("city_info.db");
            if (!localFile.exists()) {
                is = mContext.getResources().openRawResource(R.raw.city_info);
                fos = new FileOutputStream(localFile);
                byte[] buffer = new byte[1024];
                int count = -1;
                while ((count = is.read(buffer)) != -1) {
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
