//package com.cool.eye.notify;
//
//import android.content.ActivityNotFoundException;
//import android.content.BroadcastReceiver;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.text.TextUtils;
//import android.widget.Toast;
//
//import com.cool.eye.notify.util.Constant;
//
//public class NotifyHelper {
//
//    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
//    public static final String SEND_WX_BROADCAST = "SEND_WX_BROADCAST";
//
//    public static final String PLATFORM = "platform";
//
//    private IMessage im;
//    private Context context;
//
//    public NotifyHelper(IMessage im, Context context) {
//        this.im = im;
//        this.context = context;
//        registBroadCast();
//    }
//
//    private BroadcastReceiver b = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Bundle bundle = intent.getExtras();
//            if (bundle == null) return;
//            String platform = bundle.getString(PLATFORM);
//            if (platform == null) return;
//            switch (platform) {
//                case Constant.WX:
//                    im.wxMessage();
//                    break;
//                case Constant.QQ:
//                    im.qqMessage();
//                    break;
//                case Constant.MMS:
//                    im.shortMessage();
//                    break;
//                case Constant.CALL:
//                    im.phone();
//                    break;
//                default:
//                    im.other();
//                    break;
//            }
//        }
//    };
//
//    private void registBroadCast() {
//        IntentFilter filter = new IntentFilter(SEND_WX_BROADCAST);
//        context.registerReceiver(b, filter);
//    }
//
//    public void onDestroy() {
//        context.unregisterReceiver(b);
//    }
//
//    public void openSetting() {
//        if (!isEnabled()) {
//            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
//            context.startActivity(intent);
//        } else {
//            Toast.makeText(context, "已开启服务权限", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public static boolean gotoNotificationAccessSetting(Context context) {
//        try {
//            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//            return true;
//
//        } catch (ActivityNotFoundException e) {//普通情况下找不到的时候需要再特殊处理找一次
//            try {
//                Intent intent = new Intent();
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                ComponentName cn = new ComponentName("com.android.settings", "com.android" +
//                        ".settings.Settings$NotificationAccessSettingsActivity");
//                intent.setComponent(cn);
//                intent.putExtra(":settings:show_fragment", "NotificationAccessSettings");
//                context.startActivity(intent);
//                return true;
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }
//            Toast.makeText(context, "对不起，您的手机暂不支持", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    private boolean isEnabled() {
//        String pkgName = context.getPackageName();
//        final String flat = Settings.Secure.getString(context.getContentResolver(),
//                ENABLED_NOTIFICATION_LISTENERS);
//        if (!TextUtils.isEmpty(flat)) {
//            final String[] names = flat.split(":");
//            for (int i = 0; i < names.length; i++) {
//                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
//                if (cn != null) {
//                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }
//
//    public void toggleNotificationListenerService() {
//        PackageManager pm = context.getPackageManager();
//        pm.setComponentEnabledSetting(
//                new ComponentName(context, NotifyService.class),
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//
//        pm.setComponentEnabledSetting(
//                new ComponentName(context, NotifyService.class),
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//    }
//}