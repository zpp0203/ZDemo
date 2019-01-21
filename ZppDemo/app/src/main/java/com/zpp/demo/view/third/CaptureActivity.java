package com.zpp.demo.view.third;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zpp.demo.R;
import com.zpp.zxinglibrary.common.Constant;
import com.zpp.zxinglibrary.view.ViewfinderView;



/**
 * @date: 2017/10/26 15:22
 * @declare ：扫一扫界面
 */

public class CaptureActivity extends com.zpp.zxinglibrary.android.BaseCaptureActivity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = com.zpp.demo.view.third.CaptureActivity.class.getSimpleName();

    private SurfaceView previewView;
    private ViewfinderView viewfinderView;
    private ImageView flashLightIv;
    private TextView flashLightTv;
    private ImageView backIv;
    private LinearLayout flashLightLayout;
    private LinearLayout albumLayout;
    private LinearLayout bottomLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_cap_m;
    }


    private void initView() {
        previewView = findViewById(R.id.preview_view);
        previewView.setOnClickListener(this);

        viewfinderView = findViewById(R.id.viewfinder_view);


        backIv = findViewById(R.id.backIv);
        backIv.setOnClickListener(this);

        flashLightIv = findViewById(R.id.flashLightIv);
        flashLightTv = findViewById(R.id.flashLightTv);

        flashLightLayout = findViewById(R.id.flashLightLayout);
        flashLightLayout.setOnClickListener(this);
        albumLayout = findViewById(R.id.albumLayout);
        albumLayout.setOnClickListener(this);
        bottomLayout = findViewById(R.id.bottomLayout);

        initView(previewView,viewfinderView,flashLightLayout);
    }


    /**
     * @param flashState 切换闪光灯图片
     */
    public void switchFlashImg(int flashState) {

        if (flashState == Constant.FLASH_OPEN) {
            flashLightIv.setImageResource(R.drawable.ic_open);
            flashLightTv.setText("关闭闪光灯");
        } else {
            flashLightIv.setImageResource(R.drawable.ic_close);
            flashLightTv.setText("打开闪光灯");
        }
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.flashLightLayout) {
            /*切换闪光灯*/
            switchFlashLight();
        } else if (id == R.id.albumLayout) {
            /*打开相册*/
            openAlbum();
        }else if(view.getId()==R.id.backIv){
            finish();
        }
    }

}
