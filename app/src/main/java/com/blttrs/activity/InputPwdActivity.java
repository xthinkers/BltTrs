package com.blttrs.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blttrs.R;
import com.blttrs.utils.ToastUtils;
import com.blttrs.widget.HeaderView;

public class InputPwdActivity extends AppCompatActivity implements View.OnClickListener {

    private HeaderView mHeaderView;//公用标题栏
    private TextView mTvContacts;//联系人
    private TextView mTvStatus;//提示信息
    private EditText mEdtPwd;//密码
    private TextView mTvLock;//开门按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pwd);

        initView();

    }

    /**
     *init the InputPwdActivity
     */
    private void initView() {
        mHeaderView = ((HeaderView) findViewById(R.id.header_inputpwd));
        mHeaderView.setTitle("温馨小区王小峰");
        mHeaderView.setActivity(this);

        mTvContacts = ((TextView) findViewById(R.id.tv_alias));
        mTvStatus = ((TextView) findViewById(R.id.tv_status_connect));
        mEdtPwd = ((EditText) findViewById(R.id.edt_input_pwd));
        mTvLock = ((TextView) findViewById(R.id.tv_lock));

        mTvLock.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.tv_lock:
                String pwd = mEdtPwd.getText().toString().trim();
                if(TextUtils.isEmpty(pwd)){
                    ToastUtils.showShort(this, R.string.pwd_is_empty);
                    return;
                }
                openLock(pwd);
                ToastUtils.showShort(this, "开门");
                break;
        }
    }

    /**
     * the action to open lock
     * @param pwd
     */
    private void openLock(String pwd) {
        // TODO: 15/11/21 开锁的逻辑

    }
}
