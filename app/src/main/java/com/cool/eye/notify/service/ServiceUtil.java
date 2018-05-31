package com.cool.eye.notify.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import com.cool.eye.notify.NotifyService;

import java.util.Map;

/**
 * Created by cool on 16-11-2.
 */

public class ServiceUtil {

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    /**
     * 定时任务
     */
    public static void startService(Context context, long millSecond, Class<? extends Service>
            clazz) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getService(context, 501, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, 0, millSecond, pendingIntent);
    }

    public static void startService(Context context, Class<? extends Service> clazz) {
        Intent intent = new Intent(context, clazz);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.startService(intent);
    }

    public static void startService(Context context, Class<? extends Service> clazz, Map<String,
            String> parms) {
        Intent intent = new Intent(context, clazz);
        if (parms != null && !parms.isEmpty()) {
            for (String key : parms.keySet()) {
                intent.putExtra(key, parms.get(key).toString());
            }
        }
        context.startService(intent);
    }

    /**
     * 停止定时任务
     *
     * @param context
     */
    public static void stopService(Context context, Class<? extends Service> clzz) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, clzz);
        PendingIntent pendingIntent = PendingIntent.getService(context, 501, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
        context.stopService(intent);
    }

    public static void startDeviceAdmin(Activity activity) {
        // 发送广播
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        ComponentName componentName = new ComponentName(activity.getPackageName(), activity
                .getPackageName()
                + ".MyDeviceAdminReceiver");
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        activity.startActivityForResult(intent, 0);
    }


    public static void openSetting(Context context) {
        if (!isEnabled(context)) {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "已开启服务权限", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean gotoNotificationAccessSetting(Context context) {
        try {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;

        } catch (ActivityNotFoundException e) {//普通情况下找不到的时候需要再特殊处理找一次
            try {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cn = new ComponentName("com.android.settings", "com.android" +
                        ".settings.Settings$NotificationAccessSettingsActivity");
                intent.setComponent(cn);
                intent.putExtra(":settings:show_fragment", "NotificationAccessSettings");
                context.startActivity(intent);
                return true;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            Toast.makeText(context, "对不起，您的手机暂不支持", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isEnabled(Context context) {
        String pkgName = context.getPackageName();
        final String flat = Settings.Secure.getString(context.getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void toggleNotificationListenerService(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName(context, NotifyService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(
                new ComponentName(context, NotifyService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
