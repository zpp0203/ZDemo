package com.zpp.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;

import com.zpp.ProjectConfig;
import com.zpp.RHttp;

import androidx.multidex.MultiDex;



/**
 * Created by Yan on 2015/12/26.
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //从Android 7.0开始，一个应用提供自身文件给其它应用使用时，如果给出一个file://格式的URI的话，应用会抛出FileUriExposedException。
        // 这是由于谷歌认为目标app可能不具有文件权限，会造成潜在的问题。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        RHttp.Configure.get()
                .baseUrl(ProjectConfig.BASE_API)    //基础URL
                .init(this);                        //初始化
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }


}
