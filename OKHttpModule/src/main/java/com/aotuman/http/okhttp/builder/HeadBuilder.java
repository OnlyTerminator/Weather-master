package com.aotuman.http.okhttp.builder;

import com.aotuman.http.okhttp.OkHttpUtils;
import com.aotuman.http.okhttp.request.OtherRequest;
import com.aotuman.http.okhttp.request.RequestCall;

/**
 * Created by aotuman on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
