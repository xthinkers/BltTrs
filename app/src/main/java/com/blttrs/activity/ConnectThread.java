package com.blttrs.activity;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.blttrs.BltTsConstants;

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
    private BluetoothAdapter mAdapter;
    private static final String SPP_UUID = "0000111f-0000-1000-8000-00805f9b34fb";
    private boolean isConnect = false;
    private Handler mHandler;

    public ConnectThread(BluetoothDevice device, BluetoothAdapter adapter,Handler handler) {
        this.mDevice = device;
        this.mAdapter = adapter;
        this.mHandler = handler;
        UUID uuid = UUID.fromString(SPP_UUID);
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mSocket = tmp;
    }

    public void run() {
        if (mAdapter.isDiscovering()) {
            mAdapter.cancelDiscovery();
        }
        try {
            mSocket.connect();
            isConnect = true;
            mHandler.obtainMessage(BltTsConstants.CONNECT_SUCCESS).sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
            cancel();
            isConnect = false;
            mHandler.obtainMessage(BltTsConstants.CONNECT_FAILED).sendToTarget();
            return;
        }
        manageConnectedSocket(mSocket);
    }

    private void manageConnectedSocket(BluetoothSocket mSocket) {
        new ConnectedThread(mSocket, mHandler).start();
    }

    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
