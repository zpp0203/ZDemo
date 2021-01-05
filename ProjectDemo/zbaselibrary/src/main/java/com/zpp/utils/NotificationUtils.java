package com.zpp.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.content.Context.APP_OPS_SERVICE;

public class NotificationUtils {
    private static NotificationUtils instance;
    private NotificationUtils(){}
    public static NotificationUtils getInstance(){
        if(instance==null)
            instance=new NotificationUtils();

        return instance;
    }

    NotificationManager mNotificationManager;
    Notification.Builder mBuilder;
    public static final int SETREQUESTCODE=12345;//設置頁面的返回
    final int NOTIFYCATIONID = 1001;
    int progress;

    /**
     * 初始化通知对象
     * 时间、图标
     */
    public void initNotifycation(Context context) {
        mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //IMPORTANCE_MIN 静默，IMPORTANCE_HIGH 随系统使用声音或振动
            NotificationChannel channel = new NotificationChannel(context.getPackageName()+NOTIFYCATIONID, context.getPackageName(),NotificationManager.IMPORTANCE_MIN);
            channel.enableLights(false);//是否在桌面icon右上角展示小红点
            channel.enableLights(false);//亮灯
            channel.enableVibration(false);//震动
            channel.setSound(null, null);//声音
            channel.setImportance(NotificationManager.IMPORTANCE_LOW); //设置为low, 通知栏不会有声音
            channel.setBypassDnd(true);//设置可以绕过，请勿打扰模式
            mNotificationManager.createNotificationChannel(channel);
            mBuilder = new Notification.Builder(context, channel.getId());
        }else {
            mBuilder = new Notification.Builder(context).setAutoCancel(true);

        }
        //取消震动 关闭取消声音
        mBuilder.setVibrate(null);
        mBuilder.setSound(null);
        mBuilder.setOnlyAlertOnce(true);
        //点击需要跳转指定的页面
//        Intent intent=new Intent(context,MainActivity.class);
//        intent.putExtra("from_notification",true);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, NITIFICLICK, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(context.getPackageName())
//                .setContentText("下载更新");
                //.setContentIntent(pendingIntent);

    }

    /** 设置下载进度 */
    public void updateNotification(String title,String contentText,int progress) {
        if(progress>this.progress) {
            this.progress = progress;
            Notification mNotification = mBuilder.build();
            mNotification.flags = Notification.FLAG_ONGOING_EVENT;//
            mBuilder.setProgress(100, this.progress, false); // 这个方法是显示进度条
            mBuilder.setContentText(contentText).setContentTitle(title);
            if(progress==100){
                mBuilder.setContentText("下载成功").setContentTitle("完成");
            }
            mNotificationManager.notify(NOTIFYCATIONID, mNotification);
        }
    }

    /**
     * 去除通知栏
     */
    public void cancelNotification(){
        if(mNotificationManager!=null)
            mNotificationManager.cancel(NOTIFYCATIONID);// 删除一个特定的通知ID对应的通知
    }

    /**
     * 获取通知权限
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            return mNotificationManager.areNotificationsEnabled();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            String CHECK_OP_NO_THROW = "checkOpNoThrow";
            String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;

            Class appOpsClass = null;
            try {
                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                        String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

                int value = (Integer) opPostNotificationValue.get(Integer.class);
                return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
    /*跳转到自身的设置界面*/
    public void setNotificationEnabled(Activity context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivityForResult(localIntent,SETREQUESTCODE);
    }
}
