package com.owner;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.owner.widget.HeaderView;

/**
 * 业主设置界面
 */
public class OwnerSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private HeaderView mHeaderView;
    private LinearLayout mLlAppPwd;
    private LinearLayout mLlDoorGatePwd;
    private LinearLayout mLlAntiPwd;

    private AlertDialog.Builder mSetPwdDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_owner_setting);

        initView();
    }

    private void initView() {
        mHeaderView = ((HeaderView) findViewById(R.id.headerview_set_sys));
        mLlAppPwd = ((LinearLayout) findViewById(R.id.ll_setpwd));
        mLlDoorGatePwd = ((LinearLayout) findViewById(R.id.ll_door_gate_pwd));
        mLlAntiPwd = ((LinearLayout) findViewById(R.id.ll_anti_pwd));

        mHeaderView.setActivity(this);
        mHeaderView.setVisibility(HeaderView.HeadCompat.RIGHT, false);
        mLlAppPwd.setOnClickListener(this);
        mLlDoorGatePwd.setOnClickListener(this);
        mLlAntiPwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.ll_setpwd:
                // TODO: 15/12/11 密码设定
                showSetPwd();
                break;
            case R.id.ll_door_gate_pwd:
                // TODO: 15/12/11 门区密码设定

                break;
            case R.id.ll_anti_pwd:
                // TODO: 15/12/11 反胁迫密码设定
                break;
            default:
                break;
        }
    }

    private void showSetPwd() {
        if(mSetPwdDialog == null){
            mSetPwdDialog = new AlertDialog.Builder(this);
            mSetPwdDialog.setView(R.layout.layout_set_pwd);
        }
        mSetPwdDialog.show();
    }
}
