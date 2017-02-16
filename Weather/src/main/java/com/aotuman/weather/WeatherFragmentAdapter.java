package com.aotuman.weather;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.aotuman.fragment.CityWeatherFragment;
import com.aotuman.http.cityinfo.CityInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 天气首屏Fragment适配器
 * Created by aotuman on 2/16.
 */
public class WeatherFragmentAdapter extends PagerAdapter {
    private static final String KEY_WEATHER_PAGE_DATA = "key_weather_page_data";
    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;

    private ArrayList<Fragment.SavedState> mSavedState = new ArrayList<Fragment.SavedState>();
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private CityWeatherFragment mCurrentPrimaryItem = null;
    private List<CityInfo> mData;

    public WeatherFragmentAdapter(FragmentManager fm, List<CityInfo> cityInfos) {
        mFragmentManager = fm;
        mData = cityInfos;
    }
    private Fragment getItem(int position) {
        CityWeatherFragment fragment = new CityWeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_WEATHER_PAGE_DATA, mData.get(position));
        try {
            fragment.setArguments(bundle);
        }catch (IllegalStateException e){
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return null == mData ? 0 :mData.size();
    }

    @Override
    public void startUpdate(ViewGroup container) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mFragments.size() > position) {
            Fragment f = mFragments.get(position);
            if (f != null) {
                return f;
            }
        }

        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        Fragment fragment = getItem(position);
        if (mSavedState.size() > position) {
            Fragment.SavedState fss = mSavedState.get(position);
            if (fss != null) {
                fragment.setInitialSavedState(fss);
            }
        }
        while (mFragments.size() <= position) {
            mFragments.add(null);
        }
        fragment.setMenuVisibility(false);
        fragment.setUserVisibleHint(false);
        mFragments.set(position, fragment);
        mCurTransaction.add(container.getId(), fragment);

        return fragment;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;

        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        while (mSavedState.size() <= position) {
            mSavedState.add(null);
        }
        mSavedState.set(position, fragment.isAdded() ? mFragmentManager.saveFragmentInstanceState(fragment) : null);
        if (mFragments.size() > position)  {
            mFragments.set(position, null);
        }
        mCurTransaction.remove(fragment);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
//        MJLogger.d("WeatherFragmentViewPagerAdapter", "setPrimaryItem");
        CityWeatherFragment fragment = (CityWeatherFragment) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
//            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }

    @Override
    public Parcelable saveState() {
        Bundle state = null;
        if (mSavedState.size() > 0) {
            state = new Bundle();
            Fragment.SavedState[] fss = new Fragment.SavedState[mSavedState.size()];
            mSavedState.toArray(fss);
            state.putParcelableArray("states", fss);
        }
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment f = mFragments.get(i);
            if (f != null && f.isAdded()) {
                if (state == null) {
                    state = new Bundle();
                }
                String key = "f" + i;
                mFragmentManager.putFragment(state, key, f);
            }
        }
        return state;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (state != null) {
            Bundle bundle = (Bundle) state;
            bundle.setClassLoader(loader);
            Parcelable[] fss = bundle.getParcelableArray("states");
            mSavedState.clear();
            mFragments.clear();
            if (fss != null) {
                for (int i = 0; i < fss.length; i++) {
                    mSavedState.add((Fragment.SavedState) fss[i]);
                }
            }
            Iterable<String> keys = bundle.keySet();
            for (String key : keys) {
                if (key.startsWith("f")) {
                    int index = Integer.parseInt(key.substring(1));
                    Fragment f = mFragmentManager.getFragment(bundle, key);
                    if (f != null) {
                        while (mFragments.size() <= index) {
                            mFragments.add(null);
                        }
                        f.setMenuVisibility(false);
                        mFragments.set(index, f);
                    } else {
                    }
                }
            }
        }
    }

    public CityWeatherFragment getPrimaryItem() {
        return mCurrentPrimaryItem;
    }




    /**
     * 销毁所有Fragment：无法动态删除某个Fragment 或 移动Fragment在ViewPager中的位置，目前沿用5.0主版本的处理方式。
     * Added by hebin.yang
     * TODO 待优化
     */
    public void destroyAllFragment() {
        if (null == mFragmentManager) {
            return;
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        for (Fragment f : mFragments) {
            CityWeatherFragment wf = (CityWeatherFragment) f;
            if (wf != null) {
                ft.remove(wf);
            }
            f = null;
        }
        ft.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
        mFragments.clear();
    }

}
