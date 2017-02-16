package com.aotuman.http.callback;

/**
 * Created by 凹凸曼 on 2017/2/16.
 */

public interface HttpCallBack<T> {
    void callBackRequest();

    void callBackEntity(T object);

    void callBackError(Exception e);
}
