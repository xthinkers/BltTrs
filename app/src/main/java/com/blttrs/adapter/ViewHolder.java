package com.blttrs.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blttrs.R;

/**
 * @author yhw(email:861574834@qq.com)
 * @date 2015-11-27 20:31
 * @package com.blttrs.adapter
 * @description ViewHolder  TODO(公用的ViewHolder类,支持所有的ListView和GridView)
 * @params TODO(进入界面传参描述)
 */
public class ViewHolder {

    private final SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    private ViewHolder(Context context, ViewGroup parent, int layoutResId, int position){

        this.mViews = new SparseArray<>();
        this.mConvertView = LayoutInflater.from(context).inflate(layoutResId, parent, false);
        this.mPosition = position;
        this.mConvertView.setTag(this);

    }

    /**
     * Get the ViewHolder Object
     * @param context
     * @param convertView
     * @param parent
     * @param layoutResId
     * @param position
     * @return
     */
    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutResId,
                                 int position){
        if(convertView == null){
            return new ViewHolder(context, parent, layoutResId, position);
        }
        return (ViewHolder) convertView.getTag();
    }

    /**
     * Get the View by viewId
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if(view == null){
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView(){
        return mConvertView;
    }

    /**
     * set the text for the TextView
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text){
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    /**
     * set resoureId for ImageView
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int drawableId){
        ImageView imageView = getView(viewId);
        imageView.setImageResource(drawableId);
        return this;
    }

    /**
     * set bitmap for ImageView
     * @param viewId
     * @param bitmap
     * @return
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap){
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bitmap);
        return this;
    }
}
