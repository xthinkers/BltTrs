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

public class SetPwdActivity extends Activity implements View.OnClickListener {

    private EditText edt_pwd;
    private Button btn_submit;
    private Button btn_cancel;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.content_set_pwd);

        // 获取手机分辨率的大小可以让dialog来适配不同的手机
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int padding = dm.widthPixels / 8;// 8是对话框的边距系数
        getWindow().setLayout(dm.widthPixels - padding, LinearLayout.LayoutParams.WRAP_CONTENT);

        setContentView(R.layout.layout_dialog_setpwd);

        initView();
    }

    //初始化 视图
    private void initView() {
        edt_pwd = ((EditText) findViewById(R.id.edt_pwd));
        btn_submit = ((Button) findViewById(R.id.btn_submit));
        btn_cancel = ((Button) findViewById(R.id.btn_cancel));
        btn_submit.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.btn_submit:
                commitPwd();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    //设置密码
    private void commitPwd() {
        String text = edt_pwd.getText().toString().trim();
        if(TextUtils.isEmpty(text)){
            ToastUtils.showShort(context, R.string.pwd_must_not_null);
        }
        
        // TODO: 15/11/18
    }
}
