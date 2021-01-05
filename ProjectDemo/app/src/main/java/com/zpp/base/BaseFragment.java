package com.zpp.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.trello.rxlifecycle2.components.RxFragment;

import org.greenrobot.eventbus.EventBus;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class BaseFragment extends RxFragment {
    protected View mView;

    protected View rightView;


    public BaseFragment(){}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if(EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView != null) {
            ViewGroup p = (ViewGroup) mView.getParent();
            if (p != null) {
                p.removeView(mView);
            }

        } else {
            mView = inflater.inflate(getLayoutResourceId(), container, false);

            //ensureInit();
            initial();
        }

        return mView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    protected abstract int getLayoutResourceId();
    protected abstract Object[] getLayoutTool();
    /* 初始化数据 */
    protected abstract void initial();

}
