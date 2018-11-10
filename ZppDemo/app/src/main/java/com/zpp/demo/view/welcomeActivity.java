package com.zpp.demo.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zpp.demo.R;
import com.zpp.demo.adapter.ViewPagerAdapter;
import com.zpp.demo.base.BaseActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/22/022.
 */

public class welcomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @Bind(R.id.wl_viewpager)
    ViewPager viewPager;
    @Bind(R.id.dot_Layout)
    LinearLayout layout;


    int[] imgs = {R.drawable.welcome1, R.drawable.welcome2, R.drawable.welcome3};
    private ViewPagerAdapter viewPagerAdapter;
    private ArrayList<ImageView> imageViews;
    private ImageView[] dotViews;//小圆点
    private LinearLayout.LayoutParams mParams;

    int position = 1;
    private Timer timer;
    private TimerTask task;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Log.e("--","handleMessage");
            // 要做的事情
            if(position<imgs.length) {
                    viewPager.setCurrentItem(position);
            }else {
                stopTimer();
                startActivity(new Intent(welcomeActivity.this,MainActivity.class));
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initImags();
        initDots();
        viewPager.addOnPageChangeListener(this);
        viewPagerAdapter = new ViewPagerAdapter(imageViews);
        viewPager.setAdapter(viewPagerAdapter);

        reStartTimer();
    }
    @Override
    protected int setLayoutView() {
        return R.layout.activity_welcome_page;
    }
    private void reStartTimer(){
        stopTimer();
        if(timer==null){
            timer = new Timer();
        }
        if(task==null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            };
        }
        timer.schedule(task, 1000, 1000);
    }
    private void stopTimer(){
        if(timer!=null){
            timer.cancel();
            timer=null;
        }
        if(task!=null){
            task.cancel();
            task=null;
        }
    }
    private void initImags() {
        //设置每一张图片都填充窗口
        ViewPager.LayoutParams mParams = new ViewPager.LayoutParams();
        imageViews = new ArrayList<ImageView>();
        for (int i = 0; i < imgs.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            //设置布局
            iv.setImageResource(imgs[i]);
            //为ImageView添加图片资源
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            //这里也是一个图片的适配
            imageViews.add(iv);
        }

    }

    private void initDots() {
        mParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 25);
        mParams.setMargins(10, 0, 10, 0);//设置小圆点左右之间的间隔
        dotViews = new ImageView[imgs.length];
        //判断小圆点的数量，从0开始，0表示第一个
        for (int i = 0; i < imageViews.size(); i++) {
            ImageView imageView = new ImageView(this);
            //为ImageView添加图片资源
            imageView.setLayoutParams(mParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setImageResource(R.drawable.selector_dot);
            if (i == 0) {
                imageView.setSelected(true);
                //默认启动时，选中第一个小圆点
            } else {
                imageView.setSelected(false);
            }
            dotViews[i] = imageView;
            //得到每个小圆点的引用，用于滑动页面时，（onPageSelected方法中）更改它们的状态。
            layout.addView(imageView);
            // 添加到布局里面显示
        }
    }

    @OnClick({R.id.tv_jump})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_jump:
                stopTimer();
                startActivity(new Intent(welcomeActivity.this, MainActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空了消息队列
        stopTimer();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onPageScrolled(int arg0, float v, int i1) {

    }

    @Override
    public void onPageSelected(int arg0) {
        Log.e("--","onPageSelected");
        reStartTimer();

        position=arg0+1;
        for (int i = 0; i < dotViews.length; i++) {
            if (arg0 == i) {
                dotViews[i].setSelected(true);
                dotViews[i].setLayoutParams(mParams);
                dotViews[i].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                dotViews[i].setSelected(false);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }
}

