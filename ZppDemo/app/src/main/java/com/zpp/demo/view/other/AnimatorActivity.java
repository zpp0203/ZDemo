package com.zpp.demo.view.other;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zpp.demo.R;
import com.zpp.demo.base.BaseActivity;
import com.zpp.demo.bezier_anima.AnimatorPath;
import com.zpp.demo.bezier_anima.PathEvaluator;
import com.zpp.demo.bezier_anima.PathPoint;
import com.zpp.demo.bezier_anima.PathView;

import butterknife.Bind;

/**
 * 动画
 *
 * android:interpolator：动画的插入器，用于描述动画的运行情况，常用的有accelerate_decelerate_interpolator（先加速后减速），accelerate_interpolator（加速器）等
 * android:fromXScale：X轴开始动画缩放的比例
 * android:toXScale：X轴结束时动画缩放的比例
 * android:fromYScale：Y轴开始动画缩放的比例
 * android:toYScale:Y轴结束动画缩放的比例，以上四个属性定义了将图片从0.3的比例大小放大到1.0的比例大小（即原大小）
 * android:pivotX：固定点X轴坐标
 * android:pivotY：固定点Y轴坐标
 * android:duration：动画持续时间，5000表示5000毫秒，即5秒
 * android:repeatCount：动画重复次数，-1表示无限循环重复
 * Android:repeatMode：重复模式，reverse表示反向进行，在这里先放大，后缩小
 * */
public class AnimatorActivity extends BaseActivity{

    private AnimatorPath path;//声明动画集合

    @Bind(R.id.pathView)
    PathView pathView;

    @Bind(R.id.tv_center)
    TextView tvCenter;

    @Override
    protected int setLayoutView() {
        return R.layout.activity_value_animator;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startAlphaAnimator(tvCenter);
        rotationImg(tvCenter,true);
        scaleAnimation();
        AnimationSet set=new AnimationSet(this,null);
        set.addAnimation(alphaAnimation);
        set.addAnimation(rotateAnimation);
        set.addAnimation(scaleAnimation);
        set.setRepeatMode(Animation.REVERSE);
        set.setDuration(2000);
        set.setRepeatCount(Animation.INFINITE);
        //tvCenter.setAnimation(set);
        //set.start();

        //属性动画,也有其对应的对象
        //ObjectAnimator animatorA = ObjectAnimator.ofFloat(tvCenter, "TranslationX", -300, 300, 0);
        ObjectAnimator animatorA = ObjectAnimator.ofFloat(tvCenter, "scaleX", 0.1f, 1.5f, 1f);
        animatorA.setRepeatCount(ValueAnimator.INFINITE);
        animatorA.setRepeatMode(ValueAnimator.REVERSE);
        ObjectAnimator animatorB = ObjectAnimator.ofFloat(tvCenter, "scaleY", 0.1f, 1.5f, 1f);
        animatorB.setRepeatCount(ValueAnimator.INFINITE);
        animatorB.setRepeatMode(ValueAnimator.REVERSE);
        ObjectAnimator animatorC = ObjectAnimator.ofFloat(tvCenter, "rotation", 0, 360,0);
        animatorC.setRepeatCount(ValueAnimator.INFINITE);
        animatorC.setRepeatMode(ValueAnimator.REVERSE);
        ObjectAnimator animatorD= ObjectAnimator.ofFloat(tvCenter,"alpha",1f,0f);
        animatorD.setRepeatCount(ValueAnimator.INFINITE);
        animatorD.setRepeatMode(ValueAnimator.REVERSE);

        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playSequentially(animatorB, animatorC,animatorD);//顺序执行
        animatorSet.playTogether(animatorA,animatorB,animatorC,animatorD);//同时执行

        animatorSet.setStartDelay(1000);//延时执行
        animatorSet.setDuration(3*1000);

        animatorSet.start();


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }



    /**
     * 闪烁动画(透明)
     * @param view
     */
    AlphaAnimation alphaAnimation;
    private void startAlphaAnimator(View view) {
        //闪烁  
        alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
//        view.setAnimation(alphaAnimation);
//        alphaAnimation.start();
    }


    /*旋转动画*/
    RotateAnimation rotateAnimation;
    private void rotationImg(View view, boolean rotation) {
        if (rotation) {
            if (rotateAnimation == null) {
                //中心点
                rotateAnimation = new RotateAnimation(0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(1000);
               // rotateAnimation.setInterpolator(new LinearInterpolator());//不停顿
                rotateAnimation.setRepeatCount(-1);//设置动画重复次数
                rotateAnimation.setRepeatMode(ValueAnimator.RESTART);//动画重复模式
//                view.setAnimation(rotateAnimation);
//                rotateAnimation.start();//开始动画
            } else {
                rotateAnimation.reset();//恢复动画
            }
        } else {
            if (rotateAnimation != null)
                rotateAnimation.cancel();//暂停动画
        }
    }

    //缩放动画
    ScaleAnimation scaleAnimation;
    private void scaleAnimation(){
        scaleAnimation=new ScaleAnimation(1, 0.1f, 1, 0.1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        //scaleAnimation.setDuration(2000);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
    }
}
