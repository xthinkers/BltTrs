package com.blttrs.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blttrs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hongweiyu on 15/11/11.
 */
public class DeviceListAdapter extends BaseAdapter {

    private List<BluetoothDevice> deviceList;
    private List<Integer> rssis;
    private java.util.ArrayList<byte[]> bRecord;
    private LayoutInflater mInflator;

    public DeviceListAdapter(Context context, List<BluetoothDevice> deviceList){
        mInflator = LayoutInflater.from(context);
        this.deviceList = deviceList;
        rssis = new ArrayList<>();
        bRecord = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return deviceList.size() == 0 ? 0 :  deviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public BluetoothDevice getDevice(int position) {
        return deviceList.get(position);
    }

    public void addDevice(BluetoothDevice device, int rs, byte[] record) {
        if(!deviceList.contains(device)) {
            deviceList.add(device);
            rssis.add(rs);
            bRecord.add(record);
        }
    }

    public void addDevice(BluetoothDevice device){
        deviceList.add(device);
    }

    public void clear() {
        deviceList.clear();
        rssis.clear();
        bRecord.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DeviceInfoHolder holder = null;
        BluetoothDevice deviceInfo = deviceList.get(position);
        if(convertView == null){
            holder = new DeviceInfoHolder();
            convertView = mInflator.inflate(R.layout.listitem_device, null);
            holder.deviceName = ((TextView) convertView.findViewById(R.id.device_name));
            holder.deviceAddress = ((TextView) convertView.findViewById(R.id.device_address));
            convertView.setTag(holder);
        }else {
            holder = (DeviceInfoHolder) convertView.getTag();
        }

        holder.deviceName.setTextColor(Color.BLUE);
        final String deviceName = deviceInfo.getName();
        if (deviceName != null && deviceName.length() > 0){
            holder.deviceName.setText(deviceName);
        } else {
            holder.deviceName.setText(R.string.unknown_device);
        }
//        holder.deviceAddress.setText(
//                deviceInfo.getAddress() + "  RSSI:" + String.valueOf(rssis.get(position)));
        holder.deviceAddress.setText(deviceInfo.getAddress());

        return convertView;
    }

    static class DeviceInfoHolder {
        TextView deviceName;
        TextView deviceAddress;
    }
}
