package com.zpp.demo.base;

import android.app.Application;
import android.content.Context;

import com.zpp.volley.MyVolley;


/**
 * Created by Yan on 2015/12/26.
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        //初始化MyVolley
        MyVolley.init(getApplicationContext());

    }
}
