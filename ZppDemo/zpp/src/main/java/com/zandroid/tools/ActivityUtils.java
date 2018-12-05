package com.zandroid.tools;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by 我 on 2018/4/23.
 * <p>
 * 清楚掉所有activity后，startActivity时需要给它加上Intent.FLAG_ACTIVITY_CLEAR_TOP，表示start的这个activity置顶，并二次清除掉所有这个activity之上的activity
 * <p>
 */

public class ActivityUtils {
    private final String TAG = "ActivityManager";
    private Stack<Activity> activityStack;
    private static ActivityUtils instance;
    private Activity currActivity;

    private ActivityUtils() {
    }

    public static ActivityUtils getActivityUtils() {
        if (instance == null) {
            instance = new ActivityUtils();
        }
        return instance;
    }


    //退出栈顶Activity
    public void popActivity(Activity activity) {
        if (activity == null || activityStack == null) {
            return;
        }
        if (activityStack.contains(activity)) {
            activityStack.remove(activity);
        }
        currActivity = activity;
        //activity.finish();

        //activity = null;
    }

    public void destoryActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        activity.finish();
        if (activityStack.contains(activity)) {
            activityStack.remove(activity);
        }
    }

    //获得当前栈顶Activity
    public Activity currentActivity() {
        if (activityStack == null || activityStack.empty()) {
            return null;
        }
        return activityStack.lastElement();
    }

    //将当前Activity推入栈中
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    //退出栈中除指定的Activity外的所有
    public void popAllActivityExceptOne(Class cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            destoryActivity(activity);
        }
    }

    //退出栈中所有Activity
    public void popAllActivity() {
        popAllActivityExceptOne(null);
    }
    //获取当前的activity
    public Activity getCurrentActivity() {
        return currActivity;
    }

    public Stack getAllActivity(){
        return activityStack;
    }
    public int getActivityStackSize() {
        int size = 0;
        if (activityStack != null) {
            size = activityStack.size();
        }
        return size;
    }

}
