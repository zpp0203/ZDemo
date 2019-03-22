package com.zpp.zxinglibrary.common;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;

import java.util.concurrent.Executors;

/**
 * 兼容低版本的子线程开启任务
 *
 * @author
 *
 */
public class Runnable {

    @SuppressLint("NewApi")
    @SuppressWarnings("unchecked")
    public static void execAsync(AsyncTask<?, ?, ?> task) {
        if (Build.VERSION.SDK_INT >= 11) {
            //task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            task.executeOnExecutor(Executors.newCachedThreadPool());
        }
        else {
            task.execute();
        }

    }

}
