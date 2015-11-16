package com.blttrs.activity;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by hongweiyu on 15/11/16.
 */
public class ConnectThread extends Thread {

    private static final String TAG = "ConnectThread";

    private final BluetoothDevice mDevice;
    private final BluetoothSocket mSocket;
    private InputStream mInStream;
    private OutputStream mOutStream;
    private BluetoothAdapter mAdapter;
    private static final String SPP_UUID = "0000111f-0000-1000-8000-00805f9b34fb";
    private static final int CONNECT_TIME = 12 * 1000;
    private boolean isConnect = false;

    public ConnectThread(BluetoothDevice device, BluetoothAdapter adapter){
        this.mDevice = device;
        this.mAdapter = adapter;
        UUID uuid = UUID.fromString(SPP_UUID);
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        }catch(IOException e){
            e.printStackTrace();
        }
        mSocket = tmp;
    }

    public void run() {
        if (mAdapter.isDiscovering()) {
            mAdapter.cancelDiscovery();
        }

        while(!isConnect){
            try {
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {//未配对
                    Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                    Log.i(TAG, "开始配对");
                    createBondMethod.invoke(mDevice);
                } else {
                    mSocket.connect();
                }
                isConnect = true;
            } catch (Exception e) {
                e.printStackTrace();
                cancel();
                isConnect = false;
                return;
            }
        }
        manageConnectedSocket(mSocket);
    }

    private void manageConnectedSocket(BluetoothSocket mSocket) {

    }


    public void cancel(){
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
