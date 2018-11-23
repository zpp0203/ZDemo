package com.zpp.widget;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zpp.R;

import static com.zpp.widget.XRecyclerView.STATE_COMPLETE;
import static com.zpp.widget.XRecyclerView.STATE_LOADING;
import static com.zpp.widget.XRecyclerView.STATE_NOMORE;


public class DefaultMoreFooter extends LinearLayout {

        private TextView mText;
        private String loadingHint;
        private String noMoreHint;
        private String loadingDoneHint;

        private ProgressBar progressView;

        public DefaultMoreFooter(Context context) {
            super(context);
            initView();
        }

        /**
         * @param context
         * @param attrs
         */
        public DefaultMoreFooter(Context context, AttributeSet attrs) {
            super(context, attrs);
            initView();
        }

        public void destroy(){
            if(progressView != null){
                progressView = null;
            }
        }

        public void setLoadingHint(String hint) {
            loadingHint = hint;
        }

        public void setNoMoreHint(String hint) {
            noMoreHint = hint;
        }

        public void setLoadingDoneHint(String hint) {
            loadingDoneHint = hint;
        }

        public void setProgressBg(Drawable drawable){
            ClipDrawable d = new ClipDrawable(drawable, 0, 0);
            progressView.setIndeterminateDrawable(d);
        }

        private void initView(){
            setGravity(Gravity.CENTER);
            RecyclerView.LayoutParams layout=new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.setMargins(0,3,0,3);
            setLayoutParams(layout);
            progressView = new  ProgressBar(this.getContext());

            addView(progressView);

            setProgressBg(ContextCompat.getDrawable(this.getContext(),R.drawable.shape_loadprogress_bg));


            ViewGroup.LayoutParams linearParams = progressView.getLayoutParams();//需要addView后
            linearParams.height = 22;
            progressView.setLayoutParams(linearParams);

            mText = new TextView(getContext());
            mText.setText(getContext().getString(R.string.header_hint_refresh_loading));

            if(loadingHint == null || loadingHint.equals("")){
                loadingHint = (String)getContext().getText(R.string.header_hint_refresh_loading);
            }
            if(noMoreHint == null || noMoreHint.equals("")){
                noMoreHint = (String)getContext().getText(R.string.footer_load_more_hint_all);
            }
            if(loadingDoneHint == null || loadingDoneHint.equals("")){
                loadingDoneHint = (String)getContext().getText(R.string.footer_load_more_hint_normal);
            }

            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins( 3,0,0,0 );

            mText.setLayoutParams(layoutParams);
            addView(mText);
        }


        public void  setState(int state) {
            switch(state) {
                case STATE_LOADING:
                    progressView.setVisibility(VISIBLE);
                    mText.setText(loadingHint);
                    this.setVisibility(View.VISIBLE);
                    break;
                case STATE_COMPLETE:
                    mText.setText(loadingDoneHint);
                    this.setVisibility(View.GONE);
                    break;
                case STATE_NOMORE:
                    mText.setText(noMoreHint);
                    progressView.setVisibility(View.GONE);
                    this.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

