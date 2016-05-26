package com.persist.desktoppet.service;
/**
 * Created by 金盼盼 on 2016/4/28.
 */

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.persist.desktoppet.util.Const;

import java.util.List;


public class WXListenService extends AccessibilityService {

    private final static String TAG = "WXListenService";

    private String mCare;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate, toast should have been displayed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mCare = intent.getStringExtra(Const.KEY_CARE);
        Log.d(TAG, "onStartCommand, care:"+mCare);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED|AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        |AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        Log.i(TAG, "onAccessibilityEvent, type="+eventType);
        switch (eventType) {
            //第一步：监听通知栏消息
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                Log.i(TAG, "notificationChanged:");
                List<CharSequence> texts = event.getText();
                if (!texts.isEmpty()) {
                    for (CharSequence text : texts) {
                        String content = text.toString();
                        Log.i(TAG, "text:" + content);

                        Intent intent = new Intent(Const.ACTION_DISPLAY_SERVICE);
                        intent.setPackage(getPackageName());
                        intent.putExtra(Const.KEY_SERVICE_ACTION, Const.SERVICE_MSG);
                        intent.putExtra(Const.KEY_MSG, content);
                        startService(intent);

                        if(mCare != null && content.contains(mCare))
                        {
                            Toast.makeText(this, "特别关心:"+mCare, Toast.LENGTH_SHORT).show();
                        }

//                        if (content.contains("[微信红包]")) {
//                            //模拟打开通知栏消息
//                            if (event.getParcelableData() != null
//                                    &&
//                                    event.getParcelableData() instanceof Notification) {
//                                Notification notification = (Notification) event.getParcelableData();
//                                PendingIntent pendingIntent = notification.contentIntent;
//                                try {
//                                    pendingIntent.send();
//                                } catch (CanceledException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
                    }
                }
                break;
            //第二步：监听是否进入微信红包消息界面
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.i(TAG, "windowStateChanged:");
//                String className = event.getClassName().toString();
//                if (className.equals("com.tencent.mm.ui.LauncherUI")) {
//                    //开始抢红包
//                    getPacket();
//
//                } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")) {
//                    //开始打开红包
//                    openPacket();
//                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                Log.i(TAG, "windowContentChanged:");
                break;
        }
    }

    /**
     * 查找到
     */
    @SuppressLint("NewApi")
    private void openPacket() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo
                    .findAccessibilityNodeInfosByText("抢红包");
            for (AccessibilityNodeInfo n : list) {
                n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    @SuppressLint("NewApi")
    private void getPacket() {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        recycle(rootNode);
    }

    /**
     * 打印一个节点的结构
     *
     * @param info
     */
    @SuppressLint("NewApi")
    public void recycle(AccessibilityNodeInfo info) {
        if (info.getChildCount() == 0) {
            if (info.getText() != null) {
                if ("领取红包".equals(info.getText().toString())) {
                    //这里有一个问题需要注意，就是需要找到一个可以点击的View
                    Log.i("demo", "Click" + ",isClick:" + info.isClickable());
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    AccessibilityNodeInfo parent = info.getParent();
                    while (parent != null) {
                        Log.i("demo", "parent isClick:" + parent.isClickable());
                        if (parent.isClickable()) {
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            break;
                        }
                        parent = parent.getParent();
                    }

                }
            }

        } else {
            for (int i = 0; i < info.getChildCount(); i++) {
                if (info.getChild(i) != null) {
                    recycle(info.getChild(i));
                }
            }
        }
    }

    @Override
    public void onInterrupt() {
    }


}