package com.zpp.demo.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zpp.demo.bean.MainBean;
import com.zandroid.tools.ActivityUtils;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


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
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

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
        ButterKnife.unbind(this);
    }

    @Subscribe
    public void onEventMainTread(MainBean mainBean){
    }
}
