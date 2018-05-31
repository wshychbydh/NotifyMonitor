package com.cool.eye.notify.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.cool.eye.notify.base.BaseApp;

/**
 * @author ycb
 * @date 2015-4-23
 */
public class GuardService extends Service {

    private final static int PERIOD = 5 * 60 * 1000;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        startForeground(0, null);
        // 动态注册广播
            /*
            *  <action android:name="android.intent.action.SCREEN_ON"/>
                <action android:name="android.intent.action.SCREEN_OFF"/>
                <action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
                <action android:name="android.net.wifi.WIFI_STATE_CHANGED"/>
                <action android:name="android.net.wifi.STATE_CHANGE"/>
            * */
//        IntentFilter filter = new IntentFilter();
//        filter.setPriority(1000);
//        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
//        filter.addAction("android.intent.action.PHONE_STATE");
//        registerReceiver(mReceiver, filter);

        ServiceUtil.toggleNotificationListenerService(BaseApp.context);
        ServiceUtil.openSetting(BaseApp.context);

        handler.sendEmptyMessageDelayed(0,PERIOD);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ServiceUtil.toggleNotificationListenerService(BaseApp.context);
            ServiceUtil.openSetting(BaseApp.context);
            handler.sendEmptyMessageDelayed(0,PERIOD);
        }
    };

    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        startService(new Intent(this, GuardService.class));
    }
}
