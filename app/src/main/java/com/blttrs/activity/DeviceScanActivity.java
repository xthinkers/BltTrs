package com.blttrs.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.blttrs.BltTsConstants;
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

import me.drakeet.materialdialog.MaterialDialog;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * scan the device and list the device to the device list
 */
public class DeviceScanActivity extends Activity {

    public static final String TAG = "DeviceScanActivity";
    private boolean isBTConnected = false;// 蓝牙是否连接
    private BluetoothLeService mBluetoothLeService;
    private GattUpdateReceiver mGattUpdateReceiver;
    private BluetoothAdapter mBluetoothAdapter;
    private DeviceListAdapter mDeviceListAdapter;
    private String mDeviceAddress;

    private ListView listview_device;
    private Button btn_scan;

    private List<BluetoothDevice> mBluetoothDevices;

    private boolean mScanning = false;
    public static final int SCAN_PERIOD = 20000;//扫描的时间
    private static final int REQUEST_CODE_BLUETOOTH = 1;
    private Handler mHandler1;
    private Intent intent = null;
    private BluetoothDevice mDevice;
    private BluetoothSocket mBluetoothSocket;

    private ProgressDialog progressDialog;
    private AlertDialog.Builder mAlertDialog;

    private static boolean isEnable = true;

    private TimerTask mTask1;
    private Timer mTimer1;
    private TimerTask mTask2;
    private Timer mTimer2;
    private TimerTask mTask3;
    private Timer mTimer3;

    private MaterialProgressBar mProgressBar;

    private Handler mConnectResult = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, " connected reuslt : " + msg.what + "");
            switch (msg.what) {
                case 1:
                    String str = mDevice.getName();
                    if (str == null) {
                        str = "未知设备";
                    }
                    Log.i(TAG, " 连接成功 ");
                    startActivity(intent);
                    break;
                case 2:
//                    String str2 = md.getName();
                    String str2 = mDevice.getName();
                    if (str2 == null) {
                        str2 = "未知设备";
                    }
                    Log.i(TAG, " 连接失败 ");
//                    mBluetoothLeService.connect(mDeviceAddress);
//					devicename.setText("连接失败,请等待重新连接");
                    break;
                case 3:
                    if (isBTConnected) {
//					devicename.setText("重连成功");
                        startActivity(intent);
                    } else {
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
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

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

        // Initializes list view adapter.
        mBluetoothDevices = new ArrayList<>();
        mBluetoothDevices.clear();
        mDeviceListAdapter = new DeviceListAdapter(DeviceScanActivity.this, mBluetoothDevices);
        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
        if (bondedDevices.size() > 0) {
            for (BluetoothDevice device : bondedDevices) {
                mBluetoothDevices.add(device);
            }
        }
        listview_device.setAdapter(mDeviceListAdapter);

        //注册广播接收者
        mGattUpdateReceiver = new GattUpdateReceiver(mDeviceListAdapter);
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        Log.i(TAG, " onCreate ");
        super.onCreate(savedInstanceState);
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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);

        btn_scan.requestFocus();
        btn_scan.setFocusable(true);
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

    /**
     * 扫描周围设备
     *
     * @param enabled 是否扫描
     */
    private void scanDevice(boolean enabled) {
        if (enabled) {
            // Stops scanning after a pre-defined scan period.
            mHandler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mScanning) {
                        mScanning = false;
                        mBluetoothAdapter.stopLeScan(mScanCallback);
                        if (mBluetoothAdapter.isDiscovering()) {
                            mBluetoothAdapter.cancelDiscovery();
                        }
                        btn_scan.setText("Scan");
                        mProgressBar.setVisibility(View.INVISIBLE);
                        if (mBluetoothDevices.size() == 0) {
                            ToastUtils.showLong(DeviceScanActivity.this, R.string.scan_data_empty);
                        }
                    }
                }
            }, SCAN_PERIOD);
            btn_scan.setText("Scaning");
            mScanning = true;
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
            mBluetoothAdapter.startDiscovery();
            mProgressBar.setVisibility(View.VISIBLE);
            btn_scan.setBackgroundResource(R.mipmap.btn_pre);

            Log.i(TAG, " scanLeDevice ");
        } else {
            btn_scan.setText("Scan");
            mScanning = false;
//            mBluetoothAdapter.stopLeScan(mScanCallback);
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
            btn_scan.setBackgroundResource(R.mipmap.btn_lock);
            mProgressBar.setVisibility(View.INVISIBLE);
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
        if (mGattUpdateReceiver != null) {
            unregisterReceiver(mGattUpdateReceiver);
        }
        //取消绑定服务
        if (mServiceConnection != null) {
            unbindService(mServiceConnection);
        }
    }

    private IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);//
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
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
        mProgressBar = ((MaterialProgressBar) findViewById(R.id.pb_scan));
        listview_device = ((ListView) findViewById(R.id.listview_device));
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 15/11/11
                if (!mScanning) {
                    scanDevice(true);
                }
            }
        });

        listview_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!listview_device.isEnabled()) {
                    return;
                }
                Log.i(TAG, " the listview's status is : " + listview_device.isEnabled());

                // TODO: 15/11/11
                mDevice = mDeviceListAdapter.getDevice(position);
                if (mDevice == null) return;

                mDeviceAddress = mDevice.getAddress();
                Log.i(TAG, "service" + mBluetoothLeService);

                if (mScanning) {
                    scanDevice(false);
                }

                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {//未配对
                    Method createBondMethod = null;
                    try {
                        createBondMethod = BluetoothDevice.class.getMethod("createBond");
                        try {
                            Log.i(TAG, "开始配对");
                            createBondMethod.invoke(mDevice);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                } else {
                    connect(mDevice);
                    listview_device.setEnabled(false);
                    Log.i(TAG, " set the listview enabled is false ...");
                }
//                result.postDelayed(new ConnectThread(mDevice, mBluetoothAdapter), 1000);
//                mBluetoothLeService.connect(mDeviceAddress);
            }
        });
        TextView textView = new TextView(this);
        textView.setText(" Please press the scan button to scan the device ");
        listview_device.setEmptyView(textView);
    }

    private Handler mResultHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            listview_device.setEnabled(true);
            Log.i(TAG, " set the listview's enabled true ");
//            isEnable = true;

            int what = msg.what;
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }

            switch (what) {
                case BltTsConstants.CONNECT_SUCCESS:
//                    ToastUtils.showShort(DeviceScanActivity.this, "连接成功");
                    intent = new Intent(DeviceScanActivity.this, OwnerListActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString(BltTsConstants.EXTRAS_DEVICE_NAME, mDevice.getName());
//                    bundle.putString(BltTsConstants.EXTRAS_DEVICE_ADDRESS, mDevice.getAddress());
//                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    break;
                case BltTsConstants.CONNECT_FAILED:
//                    ToastUtils.showShort(DeviceScanActivity.this, "连接失败");
                    showDialog(mDevice);
                    break;
            }
        }
    };

    private void connect(BluetoothDevice device) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage("正在连接");
        progressDialog.show();
        new ConnectThread(device, mBluetoothAdapter, mResultHandler).start();
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

    private void showDialog(final BluetoothDevice dev) {
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(this);
            mAlertDialog.setTitle(R.string.message_dialog);
            mAlertDialog.setMessage(R.string.reconnect);
            mAlertDialog.setPositiveButton(R.string.message_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    connect(dev);
                    dialog.dismiss();
                }
            });
            mAlertDialog.setNegativeButton(R.string.message_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        mAlertDialog.show();
    }
}
