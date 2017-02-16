package com.aotuman.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.aotuman.basetools.L;
import com.aotuman.commontool.SPUtils;
import com.aotuman.commontool.SharePreEvent;
import com.aotuman.event.AddCityEvent;
import com.aotuman.event.DeleteCityEvent;
import com.aotuman.http.cityinfo.CityInfo;
import com.aotuman.http.cityinfo.GetWeatherCityInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.rxbus.RxBus;
import me.yokeyword.rxbus.RxBusSubscriber;
import me.yokeyword.rxbus.RxSubscriptions;
import rx.Subscription;
import rx.functions.Func1;

public class MainActivity extends FragmentActivity {
	private ViewPager mPageVp;
	private FloatingActionButton flb;
	private WeatherFragmentAdapter mFragmentAdapter;
	private List<CityInfo> ps = new ArrayList<>();
	private int currentIndex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initData();

		initView();

		initEvent();
	}

	private void initEvent() {
		Gson gson = new Gson();
		String s = (String) SPUtils.get(this,SharePreEvent.CITY_LIST,"");
		ps = gson.fromJson(s, new TypeToken<List<CityInfo>>(){}.getType());
		currentIndex = (int) SPUtils.get(this,SharePreEvent.CURRENT_INDX,0);
		mFragmentAdapter = new WeatherFragmentAdapter(this.getSupportFragmentManager(), ps);
		mPageVp.setAdapter(mFragmentAdapter);
		mPageVp.setCurrentItem(currentIndex);
		mPageVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			/**
			 * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
			 */
			@Override
			public void onPageScrollStateChanged(int state) {
			}

			/**
			 * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
			 * offsetPixels:当前页面偏移的像素位置
			 */
			@Override
			public void onPageScrolled(int position, float offset,
									   int offsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				setCurrentIndex(position);
			}
		});

		flb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new ShareAction(MainActivity.this).withText("hello")
						.setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
						.setCallback(umShareListener).open();
			}
		});

		addSubscribeEvent();
		deleteSubscribeEvent();
	}
	private Subscription mAddRxSub;
	private void addSubscribeEvent() {
		RxSubscriptions.remove(mAddRxSub);
		mAddRxSub = RxBus.getDefault().toObservable(AddCityEvent.class)
				.map(new Func1<AddCityEvent, AddCityEvent>() {
					@Override
					public AddCityEvent call(AddCityEvent event) {
						// 变换等操作
						return event;
					}
				})
				.subscribe(new RxBusSubscriber<AddCityEvent>() {
					@Override
					protected void onEvent(AddCityEvent myEvent) {
						L.i("MainActivity","addFragment==============");
						mFragmentAdapter.destroyAllFragment();
//								list.clear();
						Gson gson = new Gson();
						String s = (String) SPUtils.get(MainActivity.this,SharePreEvent.CITY_LIST,"");
						ps = gson.fromJson(s, new TypeToken<List<CityInfo>>(){}.getType());
						mFragmentAdapter = new WeatherFragmentAdapter(MainActivity.this.getSupportFragmentManager(), ps);
						mPageVp.setAdapter(mFragmentAdapter);
						mPageVp.setOffscreenPageLimit(8);
						mFragmentAdapter.notifyDataSetChanged();
//						mPageVp.setCurrentItem(index);
//						setCurrentIndex(index);
					}

					@Override
					public void onError(Throwable e) {
						super.onError(e);
						L.e("MainActivity.class", "onError");
						/**
						 * 这里注意: 一旦订阅过程中发生异常,走到onError,则代表此次订阅事件完成,后续将收不到onNext()事件,
						 * 即 接受不到后续的任何事件,实际环境中,我们需要在onError里 重新订阅事件!
						 */
						addSubscribeEvent();
					}
				});
		RxSubscriptions.add(mAddRxSub);
	}

	private Subscription mDeleteRxSub;
	private void deleteSubscribeEvent() {
		RxSubscriptions.remove(mDeleteRxSub);
		mDeleteRxSub = RxBus.getDefault().toObservable(DeleteCityEvent.class)
				.map(new Func1<DeleteCityEvent, DeleteCityEvent>() {
					@Override
					public DeleteCityEvent call(DeleteCityEvent event) {
						// 变换等操作
						return event;
					}
				})
				.subscribe(new RxBusSubscriber<DeleteCityEvent>() {
					@Override
					protected void onEvent(DeleteCityEvent myEvent) {
						L.i("MainActivity","deleteFragment==============");
						if(null != myEvent) {
//							if(myEvent.fragmentIndex < list.size()){
								mFragmentAdapter.destroyAllFragment();
//								list.clear();
								Gson gson = new Gson();
								String s = (String) SPUtils.get(MainActivity.this,SharePreEvent.CITY_LIST,"");
								ps = gson.fromJson(s, new TypeToken<List<CityInfo>>(){}.getType());
								mFragmentAdapter = new WeatherFragmentAdapter(MainActivity.this.getSupportFragmentManager(), ps);
								mPageVp.setAdapter(mFragmentAdapter);
								mPageVp.setOffscreenPageLimit(8);
								mFragmentAdapter.notifyDataSetChanged();
//								int index = currentIndex < list.size() ? currentIndex : 0;
//								mPageVp.setCurrentItem(index);
//								setCurrentIndex(index);
//							}
						}
					}

					@Override
					public void onError(Throwable e) {
						super.onError(e);
						L.e("MainActivity.class", "onError");
						/**
						 * 这里注意: 一旦订阅过程中发生异常,走到onError,则代表此次订阅事件完成,后续将收不到onNext()事件,
						 * 即 接受不到后续的任何事件,实际环境中,我们需要在onError里 重新订阅事件!
						 */
						deleteSubscribeEvent();
					}
				});
		RxSubscriptions.add(mDeleteRxSub);
	}

	private void initData() {
		new GetWeatherCityInfo().getWeatherInfo();
	}

	private void initView() {
		L.i("aotuman","is a test");
		mPageVp = (ViewPager) this.findViewById(R.id.id_page_vp);
		flb = (FloatingActionButton) findViewById(R.id.fab);
	}

	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			L.i("plat","platform"+platform);

			Toast.makeText(MainActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(MainActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
			if(t!=null){
				L.i("throw","throw:"+t.getMessage());
			}
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(MainActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}

	private void setCurrentIndex(int index){
		currentIndex = index;
		SPUtils.put(MainActivity.this,SharePreEvent.CURRENT_INDX,currentIndex);
	}
}
