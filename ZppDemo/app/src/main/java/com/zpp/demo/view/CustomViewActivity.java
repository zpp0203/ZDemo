package com.zpp.demo.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zpp.demo.R;
import com.zpp.demo.base.BaseActivity;
import com.zpp.demo.widget.CircleMessageView;
import com.zpp.demo.widget.CustomInputView;
import com.zpp.demo.widget.SuperCircleView;
import com.zpp.tools.LogUtils;
import com.zpp.widget.CustomStartView;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;


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
        circleMessageView.setBottomTextText("测试2 测试2 测试2fff 哼哼哈哈");

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
