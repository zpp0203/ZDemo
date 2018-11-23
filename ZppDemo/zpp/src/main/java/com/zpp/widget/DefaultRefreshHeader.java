package com.zpp.widget;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zpp.R;

import java.util.Date;

import static com.zpp.widget.XRecyclerView.STATE_DONE;
import static com.zpp.widget.XRecyclerView.STATE_NORMAL;
import static com.zpp.widget.XRecyclerView.STATE_REFRESHING;
import static com.zpp.widget.XRecyclerView.STATE_RELEASE_TO_REFRESH;

public class DefaultRefreshHeader extends LinearLayout{



    private static final String XR_REFRESH_KEY = "XR_REFRESH_KEY";
    private static final String XR_REFRESH_TIME_KEY = "XR_REFRESH_TIME_KEY";
    private LinearLayout mContainer;
    private ImageView mArrowImageView;

    private TextView mStatusTextView;
    private int mState = STATE_NORMAL;

    private TextView mHeaderTimeView;
    //private TextView mHeaderRefreshTimeContainer;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    private static final int ROTATE_ANIM_DURATION = 180;

    public int mMeasuredHeight;

    public void destroy(){

        if(mRotateUpAnim != null){
            mRotateUpAnim.cancel();
            mRotateUpAnim = null;
        }
        if(mRotateDownAnim != null){
            mRotateDownAnim.cancel();
            mRotateDownAnim = null;
        }
    }

    public DefaultRefreshHeader(Context context) {
        super(context);
        initView();
    }

    /**
     * @param context
     * @param attrs
     */
    public DefaultRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

//    public void setRefreshTimeVisible(boolean show){
//        if(mHeaderRefreshTimeContainer != null)
//            mHeaderRefreshTimeContainer.setVisibility(show?VISIBLE:GONE);
//    }

    private void initView() {
        // 初始情况，设置下拉刷新view高度为0
        mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(
                R.layout.xlistview_header, null);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);

        addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.BOTTOM);

        mArrowImageView = (ImageView)mContainer.findViewById(R.id.xlistview_header_arrow);
        mStatusTextView = (TextView)mContainer.findViewById(R.id.xlistview_header_hint_textview);
        mHeaderTimeView = (TextView)mContainer.findViewById(R.id.xlistview_header_time);

        //init the progress view


        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);


        measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();
    }

    public void setProgressStyle(int style) {

    }

    public void setArrowImageView(int resid){
        mArrowImageView.setImageResource(resid);
    }

    public void setState(int state) {
        if (state == mState) return ;

        if (state == STATE_REFRESHING) {	// 显示进度
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.INVISIBLE);

            smoothScrollTo(mMeasuredHeight);
        } else if(state == STATE_DONE) {
            mArrowImageView.setVisibility(View.INVISIBLE);

        } else {	// 显示箭头图片
            mArrowImageView.setVisibility(View.VISIBLE);

        }
        mHeaderTimeView.setText(friendlyTime(getLastRefreshTime()));
        switch(state){
            case STATE_NORMAL:
                if (mState == STATE_RELEASE_TO_REFRESH) {
                    mArrowImageView.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_REFRESHING) {
                    mArrowImageView.clearAnimation();
                }
                mStatusTextView.setText("正在刷新");
                break;
            case STATE_RELEASE_TO_REFRESH:
                if (mState != STATE_RELEASE_TO_REFRESH) {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                    mStatusTextView.setText("下拉刷新");
                }
                break;
            case STATE_REFRESHING:
                mStatusTextView.setText("正在刷新");
                break;
            case STATE_DONE:
                mStatusTextView.setText("下拉刷新");
                break;
            default:
        }

        mState = state;
    }

    public int getState() {
        return mState;
    }

    private long getLastRefreshTime(){
        SharedPreferences s =
                getContext()
                        .getSharedPreferences(XR_REFRESH_KEY,Context.MODE_PRIVATE);
        return s.getLong(XR_REFRESH_TIME_KEY,new Date().getTime());
    }

    private void saveLastRefreshTime(long refreshTime){
        SharedPreferences s =
                getContext()
                        .getSharedPreferences(XR_REFRESH_KEY,Context.MODE_PRIVATE);
        s.edit().putLong(XR_REFRESH_TIME_KEY,refreshTime).commit();
    }

    public void refreshComplete(){
        mHeaderTimeView.setText(friendlyTime(getLastRefreshTime()));
        saveLastRefreshTime(System.currentTimeMillis());
        setState(STATE_DONE);
        new Handler().postDelayed(new Runnable(){
            public void run() {
                reset();
            }
        }, 200);
    }

    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mContainer .getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.height;
    }

    public void onMove(float delta) {
        if(getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) delta + getVisibleHeight());
            if (mState <= STATE_RELEASE_TO_REFRESH) { // 未处于刷新状态，更新箭头
                if (getVisibleHeight() > mMeasuredHeight) {
                    setState(STATE_RELEASE_TO_REFRESH);
                }else {
                    setState(STATE_NORMAL);
                }
            }
        }
    }

    public boolean releaseAction() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (height == 0) // not visible.
            isOnRefresh = false;

        if(getVisibleHeight() > mMeasuredHeight &&  mState < STATE_REFRESHING){
            setState(STATE_REFRESHING);
            isOnRefresh = true;
        }
        // refreshing and header isn't shown fully. do nothing.
        if (mState == STATE_REFRESHING && height <=  mMeasuredHeight) {
            //return;
        }
        if (mState != STATE_REFRESHING) {
            smoothScrollTo(0);
        }

        if (mState == STATE_REFRESHING) {
            int destHeight = mMeasuredHeight;
            smoothScrollTo(destHeight);
        }

        return isOnRefresh;
    }

    public void reset() {
        smoothScrollTo(0);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                setState(STATE_NORMAL);
            }
        }, 500);
    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }


    public static String friendlyTime(Date time) {
        return friendlyTime(time.getTime());
    }

    public static String friendlyTime(long time) {
        //获取time距离当前的秒数
        int ct = (int)((System.currentTimeMillis() - time)/1000);

        if(ct == 0) {
            return "刚刚";
        }

        if(ct > 0 && ct < 60) {
            return ct + "秒前";
        }

        if(ct >= 60 && ct < 3600) {
            return Math.max(ct / 60,1) + "分钟前";
        }
        if(ct >= 3600 && ct < 86400)
            return ct / 3600 + "小时前";
        if(ct >= 86400 && ct < 2592000){ //86400 * 30
            int day = ct / 86400 ;
            return day + "天前";
        }
        if(ct >= 2592000 && ct < 31104000) { //86400 * 30
            return ct / 2592000 + "月前";
        }
        return ct / 31104000 + "年前";
    }

}
