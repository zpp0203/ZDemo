package com.zandroid.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

import com.zandroid.R;


/*
*给图片定义形状
* */
public class BitmapShaders extends View {

    private float imgWidth=100;
    private BitmapShader bitmapShader = null;
    private Bitmap mBitmap;
    private ShapeDrawable shapeDrawable = null;
    public BitmapShaders(Context context) {
        this(context,null);
    }

    public BitmapShaders(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public BitmapShaders(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BitmapShaders, defStyle, 0);
        int image=a.getResourceId(R.styleable.BitmapShaders_img,android.R.color.transparent);
        Drawable drawable=getResources().getDrawable(image);
        mBitmap=getBitmapFromDrawable(drawable);
    }

    public void setImage(int imgColor){
        //得到图像
        Drawable drawable=getResources().getDrawable(imgColor);
        mBitmap=getBitmapFromDrawable(drawable);
        invalidate();
    }
    public void setImageBitmap(Bitmap img){
        mBitmap = img;
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //构造渲染器BitmapShader
        Bitmap bitmap=Bitmap.createScaledBitmap(mBitmap, (int) imgWidth, (int)imgWidth, true);
        bitmapShader = new BitmapShader(bitmap,Shader.TileMode.MIRROR,Shader.TileMode.REPEAT);
        //将图片裁剪为椭圆形
        //构建ShapeDrawable对象并定义形状为椭圆
        shapeDrawable = new ShapeDrawable(new OvalShape());
//        //绘制矩形
//        mDrawables[0] = new ShapeDrawable(new RectShape());
//        //绘制椭圆
//        mDrawables[1] = new ShapeDrawable(new OvalShape());
//        //绘制上面俩个角是圆角的矩形
//        mDrawables[2] = new ShapeDrawable(new RoundRectShape(outerR, null,
//                null));
//        //绘制上面俩角是圆角，并且有一个内嵌的矩形
//        mDrawables[3] = new ShapeDrawable(new RoundRectShape(outerR, inset,
//                null));
//        ////绘制上面俩角是圆角，并且有一个内嵌的矩形且左上角和右下角是圆形矩形环
//        mDrawables[4] = new ShapeDrawable(new RoundRectShape(outerR, inset,
//                innerR));
//        //绘制指定路径的集合体
//        mDrawables[5] = new ShapeDrawable(new PathShape(path, 100, 100));
//        // 用自定的ShapDrawble绘制开始弧度45扫过弧度-270的椭圆
//        mDrawables[6] = new MyShapeDrawable(new ArcShape(45, -270));
        //得到画笔并设置渲染器
        shapeDrawable.getPaint().setShader(bitmapShader);
        //设置显示区域
        shapeDrawable.setBounds(0, 0,(int)imgWidth,(int)imgWidth);//可见区域

        //绘制shapeDrawable
        shapeDrawable.draw(canvas);
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap((int)imgWidth, (int)imgWidth, Bitmap.Config.ARGB_8888);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
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
        int result = getPaddingTop()+getPaddingBottom();
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
        int result = getPaddingLeft()+getPaddingRight();
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
        imgWidth=result;
        return result;
    }
}