package com.zpp.demo.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zpp.demo.R;


/**
 * 进度圆环（画圆弧的方式）
 *
 * .invalidate方法
 * 在UI主线程中，用invalidate()；本质是调用View的onDraw()绘制。
* */
public class SuperCircleView extends View {

    private final String TAG = "SuperCircleView";

    private ValueAnimator valueAnimator;
    private int mViewCenterX;   //view宽的中心点(可以暂时理解为圆心)
    private int mViewCenterY;   //view高的中心点(可以暂时理解为圆心)

    private float mMinRadio; //最里面白色圆的半径
    private float mRingWidth; //圆环的宽度
    private int mMinCircleColor;    //最里面圆的颜色
    private int mRingNormalColor;    //默认圆环的颜色
    private Paint mPaint;
    private int color[] ;   //渐变颜色

    private RectF mRectF; //圆环的矩形区域
    private int mSelectRing = 0; //要显示的彩色区域(岁数值变化)
    private int mMaxValue;

    //文字内容
    private boolean mTextIsShow;
    //字体大小
    private float mTextSize;
    //文字颜色
    private int mTextColor;
    private String text;

    public SuperCircleView(Context context) {
        this(context, null);
    }
    public SuperCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SuperCircleView);
        //最里面白色圆的半径
        //mMinRadio = a.getDimension(R.styleable.SuperCircleView_min_circle_radio, 300);
        //圆环宽度
        mRingWidth = a.getDimension(R.styleable.SuperCircleView_ring_width, 40);

        //最里面的圆的颜色(绿色)
        mMinCircleColor = a.getColor(R.styleable.SuperCircleView_circle_color, ContextCompat.getColor(context,R.color.white));
        //圆环的默认颜色(圆环占据的是里面的圆的空间)
        mRingNormalColor = a.getColor(R.styleable.SuperCircleView_ring_normal_color, ContextCompat.getColor(context,R.color.grey));
        //圆环要显示的彩色的区域
        mSelectRing = a.getInt(R.styleable.SuperCircleView_ring_progress, 0);

        mMaxValue = a.getInt(R.styleable.SuperCircleView_maxValue, 100);

        mTextIsShow = a.getBoolean(R.styleable.SuperCircleView_textIsShow, false);//文字
        mTextSize = a.getDimension(R.styleable.SuperCircleView_textSize, 14);//文字大小
        mTextColor = a.getColor(R.styleable.SuperCircleView_textColor, Color.BLACK);//文字颜色
        text=a.getString(R.styleable.SuperCircleView_text);
        a.recycle();

        //抗锯齿画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //防止边缘锯齿
        mPaint.setAntiAlias(true);
        //需要重写onDraw就得调用此
        this.setWillNotDraw(false);

        //圆环渐变的颜色
        color= new int[3];
        color[0] = Color.parseColor("#FFD300");
        color[1] = Color.parseColor("#FF0084");
        color[2] = Color.parseColor("#16FF00");

    }
    /**
     * 设置进度渐变颜色
     * */
    public void setColor(int[] color) {
        this.color = color;
    }

    /**
     * 设置当前值
     *
     * @param value
     */
    public void setValue(int value) {
        if (value > mMaxValue) {
            value = mMaxValue;
        }
        int start = 0;
        int end = value;
        startAnimator(start, end, 2000);
    }

    private void startAnimator(int start, int end, long animTime) {
        valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.setDuration(animTime);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.i(TAG, "onAnimationUpdate: animation.getAnimatedValue()::"+animation.getAnimatedValue());
                int i = Integer.valueOf(String.valueOf(animation.getAnimatedValue()));
                text=i+"%";
                //每个单位长度占多少度
                mSelectRing=(int) (360 * (i / 100f));
                Log.i(TAG, "onAnimationUpdate: mSelectRing::"+mSelectRing);
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //view的宽和高,相对于父布局(用于确定圆心)
        int viewWidth = getMeasuredWidth();
        mMinRadio=viewWidth / 2-mRingWidth;
        mViewCenterX = viewWidth / 2;
        mViewCenterY = viewWidth / 2;
        //画矩形（内弧线所在的位置）
        mRectF = new RectF(mRingWidth/2, mRingWidth/2, viewWidth-mRingWidth/2, viewWidth-mRingWidth/2);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(mMinCircleColor);
        canvas.drawCircle(mViewCenterX, mViewCenterY, mMinRadio, mPaint);
        //画默认圆环（360度圆弧）
        drawNormalRing(canvas);
        //画彩色圆环
        drawColorRing(canvas);

        //画文字
        drawText(canvas);
    }
    /**
     * 画默认文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        if (mTextIsShow && text!=null && !text.isEmpty()) {
            mPaint.setColor(mTextColor);
            mPaint.setStrokeWidth(0);
            mPaint.setTypeface(Typeface.DEFAULT);
            mPaint.setTextSize(mTextSize * 2);
            float textWidth = mPaint.measureText(text);
            //因为画彩色圆环时逆时针了90度，所以需要顺时针旋转90度
            canvas.rotate(90, mViewCenterX, mViewCenterY);
            canvas.drawText(text, mViewCenterX - textWidth / 2, mViewCenterX+mTextSize / 2, mPaint);
        }
    }

    /**
     * 画默认圆环
     *
     * @param canvas
     */
    private void drawNormalRing(Canvas canvas) {
        Paint ringNormalPaint = new Paint(mPaint);
        ringNormalPaint.setStyle(Paint.Style.STROKE);
        ringNormalPaint.setStrokeWidth(mRingWidth);
        ringNormalPaint.setColor(mRingNormalColor);//圆环默认颜色为灰色
        canvas.drawArc(mRectF, 360, 360, false, ringNormalPaint);
    }

    /**
     * 画彩色圆环
     *
     * @param canvas
     */
    private void drawColorRing(Canvas canvas) {
        Paint ringColorPaint = new Paint(mPaint);
        ringColorPaint.setStyle(Paint.Style.STROKE);
        ringColorPaint.setStrokeWidth(mRingWidth);
        ringColorPaint.setShader(new SweepGradient(mViewCenterX, mViewCenterX, color, null));
        //逆时针旋转90度
        canvas.rotate(-90, mViewCenterX, mViewCenterY);
        canvas.drawArc(mRectF, 360, mSelectRing, false, ringColorPaint);
        ringColorPaint.setShader(null);
    }
}
