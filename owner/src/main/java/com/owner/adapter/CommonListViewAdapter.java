package com.owner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @author yhw(email:861574834@qq.com)
 * @date 2015-11-27 20:42
 * @package com.blttrs.adapter
 * @description CommonListViewAdapter  TODO(公共的ListView或者GridView的适配器类)
 * @params TODO(进入界面传参描述)
 */
public abstract class CommonListViewAdapter<T> extends BaseAdapter {

    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDatas;
    protected int mLayoutResId;

    public CommonListViewAdapter(Context mContext, int mLayoutResId, List<T> mDatas){
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mLayoutResId = mLayoutResId;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        final ViewHolder viewHolder = getViewHolder(position, convertView, parent);
//        convert(viewHolder, getItem(position), position);
//        return viewHolder.getConvertView();
//    }

    /**
     * 为了全面考虑,将position也返回
     * @param convertView
     * @param parent
     * @param position
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView,
                parent);
        convert(viewHolder, getItem(position));
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder helper, T item);

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mLayoutResId, position);
    }
}
