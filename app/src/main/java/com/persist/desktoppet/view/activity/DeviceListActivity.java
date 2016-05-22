/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.persist.desktoppet.view.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.persist.desktoppet.R;
import com.persist.desktoppet.adapter.DeviceAdapter;
import com.persist.desktoppet.bean.DeviceBean;
import com.persist.desktoppet.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * not finish yet
 *
 * This Activity appears as a dialog. It lists any paired devices and
 * devices detected in the area after discovery. When a device is chosen
 * by the user, the MAC address of the device is sent back to the parent
 * Activity in the result Intent.
 *
 * this layout of the activity should be rewrite...
 */
public class DeviceListActivity extends BaseActivity {

    /**
     * Tag for Log
     */
    private static final String TAG = "DeviceListActivity";

    /**
     * Return Intent extra
     */
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    /**
     * Member fields
     */
    private BluetoothAdapter mBtAdapter;

    private RecyclerView mPairdDeviceRecycler;
    private RecyclerView mOtherDeviceRecycler;

    private DeviceAdapter mPairdDeviceAdapter;
    private DeviceAdapter mOtherDeviceAdapter;

    private List<DeviceBean> mPairedDevices;
    private List<DeviceBean> mOtherDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_device_list);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mPairdDeviceRecycler = (RecyclerView) findViewById(R.id.paired_devices);
        mOtherDeviceRecycler = (RecyclerView) findViewById(R.id.other_devices);

        initViews();

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        initBoundedDevices();

        registerReceivers();

        doDiscovery();

    }

    private void initViews()
    {

        mPairedDevices = new ArrayList<>();
        mOtherDevices = new ArrayList<>();
        mPairdDeviceAdapter = new DeviceAdapter(mPairedDevices);
        mOtherDeviceAdapter = new DeviceAdapter(mOtherDevices);
        mPairdDeviceAdapter.setOnItemClickListener(mDeviceClickListener);
        mOtherDeviceAdapter.setOnItemClickListener(mDeviceClickListener);
        mPairdDeviceRecycler.setLayoutManager(new LinearLayoutManager(this));
        mOtherDeviceRecycler.setLayoutManager(new LinearLayoutManager(this));
        mPairdDeviceRecycler.setAdapter(mPairdDeviceAdapter);
        mOtherDeviceRecycler.setAdapter(mOtherDeviceAdapter);

    }

    private void initBoundedDevices()
    {
        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        for (BluetoothDevice device : pairedDevices) {
            LogUtil.d(TAG, "bounded device:"+device);
            mPairedDevices.add(new DeviceBean(device.getName(), device.getAddress()));
//            mPairdDeviceAdapter.notifyItemInserted(mPairedDevices.size()-1);
        }
        mPairdDeviceAdapter.notifyDataSetChanged();
    }

    private void registerReceivers()
    {
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            case R.id.scan:
                doDiscovery();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
        LogUtil.d(TAG, "doDiscovery()");

        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);

        // Turn on sub-title for new devices
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }

    /**
     * The on-click listener for all devices in the ListViews
     */
    private DeviceAdapter.OnItemClickListener mDeviceClickListener
            = new DeviceAdapter.OnItemClickListener() {
        public void onItemClick(View v, int position, DeviceBean deviceBean) {
            // Cancel discovery because it's costly and we're about to connect
            mBtAdapter.cancelDiscovery();

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, deviceBean.address);

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    /**
     * The BroadcastReceiver that listens for discovered devices and changes the title when
     * discovery is finished
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mOtherDevices.add(new DeviceBean(device.getName(), device.getAddress()));
                    mOtherDeviceAdapter.notifyItemInserted(mOtherDevices.size()-1);
                }
                else
                {
                    mPairedDevices.add(new DeviceBean(device.getName(), device.getAddress()));
                    mPairdDeviceAdapter.notifyItemInserted(mPairedDevices.size()-1);
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);
            }
        }
    };

}
