package com.zpp.ztoast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/*会导致上一个activity的onResume不断执行？*/
public class ZToast extends Activity {

    private TextView mTextView;
    private LinearLayout mContainer;

    private static int mGravity = Gravity.BOTTOM;
    private static int mXOffset ,mYOffset=200;
    private static int mHIDE_DELAY = 2000;
    private static CharSequence mMessage;

    private AlphaAnimation mFadeOutAnimation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ztoast);

        //设置内容占据全屏
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        getWindow().setGravity(Gravity.CENTER);


        Window window=getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.gravity=Gravity.BOTTOM|Gravity.CENTER;
        window.setAttributes(lp);
        //为了点击旁边的下方view有效
        // Make us non-modal, so that others can receive touch events.
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        // ...but notify us that it happened.
        window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        //dialog和软键盘冲突
        window.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        setFinishOnTouchOutside(true);

        mFadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
        mFadeOutAnimation.setDuration(600);
        mFadeOutAnimation
                .setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mContainer.setVisibility(View.GONE);
                        finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });


        mContainer = (LinearLayout) findViewById(R.id.mbContainer);
        mContainer.setVisibility(View.GONE);
        mTextView = (TextView) findViewById(R.id.mbMessage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(0,0);
        Window window=getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity=mGravity|Gravity.CENTER;
        window.setAttributes(lp);


        FrameLayout.LayoutParams params= (FrameLayout.LayoutParams) mContainer.getLayoutParams();
        params.gravity=mGravity|Gravity.CENTER;
        params.bottomMargin=mYOffset;

        mContainer.setLayoutParams(params);
        mTextView.setText(mMessage);
        mContainer.setVisibility(View.VISIBLE);
//        int h=KeyboardUtils.getSoftInputHeight(mContext);
//        mContainer.setPadding(0,0,0,h);
//        mContainer.startAnimation(mFadeInAnimation);
        mContainer.postDelayed(mHideRunnable,mHIDE_DELAY);


        mGravity=Gravity.BOTTOM;
        mYOffset=200;
    }

    public static void setGravity(int gravity, int xOffset, int yOffset) {
        mGravity = gravity;
        mXOffset = xOffset;
        mYOffset = yOffset;
    }

    /**
     * @param context must instanceof Activity
     * */
    public static void makeText(Context context, CharSequence message, int HIDE_DELAY) {
        mMessage=message;
        mHIDE_DELAY=HIDE_DELAY;
        Intent intent = new Intent(context, ZToast.class);
//        intent.putExtra("message",message);
//        intent.putExtra("HIDE_DELAY",HIDE_DELAY);
//        intent.putExtra("gravity",mGravity);
//        intent.putExtra("xOffset",mXOffset);
//        intent.putExtra("yOffset",mYOffset);
        context.startActivity(intent);
    }


    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            cancel();
        }
    };


    public void cancel(){
        mContainer.removeCallbacks(mHideRunnable);
        mContainer.startAnimation(mFadeOutAnimation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
