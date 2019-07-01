package com.zandroid.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ShapeGradienView extends View {
    private Paint paint;

    private float mWidth;
    private float mHeight;
    public ShapeGradienView(Context context) {
        this(context,null);
    }

    public ShapeGradienView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShapeGradienView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth=getMeasuredWidth();
        mHeight=getMeasuredHeight();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint =new Paint();

        Log.e(getClass().getName(),"w:"+mWidth+" h:"+mHeight);

        float radius= (float) Math.sqrt(mWidth/2*mWidth/2+mHeight/2*mHeight/2);//勾股
        RadialGradient radialGradient =new RadialGradient(mWidth/2,mHeight/2,radius, new int[]{Color.argb(255,255,0,0),Color.argb(255,255,255,0),Color.argb(255,0,0,255)},null, Shader.TileMode.CLAMP);
        paint.setShader(radialGradient);
        canvas.drawCircle(mWidth/2,mHeight/2,radius,paint);

        float radius1= (float) Math.sqrt(mWidth*3/4*mWidth*3/4+mHeight*3/4*mHeight*3/4);//勾股
        RadialGradient radialGradient1 =new RadialGradient(mWidth/4,mHeight/4,radius1, new int[]{Color.argb(127,255,0,0),Color.argb(127,255,255,0),Color.argb(127,0,0,255)},null, Shader.TileMode.CLAMP);
        paint.setShader(radialGradient1);
        canvas.drawCircle(mWidth/4,mHeight/4,radius1,paint);


    }

    /**
     * view的大小控制
     * 设置其按照内容大小
     */
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        setMeasuredDimension(measureWidth(widthMeasureSpec),
//                measureHeight(heightMeasureSpec));
//    }
//    private int measureHeight(int measureSpec) {
//        int result = getPaddingTop()+getPaddingBottom();
//        int mode = MeasureSpec.getMode(measureSpec);
//        int size = MeasureSpec.getSize(measureSpec);
//
//        switch (mode) {
//            case MeasureSpec.UNSPECIFIED://不指定测量模式，view大小没有限制
//            case MeasureSpec.EXACTLY://精确值模式
//                result = size;
//                break;
//            case MeasureSpec.AT_MOST://最大值模式
//                result = Math.min(result, size);
//                break;
//        }
//        return result;
//    }
//
//    private int measureWidth(int measureSpec) {
//        int result = getPaddingLeft()+getPaddingRight();
//        int mode = MeasureSpec.getMode(measureSpec);
//        int size = MeasureSpec.getSize(measureSpec);
//        switch (mode) {
//            case MeasureSpec.UNSPECIFIED://不指定测量模式，view大小没有限制
//            case MeasureSpec.EXACTLY://精确值模式
//                result = size;
//                break;
//            case MeasureSpec.AT_MOST://最大值模式
//                result = Math.min(result, size);
//                break;
//        }
//        return result;
//    }
}
