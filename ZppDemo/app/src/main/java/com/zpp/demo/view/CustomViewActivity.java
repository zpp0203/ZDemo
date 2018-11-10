package com.zpp.demo.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zpp.demo.R;
import com.zpp.demo.base.BaseActivity;
import com.zpp.demo.widget.CircleMessageView;
import com.zpp.demo.widget.SuperCircleView;
import com.zpp.widget.CustomStartView;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;


public class CustomViewActivity extends BaseActivity {

    @Bind(R.id.ed_input1)
    EditText edInput1;
    @Bind(R.id.ed_input2)
    EditText edInput2;
    @Bind(R.id.ed_input3)
    EditText edInput3;
    @Bind(R.id.ed_input4)
    EditText edInput4;

    int countItem=3;

    @Bind(R.id.super_ring)
    SuperCircleView superCircleView;
    @Bind(R.id.circle_message_view)
    CircleMessageView circleMessageView;

    protected CustomStartView custom_start_view;
    @Override
    protected int setLayoutView() {
        return R.layout.activity_custom_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        custom_start_view=findViewById(R.id.custom_start_view);

        initView();


        circleMessageView.setTopText("测试1 测试1 测试1，大大是");
        circleMessageView.setBottomTextText("测试2 测试2 测试2fff 哼哼哈哈");
    }

    private void initView() {
        TextChangeListen[] mTextWatcher = new TextChangeListen[4];
        EditText[] editTexts_List = new EditText[4];
        editTexts_List[0] = edInput1;
        editTexts_List[1] = edInput2;
        editTexts_List[2] = edInput3;
        editTexts_List[3] = edInput4;

         //循环添加监听事件
        for (int i = 0; i < 4; i++) {
            mTextWatcher[i] = new TextChangeListen(editTexts_List[i]);
            editTexts_List[i].addTextChangedListener(mTextWatcher[i]);
        }

    }
    public class TextChangeListen implements TextWatcher {

        public EditText Edit;

        public TextChangeListen(EditText Edit) {
            super();
            this.Edit = Edit;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            Log.e("ed",s.length()+"-"+s.toString());
            if (s.length() == countItem) {
                if (this.Edit == edInput1) {
                    edInput2.requestFocus();
                }
                if (this.Edit == edInput2) {
                    edInput3.requestFocus();
                }
                if (this.Edit == edInput3) {
                    edInput4.requestFocus();
                }
            } else if (s.length() == 0) {
                if (this.Edit == edInput2) {
                    edInput1.requestFocus();
                }
                if (this.Edit == edInput3) {
                    edInput2.requestFocus();
                }
                if (this.Edit == edInput4) {
                    edInput3.requestFocus();
                }
            }
        }
    }
    @OnClick({R.id.super_ring})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.super_ring:
                superCircleView.setValue(80);
                break;
        }
    }

}
