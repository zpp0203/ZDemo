package com.zpp.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import com.zpp.demo.R;

public class CustomInputView extends View {
    private final String TAG="CustomInputView";


    private float itemWidth;//每个格子的宽度
    private int itemNumber;//多少个格子
    private int itemLength;//每个格子里的文字长度
    private Drawable itemBackGround;//格子背景
    private float dividerWidth;//分隔线宽度
    private String mText;//文字内容
    private boolean isPassword;//是否是密码形式
    private float textSize;//字体大小
    private int textColor;//文字颜色

    private Paint mPaint;

    private int inputType;//输入的类型
    private int currentIndex;
    private int inputLength;
    private String[] values;
    private InputCompleteListener completeListener;

    public CustomInputView(Context context) {
        this(context,null);
    }

    public CustomInputView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final float scale = context.getResources().getDisplayMetrics().density;

        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.CustomInputView);
        itemWidth=a.getDimension(R.styleable.CustomInputView_itemWidth,40*scale);

        itemBackGround=a.getDrawable(R.styleable.CustomInputView_itemBackground);
        itemNumber=a.getInt(R.styleable.CustomInputView_itemNumber,4);
        itemLength=a.getInt(R.styleable.CustomInputView_itemLength,2);
        dividerWidth=a.getDimension(R.styleable.CustomInputView_dividerWidth,5*scale);
        isPassword=a.getBoolean(R.styleable.CustomInputView_isPassword,true);
        textSize=a.getDimension(R.styleable.CustomInputView_textSize,14*scale);
        textColor=a.getColor(R.styleable.CustomInputView_textColor,Color.BLACK);
        inputType = a.getInt(R.styleable.CustomInputView_inputType, InputType.TYPE_CLASS_TEXT);//TYPR_NULL
        currentIndex = 0;
        inputLength = itemLength*itemNumber;
        values=new String[inputLength];


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
        mText=getText();

        //画格子
        drawItem(canvas);
        
        //画文字
        drewText(canvas);
    }

    private void drewText(Canvas canvas) {
        if(currentIndex== 0)
            return;
        mPaint.setColor(textColor);
        mPaint.setStrokeWidth(0);
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setTextSize(textSize * 2);
        mText=isPassword?getText().replaceAll(".","*"):getText();
        float y=getMeasuredHeight()/2+textSize;
        //计算有多少个item的内容
        int items=currentIndex%itemLength==0?currentIndex/itemLength:currentIndex/itemLength+1;

        for(int i=0;i<items;i++){
            String text=i==items-1?mText.substring(i*itemLength,currentIndex):mText.substring(i*itemLength,(i+1)*itemLength);
            float x=(itemWidth+dividerWidth)*i+(itemWidth-mPaint.measureText(text))/2;
            canvas.drawText(text,x,y,mPaint);
        }
    }
    private void drawItem(Canvas canvas) {
        //background包括color和Drawable,这里分开取值
        Bitmap bitmap=null;
        if (itemBackGround instanceof ColorDrawable) {
            ColorDrawable colordDrawable = (ColorDrawable) itemBackGround;
            int color = colordDrawable.getColor();
            mPaint.setColor(color);
        } else {
            bitmap = ((BitmapDrawable) itemBackGround).getBitmap();
        }
        for (int i = 1; i <= itemNumber; i++) {
            if(bitmap!=null) {
                Rect bitRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                Rect canRect = new Rect((int) ((i - 1) * dividerWidth + itemWidth * (i - 1)), 0, (int) (itemWidth * i + (i - 1) * dividerWidth), getMeasuredHeight());
                canvas.drawBitmap(bitmap, bitRect, canRect, mPaint);
            }else {
                canvas.drawRect((i - 1) * dividerWidth + itemWidth * (i - 1), 0, itemWidth * i + (i - 1) * dividerWidth, getMeasuredHeight(), mPaint);
            }
        }
    }
/*
 * 光标的闪烁
 * */
//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        isLive = true;
//        new Thread(() -> {
//            while (isLive) {
//                if (hasFocus()) {
//                    isShowCursor = !isShowCursor;
//                    postInvalidate();
//                } else {
//                    isShowCursor = false;
//                }
//                try {
//                    Thread.sleep(800);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        isLive = false;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setFocusableInTouchMode(true); //important
                setFocusable(true);
                requestFocus();
                return true;
            case MotionEvent.ACTION_UP:
                try {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(this, InputMethodManager.RESULT_SHOWN);
                    imm.restartInput(this);
                } catch (Exception ignore) {
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    /*
    * 设置View为可输入
    * */
    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }
    /*
    * 输入法输入内容的桥梁
    * */
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = inputType;
        outAttrs.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI;

        return new BaseInputConnection(this, true) {
            @Override
            public boolean commitText(CharSequence text, int newCursorPosition) {
                Log.e(TAG,text+" - "+newCursorPosition);
                if (currentIndex == inputLength) {
                    //isShowCursor = false;
                    return false;
                }
                inputText(text.toString());

                invalidate();
                return true;
            }

            @Override
            public boolean deleteSurroundingText(int beforeLength, int afterLength) {
                for (int i = afterLength; i < beforeLength; i++) {
                    deleteLastText();
                }
                invalidate();
                return true;
            }

            @Override
            public boolean sendKeyEvent(KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    switch (event.getKeyCode()) {
                        case KeyEvent.KEYCODE_DEL:
                            if (currentIndex > 0) {
                                deleteSurroundingText(currentIndex, currentIndex - 1);
                            }
                            break;
                        case KeyEvent.KEYCODE_0:
                            commitText("0", currentIndex);
                            break;
                        case KeyEvent.KEYCODE_1:
                            commitText("1", currentIndex);
                            break;
                        case KeyEvent.KEYCODE_2:
                            commitText("2", currentIndex);
                            break;
                        case KeyEvent.KEYCODE_3:
                            commitText("3", currentIndex);
                            break;
                        case KeyEvent.KEYCODE_4:
                            commitText("4", currentIndex);
                            break;
                        case KeyEvent.KEYCODE_5:
                            commitText("5", currentIndex);
                            break;
                        case KeyEvent.KEYCODE_6:
                            commitText("6", currentIndex);
                            break;
                        case KeyEvent.KEYCODE_7:
                            commitText("7", currentIndex);
                            break;
                        case KeyEvent.KEYCODE_8:
                            commitText("8", currentIndex);
                            break;
                        case KeyEvent.KEYCODE_9:
                            commitText("9", currentIndex);
                            break;
                    }
                }
                return true;
            }
        };
    }

    public void clearInputText() {
        currentIndex = 0;
        values = new String[inputLength];
    }

    public void inputText(String str) {
        char[] chars=str.toCharArray();
        for(int i=0;i<str.length();i++) {
            values[currentIndex] = String.valueOf(chars[i]);
            currentIndex++;
        }

        if (completeListener != null) {
            completeListener.onComplete(getText());
        }
        if (currentIndex == inputLength) {
            try {
                //隐藏软键盘
                ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getWindowToken(), 0);
            } catch (Exception ignore) {
            }
        }
    }

    private void deleteLastText() {
        if (currentIndex > 0) {
            currentIndex--;
        }
        values[currentIndex] = null;
        completeListener.onComplete(getText());
    }

    public String[] getValues() {
        return values;
    }

    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (String str : values) {
            if (str != null)
                sb.append(str);
        }
        return sb.toString();
    }

    public void setCompleteListener(InputCompleteListener listener) {
        this.completeListener = listener;
    }
    //输入监听
    public interface InputCompleteListener {
        void onComplete(String values);
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
        int result = (int) itemWidth;
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
