package com.zpp.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * setGravity : 设置吐司位置
 * setBgColor : 设置背景颜色
 * setBgResource : 设置背景资源
 * setMessageColor: 设置消息颜色
 * showShort : 显示短时吐司
 * showLong : 显示长时吐司
 * showCustomShort: 显示短时自定义吐司
 * showCustomLong : 显示长时自定义吐司
 * cancel : 取消吐司显示
 */

public final class ToastUtils {


    private static final int COLOR_DEFAULT = 0xFEFFFFFF;

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());


    private static Toast sToast;

    private static int sGravity = -1;

    private static int sXOffset = -1;

    private static int sYOffset = -1;

    private static int sBgColor = COLOR_DEFAULT;

    private static int sBgResource = -1;

    private static int sMsgColor = COLOR_DEFAULT;

    private static int sMsgTextSize = -1;


    private ToastUtils() {

        throw new UnsupportedOperationException("u can't instantiate me...");

    }


    /**
     * Set the gravity.
     *
     * @param gravity The gravity.
     * @param xOffset X-axis offset, in pixel.
     * @param yOffset Y-axis offset, in pixel.
     */

    public static void setGravity(final int gravity, final int xOffset, final int yOffset) {

        sGravity = gravity;

        sXOffset = xOffset;

        sYOffset = yOffset;

    }


    /**
     * Set the color of background.
     *
     * @param backgroundColor The color of background.
     */

    public static void setBgColor(@ColorInt final int backgroundColor) {

        sBgColor = backgroundColor;

    }


    /**
     * Set the resource of background.
     *
     * @param bgResource The resource of background.
     */

    public static void setBgResource(@DrawableRes final int bgResource) {

        sBgResource = bgResource;

    }


    /**
     * Set the color of message.
     *
     * @param msgColor The color of message.
     */

    public static void setMsgColor(@ColorInt final int msgColor) {

        sMsgColor = msgColor;

    }


    /**
     * Set the text size of message.
     *
     * @param textSize The text size of message.
     */

    public static void setMsgTextSize(final int textSize) {

        sMsgTextSize = textSize;

    }


    /**
     * Show the sToast for a short period of time.
     *
     * @param text The text.
     */

    public static void showShort(Context context, @NonNull final CharSequence text) {

        show(context, text, Toast.LENGTH_SHORT);

    }


    /**
     * Show the sToast for a short period of time.
     *
     * @param resId The resource id for text.
     */

    public static void showShort(Context context, @StringRes final int resId) {

        show(context, resId, Toast.LENGTH_SHORT);

    }


    /**
     * Show the sToast for a short period of time.
     *
     * @param resId The resource id for text.
     * @param args  The args.
     */

    public static void showShort(Context context, @StringRes final int resId, final Object... args) {

        if (args != null && args.length == 0) {

            show(context, resId, Toast.LENGTH_SHORT);

        } else {

            show(context, resId, Toast.LENGTH_SHORT, args);

        }

    }


    /**
     * Show the sToast for a short period of time.
     *
     * @param format The format.
     * @param args   The args.
     */

    public static void showShort(final String format, final Object... args) {

        if (args != null && args.length == 0) {
            show(format, Toast.LENGTH_SHORT);
        } else {
            show(format, Toast.LENGTH_SHORT, args);
        }

    }


    /**
     * Show the sToast for a long period of time.
     *
     * @param text The text.
     */

    public static void showLong(Context context, @NonNull final CharSequence text) {

        show(context, text, Toast.LENGTH_LONG);

    }


    /**
     * Show the sToast for a long period of time.
     *
     * @param resId The resource id for text.
     */

    public static void showLong(Context context, @StringRes final int resId) {

        show(context, resId, Toast.LENGTH_LONG);

    }


    /**
     * Show the sToast for a long period of time.
     *
     * @param resId The resource id for text.
     * @param args  The args.
     */

    public static void showLong(Context context, @StringRes final int resId, final Object... args) {
        if (args != null && args.length == 0) {
            show(context, resId, Toast.LENGTH_SHORT);
        } else {
            show(context, resId, Toast.LENGTH_LONG, args);
        }

    }


    /**
     * Show the sToast for a long period of time.
     *
     * @param format The format.
     * @param args   The args.
     */

    public static void showLong(final String format, final Object... args) {

        if (args != null && args.length == 0) {
            show(format, Toast.LENGTH_SHORT);
        } else {
            show(format, Toast.LENGTH_LONG, args);
        }

    }


    /**
     * Cancel the sToast.
     */
    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
        }
    }

    private static void show(Context context, @StringRes final int resId, final int duration) {
        show(context.getResources().getText(resId).toString(), duration);
    }

    private static void show(Context context, @StringRes final int resId, final int duration, final Object... args) {
        show(String.format(context.getResources().getString(resId), args), duration);
    }

    private static void show(final String format, final int duration, final Object... args) {
        show(String.format(format, args), duration);
    }

    private static void show(final Context context, final CharSequence text, final int duration) {
        HANDLER.post(new Runnable() {
            @SuppressLint("ShowToast")
            @Override
            public void run() {
                cancel();
                sToast = Toast.makeText(context, text, duration);
                final TextView tvMessage = (TextView) sToast.getTextView();
                if (sMsgColor != COLOR_DEFAULT) {
                    tvMessage.setTextColor(sMsgColor);
                }
                if (sMsgTextSize != -1) {

                    tvMessage.setTextSize(sMsgTextSize);

                }

//                if (sGravity != -1 || sXOffset != -1 || sYOffset != -1) {
//
//                    sToast.setGravity(sGravity, sXOffset, sYOffset);
//
//                }

                setBg(tvMessage);

                showToast(context);

            }

        });

    }


//    private static void show(final Context context, final View view, final int duration) {
//
//        HANDLER.post(new Runnable() {
//
//            @Override
//
//            public void run() {
//
//                cancel();
//
//                sToast = new Toast(context);
//
//                sToast.setView(view);
//
//                sToast.setDuration(duration);
//
//                if (sGravity != -1 || sXOffset != -1 || sYOffset != -1) {
//
//                    sToast.setGravity(sGravity, sXOffset, sYOffset);
//
//                }
//
//                setBg();
//
//                showToast(context);
//
//            }
//
//        });
//
//    }


    private static void showToast(Context context) {

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {

            try {

                Field field = View.class.getDeclaredField("mContext");

                field.setAccessible(true);

                field.set(sToast.getTextView(), new ApplicationContextWrapperForApi25(context));

            } catch (Throwable throwable) {

                throwable.printStackTrace();

            }

        }

        sToast.show();

    }


    private static void setBg() {

        if (sBgResource != -1) {

            final View toastView = sToast.getTextView();

            toastView.setBackgroundResource(sBgResource);

        } else if (sBgColor != COLOR_DEFAULT) {

            final View toastView = sToast.getTextView();

            Drawable background = toastView.getBackground();

            if (background != null) {

                background.setColorFilter(

                        new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN)

                );

            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                    toastView.setBackground(new ColorDrawable(sBgColor));

                } else {

                    toastView.setBackgroundDrawable(new ColorDrawable(sBgColor));

                }

            }

        }

    }


    private static void setBg(final TextView tvMsg) {

        if (sBgResource != -1) {

            final View toastView = sToast.getTextView();

            toastView.setBackgroundResource(sBgResource);

            tvMsg.setBackgroundColor(Color.TRANSPARENT);

        } else if (sBgColor != COLOR_DEFAULT) {

            final View toastView = sToast.getTextView();

            Drawable tvBg = toastView.getBackground();

            Drawable msgBg = tvMsg.getBackground();

            if (tvBg != null && msgBg != null) {

                tvBg.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));

                tvMsg.setBackgroundColor(Color.TRANSPARENT);

            } else if (tvBg != null) {

                tvBg.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));

            } else if (msgBg != null) {

                msgBg.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));

            } else {

                toastView.setBackgroundColor(sBgColor);

            }

        }

    }


    private static View getView(Context context, @LayoutRes final int layoutId) {

        LayoutInflater inflate =

                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return inflate != null ? inflate.inflate(layoutId, null) : null;

    }


    private static final class ApplicationContextWrapperForApi25 extends ContextWrapper {


        ApplicationContextWrapperForApi25(Context context) {

            super(context);

        }


        @Override

        public Context getApplicationContext() {

            return this;

        }


        @Override

        public Object getSystemService(@NonNull String name) {

            if (Context.WINDOW_SERVICE.equals(name)) {

                // noinspection ConstantConditions

                return new WindowManagerWrapper(

                        (WindowManager) getBaseContext().getSystemService(name)

                );

            }

            return super.getSystemService(name);

        }


        private static final class WindowManagerWrapper implements WindowManager {


            private final WindowManager base;


            private WindowManagerWrapper(@NonNull WindowManager base) {

                this.base = base;

            }


            @Override

            public Display getDefaultDisplay() {

                return base.getDefaultDisplay();

            }


            @Override

            public void removeViewImmediate(View view) {

                base.removeViewImmediate(view);

            }


            @Override

            public void addView(View view, ViewGroup.LayoutParams params) {

                try {

                    base.addView(view, params);

                } catch (BadTokenException e) {

                    Log.e("WindowManagerWrapper", e.getMessage());

                } catch (Throwable throwable) {

                    Log.e("WindowManagerWrapper", "[addView]", throwable);

                }

            }


            @Override

            public void updateViewLayout(View view, ViewGroup.LayoutParams params) {

                base.updateViewLayout(view, params);

            }


            @Override

            public void removeView(View view) {

                base.removeView(view);

            }

        }

    }

}