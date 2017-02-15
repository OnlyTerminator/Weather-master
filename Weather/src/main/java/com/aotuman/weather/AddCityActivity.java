package com.aotuman.weather;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aotuman.database.CityInfoDataManager;
import com.aotuman.event.AddCityEvent;
import com.aotuman.event.CityChangeEvent;
import com.aotuman.http.cityinfo.CityInfo;
import com.aotuman.view.sortlistview.CharacterParser;
import com.aotuman.view.sortlistview.ClearEditText;
import com.aotuman.view.sortlistview.PinyinComparator;
import com.aotuman.view.sortlistview.SideBar;
import com.aotuman.view.sortlistview.SortAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.yokeyword.rxbus.RxBus;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AddCityActivity extends Activity {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<CityInfo> SourceDateList = new ArrayList<CityInfo>();

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_city);
		initViews();
	}

	private void initViews() {
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		//设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				//该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				//这里要利用adapter.getItem(position)来获取当前position所对应的对象
				CityInfo cityInfo = (CityInfo)adapter.getItem(position);
				Toast.makeText(getApplication(), cityInfo.citynm+"添加成功", Toast.LENGTH_SHORT).show();
				RxBus.getDefault().post(new AddCityEvent(cityInfo));
				finish();
			}
		});

		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);
		filledData();

		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

		//根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}


	/**
	 * 为ListView填充数据
	 * @return
	 */
	private void filledData() {
		Observable.create(new Observable.OnSubscribe<List<CityInfo>>() {

			@Override
			public void call(Subscriber<? super List<CityInfo>> subscriber) {
				subscriber.onNext(CityInfoDataManager.getInstance(AddCityActivity.this).findAllCitys());
				subscriber.onCompleted();
			}
		})
				.subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
				.observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
				.subscribe(new Action1<List<CityInfo>>() {
					@Override
					public void call(List<CityInfo> list) {
						// 根据a-z进行排序源数据
						Collections.sort(list, pinyinComparator);
						SourceDateList.clear();
						SourceDateList.addAll(list);
						adapter.notifyDataSetChanged();
					}
				});
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<CityInfo> filterDateList = new ArrayList<CityInfo>();

		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(CityInfo sortModel : SourceDateList){
				String name = sortModel.citynm;
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

}
