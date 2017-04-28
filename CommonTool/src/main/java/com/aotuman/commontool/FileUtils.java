package com.aotuman.commontool;

import android.content.Context;

import java.io.File;

/**
 * Created by aotuman on 2017/4/28.
 */

public class FileUtils {
    public static File getFilesDir(Context context, String folder) {
        File file = context.getExternalFilesDir(folder);
        if (null == file) {
            file = context.getFilesDir();
        }
        return file;
    }
}
