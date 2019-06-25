package com.zandroid.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.zandroid.tools.StatusBarUtil;


public class GuideView  {

    private Activity activity;
    private ViewGroup layout;
    private GuideShape shape;//绘制高亮的view
    private View view;//自己的布局
    private boolean cancelTouchout;//是否可点击旁边消失
    private boolean isShowing;

    private Rect lightRect;

    public final int Left=0;
    public final int Top=1;
    public final int Right=2;
    public final int Bottom=3;
    public GuideView(Activity activity) {
        if(activity.isFinishing()) {
            Log.e("GuideView","activity is finishing");
            return;
        }
        this.activity=activity;
        shape=new GuideShape(activity);
    }

    int zhiId;
    int direction;
    /**
     * 指引图标基于高亮部分的左右？
     * */
    public GuideView setLightDraw(int zhiId,int direction) {
        this.zhiId=zhiId;
        this.direction=direction;
        return this;
    }
    private void setLightDraw() {
        View zview=view.findViewById(zhiId);
        if(lightRect!=null && zview!=null){
            //屏幕
            WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;         // 屏幕宽度（像素）
            int height = dm.heightPixels;       // 屏幕高度（像素）
            int nativiH=StatusBarUtil.getNavigationBarHeight(activity);//虚拟按键高度

            RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) zview.getLayoutParams();
            switch (direction){
                case Left:
                    layoutParams.setMargins(layoutParams.leftMargin,layoutParams.topMargin,width-lightRect.left+10,layoutParams.bottomMargin);
                    break;
                case Top:
                    layoutParams.setMargins(layoutParams.leftMargin,layoutParams.topMargin,layoutParams.rightMargin,height-lightRect.top+nativiH);
                    break;
                case Right:
                    layoutParams.setMargins(lightRect.right,layoutParams.topMargin,layoutParams.rightMargin,layoutParams.bottomMargin);
                    break;
                case Bottom:
                    layoutParams.setMargins(layoutParams.leftMargin,lightRect.bottom,layoutParams.rightMargin,layoutParams.bottomMargin);
                    break;
            }

            zview.setLayoutParams(layoutParams);
        }
    }
    public boolean isShowing(){
        return isShowing;
    }
    public GuideView addHightLight(final View view){
        view.post(new Runnable() {
            @Override
            public void run() {
                lightRect=new Rect();
                view.getGlobalVisibleRect(lightRect);//获取在整个屏幕内的绝对坐标
                shape.addHightLight(lightRect);

                if(zhiId!=0)
                    setLightDraw();
            }
        });
        return this;
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
    public GuideView addHightLight(final View view, final int paddingLeft,final int paddingTop,final int paddingRight,final int paddingBottom){
        view.post(new Runnable() {
            @Override
            public void run() {
                Rect rect=new Rect();
                view.getGlobalVisibleRect(rect);//获取在整个屏幕内的绝对坐标
                rect.left+=paddingLeft;
                rect.top+=paddingTop;
                rect.right-=paddingRight;
                rect.bottom-=paddingBottom;

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
        if(isShowing)
            cancel();

        layout= (FrameLayout) activity.getWindow().getDecorView();
        layout.addView(shape);
        layout.addView(view);
        isShowing=true;
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
        isShowing=false;
        if(cancelListener!=null)
            cancelListener.cancelListener();
    }
    private CancelListener cancelListener;
    public GuideView setOnCancelListener(CancelListener listener){
        cancelListener=listener;
        return this;
    }
    public interface CancelListener{
        void cancelListener();
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
