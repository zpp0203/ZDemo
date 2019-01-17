package com.zandroid.widget;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/*
* 引导页
* */
public class GuideView  {

    private Activity activity;
    private ViewGroup layout;
    private GuideShape shape;//绘制高亮的view
    private View view;//自己的布局
    private boolean cancelTouchout;//是否可点击旁边消失


    public GuideView(Activity activity) {
        this.activity=activity;
        shape=new GuideShape(activity);
    }
    public GuideView addHightLight(final View view, final int marggin){
        view.post(new Runnable() {
            @Override
            public void run() {
                Rect rect=new Rect();
                view.getGlobalVisibleRect(rect);//获取在整个屏幕内的绝对坐标
                rect.left=rect.left-marggin;
                rect.top=rect.top-marggin;
                rect.right=rect.right+marggin;
                rect.bottom=rect.bottom+marggin;
                shape.addHightLight(rect);
            }
        });
        return this;
    }
    public GuideView addHightLight(final View view){
        view.post(new Runnable() {
            @Override
            public void run() {
                Rect rect=new Rect();
                view.getGlobalVisibleRect(rect);//获取在整个屏幕内的绝对坐标
                shape.addHightLight(rect);
            }
        });
        return this;
    }
    public GuideView addHightLight(Rect rect){
        shape.addHightLight(rect);
        return this;
    }

    public void show(){
        layout= (FrameLayout) activity.getWindow().getDecorView();
        layout.addView(shape);
        layout.addView(view);

        if(cancelTouchout){
            shape.setTouchSideListener(new GuideShape.OnTouchSideListener() {
                @Override
                public void onTouchSide() {
                    cancel();
                }
            });
        }
    }
    public void cancel(){
        if(layout!=null){
            layout.removeView(shape);
            layout.removeView(view);
        }
    }
    public GuideView addClickListener(int viewId, View.OnClickListener listener){
        if(view!=null){
            View v=view.findViewById(viewId);
            v.setOnClickListener(listener);
        }
        return this;
    }

    public GuideView setView(int view){
        this.view=LayoutInflater.from(activity).inflate(view, null, false);
        this.view.setBackgroundColor(Color.TRANSPARENT);
        return this;
    }
    public View getView(int viewId) {
        return view.findViewById(viewId);
    }

    public GuideView setHightTouch(GuideShape.OnTouchHighListener listener){
        if(shape!=null){
            shape.setTouchHighListener(listener);
        }
        return this;
    }

    public GuideView setCancelTouchout(boolean cancelTouchout) {
        this.cancelTouchout = cancelTouchout;
        return this;
    }
}
