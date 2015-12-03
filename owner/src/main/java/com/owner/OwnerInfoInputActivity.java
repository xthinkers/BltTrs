package com.owner;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.owner.utils.ToastUtils;
import com.owner.widget.HeaderView;

/**
 * 业主信息填写页面
 */
public class OwnerInfoInputActivity extends AppCompatActivity implements View.OnClickListener {

    private HeaderView mHeaderView;
    private EditText mEtUserName, mEtPwd, mEtPhone1, mEtPhone2, mEtPhone3, mEtStart, mEtEnd;
    private TextView mTvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.content_owner_info_input);

        initView();
    }

    private void initView() {
        mHeaderView = ((HeaderView) findViewById(R.id.hv_owner_info_input));
        mHeaderView.setActivity(this);
        mHeaderView.setVisibility(HeaderView.HeadCompat.BACK, false);

        mEtUserName = ((EditText) findViewById(R.id.et_username));
        mEtPwd = ((EditText) findViewById(R.id.et_pwd));
        mEtPhone1 = ((EditText) findViewById(R.id.et_phone1));
        mEtPhone2 = ((EditText) findViewById(R.id.et_phone2));
        mEtPhone3 = ((EditText) findViewById(R.id.et_phone3));
        mTvSubmit = ((TextView) findViewById(R.id.tv_submit));
        mEtStart = ((EditText) findViewById(R.id.et_start));
        mEtEnd = ((EditText) findViewById(R.id.et_end));

        mTvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.tv_submit:
                submit();
                break;
            default:
                break;
        }
    }

    private void submit() {
        String userName = mEtUserName.getText().toString().trim();
        String pwd = mEtPwd.getText().toString().trim();
        String phone1 = mEtPhone1.getText().toString().trim();
        String phone2 = mEtPhone2.getText().toString().trim();
        String phone3 = mEtPhone3.getText().toString().trim();
        String start = mEtStart.getText().toString().trim();
        String end = mEtEnd.getText().toString().trim();
        if(TextUtils.isEmpty(userName)){
            ToastUtils.showShort(this, "请输入用户名");
        }else if(TextUtils.isEmpty(pwd)){
            ToastUtils.showShort(this, "请输入密码");
        }else if(TextUtils.isEmpty(phone1) && TextUtils.isEmpty(phone2) && TextUtils.isEmpty(phone3)){
            ToastUtils.showShort(this, "请至少输入一个绑定手机号码");
        }

        // TODO: 15/12/2 自行保存信息动作

    }
}
