package com.zandroid.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zandroid.tools.ActivityUtils;



/**
 * Created by 墨 on 2018/3/27.
 */

public abstract class BaseActivity extends Activity {
    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(setLayoutView());
    }
    protected abstract int setLayoutView();
    @Override
    protected void onPause() {
        super.onPause();
        ActivityUtils.getActivityUtils().pushActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityUtils.getActivityUtils().popActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
