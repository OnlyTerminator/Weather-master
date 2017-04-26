package com.aotuman.weather;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.aotuman.basetools.L;
import com.aotuman.commontool.SPUtils;
import com.aotuman.commontool.SharePreEvent;
import com.aotuman.database.CityInfoDBCreat;
import com.aotuman.event.AddCityEvent;
import com.aotuman.event.DeleteCityEvent;
import com.aotuman.fragment.AddCityFragment;
import com.aotuman.http.cityinfo.CityInfo;
import com.aotuman.http.okhttp.OkHttpUtils;
import com.aotuman.http.okhttp.callback.Callback;
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
import okhttp3.Call;
import okhttp3.Response;
import rx.Subscription;
import rx.functions.Func1;

public class MainActivity extends FragmentActivity {
    private ViewPager mPageVp;
    private FloatingActionButton flb;
    private WeatherFragmentAdapter mFragmentAdapter;
    private List<CityInfo> ps = new ArrayList<>();
    private int currentIndex;
    private AddCityFragment mCityFragment;
    private DrawerLayout mDrawerLayout;

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
        String s = (String) SPUtils.get(this, SharePreEvent.CITY_LIST, "");
        ps = gson.fromJson(s, new TypeToken<List<CityInfo>>() {
        }.getType());
        currentIndex = (int) SPUtils.get(this, SharePreEvent.CURRENT_INDX, 0);
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

        mCityFragment.setCityOnClickListion(new AddCityFragment.CityClickCallBack() {
            @Override
            public void onClickListion(int position) {
                mPageVp.setCurrentItem(position);
                mDrawerLayout.closeDrawers();
            }
        });
        flb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new ShareAction(MainActivity.this).withText("hello")
//                        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
//                        .setCallback(umShareListener).open();
                Toast.makeText(MainActivity.this,"此功能暂未开通，敬请期待！",Toast.LENGTH_SHORT).show();
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
                        L.i("MainActivity", "addFragment==============");
                        mFragmentAdapter.destroyAllFragment();
                        Gson gson = new Gson();
                        String s = (String) SPUtils.get(MainActivity.this, SharePreEvent.CITY_LIST, "");
                        ps = gson.fromJson(s, new TypeToken<List<CityInfo>>() {
                        }.getType());
                        mFragmentAdapter = new WeatherFragmentAdapter(MainActivity.this.getSupportFragmentManager(), ps);
                        mPageVp.setAdapter(mFragmentAdapter);
                        mPageVp.setOffscreenPageLimit(8);
                        mFragmentAdapter.notifyDataSetChanged();
                        if (null != ps) {
                            int index = ps.size() - 1;
                            mPageVp.setCurrentItem(index);
                            setCurrentIndex(index);
                        }
                        mDrawerLayout.closeDrawers();
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
                        L.i("MainActivity", "deleteFragment==============");
                        if (null != myEvent) {
                            mFragmentAdapter.destroyAllFragment();
                            Gson gson = new Gson();
                            String s = (String) SPUtils.get(MainActivity.this, SharePreEvent.CITY_LIST, "");
                            ps = gson.fromJson(s, new TypeToken<List<CityInfo>>() {
                            }.getType());
                            mFragmentAdapter = new WeatherFragmentAdapter(MainActivity.this.getSupportFragmentManager(), ps);
                            mPageVp.setAdapter(mFragmentAdapter);
                            mPageVp.setOffscreenPageLimit(8);
                            mFragmentAdapter.notifyDataSetChanged();
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
        new CityInfoDBCreat().loadCityDB(this);
    }

    private void initView() {
        L.i("aotuman", "is a test");
        mPageVp = (ViewPager) findViewById(R.id.id_page_vp);
        flb = (FloatingActionButton) findViewById(R.id.fab);
        mCityFragment = (AddCityFragment) getSupportFragmentManager().findFragmentById(R.id.fg_city_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            L.i("plat", "platform" + platform);

            Toast.makeText(MainActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MainActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                L.i("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void setCurrentIndex(int index) {
        currentIndex = index;
        SPUtils.put(MainActivity.this, SharePreEvent.CURRENT_INDX, currentIndex);
    }

    // 获取网络图片的数据
    public void getImage() {

        OkHttpUtils.get().url("https://www.dujin.org/sys/bing/1366.php").build().execute(new Callback<Bitmap>() {
            @Override
            public Bitmap parseNetworkResponse(Response response, int id) throws Exception {
                //response的body是图片的byte字节
                byte[] bytes = response.body().bytes();
                //response.body().close();

                //把byte字节组装成图片
                final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                return bmp;
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Bitmap response, int id) {
                if (null != response) {
                    mDrawerLayout.setBackground(new BitmapDrawable(response));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }

    }
}
