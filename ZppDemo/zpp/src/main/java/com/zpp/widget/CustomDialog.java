package com.zpp.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.zpp.tools.ActivityUtils;
import com.zpp.tools.DensityUtils;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by 我 on 2018/4/20.
 */

public class CustomDialog extends AlertDialog {

    private Context context;
    private int height, width;//宽高
    private boolean cancelTouchout;//是否可点击旁边消失
    private int gravity;//位置
    private View view;
    private int startaAnim;
    private float dimAmount;

    private CustomDialog(Builder builder) {
        super(builder.context);
        context = builder.context;
        height = builder.height;
        width = builder.width;
        cancelTouchout = builder.cancelTouchout;
        gravity=builder.gravity;
        view = builder.view;
        startaAnim=builder.startaAnim;
        dimAmount=builder.dimAmount;
    }


    private CustomDialog(Builder builder, int resStyle) {
        super(builder.context, resStyle);
        context = builder.context;
        height = builder.height;
        width = builder.width;
        cancelTouchout = builder.cancelTouchout;
        gravity=builder.gravity;
        view = builder.view;
        startaAnim=builder.startaAnim;
        dimAmount=builder.dimAmount;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initWindows(ContextCompat.getColor(mContext, R.color.red));
        setContentView(view);

        setCanceledOnTouchOutside(cancelTouchout);

        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = gravity;
        lp.height = height;
        lp.width = width;

        window.setAttributes(lp);
        window.setWindowAnimations(startaAnim);   //设置dialog的显示动画
        window.setDimAmount(dimAmount);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |//Activity全屏显示，但状态栏依然可见，Activity顶端布局部分会被状态遮住。
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {//4.4 全透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


    }

    public View getView(int viewId) {
        return view.findViewById(viewId);
    }

    private  Boolean isExit = false;
    private  Boolean hasTask = false;
    Timer tExit = new Timer();
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            isExit = false;
            hasTask = true;
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit == false) {
                Toast.makeText(context, "再次按下退出应用", Toast.LENGTH_SHORT).show();
                isExit = true;
                System.out.println("再次按下退出应用");
                if (!hasTask) {
                    tExit.schedule(task, 1000);
                }
            } else {

                ActivityUtils.getActivityUtils().popAllActivity();
                dismiss();
                System.exit(0);
            }
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {

        }
        return false;

    }

    public static final class Builder {
        private CustomDialog dialog;

        private Context context;
        private int height, width;
        private boolean cancelTouchout;
        private View view;
        private int gravity;
        private int resStyle = -1;
        private int startaAnim;
        private float dimAmount=0.4f;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder view(int resView) {
            view = LayoutInflater.from(context).inflate(resView, null);
            return this;
        }

        public Builder height(int val) {
            height = val;
            return this;
        }

        public Builder width(int val) {
            width = val;
            return this;
        }

        public Builder heightdp(int val) {
            height = DensityUtils.dip2px(context, val);
            return this;
        }

        public Builder widthdp(int val) {
            width = DensityUtils.dip2px(context, val);
            return this;
        }

        public Builder heightDimenRes(int dimenRes) {
            height = context.getResources().getDimensionPixelOffset(dimenRes);
            return this;
        }

        public Builder widthDimenRes(int dimenRes) {
            width = context.getResources().getDimensionPixelOffset(dimenRes);
            return this;
        }

        public Builder dimAmount(float dimAmount){
            this.dimAmount=dimAmount;
            return this;
        }

        public Builder gravity(int gravity){
            this.gravity = gravity;
            return this;
        }
        public Builder style(int resStyle) {
            this.resStyle = resStyle;
            return this;
        }

        public Builder cancelTouchout(boolean val) {
            cancelTouchout = val;
            return this;
        }

        public Builder addViewOnclick(int viewRes,View.OnClickListener listener){
            view.findViewById(viewRes).setOnClickListener(listener);
            return this;
        }

        public  Builder setStartaAnim(int startaAnim){
            this.startaAnim=startaAnim;
            return this;
        }

        public View getView(int viewRes){
            return view.findViewById(viewRes);
        }
        public CustomDialog build() {
            if(dialog!=null){
                dialog.dismiss();
            }
            dialog=null;
            if (resStyle != -1) {
                dialog=new CustomDialog(this, resStyle);
                return dialog;
            } else {
                dialog=new CustomDialog(this);
                return dialog;
            }
        }
    }


}
