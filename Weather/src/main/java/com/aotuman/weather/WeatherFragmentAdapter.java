package com.aotuman.weather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.aotuman.fragment.CityWeatherFragment;

import java.util.ArrayList;
import java.util.List;

public class WeatherFragmentAdapter extends FragmentPagerAdapter {
	private FragmentManager mFragmentManager;
	List<Fragment> fragmentList = new ArrayList<Fragment>();
	public WeatherFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
		super(fm);
		this.mFragmentManager = fm;
		this.fragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
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
		for (Fragment f : fragmentList) {
			CityWeatherFragment wf = (CityWeatherFragment) f;
			if (wf != null) {
				ft.remove(wf);
			}
			f = null;
		}
		ft.commitAllowingStateLoss();
		mFragmentManager.executePendingTransactions();
	}
}
