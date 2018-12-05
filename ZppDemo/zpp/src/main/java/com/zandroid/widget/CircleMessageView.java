package com.zandroid.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.zandroid.R;


/**
 * 圆形，中间有文字，左右提示文字
 * */
public class CircleMessageView extends View {
    private final String TAG="CircleMessageView";

    private float circleRadio;
    private int circleColor;
    private float mCircleCenterX;
    private float mCircleCenterY;


    //字体大小
    private float topTextSize;
    //文字颜色
    private int topTextColor;
    //文字内容
    private String topText;
    //中间的分割线
    private int lineColor;
    private float lineWidth;
    //字体大小
    private float bottomTextSize;
    //文字颜色
    private int bottomTextColor;
    //文字内容
    private String bottomText;

    private Paint mPaint;

    public CircleMessageView(Context context) {
        this(context,null);
    }

    public CircleMessageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    @SuppressLint("ResourceAsColor")
    public CircleMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleMessageView);
        //圆的半径
        circleRadio = a.getDimension(R.styleable.CircleMessageView_circleRadio, 300);
        //圆的颜色
        circleColor=a.getColor(R.styleable.CircleMessageView_circleColor,Color.WHITE);

        topTextSize = a.getDimension(R.styleable.CircleMessageView_topTextSize, 14);//文字大小
        topTextColor = a.getColor(R.styleable.CircleMessageView_topTextColor, Color.BLACK);//文字颜色
        topText=a.getString(R.styleable.CircleMessageView_topText);
        lineColor = a.getColor(R.styleable.CircleMessageView_lineColor, Color.BLACK);
        lineWidth=a.getDimension(R.styleable.CircleMessageView_lineWidth,1);
        bottomTextSize = a.getDimension(R.styleable.CircleMessageView_bottomTextSize, 14);//文字大小
        bottomTextColor = a.getColor(R.styleable.CircleMessageView_bottomTextColor, Color.BLACK);//文字颜色
        bottomText=a.getString(R.styleable.CircleMessageView_bottomText);

        a.recycle();

        //抗锯齿画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //防止边缘锯齿
        mPaint.setAntiAlias(true);
        //需要重写onDraw就得调用此
        this.setWillNotDraw(false);
    }

    public void setTopText(String topText){
        this.setTopText(topText,0);
    }
    public void setTopText(String topText,int topTextColor){
        this.setTopText(topText,topTextColor,0);
    }
    public void setTopText(String topText,int topTextColor,int topTextSize){
        this.topText=topText;
        if(topTextColor!=0)
            this.topTextColor=topTextColor;
        if(topTextSize!=0)
            this.topTextSize=topTextSize;
        invalidate();
    }
    public void setBottomTextText(String bottomText){
        this.setBottomTextText(bottomText,0);
    }
    public void setBottomTextText(String bottomText,int bottomTextColor){
        this.setBottomTextText(bottomText,bottomTextColor,0);
    }
    public void setBottomTextText(String bottomText,int bottomTextColor,int bottomTextSize){
        this.bottomText=bottomText;
        if(bottomTextColor!=0)
            this.bottomTextColor=bottomTextColor;
        if(bottomTextSize!=0)
            this.bottomTextSize=bottomTextSize;
        invalidate();
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int viewWidth = getMeasuredWidth();
        int viewHidth = getMeasuredHeight();
        Log.e(TAG,"viewWidth:"+viewWidth+" -- viewHidth:"+viewHidth);
        mCircleCenterX = (viewWidth-getPaddingLeft()-getPaddingRight()) / 2+getPaddingLeft();
        mCircleCenterY = (viewHidth-getPaddingTop()-getPaddingBottom()) / 2+getPaddingTop();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(circleColor);
        canvas.drawCircle(mCircleCenterX,mCircleCenterY,circleRadio,mPaint);

        if(!topText.isEmpty())
            drawTopText(canvas);
        drawTopTextLine(canvas);
        if(!bottomText.isEmpty())
            drawBottomText(canvas);
    }
    /**
     * 绘制分割线
     * */
    private void drawTopTextLine(Canvas canvas) {
        mPaint.setColor(lineColor);
        float left=mCircleCenterX-circleRadio*4/5;
        float top=mCircleCenterY-lineWidth/2;
        float right=mCircleCenterX+circleRadio*4/5;
        float bottom=mCircleCenterY+lineWidth/2;
        canvas.drawRect(left,top,right,bottom,mPaint);//画矩形的方式
    }

    /**
     * 绘制上部分文字
     * */
    private void drawTopText(Canvas canvas) {
//        mPaint.setColor(topTextColor);
//        mPaint.setStrokeWidth(0);
//        mPaint.setTypeface(Typeface.DEFAULT);
//        mPaint.setTextSize(topTextSize*2);
//        float textWidth = mPaint.measureText(topText);
//        float startX=mCircleCenterX-textWidth/2;
//        float startY=mCircleCenterY-topTextSize;
//        canvas.drawText(topText,startX,startY,mPaint);

        TextPaint textPaint=new TextPaint();
        textPaint.setColor(topTextColor);
        textPaint.setTextSize(topTextSize*2);
        textPaint.setTypeface(Typeface.DEFAULT);
        float textWidth = mPaint.measureText(topText);
        float width=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,textWidth,this.getResources().getDisplayMetrics());
        Log.e(TAG,"行数："+Math.round(width/(circleRadio*8/5)));
        float startX=mCircleCenterX-circleRadio*4/5;
        float startY= mCircleCenterY-bottomTextSize*2*(int)Math.ceil(width/(circleRadio*8/5))-bottomTextSize-lineWidth/2;//通过计算行数，确认Y轴起始
        textCenter(topText,textPaint,canvas,startX,startY, (int) (circleRadio*8/5),Layout.Alignment.ALIGN_CENTER,1.0f,0,false);

    }
    /**
     * 绘制下部分文字
     * */
    private void drawBottomText(Canvas canvas) {

        //换行
        TextPaint textPaint=new TextPaint();
        textPaint.setColor(bottomTextColor);
        textPaint.setTextSize(bottomTextSize*2);
        textPaint.setTypeface(Typeface.DEFAULT);
        float startX=mCircleCenterX-circleRadio*4/5;
        float startY=mCircleCenterY+bottomTextSize+lineWidth/2;
        textCenter(bottomText,textPaint,canvas,startX,startY, (int) (circleRadio*8/5),Layout.Alignment.ALIGN_CENTER,1.0f,0,false);

//        不需要换行
//        mPaint.setColor(bottomTextColor);
//        mPaint.setStrokeWidth(0);
//        mPaint.setTypeface(Typeface.DEFAULT);
//        mPaint.setTextSize(bottomTextSize*2);
//        float textWidth = mPaint.measureText(bottomText);
//        float startX=mCircleCenterX-textWidth/2;
//        float startY=mCircleCenterY+bottomTextSize*3;
//        canvas.drawText(bottomText,startX,startY,mPaint);
    }

    /**
     * 绘制分行的文字
     * source : 需要分行的字符串
     * textPaint : 画笔对象
     * width : layout的宽度，字符串超出宽度时自动换行。
     * startX:起始x
     * startY:起始Y
     * align : layout的对其方式，有ALIGN_CENTER， ALIGN_NORMAL， ALIGN_OPPOSITE 三种。
     * spacingmult : 相对行间距，相对字体大小，1.5f表示行间距为1.5倍的字体高度。
     * spacingadd : 在基础行距上添加多少,实际间距为spacingmult spacingadd 的和
     * includepad : 不明，希望知道的朋友可以告知一下
     */
    private void textCenter(String string, TextPaint textPaint, Canvas canvas, float startX,float startY, int width, Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad){
        StaticLayout staticLayout = new StaticLayout(string,textPaint,width, align,spacingmult,spacingadd,includepad);
        canvas.save();//保存之前的状态
        canvas.translate(startX,startY);
        staticLayout.draw(canvas);
        canvas.restore();//回到之前的状态
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
        int result = (int) (circleRadio*2)+getPaddingTop()+getPaddingBottom();
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
        int result = (int) (circleRadio*2)+getPaddingLeft()+getPaddingRight();
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
    @Override
    public boolean isFocused() {
        return false;
    }
}
