package com.blttrs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @author yhw(email:861574834@qq.com)
 * @date 2015-11-26 19:07
 * @package com.blttrs.adapter
 * @description CommonListAdapter  TODO(界面功能描述)
 * @params TODO(进入界面传参描述)
 */
public abstract class CommonListAdapter<T> extends BaseAdapter {

    private Context mContext;
    private int mResId;
    private List<T> mData;
    private Holder mHolder;

    public CommonListAdapter(Context context, int resId){
        this.mContext = context;
        this.mResId = resId;
    }

    public CommonListAdapter(Context context, int resId, List<T> data){
        this.mContext = context;
        this.mResId = resId;
        this.mData = data;
    }

    public void setData(List<T> data){
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(mResId, null);
            mHolder = initView(convertView);
            convertView.setTag(mHolder);
        }else {
            mHolder = (Holder) convertView.getTag();
        }
        T  obj = mData.get(position);
        if(obj != null){
            initData(position, mHolder, obj);
        }
        return convertView;
    }

    public abstract Holder initView(View container);
    public abstract void initData(int position, Holder holder, T obj);
    public static class Holder {}
}
