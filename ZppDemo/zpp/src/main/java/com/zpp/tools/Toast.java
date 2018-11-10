package com.zpp.tools;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

/**
 * 系统的Toast通过NotificationManagerService 维护一个toast队列，在用户关闭通知权限时无法使用
 * **/
public class Toast {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    private static int checkNotification = 0;
    private Object mToast;
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;

    private Toast(Context context, CharSequence message, int duration) {
        try{
            checkNotification = NotificationUtils.getInstance().isNotificationEnabled(context) ? 0 : 1;
            if (checkNotification == 1 && context instanceof Activity) {
                mToast = EToast.makeText(context, message, duration);
            } else {
                mToast = android.widget.Toast.makeText(context, message, duration);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private Toast(Context context, int resId, int duration) {
        if (checkNotification == -1){
            checkNotification = NotificationUtils.getInstance().isNotificationEnabled(context) ? 0 : 1;
        }

        if (checkNotification == 1 && context instanceof Activity) {
            mToast = EToast.makeText(context, resId, duration);
        } else {
            mToast = android.widget.Toast.makeText(context, resId, duration);
        }
    }

    public static Toast makeText(Context context, CharSequence message, int duration) {
        return new Toast(context,message.toString(),duration);
    }
    public static Toast makeText(Context context, int resId, int duration) {
        return new Toast(context,resId,duration);
    }

    public void show() {
        if(mToast instanceof EToast){
            ((EToast) mToast).show();
        }else if(mToast instanceof android.widget.Toast){
            ((android.widget.Toast) mToast).show();
        }
    }
    public void cancel(){
        if(mToast instanceof EToast){
            ((EToast) mToast).cancel();
        }else if(mToast instanceof android.widget.Toast){
            ((android.widget.Toast) mToast).cancel();
        }
    }
    public void setText(int resId){
        if(mToast instanceof EToast){
            ((EToast) mToast).setText(resId);
        }else if(mToast instanceof android.widget.Toast){
            ((android.widget.Toast) mToast).setText(resId);
        }
    }
    public void setText(CharSequence s){
        if(mToast instanceof EToast){
            ((EToast) mToast).setText(s);
        }else if(mToast instanceof android.widget.Toast){
            ((android.widget.Toast) mToast).setText(s);
        }
    }

    public TextView getTextView(){
        TextView textView=null;
        if(mToast instanceof EToast){
            textView=((EToast) mToast).getTextView();
        }else if(mToast instanceof android.widget.Toast){
            textView = ((android.widget.Toast) mToast).getView().findViewById(android.R.id.message);
        }
        return textView;
    }
}

