package com.zpp.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.zpp.demo.R;

public class CustomInputView extends EditText {
    private final String TAG="CustomInputView";

    private float itemWidth;//每个格子的宽度
    private int itemNumber;//多少个格子
    private int itemLength;//每个格子里的文字长度
    private int itemBackGroundColor;//格子颜色

    private float dividerWidth;

    private boolean isHasFocus;
    
    private String mText;
    //文字内容
    private boolean textIsShow;
    //字体大小
    private float textSize;
    //文字颜色
    private int textColor;

    private Paint mPaint;

    public CustomInputView(Context context) {
        this(context,null);
    }

    public CustomInputView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.CustomInputView);
        itemWidth=a.getDimension(R.styleable.CustomInputView_itemWidth,50);
        itemBackGroundColor=a.getColor(R.styleable.CustomInputView_itemBackgroundColor,Color.GRAY);
        itemNumber=a.getInt(R.styleable.CustomInputView_itemNumber,4);
        itemLength=a.getInt(R.styleable.CustomInputView_itemLength,2);
        dividerWidth=a.getDimension(R.styleable.CustomInputView_dividerWidth,5);
        textIsShow=a.getBoolean(R.styleable.CustomInputView_textIsShow,true);
        textSize=a.getDimension(R.styleable.CustomInputView_textSize,18);
        textColor=a.getColor(R.styleable.CustomInputView_textColor,Color.BLACK);

        this.setTextColor(Color.TRANSPARENT);//默认的文字颜色
        this.setCursorVisible(false);//光标是否可见

        //设置EditText文字变化的监听
        this.addTextChangedListener(new TextWatcherImpl());
        //设置焦点变化的监听
        //this.setOnFocusChangeListener(new FocusChangeListenerImpl());
        this.requestFocus();
        this.setFilters(new InputFilter[]{new InputFilter.LengthFilter(itemLength*itemNumber)});

        //抗锯齿画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //防止边缘锯齿
        mPaint.setAntiAlias(true);
        //需要重写onDraw就得调用此
        this.setWillNotDraw(false);
    }



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        //画格子
        drawItem(canvas);
        
        //画文字
        drewText(canvas);
    }

    private void drewText(Canvas canvas) {
        if(mText==null || mText.isEmpty())
            return;

        mPaint.setColor(textColor);
        mPaint.setStrokeWidth(0);
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setTextSize(textSize * 2);

        int ll=mText.length()%itemLength==0?mText.length()/itemLength:mText.length()/itemLength+1;
        float y=getMeasuredHeight()/2+textSize;
       switch (ll){
           case 4:
               String text4=mText.substring(3*itemLength);
               float textWidth4=mPaint.measureText(text4);
               float x4=(itemWidth+dividerWidth)*3+(itemWidth-textWidth4)/2;
               canvas.drawText(text4,x4,y,mPaint);
               drawOtherText(canvas,3,y);
               break;
           case 3:
               String text3=mText.substring(2*itemLength);
               float textWidth3=mPaint.measureText(text3);
               float x3=(itemWidth+dividerWidth)*2+(itemWidth-textWidth3)/2;
               canvas.drawText(text3,x3,y,mPaint);
               drawOtherText(canvas,2,y);
               break;
           case 2:
               String text2=mText.substring(1*itemLength);
               float textWidth2=mPaint.measureText(text2);
               float x2=(itemWidth+dividerWidth)*1+(itemWidth-textWidth2)/2;
               canvas.drawText(text2,x2,y,mPaint);
               drawOtherText(canvas,1,y);
               break;
           case 1:
               String text=mText.substring(0*itemLength);
               float textWidth=mPaint.measureText(text);
               float x=(itemWidth+dividerWidth)*0+(itemWidth-textWidth)/2;
               canvas.drawText(text,x,y,mPaint);
               break;
       }

    }
    private void drawOtherText(Canvas canvas,int t,float y){
        for(int i4=t;i4>0;i4--){
            String text=mText.substring((i4-1)*itemLength,i4*itemLength);
            float textWidth=mPaint.measureText(text);
            float x=(itemWidth+dividerWidth)*(i4-1)+(itemWidth-textWidth)/2;
            canvas.drawText(text,x,y,mPaint);
        }
    }
    private void drawItem(Canvas canvas) {
        mPaint.setColor(itemBackGroundColor);
        for(int i=1;i<=itemNumber;i++) {
            canvas.drawRect((i - 1) * dividerWidth+itemWidth * (i-1), 0, itemWidth * i + (i - 1) * dividerWidth, getMeasuredHeight(), mPaint);
        }
    }

    public void setFilter(InputFilter[] filters) {
        //限制长度
        InputFilter[] filters1 = new InputFilter[filters.length + 1];
        System.arraycopy(filters, 0, filters1, 0, filters.length);
        filters1[filters1.length - 1] = new InputFilter.LengthFilter(itemLength*itemNumber);
        this.setFilters(filters1);
    }

//    //当单击范围在指定范围时
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_UP:
//                //点击的位置，需要做出的操作
//                boolean isClean = (event.getX() > (getWidth())) &&
//                        (event.getX() < (getWidth() - getPaddingRight()));
//                if (isClean){
//
//                }
//                break;
//            default:
//                break;
//        }
//        return super.onTouchEvent(event);
//    }



    /**
     * 光标监控
     * */
    private class FocusChangeListenerImpl implements OnFocusChangeListener{
        @Override
        public void onFocusChange(View v, boolean hasFocus){
            isHasFocus = hasFocus;
            if (isHasFocus){


            } else{

            }
        }
    }

    //输入监听
    private class TextWatcherImpl implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.e(TAG,"onTextChanged:"+getText().toString()+" start:"+start+" before:"+before+" cout:"+count);
            mText=getText().toString();
            invalidate();
        }
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
        int result = 50;
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
        int result = (int) (itemWidth*itemNumber+dividerWidth*(itemNumber-1));
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
