package com.persist.desktoppet.view.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.persist.desktoppet.PetApplication;
import com.persist.desktoppet.R;
import com.persist.desktoppet.adapter.ReplyAdapter;
import com.persist.desktoppet.bean.PetBean;
import com.persist.desktoppet.bean.Reply;
import com.persist.desktoppet.bluetooth.BluetoothChatService;
import com.persist.desktoppet.presenter.impl.ConnectPresenterImpl;
import com.persist.desktoppet.presenter.ipresenter.IConnectPresenter;
import com.persist.desktoppet.util.Const;
import com.persist.desktoppet.util.LogUtil;
import com.persist.desktoppet.util.NumberBytesUtil;
import com.persist.desktoppet.view.iview.IConnectView;

import java.util.ArrayList;

/**
 * Created by taozhiheng on 16-4-17.
 *
 * bluetooth connect activity
 *
 * this activity should be rewrite with mvp framework...
 */
public class ConnectActivity extends BaseActivity implements IConnectView{


    private static final String TAG = "ConnectActivity";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;

    private TextView mStateText;
    private TextView mIntimateText;
    private View mContainer;
    private RecyclerView mRecycler;
    private EditText mContent;
    private View mSend;
    private View mHi;
    private View mSet;
    private View mRemove;

    private AlertDialog mDialog;
    private char mTag = 3;

    private ReplyAdapter mAdapter;

    private int mState = BluetoothChatService.STATE_NONE;

    private PetBean mIntimatePet;
    private IConnectPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        mStateText = (TextView) findViewById(R.id.connect_state);
        mIntimateText = (TextView) findViewById(R.id.connect_intimate);
        mContainer = findViewById(R.id.connect_container);
        mRecycler = (RecyclerView) findViewById(R.id.connect_recycler);
        mContent = (EditText) findViewById(R.id.connect_content);
        mSend = findViewById(R.id.connect_send);
        mHi =  findViewById(R.id.connect_hi);
        mSet =  findViewById(R.id.connect_set);
        mRemove =  findViewById(R.id.connect_remove);

        createDialog();

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ReplyAdapter(new ArrayList<Reply>());
        mRecycler.setAdapter(mAdapter);

        mSend.setOnClickListener(onClickListener);
        mHi.setOnClickListener(onClickListener);
        mSet.setOnClickListener(onClickListener);
        mRemove.setOnClickListener(onClickListener);

        mIntimateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mIntimateText.getText().toString().equals("未配对"))
                {
                    Intent intent = new Intent(ConnectActivity.this, DetailActivity.class);
                    intent.putExtra(Const.KEY_PET, mIntimatePet);
                    startActivity(intent);
                }
            }
        });

        setEnable(true, false);
        setEnable(false, false);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("结对");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }

        mIntimatePet = new PetBean();
        mPresenter = new ConnectPresenterImpl(PetApplication.getPetModel(this), this);
    }

    private void createDialog()
    {
        mDialog = new AlertDialog.Builder(this)
                .setTitle("是否接受配对请求")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendMsg(mTag, "1");
                        if(mTag == 3)
                            mPresenter.setIntimatePet(mIntimatePet);
                        else
                            mPresenter.removeIntimatePet();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendMsg(mTag, "0");
                    }
                })
                .create();
        mDialog.setCancelable(false);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.connect_send:
                    sendMsg((char)5, mContent.getText().toString());
                    mContent.setText(null);
                    break;
                case R.id.connect_hi:
                    PetBean petBean = mPresenter.getModel().getPet();
                    sendMsg((char)0, PetApplication.getGson().toJson(petBean));
                    break;
                case R.id.connect_set:
                    sendMsg((char)1, "请求建立配对,请回应\n0.拒绝 1.同意");
                    break;
                case R.id.connect_remove:
                    sendMsg((char)2, "请求解除配对,请回应\n0.拒绝 1.同意");
                    break;
            }
        }
    };


    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        LogUtil.d(TAG, "setupChat()");
        mChatService = new BluetoothChatService(this, mHandler);
        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    /**
     * start bluetooth accept threads
     * */
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadIntimatePet();
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }

    /**
     * cancel bluetooth threads
     * */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }


    /**
     * Makes this device discoverable.
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * start connect thread
     *
     * Establish connection with other device
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMsg(char tag, String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        if (message.length() > 0) {
            String send = tag+message;
            mChatService.write(send.getBytes());
            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }


    /**
     * handle message
     * */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
//                            mContainer.setVisibility(View.VISIBLE);
                            Log.d(TAG, "visible:"+mContainer.getVisibility());
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            setEnable(true, true);
                            Toast.makeText(ConnectActivity.this, "Connected to "
                                    + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    mState = msg.arg1;
                    break;
                case Const.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    if(writeMessage.charAt(0) == 0)
                        addReply(new Reply(Reply.TYPE_USER, "您向对方打了招呼"));
                    else
                        addReply(new Reply(Reply.TYPE_USER, writeMessage));
                    break;
                case Const.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String total = new String(readBuf, 0, msg.arg1);
                    String response = total.substring(1);
                    Log.d(TAG, "tag="+((int)total.charAt(0))+"response:"+response);
                    switch (total.charAt(0))
                    {
                        case 0://receive pet info
                            mIntimatePet = PetApplication.getGson().fromJson(response, PetBean.class);
//                            addReply(new Reply(Reply.TYPE_CLIENT, response));
                            addReply(new Reply(Reply.TYPE_CLIENT, mIntimatePet.getName()+"向您打了招呼"));
                            setEnable(false, true);
                            break;
                        case 1://receive request for connect
                            LogUtil.d(TAG, "receive connect request");
                            addReply(new Reply(Reply.TYPE_CLIENT, response));
                            mTag = 3;
                            mDialog.setTitle("是否接受建立配对请求?");
                            mDialog.show();
                            break;
                        case 2://receive request for disconnect
                            LogUtil.d(TAG, "receive disconnect request");
                            addReply(new Reply(Reply.TYPE_CLIENT, response));
                            mTag = 4;
                            mDialog.setTitle("是否接受解除配对请求?");
                            mDialog.show();
                            break;
                        case 3://receive response about connection request
                            LogUtil.d(TAG, "receive response "+response);
                            //response ok
                            if(response.charAt(0) == '1')
                            {
                                mPresenter.setIntimatePet(mIntimatePet);
                                Toast.makeText(ConnectActivity.this, "client has accept your connect request",
                                        Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "client has accept your connect request");
                            }
                            addReply(new Reply(Reply.TYPE_CLIENT, response));
                            break;
                        case 4://receive response about disconnection request
                            LogUtil.d(TAG, "receive disconnect response "+response);
                            //response ok
                            if(response.charAt(0) == '1')
                            {
                                mPresenter.removeIntimatePet();
                                Toast.makeText(ConnectActivity.this, "client has accept your disconnect request",
                                        Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "client has accept your disconnect request");
                            }
                            addReply(new Reply(Reply.TYPE_CLIENT, response));
                            break;
                        case 5:
                            addReply(new Reply(Reply.TYPE_CLIENT, response));
                            break;
                    }
                    // construct a string from the valid bytes in the buffer
                    break;
                case Const.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Const.DEVICE_NAME);
                    break;
                case Const.MESSAGE_TOAST:
                    Toast.makeText(ConnectActivity.this, msg.getData().getString(Const.TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void setEnable(boolean enter, boolean enable)
    {
        if(enter)
        {
            mSend.setEnabled(enable);
            mHi.setEnabled(enable);
        }
        else {
            mSet.setEnabled(enable);
            mRemove.setEnabled(enable);
        }
    }

    private void addReply(Reply reply)
    {
        mAdapter.addReply(reply);
        mRecycler.smoothScrollToPosition(mAdapter.getItemCount());
    }



    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {

        mStateText.setText(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {

        mStateText.setText(subTitle);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    LogUtil.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_connect, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.secure_connect_scan:
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
//            case R.id.insecure_connect_scan:
//                // Launch the DeviceListActivity to see devices and do scan
//                Intent intent = new Intent(this, DeviceListActivity.class);
//                startActivityForResult(intent, REQUEST_CONNECT_DEVICE_INSECURE);
//                return true;
            case R.id.discoverable:
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadIntimatePet(PetBean petBean) {
        if(petBean != null) {
            mIntimateText.setText(petBean.getName());
            mIntimatePet.setId(petBean.getId());
            mIntimatePet.setName(petBean.getName());
            mIntimatePet.setAge(petBean.getAge());
            mIntimatePet.setPhrase(petBean.getPhrase());
            mIntimatePet.setSex(petBean.getSex());
            mIntimatePet.setType(petBean.getType());
            mIntimatePet.setLevel(petBean.getLevel());
            mIntimatePet.setEmotion(petBean.getEmotion());
            mIntimatePet.setExperience(petBean.getExperience());
        }
    }

    @Override
    public void setIntimatePet(boolean success, PetBean petBean) {
        Log.d(TAG, "setIntimatePet:"+success);
        if(success)
        {
//            sendMsg(mTag, "1");
            mIntimateText.setText(petBean.getName());
        }
//        else {
//            sendMsg(mTag, "0");
//            Toast.makeText(getBaseContext(), "建立配对失败", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void removeIntimatePet(boolean success) {
        Log.d(TAG, "removeIntimatePet:"+success);
        if(success) {
//            sendMsg(mTag, "1");
            mIntimateText.setText("未配对");
        }
//        else {
//            sendMsg(mTag, "0");
//            Toast.makeText(getBaseContext(), "解除配对失败", Toast.LENGTH_SHORT).show();
//        }

    }
}
