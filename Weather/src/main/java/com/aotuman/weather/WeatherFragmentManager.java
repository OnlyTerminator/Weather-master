//package com.aotuman.weather;
//
//import android.support.v4.app.Fragment;
//
//import com.aotuman.fragment.CityWeatherFragment;
//import com.aotuman.http.cityinfo.CityInfo;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by 凹凸曼 on 2016/12/10.
// */
//
//public class WeatherFragmentManager {
//    private static WeatherFragmentManager instance = null;
//    private FragmentAdapter mFragmentAdapter = null;
//    private List<Fragment> fragmentList = null;
//    private List<CityInfo> cityInfoList = null;
//    private int current = 0;
//    private WeatherFragmentManager(){
//        fragmentList = new ArrayList<Fragment>();
//        cityInfoList = new ArrayList<CityInfo>();
//    }
//    public static WeatherFragmentManager getInstance(){
//        if(null == instance){
//            instance = new WeatherFragmentManager();
//        }
//        return instance;
//    }
//
//    public void setAdapter(FragmentAdapter adapter){
//        mFragmentAdapter = adapter;
//    }
//
//    public void addCityInfo(CityInfo cityInfo){
//        cityInfoList.add(cityInfo);
//        addFragment(new CityWeatherFragment());
//    }
//
//    public void deleteCityInfo(int index){
//        cityInfoList.remove(index);
//        fragmentList.remove(index);
//    }
//    public void addFragment(List<Fragment> fragment){
//        fragmentList.addAll(fragment);
//        mFragmentAdapter.notifyDataSetChanged();
//    }
//
//    public void addFragment(Fragment fragment){
//        fragmentList.add(fragment);
//        mFragmentAdapter.notifyDataSetChanged();
//    }
//
//    public List<Fragment> getFragmentList(){
//        return fragmentList;
//    }
//
//    public FragmentAdapter getmFragmentAdapter(){
//        return mFragmentAdapter;
//    }
//
//    public void setCurrent(int index){
//        current = index;
//    }
//
//    public int getCurrent(){
//        return current;
//    }
//}
