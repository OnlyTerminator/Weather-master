package com.aotuman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aotuman.adapter.clicklistener.OnItemClickListener;
import com.aotuman.http.cityinfo.CityInfo;
import com.aotuman.weather.R;

import java.util.List;

/**
 * Created by 凹凸曼 on 2016/12/6.
 */

public class CityNameAdapter extends RecyclerView.Adapter<CityNameAdapter.MyViewHolder> {
    private List<CityInfo> data;
    private LayoutInflater layoutInflater = null;
    private OnItemClickListener mOnItemClickLitener = null;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CONTENT = 1;
    public CityNameAdapter(List<CityInfo> data, Context context){
        this.data = data;
        if(data.isEmpty() || !"header".equals(data.get(0).type)) {
            this.data.add(0, new CityInfo("header"));
        }
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return TextUtils.isEmpty(data.get(position).type) ? TYPE_CONTENT : TYPE_HEADER;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = null;
        if(TYPE_CONTENT == viewType) {
            View view = layoutInflater.inflate(R.layout.item_city_name, parent, false);
            myViewHolder= new MyViewHolder(view);
        }else {
            View view = layoutInflater.inflate(R.layout.item__city_name_header, parent, false);
            myViewHolder= new MyViewHolder(view);
        }
        return myViewHolder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if(getItemViewType(position) == TYPE_HEADER) return;
        holder.tv_city_name.setText(data.get(position).citynm);
        if(null != mOnItemClickLitener){
            holder.ll_ba.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView,holder.getLayoutPosition());
                }
            });

            holder.ll_ba.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickLitener.onItemLongClick(holder.itemView,holder.getLayoutPosition());
                    return true;
                }
            });
        }
    }

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener){
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_city_name;
        private LinearLayout ll_ba;
        private ImageView iv;
        private TextView tv_city_des;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_city_name = (TextView) itemView.findViewById(R.id.tv_city_name);
            ll_ba = (LinearLayout) itemView.findViewById(R.id.ll_ba);
            iv = (ImageView) itemView.findViewById(R.id.iv_city_icon);
            tv_city_des = (TextView) itemView.findViewById(R.id.tv_city_des);
        }
    }
}
