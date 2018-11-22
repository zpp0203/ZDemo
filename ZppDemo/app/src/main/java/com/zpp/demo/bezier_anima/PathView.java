package com.zpp.demo.bezier_anima;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;



public class PathView extends View {
    private Paint paint;

    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    AnimatorPath animatorPath;
    Path path;
    private void initView(Context context) {
        paint = new Paint();
        //抗锯齿
        paint.setAntiAlias(true);
        //防抖动
        paint.setDither(true);
        //设置画笔未实心
        //paint.setStyle(Paint.Style.STROKE);
        //设置颜色
        //paint.setColor(Color.RED);
        //设置画笔宽度
        //paint.setStrokeWidth(3);

        //需要重写onDraw就得调用此
        this.setWillNotDraw(false);
    }
    boolean isFirstInit;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width=getMeasuredWidth();
        int height=getMeasuredHeight();

        if (!isFirstInit) {
            //动画的轨迹
            animatorPath = new AnimatorPath();
            animatorPath.moveTo(width/2,height/4);
            animatorPath.thirdBesselCurveTo(width,-height*5/12,width*5/4,height/2,width/2,height);
            animatorPath.thirdBesselCurveTo(-width/5,height/2,0,-height*5/12,width/2,height/4);
            startAnimatorPath();   //开始动画
            isFirstInit = true;
        }

        path = new Path();
        path.moveTo(width/2,height/4);//起始点
//        path.lineTo(460,460);//线条
//        path.quadTo(width*4/5,-height/5,width,height/3); //圆滑曲线
        path.cubicTo(width,-height*5/12,width*5/4,height/2,width/2,height);//圆滑曲线
        path.cubicTo(-width/5,height/2,0,-height*5/12,width/2,height/4);//圆滑曲线
//        path.cubicTo(width,height/3,width,height*3/4,width/2,height);//圆滑曲线

        paint.setColor(Color.RED);
        canvas.drawPath(path,paint);

        currX=currX==0?width/2:currX;
        currY=currY==0?height/4:currY;
        paint.setColor(Color.WHITE);
        canvas.drawCircle(currX, currY, 15, paint);


    }
    float currX,currY;
    /**
     * 设置动画
     */
    public void startAnimatorPath() {


        ObjectAnimator  anim = ObjectAnimator.ofObject(this, "---", new PathEvaluator(), animatorPath.getPoints().toArray());
//        anim.setEvaluator(new PathEvaluator());
//        anim.setObjectValues(path.getPoints().toArray());
        anim.setInterpolator(new DecelerateInterpolator());//动画插值器
        anim.setDuration(3000);
        anim.setRepeatCount(-1);
        anim.start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                PathPoint point = (PathPoint) animation.getAnimatedValue();
//                Log.e("---",point.mX+"--"+point.mY+"-"+point.mContorl0X+"--"+point.mContorl1X);
                currX=point.mX;
                currY=point.mY;
                invalidate();
            }
        });


    }

    /**
     * view的大小控制
     * 设置其按照内容大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }
    private int measureHeight(int measureSpec) {
        int result = 100+getPaddingTop()+getPaddingBottom();
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED://不指定测量模式，view大小没有限制
            case MeasureSpec.EXACTLY://精确值模式
                result = size;
                break;
            case MeasureSpec.AT_MOST://最大值模式
                result = Math.min(result, size);
                break;
        }
        return result;
    }

    private int measureWidth(int measureSpec) {
        int result = 100+getPaddingLeft()+getPaddingRight();
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED://不指定测量模式，view大小没有限制
            case MeasureSpec.EXACTLY://精确值模式
                result = size;
                break;
            case MeasureSpec.AT_MOST://最大值模式
                result = Math.min(result, size);
                break;
        }
        return result;
    }
}
