package com.owner.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.owner.R;


/**
 * Created by hongweiyu on 15/11/21.
 */
public class HeaderView extends LinearLayout implements View.OnClickListener {

    private static final String  TAG = "HeaderView";

    public enum HeadCompat{
        BACK, TITLE,RIGHT
    }

    private String title;

    private TextView mBack;
    private TextView mTitle;
    private TextView mRight;

    private Activity mActivity;

    public OnClickCallBack onClickLisenter;

    public OnClickCallBack getOnClickCallBackLisenter() {
        return onClickLisenter;
    }

    public void setOnClickCallBackLisenter(OnClickCallBack onClickLisenter) {
        this.onClickLisenter = onClickLisenter;
    }

    public void setActivity(Activity mActivity){
        this.mActivity = mActivity;
    }

    public Activity getActivity(){
        return mActivity;
    }

    public HeaderView(Context context) {
        this(context, null);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context
                .obtainStyledAttributes(attrs, R.styleable.HeaderView);
        title = a.getString(R.styleable.HeaderView_headtitle);
        a.recycle();

        View root = LayoutInflater.from(context).inflate(R.layout.layout_headerview, this);
        mBack = ((TextView) root.findViewById(R.id.tv_back));
        mTitle = ((TextView) root.findViewById(R.id.tv_title));
        mRight = ((TextView) root.findViewById(R.id.tv_right));

        if(!TextUtils.isEmpty(title)){
            mTitle.setText(title);
        }
        mBack.setOnClickListener(this);
    }

    public void setVisibility(HeadCompat compatName ,boolean isShow){
        switch (compatName){
            case BACK:
                mBack.setVisibility(isShow ? View.VISIBLE : View.GONE);
                break;
            case TITLE:
                mTitle.setVisibility(isShow ? View.VISIBLE : View.GONE);
                break;
            case RIGHT:
                mRight.setVisibility(isShow ? View.VISIBLE : View.GONE);
                break;
        }
    }

    public void setTitle(String title){
        if(!TextUtils.isEmpty(title)){
            mTitle.setText(title);
        }
    }

    public void setBackGround(HeadCompat compat, int res){
        switch (compat){
            case BACK:
                mBack.setBackgroundResource(res);
                break;
            case TITLE:
                mTitle.setBackgroundResource(res);
                break;
            case RIGHT:
                mRight.setBackgroundResource(res);
                break;
            default:
                break;
        }
    }

    public void setString(HeadCompat compat, String str){
        switch (compat){
            case BACK:
                mBack.setText(str);
                break;
            case TITLE:
                mTitle.setText(str);
                break;
            case RIGHT:
                mRight.setText(str);
                break;
            default:
                break;
        }
    }

    public String getTitle(){
        return mTitle.getText().toString().trim();
    }

    public void setClilckListener(HeadCompat compat){
        switch (compat){
            case RIGHT:
                mRight.setOnClickListener(this);
                break;
            default:
                break;
        }

    }

    public interface OnClickCallBack{
        void onClickCallBack(HeadCompat headCompat);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tv_back:
                if(mActivity != null){
                    mActivity.finish();
                }
                break;
            case R.id.tv_right:
                onClickLisenter.onClickCallBack(HeadCompat.RIGHT);
                break;
        }
    }
}
