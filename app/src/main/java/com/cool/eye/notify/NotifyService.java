package com.cool.eye.notify;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import com.cool.eye.func.notify.util.Util;
import com.cool.eye.notify.base.BaseApp;
import com.cool.eye.notify.business.notify.model.Notify;
import com.cool.eye.notify.db.DBHelper;
import com.cool.eye.notify.service.GuardService;
import com.cool.eye.notify.util.Constant;

import java.util.List;
import java.util.Map;

public class NotifyService extends NotificationListenerService {

    private volatile Map<String, List<String>> users;

    @Override
    public void onCreate() {
        super.onCreate();
        users = DBHelper.INSTANCE.getUsers();
        startForeground(0, null);
        Util.startService(BaseApp.context, GuardService.class);
    }


    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        Util.startService(BaseApp.context, GuardService.class);
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        //  printClass(sbn.getNotification());
        if (sbn == null || TextUtils.isEmpty(sbn.getPackageName())) return;
        Notification notification = sbn.getNotification();
        if (notification != null && notification.tickerText != null) {
            filterNotify(sbn.getPackageName(), notification);
        }
    }

    private void filterNotify(final String platform, final Notification notification) {
        if (users == null || users.isEmpty()) return;
        final String tickerText = notification.tickerText.toString();
        if (!tickerText.contains(":")) return;
        final String name = tickerText.substring(0, tickerText.indexOf(":"));
        if ((users.containsKey(platform) && users.get(platform).contains(name)) ||
                (users.containsKey(Constant.OTHER) && users.get(Constant.OTHER).contains(name))) {

            String msg = tickerText.substring(tickerText.indexOf(":") + 1, tickerText.length());
            Notify notify = DBHelper.insertNotify(platform, name, msg, notification.when);

            Intent intent = new Intent();
            intent.setAction(Constant.SEND_WX_BROADCAST);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.NOTIFY, notify);
            intent.putExtras(bundle);
            this.sendBroadcast(intent);
            if (!Constant.CALL.equals(platform) && !Constant.MMS.equals(platform)) {
                Util.startAlarm(BaseApp.context);
            }
        }
    }

//    private void printClass(Object obj) {
//        Field[] fields = obj.getClass().getFields();
//        try {
//            for (Field field : fields) {
//                field.setAccessible(true);
//                System.out.println(obj.getClass().getName() + "--->" + field.getName() + ": " +
//                        field.get(obj));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
    }
}