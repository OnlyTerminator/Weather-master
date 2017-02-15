package com.aotuman.event;

import com.aotuman.http.cityinfo.CityInfo;

/**
 * Created by 凹凸曼 on 2016/12/13.
 */

public class AddCityEvent {
    public CityInfo cityInfo;
    public AddCityEvent(CityInfo cityInfo){
        this.cityInfo = cityInfo;
    }
}
