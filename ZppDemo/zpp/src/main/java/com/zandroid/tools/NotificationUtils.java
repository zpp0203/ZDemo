package com.zandroid.tools;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
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
    final int NOTIFYCATIONID = 1001;
    int progress;

    /**
     * 初始化通知对象
     * 时间、图标
     */
    public void initNotifycation(Context context,int smallIcon) {
        mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFYCATIONID+"", context.getPackageName(),NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder = new Notification.Builder(context, channel.getId());
        }else {
            mBuilder = new Notification.Builder(context);
        }
        //需要跳转指定的页面
//        Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setTicker("new message")
                .setSmallIcon(smallIcon)
                .setContentTitle(AppUtils.getAppUtils().getAppName(context));
//                .setContentText("内容")
//                .setContentIntent(pendingIntent);

//        mNotificationManager.notify(NOTIFYCATIONID,mBuilder.build());
    }

    /** 设置下载进度 */
    public void updateNotification(String title,String contentText,int progress) {
        if(progress>progress) {
            this.progress = progress;
            Notification mNotification = mBuilder.build();
            mNotification.flags = Notification.FLAG_ONGOING_EVENT;//
            mBuilder.setProgress(100, this.progress, false); // 这个方法是显示进度条
            mBuilder.setContentText(contentText).setContentTitle(title);
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

}
