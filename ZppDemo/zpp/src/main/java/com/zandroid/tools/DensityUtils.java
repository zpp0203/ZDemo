package com.zandroid.tools;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 墨 on 2018/3/30.
 */

public class DensityUtils {

    //头条屏幕适配方案
    private static float sNoncompatDensity,sNoncompatScaledDensity;
    private static void setCustomDensity(@NonNull Activity activity, @NonNull final Application
            application) {
        final DisplayMetrics appDisplayMetrics =
                application.getResources().getDisplayMetrics();
        if (sNoncompatDensity == 0) {
            sNoncompatDensity = appDisplayMetrics.density;
            sNoncompatScaledDensity = appDisplayMetrics.scaledDensity;
            // 监听字体切换
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNoncompatScaledDensity =
                                application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }
                @Override
                public void onLowMemory() {
                }
            });
        }
        // 适配后的 dpi 将统一为 360dpi
        final float targetDensity = appDisplayMetrics.widthPixels / 360;
        final float targetScaledDensity = targetDensity * (sNoncompatScaledDensity /
                sNoncompatDensity);
        final int targetDensityDpi = (int)(160 * targetDensity);
        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;
        final DisplayMetrics activityDisplayMetrics =
                activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 各种单位转换
     * Converts an unpacked complex data value holding a dimension to its final floating
     * point value. The two parameters <var>unit</var> and <var>value</var>
     * are as in {@link TypedValue#TYPE_DIMENSION}.
     *
     * @param value The value to apply the unit to.
     * @param unit  The unit to convert from.
     * @return The complex floating point value multiplied by the appropriate
     * <p>
     * metrics depending on its unit.
     */
    public static float applyDimension(Context context, final float value, final int unit) {

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        switch (unit) {

            case TypedValue.COMPLEX_UNIT_PX:

                return value;

            case TypedValue.COMPLEX_UNIT_DIP:

                return value * metrics.density;

            case TypedValue.COMPLEX_UNIT_SP:

                return value * metrics.scaledDensity;

            case TypedValue.COMPLEX_UNIT_PT:

                return value * metrics.xdpi * (1.0f / 72);

            case TypedValue.COMPLEX_UNIT_IN:

                return value * metrics.xdpi;

            case TypedValue.COMPLEX_UNIT_MM:

                return value * metrics.xdpi * (1.0f / 25.4f);

        }

        return 0;

    }


    /**
     * 在 onCreate 中获取视图的尺寸
     *
     * @param view     The view.
     * @param listener The get size listener.
     */

    public static void forceGetViewSize(final View view, final onGetSizeListener listener) {

        view.post(new Runnable() {

            @Override

            public void run() {

                if (listener != null) {

                    listener.onGetSize(view);

                }

            }

        });

    }


    /**
     * 获取测量视图宽度
     *
     * @param view The view.
     * @return the width of view
     */

    public static int getMeasuredWidth(final View view) {

        return measureView(view)[0];

    }


    /**
     * 获取测量视图高度
     *
     * @param view The view.
     * @return the height of view
     */

    public static int getMeasuredHeight(final View view) {

        return measureView(view)[1];

    }


    /**
     * 测量视图尺寸
     *
     * @param view The view.
     * @return arr[0]: view's width, arr[1]: view's height
     */

    public static int[] measureView(final View view) {

        ViewGroup.LayoutParams lp = view.getLayoutParams();

        if (lp == null) {
            lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

        }

        int widthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width);

        int lpHeight = lp.height;

        int heightSpec;

        if (lpHeight > 0) {

            heightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);

        } else {

            heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        }

        view.measure(widthSpec, heightSpec);

        return new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};

    }
    /**获取xml中的值*/
    public static int getXmlDef(Context context, int id){
        TypedValue mTmpValue = new TypedValue();
        synchronized (mTmpValue) {
            TypedValue value = mTmpValue;
            context.getResources().getValue(id, value, true);
            return (int)TypedValue.complexToFloat(value.data);
        }
    }
    public interface onGetSizeListener {

        void onGetSize(View view);

    }
}
