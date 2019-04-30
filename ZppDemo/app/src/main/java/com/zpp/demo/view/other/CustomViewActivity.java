package com.zpp.demo.view.other;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zpp.demo.R;
import com.zpp.demo.base.BaseActivity;
import com.zandroid.tools.LogUtils;
import com.zandroid.widget.CircleMessageView;
import com.zandroid.widget.CustomInputView;
import com.zandroid.widget.CustomStartView;
import com.zandroid.widget.SuperCircleView;

import butterknife.Bind;
import butterknife.OnClick;


public class CustomViewActivity extends BaseActivity {

    @Bind(R.id.super_ring)
    SuperCircleView superCircleView;
    @Bind(R.id.circle_message_view)
    CircleMessageView circleMessageView;
    @Bind(R.id.custom_input_view)
    CustomInputView customInputView;

    protected CustomStartView custom_start_view;
    @Override
    protected int setLayoutView() {
        return R.layout.activity_custom_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        custom_start_view=findViewById(R.id.custom_start_view);

        circleMessageView.setTopText("测试1 测试1 测试1，大大是");
        circleMessageView.setBottomText("测试2 测试2 测试2fff 哼哼哈哈");

//        circleMessageView.setBgColor(getResources().getColor(R.color.red),getResources().getColor(R.color.green));
        customInputView.setCompleteListener(new CustomInputView.InputCompleteListener() {
            @Override
            public void onComplete(String values) {
                LogUtils.e("customInputView:"+values);
            }
        });
    }

    @OnClick({R.id.super_ring,R.id.custom_input_view})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.super_ring:
                superCircleView.setValue(80);
                break;
            case R.id.custom_input_view:
                Log.e("---","custom_input_view 点击");
                break;
        }
    }

}
