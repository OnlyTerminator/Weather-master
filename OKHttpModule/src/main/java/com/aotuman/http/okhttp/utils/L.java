package com.aotuman.http.okhttp.utils;

import android.util.Log;

/**
 * Created by aotuman on 15/11/6.
 */
public class L
{
    private static boolean debug = false;

    public static void e(String msg)
    {
        if (debug)
        {
            Log.e("OkHttp", msg);
        }
    }

}

