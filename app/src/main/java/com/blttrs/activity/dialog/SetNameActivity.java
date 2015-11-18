package com.blttrs.activity.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blttrs.R;
import com.blttrs.utils.ToastUtils;


public class SetNameActivity extends Activity implements View.OnClickListener {

    private EditText mEdtName;
    private Button mBtnSubmit;
    private Button mBtnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.content_set_name);

        // 获取手机分辨率的大小可以让dialog来适配不同的手机
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int padding = dm.widthPixels / 8;// 8是对话框的边距系数
        getWindow().setLayout(dm.widthPixels - padding, LinearLayout.LayoutParams.WRAP_CONTENT);

        setContentView(R.layout.layout_dialog_setname);

        initView();

    }

    private void initView() {
        mEdtName = ((EditText) findViewById(R.id.edt_setname));
        mBtnSubmit = ((Button) findViewById(R.id.btn_submit_setname));
        mBtnCancel = ((Button) findViewById(R.id.btn_cancel_setname));
        mBtnSubmit.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.btn_submit_setname:
                commitName();
                break;
            case R.id.btn_cancel_setname:
                finish();
                break;
            default:
                break;
        }
    }

    private void commitName() {
        String name = mEdtName.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            ToastUtils.showShort(this, R.string.name_not_null);
        }
        // TODO: 15/11/18  
    }
}
