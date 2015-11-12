package com.blttrs.broadcast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;

import com.blttrs.adapter.DeviceListAdapter;
import com.blttrs.service.BluetoothLeService;
import com.blttrs.utils.ToastUtils;

import java.util.List;

public class GattUpdateReceiver extends BroadcastReceiver {

    public static final String TAG = "GattUpdateReceiver";
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
        if(BluetoothDevice.ACTION_FOUND.equals(action)) {//找到设备
            final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.e(TAG, " action_found " + device.getName() + " " + device.getAddress());

//            if (device.getBondState() != BluetoothDevice.BOND_BONDED){
                mDeviceListAdapter.addDevice(device);
                mDeviceListAdapter.notifyDataSetChanged();
//            }
        }else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
            Log.i(TAG, " action finished ");
        }

        if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
            Log.i(TAG, "Only gatt, just wait");
        } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
                .equals(action)) {
            mConnected = false;
        } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
                .equals(action)) {
            mConnected = true;
            isBTConnected = true;
            Log.i(TAG, "In what we need");
        } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
            Log.i(TAG, "RECV DATA");
            String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
            if (data != null) {
            }
        }
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