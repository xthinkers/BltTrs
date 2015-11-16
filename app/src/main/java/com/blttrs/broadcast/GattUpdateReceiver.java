package com.blttrs.broadcast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blttrs.adapter.DeviceListAdapter;
import com.blttrs.utils.ToastUtils;

public class GattUpdateReceiver extends BroadcastReceiver {

    public static final String TAG = "DeviceScanActivity";
//    private static final String TAG = "DeviceScanActivity";

    private boolean mConnected = false;
    private boolean isBTConnected = false;
    private DeviceListAdapter mDeviceListAdapter;
    public GattUpdateReceiver(DeviceListAdapter deviceListAdapter) {
        this.mDeviceListAdapter = deviceListAdapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();
        BluetoothDevice device;
        if(BluetoothDevice.ACTION_FOUND.equals(action)) {//找到设备
             device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.e(TAG, " action_found " + device.getName() + " " + device.getAddress());

            if(!mDeviceListAdapter.contains(device)){
                mDeviceListAdapter.addDevice(device);
                mDeviceListAdapter.notifyDataSetChanged();
            }
//            if (device.getBondState() != BluetoothDevice.BOND_BONDED){
//                mDeviceListAdapter.addDevice(device);
//                mDeviceListAdapter.notifyDataSetChanged();
//            }
        }else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            int state = device.getBondState();

            switch(state){
                case BluetoothDevice.BOND_BONDING://正在配对
                    Log.i(TAG, " 正在配对 ");
                    mDeviceListAdapter.notifyDataSetChanged();
                    break;
                case BluetoothDevice.BOND_BONDED://完成配对
                    Log.i(TAG, " 完成配对 ");
                    mDeviceListAdapter.notifyDataSetChanged();
                    ToastUtils.showShort(context, "配对成功");
                    break;
                case BluetoothDevice.BOND_NONE://取消配对
                    Log.i(TAG, " 取消配对 ");
                    mDeviceListAdapter.notifyDataSetChanged();
                    ToastUtils.showShort(context, "取消配对");
                    break;
            }
        }else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
            Log.i(TAG, " action finished ");
        }

//        if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
//            Log.i(TAG, "Only gatt, just wait");
//        } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
//                .equals(action)) {
//            mConnected = false;
//        } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
//                .equals(action)) {
//            mConnected = true;
//            isBTConnected = true;
//            Log.i(TAG, "In what we need");
//        } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//            Log.i(TAG, "RECV DATA");
//            String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
//            if (data != null) {
//            }
//        }
    }

    public boolean ismConnected() {
        return mConnected;
    }

    public void setmConnected(boolean mConnected) {
        this.mConnected = mConnected;
    }

    public boolean isBTConnected() {
        return isBTConnected;
    }

    public void setIsBTConnected(boolean isBTConnected) {
        this.isBTConnected = isBTConnected;
    }
}