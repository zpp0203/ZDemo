package com.zpp.demo.view.other;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.zpp.demo.R;
import com.zpp.demo.base.BaseActivity;
import com.zpp.demo.bezier_anima.AnimatorPath;
import com.zpp.demo.bezier_anima.PathEvaluator;
import com.zpp.demo.bezier_anima.PathPoint;

/**
 * 使用动画延轨迹移动
 * */
public class ValueAnimatorActivity extends BaseActivity implements View.OnClickListener {
    private Button fab;
    private AnimatorPath path;//声明动画集合

    @Override
    protected int setLayoutView() {
        return R.layout.activity_value_animator;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fab = (Button) findViewById(R.id.fab);

        setPath();

        fab.setOnClickListener(this);

    }
    /*设置动画路径*/
    public void setPath(){
        path = new AnimatorPath();
        path.moveTo(0,0);
        path.lineTo(400,400);
        path.secondBesselCurveTo(600, 200, 800, 400); //订单
        path.thirdBesselCurveTo(100,600,900,1000,200,1200);
    }

    /**
     * 设置动画
     * @param view
     * @param propertyName
     * @param path
     */
    private void startAnimatorPath(View view, String propertyName, AnimatorPath path) {
        ObjectAnimator anim = ObjectAnimator.ofObject(this, propertyName, new PathEvaluator(), path.getPoints().toArray());
        anim.setInterpolator(new DecelerateInterpolator());//动画插值器
        anim.setDuration(3000);
        anim.start();
    }

    /**
     * 设置View的属性通过ObjectAnimator.ofObject()的反射机制来调用
     * @param newLoc
     */
    public void setFab(PathPoint newLoc) {
        fab.setTranslationX(newLoc.mX);
        fab.setTranslationY(newLoc.mY);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                startAnimatorPath(fab, "fab", path);
                break;
        }
    }

    /*旋转动画*/
    ObjectAnimator animator;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void rotationImg(ImageView imageView, boolean rotation) {
        if (rotation) {
            if (animator == null) {
                animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360.0f);
                animator.setDuration(2000);
                animator.setInterpolator(new LinearInterpolator());//不停顿
                animator.setRepeatCount(-1);//设置动画重复次数
                animator.setRepeatMode(ValueAnimator.RESTART);//动画重复模式
                animator.start();//开始动画
            } else {
                animator.resume();//恢复动画
            }
        } else {
            if (animator != null)
                animator.pause();//暂停动画
        }
    }

}
