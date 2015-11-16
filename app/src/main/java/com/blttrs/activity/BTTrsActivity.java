package com.blttrs.activity;

import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.blttrs.R;
import com.blttrs.service.BluetoothLeService;

public class BTTrsActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "BTTrsActivity";
    private ToggleButton tg1_on_off, tg2_on_off;//1路开关，2路开关
    private SeekBar sb1, sb2; //1路灯控 2路灯控
    private TextView tv_message_show;//提示信息
    private Button btn_set_pwd, btn_set_new_pwd, btn_change_name;//设置秘密 设置新秘密 修改名称
    private BluetoothSocket mSocket;//通信套接字
    private BluetoothLeService mBluetoothLeService;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
                Log.e(TAG, "mBluetoothLeService is okay");

            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mBluetoothLeService = null;
            }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bttrs);
        initView();
    }

    public void setSocket(BluetoothSocket socket){
        this.mSocket = socket;
    }

    private void initView() {
        tg1_on_off = ((ToggleButton) findViewById(R.id.ig1_on_off));
        tg2_on_off = ((ToggleButton) findViewById(R.id.ig2_on_off));

        sb1 = ((SeekBar) findViewById(R.id.sb1));
        sb2 = ((SeekBar) findViewById(R.id.sb2));

        tv_message_show = ((TextView) findViewById(R.id.tv_message_show));

        btn_set_pwd = ((Button) findViewById(R.id.btn_set_pwd));
        btn_set_new_pwd = ((Button) findViewById(R.id.btn_set_new_pwd));
        btn_change_name = ((Button) findViewById(R.id.btn_change_name));

        sb1.setOnSeekBarChangeListener(this);
        sb2.setOnSeekBarChangeListener(this);

        tg1_on_off.setOnClickListener(this);
        tg2_on_off.setOnClickListener(this);
        btn_set_pwd.setOnClickListener(this);
        btn_set_new_pwd.setOnClickListener(this);
        btn_change_name.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.ig1_on_off:
                ToggleButton button_off = ((ToggleButton) v);
                boolean check_off = button_off.isChecked();

                break;
            case R.id.ig2_on_off:
                ToggleButton button_on = ((ToggleButton) v);
                boolean check_on = button_on.isChecked();

                break;
            case R.id.btn_set_pwd:
                break;
            case R.id.btn_set_new_pwd:
                break;
            case R.id.btn_change_name:
                break;
        }
    }
//
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
