package com.zandroid.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GuideShape extends View implements View.OnTouchListener{
    private Paint mPaint; //画笔
    private int width; //屏幕宽度（也是蒙版宽度）
    private int height;//屏幕高度
    private PorterDuffXfermode pdf;
    private OnTouchSideListener touchSideListener;//自定义的点击交互监听器
    private OnTouchHighListener touchHighListener;//自定义的点击交互监听器

    private List<Rect> hightLightView;

    public GuideShape(Context context) {//三个构造函数
        super(context);
    }

    public GuideShape(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GuideShape(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addHightLight(Rect rect){
        if(hightLightView==null){
            hightLightView=new ArrayList<>();
        }
        hightLightView.add(rect);
        invalidate();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width =MeasureSpec.getMode(widthMeasureSpec)==MeasureSpec.UNSPECIFIED?100:MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getMode(heightMeasureSpec)==MeasureSpec.UNSPECIFIED?100:MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas){
        pdf = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mPaint = new Paint();//创建画笔
        int sc = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        //其实在canvas上的作画都是做在默认layer上，但这里我们利用saveLayer创建了一个新的layer，之后调用canvas.drawXXX就是画在这个新的layer上
        mPaint.setColor(Color.BLACK);//设置Color为黑色
        mPaint.setAlpha(0x80);//设置透明度，这个一定要在设置颜色后调用，因为setColor的参数也是带有透明度的
        mPaint.setStyle(Paint.Style.FILL);//设置为填充样式
        canvas.drawRect(0,0,width,height,mPaint);//画出覆盖整个View的长方形。
        mPaint.setXfermode(pdf);//设置Xfermode为pdf

        if(hightLightView!=null){
            for(Rect rect:hightLightView) {
                canvas.drawRect(rect,mPaint);
            }
        }

        //canvas.drawCircle(200,200,100,mPaint);//在一个位置镂空一个圆形
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {//因为继承了OnTouchListener借口必须实现这个方法
        return false;
    }

    public void setTouchSideListener(OnTouchSideListener listener){
        this.touchSideListener=listener;
    }
    public interface OnTouchSideListener {//点击事件接口
        void onTouchSide();
    }
    public void setTouchHighListener(OnTouchHighListener listener){
        this.touchHighListener=listener;
    }
    public interface OnTouchHighListener {//点击事件接口
        void onTouchHigh();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            double x = (double) event.getRawX();
            double y = (double) event.getRawY();
            boolean inHigh = false;
            if (hightLightView != null) {
                for (Rect rect : hightLightView) {
                    if (x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom) {//在高亮区域
                        inHigh = true;
                    }
                }
            }
            if (touchSideListener != null &&!inHigh) {//点击非高亮区域时
                this.touchSideListener.onTouchSide();
            }else if(touchHighListener!=null && inHigh){
                this.touchHighListener.onTouchHigh();
            }
        }
        return true;
    }
}
