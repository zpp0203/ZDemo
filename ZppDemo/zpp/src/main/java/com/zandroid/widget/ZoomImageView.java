package com.zandroid.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
//可缩放、移动的ImageView
public class ZoomImageView extends android.support.v7.widget.AppCompatImageView implements Runnable {

    private static final int DEFAULT_LONG_PRESS_TIMEOUT = 500;
    private boolean isLoadImg;
    private boolean blockClick;//是否截留单击事件
    private int mode;
    private float[] values = new float[9];//存储matrix数据
    private float minScale, maxScale;
    private long lastClickTime;
    private static final long TIME_DELAY = 200;
    private static final int DRAG = 1, ZOOM = 2;
    private Matrix matrix;
    private Drawable drawable;
    private Point p0, p1, p2;

    public ZoomImageView(Context context) {
        this(context, null, 0);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        isLoadImg = true;
        p0 = new Point();
        p1 = new Point();
        p2 = new Point();
        matrix = new Matrix();
        drawable = getDrawable();
    }


    //reset the image to the initial state
    public void reset() {
        matrix.reset();
        isLoadImg = true;
        invalidate();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        matrix.reset();
        isLoadImg = true;
        invalidate();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        setImageDrawable(new BitmapDrawable(bm));
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        this.drawable = drawable;
        isLoadImg = true;
        if(matrix!=null)
            matrix.reset();//重置matrix
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isLoadImg && drawable != null) {

            int iWidth = drawable.getIntrinsicWidth();
            int iHeight = drawable.getIntrinsicHeight();
            float sx = getWidth() * 1.0f / iWidth;
            float sy = getHeight() * 1.0f / iHeight;
            if (sx > sy) {
                minScale = sy;
                matrix.postScale(sy, sy);
                matrix.postTranslate((getWidth() - sy * iWidth) / 2, 0);
            } else {
                minScale = sx;
                matrix.postScale(sx, sx);
                matrix.postTranslate(0, (getHeight() - sx * iHeight) / 2);
            }
            minScale = minScale > 1 ? 1 : minScale;
            //maxScale = minScale * 10;
            maxScale=Math.max(getWidth()/iWidth,getHeight()/iHeight)+2;
            isLoadImg = false;
        }
        canvas.concat(matrix);

        if (drawable != null) {
            drawable.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                postDelayed(this, DEFAULT_LONG_PRESS_TIMEOUT);//longClick
                mode = DRAG;
                p0.setXY(event.getX(), event.getY());
                p1.setXY(event.getX(), event.getY());
                if (drawable != null) {
                    onDoubleClick(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                }
                lastClickTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                if (drawable == null)
                    return true;
                if (distance(event.getX(), event.getY(), p1.x, p1.y) > 8) {//判断手指移动距离，超过则取消长按
                    removeCallbacks(this);
                }
                matrix.getValues(values);
                if (mode == DRAG) {//平移模式
                    float dx = event.getX() - p1.x;
                    float dy = event.getY() - p1.y;
                    dx = checkXboundary(dx, drawable.getIntrinsicWidth());
                    dy = checkYboundary(dy, drawable.getIntrinsicHeight());
                    matrix.postTranslate(dx, dy);
                    p1.setXY(event.getX(), event.getY());
                } else if (mode == ZOOM) {//缩放模式
                    float x1 = event.getX(0);
                    float y1 = event.getY(0);
                    float x2 = event.getX(1);
                    float y2 = event.getY(1);
                    float scale = (float) (distance(x1, y1, x2, y2) / distance(p1.x, p1.y, p2.x, p2.y));
                    if (scale * values[Matrix.MSCALE_Y] < minScale)
                        scale = minScale / values[Matrix.MSCALE_Y];
                    else if (scale * values[Matrix.MSCALE_Y] > maxScale)
                        scale = maxScale / values[Matrix.MSCALE_Y];
                    //长宽均小于控件,则居中显示
                    if (values[Matrix.MSCALE_X] * drawable.getIntrinsicWidth() * scale < getWidth() && values[Matrix.MSCALE_X] * drawable.getIntrinsicHeight() * scale < getHeight()) {
                        matrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
                    } else
                        matrix.postScale(scale, scale, (x1 + x2) / 2, (y1 + y2) / 2);
                    p1.setXY(x1, y1);
                    p2.setXY(x2, y2);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                removeCallbacks(this);
                if (blockClick) {
                    blockClick = false;
                } else if (distance(event.getX(), event.getY(), p0.x, p0.y) < 8) {
                    performClick();//调用onClickListener
                }
                matrix.getValues(values);
                if (drawable == null) {
                    mode = 0;
                    return true;
                }
                float dx = checkXboundary(0, drawable.getIntrinsicWidth());
                float dy = checkYboundary(0, drawable.getIntrinsicHeight());
                matrix.postTranslate(dx, dy);
                invalidate();
                mode = 0;
                break;
            case MotionEvent.ACTION_POINTER_DOWN://第二个手指或更多手指按下
                removeCallbacks(this);
                if (event.getPointerCount() == 2) {//第二根手指
                    p2.setXY(event.getX(1), event.getY(1));
                }
                mode = ZOOM;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = 0;//不用DRAG,避免两根手指先后移开导致的图片移动
                break;
        }

        return true;
    }

    //distance between (x1,y1) and (x2,y2)
    private double distance(float x1, float y1, float x2, float y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    //双击进行图片缩放
    private void onDoubleClick(int width, int height) {
        if (System.currentTimeMillis() - lastClickTime < TIME_DELAY) {
            blockClick = true;
            matrix.getValues(values);
            float dx = (getWidth() - width * values[Matrix.MSCALE_X]) / 2 - values[Matrix.MTRANS_X];
            float dy = (getHeight() - height * values[Matrix.MSCALE_Y]) / 2 - values[Matrix.MTRANS_Y];
            matrix.postTranslate(dx, dy);//移到控件中心
            float scale = 1;
            if (values[Matrix.MSCALE_Y] < maxScale)
                scale = maxScale / values[Matrix.MSCALE_Y];
            else
                scale = minScale / values[Matrix.MSCALE_Y];
            matrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
            invalidate();
        }
    }

    private float checkYboundary(float dy, int height) {
        if (height * values[Matrix.MSCALE_Y] < getHeight()) {//纵向向未填满，则纵向居中
            dy = (getHeight() - height * values[Matrix.MSCALE_Y]) / 2 - values[Matrix.MTRANS_Y];
        } else {
            if (values[Matrix.MTRANS_Y] + dy >= 0)
                dy = -values[Matrix.MTRANS_Y];
            else if (values[Matrix.MTRANS_Y] + height * values[Matrix.MSCALE_Y] + dy < getHeight())
                dy = getHeight() - values[Matrix.MTRANS_Y] - height * values[Matrix.MSCALE_Y];
        }
        return dy;
    }

    private float checkXboundary(float dx, int width) {
        if (width * values[Matrix.MSCALE_X] < getWidth()) {//横向向未填满，则横向居中
            dx = (getWidth() - width * values[Matrix.MSCALE_X]) / 2 - values[Matrix.MTRANS_X];
        } else {//不允许拉出空白区域
            if (values[Matrix.MTRANS_X] + dx >= 0)
                dx = -values[Matrix.MTRANS_X];
            else if (values[Matrix.MTRANS_X] + width * values[Matrix.MSCALE_X] + dx < getWidth())
                dx = getWidth() - values[Matrix.MTRANS_X] - width * values[Matrix.MSCALE_X];
        }
        return dx;
    }

    @Override
    public void run() {//显示上下文菜单
        blockClick = super.showContextMenu();
    }

    public class Point {
        float x;
        float y;

        void setXY(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
