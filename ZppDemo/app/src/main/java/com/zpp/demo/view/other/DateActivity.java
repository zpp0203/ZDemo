package com.zpp.demo.view.other;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zandroid.widget.CustomDatePicker;
import com.zpp.demo.R;
import com.zpp.demo.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class DateActivity extends BaseActivity {
    @Bind(R.id.data_picker)
    Button date;

    @Override
    protected int setLayoutView() {
        return R.layout.activity_date;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

   @OnClick({R.id.data_picker})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.data_picker:
                CustomDatePicker datePicker=new CustomDatePicker(mContext, new CustomDatePicker.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        date.setText(time);
                    }
                },"1990-01-01 00:00","2020-01-01 00:00");
                datePicker.show("2019-01-14");
                break;
        }
   }
}
