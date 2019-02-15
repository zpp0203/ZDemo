package com.zandroid.falldown;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现循环动画
 * 线程通信实现一个简单的定时器，达到周期性绘制View的效果
* */
public class FallingView extends View {

    private int viewWidth;
    private int viewHeight;

    private static final int defaultWidth = 600;//默认宽度
    private static final int defaultHeight = 1000;//默认高度
    private static final int intervalTime = 5;//重绘间隔时间

    //private Paint testPaint;
    //private int snowY;
    private List<FallObject> fallObjects;


    public FallingView(Context context) {
        super(context);
        init();
    }

    public FallingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init(){
        fallObjects = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = measureSize(defaultHeight, heightMeasureSpec);
        int width = measureSize(defaultWidth, widthMeasureSpec);
        setMeasuredDimension(width, height);

        viewWidth = width;
        viewHeight = height;
    }

    private int measureSize(int defaultSize,int measureSpec) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getHandler().postDelayed(runnable, intervalTime);//间隔一段时间再进行重绘

        if(fallObjects.size()>0){
            for (int i=0;i<fallObjects.size();i++) {
                //然后进行绘制
                fallObjects.get(i).drawObject(canvas);
            }
            // 隔一段时间重绘一次, 动画效果
            getHandler().postDelayed(runnable, intervalTime);
        }
    }

    // 重绘线程
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    /**
     * 向View添加下落物体对象
     * @param fallObject 下落物体对象
     * @param num
     */
    public void addFallObject(final FallObject fallObject, final int num) {
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                for (int i = 0; i < num; i++) {
                    FallObject newFallObject = new FallObject(fallObject.builder,viewWidth,viewHeight);
                    fallObjects.add(newFallObject);
                }
                invalidate();
                return true;
            }
        });
    }
}
