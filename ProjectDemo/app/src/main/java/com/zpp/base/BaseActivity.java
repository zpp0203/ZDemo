package com.zpp.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zpp.project.R;
import com.zpp.utils.ActivityStackManager;
import com.zpp.utils.StatusBarUtil;
import com.zpp.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by 墨 on 2018/3/27.
 */

public abstract class BaseActivity extends RxAppCompatActivity {

    public String TAG;
    public Activity mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        this.mActivity = this;
        TAG=getLocalClassName();
        ActivityStackManager.getManager().push(this);

        process(savedInstanceState);
        setStatusBar();

        setContentView(getLayoutResourceId());

        if(!EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().register(this);

        Log.d(getLocalClassName(),"onCreate");


        initial();

    }

    protected abstract int getLayoutResourceId();

    protected abstract void initial();

    /**
     * 防止快速点击
     * @param ev
     * @return
     */
    private long lastClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 500;  // 快速点击间隔
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFastDoubleClick()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private  boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD >= 0 && timeD <= FAST_CLICK_DELAY_TIME) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        Log.d(this.getLocalClassName(),"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        ActivityStackManager.getManager().remove(this);
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
        Log.d(this.getLocalClassName(),"onDestroy");
    }

    protected void process(Bundle savedInstanceState) {
        // 华为,OPPO机型在StatusBarUtil.setLightStatusBar后布局被顶到状态栏上去了
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View content = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            if (content != null && !isUseFullScreenMode()) {
                content.setFitsSystemWindows(true);
            }
        }
    }

    // 在setContentView之前执行
    public void setStatusBar() {
    /*
     为统一标题栏与状态栏的颜色，我们需要更改状态栏的颜色，而状态栏文字颜色是在android 6.0之后才可以进行更改
     所以统一在6.0之后进行文字状态栏的更改
    */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isUseFullScreenMode()) {
                StatusBarUtil.transparencyBar(this);
            } else {
                StatusBarUtil.setStatusBarColor(this, setStatusBarColor());
            }

            if (isUserLightMode()) {
                StatusBarUtil.setLightStatusBar(this, true);
            }
        }

    }

    // 是否设置成透明状态栏，即就是全屏模式
    protected boolean isUseFullScreenMode() {
        return true;
    }

    protected int setStatusBarColor() {
        return R.color.colorPrimary;
    }

    // 是否改变状态栏文字颜色为黑色，默认为黑色
    protected boolean isUserLightMode() {
        return true;
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        int nums=info.numRunning;
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && nums<=1) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - lastClickTime > 2000) {
                ToastUtils.showToast(mActivity,"再按一次退出程序");
                lastClickTime = secondTime;
                return true;
            } else{
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 设置 app 不随着系统字体的调整而变化
     *
     * dialog在7.0切换屏幕后show无法显示？
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.fontScale = 1;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }

}
