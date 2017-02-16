package com.aotuman.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aotuman.adapter.CityNameAdapter;
import com.aotuman.adapter.clicklistener.OnItemClickListener;
import com.aotuman.basetools.L;
import com.aotuman.commontool.SPUtils;
import com.aotuman.commontool.SharePreEvent;
import com.aotuman.event.AddCityEvent;
import com.aotuman.event.DeleteCityEvent;
import com.aotuman.http.cityinfo.CityInfo;
import com.aotuman.http.weatherinfo.GetAQIWeather;
import com.aotuman.http.weatherinfo.GetForecastWeather;
import com.aotuman.http.weatherinfo.GetNowWeather;
import com.aotuman.weather.AddCityActivity;
import com.aotuman.weather.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.rxbus.RxBus;
import me.yokeyword.rxbus.RxBusSubscriber;
import me.yokeyword.rxbus.RxSubscriptions;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by 凹凸曼 on 2016/11/14.
 */

public class AddCityFragment extends Fragment {
    private View view;
    private RecyclerView rv_city_name;
    private TextView mBtnAddCity;
    private List<CityInfo> data = new ArrayList<CityInfo>();
    private CityNameAdapter adapter = null;
    private CityClickCallBack mCallback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if(null == view){
            view = inflater.inflate(R.layout.fragment_add_city,container,false);
        }

        initData();

        initView(view);

        initEvent();

        return view;
    }

    private void initEvent() {
        rv_city_name.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rv_city_name.setItemAnimator(new DefaultItemAnimator());
        rv_city_name.setAdapter(adapter);
        subscribeEvent();
        adapter.setOnItemClickLitener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(null != mCallback){
                    mCallback.onClickListion(position-1);
                }
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                Snackbar snackbar = Snackbar.make(view,"主人，您真的不需要“"+data.get(position).citynm+"”了吗？",Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.remove(position);
                        List<CityInfo> list =  new ArrayList<CityInfo>();
                        list.addAll(data);
                        list.remove(0);
                        SPUtils.put(AddCityFragment.this.getContext(),SharePreEvent.CITY_LIST,new Gson().toJson(list));
                        RxBus.getDefault().post(new DeleteCityEvent(position-1));
                        adapter.notifyDataSetChanged();
                    }
                });
                snackbar.setActionTextColor(0xffffffff);
                setSnackbarColor(snackbar,0xffffffff,0xfff44336);
                snackbar.show();
            }
        });
        mBtnAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.size() < 10){
                    startActivity(new Intent(AddCityFragment.this.getActivity(), AddCityActivity.class));
                }else {
                    Toast.makeText(AddCityFragment.this.getActivity(),"主人，最多只能添加9个，不要太贪心喔！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 设置Snackbar背景颜色
     * @param snackbar
     * @param backgroundColor
     */
    private void setSnackbarColor(Snackbar snackbar, int messageColor, int backgroundColor) {
        View view = snackbar.getView();//获取Snackbar的view
        if(view!=null){
            view.setBackgroundColor(backgroundColor);//修改view的背景色
            ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(messageColor);//获取Snackbar的message控件，修改字体颜色
        }
    }

    private void initData() {
        adapter = new CityNameAdapter(data,this.getActivity());
        Gson gson = new Gson();
        String s = (String) SPUtils.get(AddCityFragment.this.getContext(),SharePreEvent.CITY_LIST,"");
        List<CityInfo> ps = gson.fromJson(s, new TypeToken<List<CityInfo>>(){}.getType());
        if(null != ps) {
            data.addAll(ps);
            adapter.notifyDataSetChanged();
        }
    }

    private void initView(View view){
        rv_city_name = (RecyclerView) view.findViewById(R.id.rv_city_name);
        mBtnAddCity = (TextView) view.findViewById(R.id.btn_add_city);
    }

    private Subscription mRxSub;
    private void subscribeEvent() {
        RxSubscriptions.remove(mRxSub);
        mRxSub = RxBus.getDefault().toObservable(AddCityEvent.class)
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
                        data.add(myEvent.cityInfo);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        L.e("MainActivity.class", "onError");
                        /**
                         * 这里注意: 一旦订阅过程中发生异常,走到onError,则代表此次订阅事件完成,后续将收不到onNext()事件,
                         * 即 接受不到后续的任何事件,实际环境中,我们需要在onError里 重新订阅事件!
                         */
                        subscribeEvent();
                    }
                });
        RxSubscriptions.add(mRxSub);
    }

    public interface CityClickCallBack{
        void onClickListion(int position);
    }

    public void setCityOnClickListion(CityClickCallBack callBack){
        mCallback = callBack;
    }
}
