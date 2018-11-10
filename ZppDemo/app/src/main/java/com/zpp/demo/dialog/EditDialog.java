package com.zpp.demo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zpp.demo.R;

public abstract class EditDialog extends Dialog implements View.OnClickListener {
    private TextView dialog_title;
    private ImageButton dialog_right;
    private EditText dialog_edit;
    private Button dialog_submit;
    public EditDialog(Context context) {
        super(context);

        setContentView(R.layout.dialog_etid_layout);
        dialog_title=findViewById(R.id.dialog_edit_title);
        dialog_right=findViewById(R.id.dialog_edit_right);
        dialog_right.setOnClickListener(this);
        dialog_edit=findViewById(R.id.dialog_edit_edit);
        dialog_submit=findViewById(R.id.dialog_edit_submit);
        dialog_submit.setOnClickListener(this);
    }
    /**
     * 设置Dialgo的标题，默认不显示
     *
     * @param title
     * @return
     */
    public EditDialog addTitle(String title) {
        if (title != null && title.length() > 0) {
            dialog_title.setText(title);
            dialog_title.setVisibility(View.VISIBLE);
        } else {
            dialog_title.setVisibility(View.GONE);
        }
        return this;
    }

    public boolean dismissDialog() {
        if (isShowing()) {
            try {
                dismiss();
            } catch (Exception e) {
            }
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_edit_submit:
                submitOnClick(dialog_edit.getText().toString());
                dismissDialog();
                break;
            case R.id.dialog_edit_right:
                dismissDialog();
                 break;
        }
    }
    public abstract void submitOnClick(String editText);
}
