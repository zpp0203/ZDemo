package com.zpp.tools;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zpp.R;

import java.lang.ref.WeakReference;

public class EToast {
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;
    private final int ANIMATION_DURATION = 600;
    private WeakReference<Activity> reference;
    private TextView mTextView;
    private ViewGroup container;
    private View v;
    private LinearLayout mContainer;
    private int HIDE_DELAY = 2000;
    private AlphaAnimation mFadeOutAnimation;
    private AlphaAnimation mFadeInAnimation;
    private boolean isShow = false;
    private String TOAST_TAG = "EToast_Log";

    private EToast(Activity activity) {
        reference = new WeakReference<>(activity);
        container = (ViewGroup) activity
                .findViewById(android.R.id.content);
        View viewWithTag = container.findViewWithTag(TOAST_TAG);
        if(viewWithTag == null){
            v = activity.getLayoutInflater().inflate(
                    R.layout.etoast, container);
            v.setTag(TOAST_TAG);
        }else{
            v = viewWithTag;
        }

        mContainer = (LinearLayout) v.findViewById(R.id.mbContainer);
        mContainer.setVisibility(View.GONE);
        mTextView = (TextView) v.findViewById(R.id.mbMessage);
    }

    /**
     * @param context must instanceof Activity
     * */
    public static EToast makeText(Context context, CharSequence message, int HIDE_DELAY) {
        if(context instanceof Activity){
            EToast eToast = new EToast((Activity) context);
            if(HIDE_DELAY == LENGTH_LONG){
                eToast.HIDE_DELAY = 2500;
            }else{
                eToast.HIDE_DELAY = 1500;
            }
            eToast.setText(message);
            return eToast;
        }else{
            throw new RuntimeException("EToast @param context must instanceof Activity");
        }
    }
    public static EToast makeText(Context context, int resId, int HIDE_DELAY) {
        return makeText(context,context.getText(resId),HIDE_DELAY);
    }
    public void show() {
        if(isShow){
            return;
        }
        isShow = true;
        mFadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        mFadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
        mFadeOutAnimation.setDuration(ANIMATION_DURATION);
        mFadeOutAnimation
                .setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        isShow = false;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if(!reference.get().isFinishing())
                            mContainer.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
        mContainer.setVisibility(View.VISIBLE);

        mFadeInAnimation.setDuration(ANIMATION_DURATION);

        mContainer.startAnimation(mFadeInAnimation);
        mContainer.postDelayed(mHideRunnable,HIDE_DELAY);
    }

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            if (reference.get().hasWindowFocus())
                mContainer.startAnimation(mFadeOutAnimation);
            else{
                if(!reference.get().isFinishing())
                    mContainer.setVisibility(View.GONE);
            }
        }
    };
    public void cancel(){
        if(isShow) {
            isShow = false;
            mContainer.setVisibility(View.GONE);
            mContainer.removeCallbacks(mHideRunnable);
        }
    }
    public void setText(CharSequence s){
        if(v == null) throw new RuntimeException("This Toast was not created with com.mic.toast.Toast.makeText()");
        mTextView.setText(s);
    }
    public void setText(int resId) {
        setText(reference.get().getText(resId));
    }

    public TextView getTextView(){
        return mTextView;
    }
}

