package com.blttrs.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blttrs.BltTsConstants;
import com.blttrs.MainActivity;
import com.blttrs.R;
import com.blttrs.adapter.DeviceListAdapter;
import com.blttrs.broadcast.GattUpdateReceiver;
import com.blttrs.service.BluetoothLeService;
import com.blttrs.utils.ToastUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * scan the device and list the device to the device list
 */
public class DeviceScanActivity extends AppCompatActivity {

    public static final String TAG = "DeviceScanActivity";
    private boolean isBTConnected = false;// 蓝牙是否连接

    private BluetoothLeService mBluetoothLeService;
    private GattUpdateReceiver mGattUpdateReceiver;
    private BluetoothAdapter mBluetoothAdapter;
    private DeviceListAdapter mDeviceListAdapter;
    private String mDeviceAddress;
    private BluetoothDevice mDevice;

    private ListView listview_device;
    private Button btn_scan;

    private List<BluetoothDevice> mBluetoothDevices;

    private boolean mScanning = false;
    public static final int SCAN_PERIOD = 60000;//扫描的时间
    private static final int REQUEST_CODE_BLUETOOTH = 1;
    private Handler mHandler1;

    private TimerTask mTask1;
    private Timer mTimer1;
    private TimerTask mTask2;
    private Timer mTimer2;
    private TimerTask mTask3;
    private Timer mTimer3;

    private Handler mHandler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String str = mDevice.getName();
                    if (str == null) {
                        str = "未知设备";
                    }
//                    startActivity(intent);
                    break;

                case 2:
//                    String str2 = md.getName();
//                    if (str2 == null) {
//                        str2 = "未知设备";
//                    }

                    mBluetoothLeService.connect(mDeviceAddress);
//					devicename.setText("连接失败,请等待重新连接");
                    break;

                case 3:
                    if (isBTConnected) {
//					devicename.setText("重连成功");

//                        startActivity(intent);
                    }else {
//						devicename.setText("重连失败，请点击重试");
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };


    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName,
                                       IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.i(TAG, " Unable to initialize Bluetooth ");
                finish();
            }
            Log.i(TAG, " mBluetoothLeService is okay");
//  			mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            ToastUtils.showShort(this, R.string.ble_not_supported);
            finish();
            return;
        }

        mHandler1 = new Handler();
        final BluetoothManager bluetoothManager = ((BluetoothManager) getSystemService(BLUETOOTH_SERVICE));
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            ToastUtils.showShort(this, R.string.error_bluetooth_not_supported);
            finish();
            return;
        }

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        Log.i(TAG, " Try to bindService = " + bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE));

        setContentView(R.layout.activity_device_scan);

        initView();

        Log.i(TAG, " onCreate ");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG, " onResume ");

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_CODE_BLUETOOTH);
            }
        }

        // Initializes list view adapter.
        mBluetoothDevices = new ArrayList<>();
        mDeviceListAdapter = new DeviceListAdapter(DeviceScanActivity.this, mBluetoothDevices);
        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
        if(bondedDevices.size() > 0){
            for (BluetoothDevice device : bondedDevices){
                mBluetoothDevices.add(device);
            }
        }
        listview_device.setAdapter(mDeviceListAdapter);

        //注册广播接收者
        mGattUpdateReceiver = new GattUpdateReceiver(mDeviceListAdapter);
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, " onActivityResult ");
        // User chose not to enable Bluetooth. 拒绝开启蓝牙
        if (requestCode == REQUEST_CODE_BLUETOOTH && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        scanDevice(false);
//        mDeviceListAdapter.clear();
//    }

    /**
     * 扫描周围设备
     * @param enabled 是否扫描
     */
    private void scanDevice(boolean enabled) {
        if (enabled) {
            // Stops scanning after a pre-defined scan period.
            mHandler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mScanning) {
                        //10 sec
                        mScanning = false;
                        mBluetoothAdapter.stopLeScan(mScanCallback);
                        if(mBluetoothAdapter.isDiscovering()){
                            mBluetoothAdapter.cancelDiscovery();
                        }
//                		invalidateOptionsMenu();
                        btn_scan.setText("Scan");
                    }
                }
            }, SCAN_PERIOD);
            btn_scan.setText("Scaning");
            mScanning = true;
            //F000E0FF-0451-4000-B000-000000000000
//            mBluetoothAdapter.startLeScan(mScanCallback);
            //搜索周围的蓝牙设备
            if(mBluetoothAdapter.isDiscovering()){
                mBluetoothAdapter.cancelDiscovery();
            }
            mBluetoothAdapter.startDiscovery();
            Log.i(TAG, " scanLeDevice ");
        } else {
            //停止
            btn_scan.setText("Scan");
            mScanning = false;
//            mBluetoothAdapter.stopLeScan(mScanCallback);
            if(mBluetoothAdapter.isDiscovering()){
                mBluetoothAdapter.cancelDiscovery();
            }
        }
//        invalidateOptionsMenu();
//        buttonScan.setText("Stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scanDevice(false);
        mDeviceListAdapter.clear();
        //取消注册广播
        if(mGattUpdateReceiver != null){
            unregisterReceiver(mGattUpdateReceiver);
        }
        //取消绑定服务
        if(mServiceConnection != null){
            unbindService(mServiceConnection);
        }
    }

    private IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);//
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
//        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
//        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        intentFilter.setPriority(1000);
        return intentFilter;
    }

    private void initView() {
        btn_scan = ((Button) findViewById(R.id.btn_scan));
        listview_device = ((ListView) findViewById(R.id.listview_device));
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 15/11/11
                if(!mScanning){
                    scanDevice(true);
                }
            }
        });

        listview_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //// TODO: 15/11/11
                mDevice = mDeviceListAdapter.getDevice(position);
                if (mDevice == null) return;

//                if (mServiceConnection == null) {
//                    Intent gattServiceIntent = new Intent(DeviceScanActivity.this, BluetoothLeService.class);
//                    Log.i(TAG, "Try to bindService=" + bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE));
//                    registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
//                }

                mDeviceAddress = mDevice.getAddress();
                Log.i(TAG, "service"+mBluetoothLeService);

                //先判断是否配对
//                int status = mDevice.getBondState();
//                switch (status){
//                    case BluetoothDevice.BOND_NONE:
//                        try {
//                            Method bondMethod = BluetoothDevice.class.getMethod("createBond");
//                            bondMethod.invoke(mDevice);
//                        } catch (NoSuchMethodException e) {
//                            e.printStackTrace();
//                        } catch (InvocationTargetException e) {
//                            e.printStackTrace();
//                        } catch (IllegalAccessException e) {
//                            e.printStackTrace();
//                        }
//                        break;
//                    case BluetoothDevice.BOND_BONDING:
//
//                        break;
//                    case BluetoothDevice.BOND_BONDED:
//                        mBluetoothLeService.connect(mDeviceAddress);
//                        break;
//                }

                mBluetoothLeService.connect(mDeviceAddress);

                final Intent intent = new Intent(DeviceScanActivity.this, BTTrsActivity.class);
                intent.putExtra(BltTsConstants.EXTRAS_DEVICE_NAME, mDevice.getName());
                intent.putExtra(BltTsConstants.EXTRAS_DEVICE_ADDRESS, mDevice.getAddress());

                if (mScanning) {
                    mBluetoothAdapter.stopLeScan(mScanCallback);
                    mScanning = false;
                }

                if (mDeviceAddress == null || mDeviceAddress == "" ) {
                    ToastUtils.showShort(DeviceScanActivity.this, R.string.connected_failed);
                    return;
                }

                //计时器任务1
                mTask1 = new TimerTask() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 2;
                        mHandler2.sendMessage(message);
                    }
                };

                //计时器任务2
                mTask2 = new TimerTask() {
                    @Override
                    public void run() {
                        if (isBTConnected) {
                            Message message = new Message();
                            message.what = 1;
                            mHandler2.sendMessage(message);
                        }else {
                            mTimer1 = new Timer();
                            mTimer1.schedule(mTask1, 1000);
                            mTimer3 = new Timer();
                            mTimer3.schedule(mTask3, 2000);
                        }
                    }
                };

                //800ms
                mTimer2 = new Timer();
                mTimer2.schedule(mTask2, 800);

                mTask3 = new TimerTask() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 3;
                        mHandler2.sendMessage(message);
                    }
                };
            }
        });



        TextView textView = new TextView(this);
        textView.setText(" Please press the scan button to scan the device ");
        listview_device.setEmptyView(textView);
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                //扫描到蓝牙设备后进行数据添加 并进行数据更新
                public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                    Log.i(TAG, " BluetoothAdapter.LeScanCallback's method onLeScan ");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mDeviceListAdapter.addDevice(device, rssi, scanRecord);
                            mDeviceListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

}
