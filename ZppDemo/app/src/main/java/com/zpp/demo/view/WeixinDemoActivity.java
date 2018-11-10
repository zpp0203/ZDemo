package com.zpp.demo.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zpp.demo.R;
import com.zpp.demo.base.BaseActivity;
import com.zpp.demo.manager.WechatShareManager;

public class WeixinDemoActivity extends BaseActivity implements View.OnClickListener {

    private Button mShareText, mSharePicture, mShareVideo;
    private WechatShareManager mShareManager;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mShareText = (Button) findViewById(R.id.share_text);
        mSharePicture = (Button) findViewById(R.id.share_picture);
        mShareVideo = (Button) findViewById(R.id.share_video);
        mShareText.setOnClickListener(this);
        mSharePicture.setOnClickListener(this);
        mShareVideo.setOnClickListener(this);

        mContext = this;

        mShareManager = WechatShareManager.getInstance(mContext);
    }
    @Override
    protected int setLayoutView() {
        return R.layout.activity_weixin_demo;
    }
    @Override
    public void onClick(View v) {
        if (!isWebchatAvaliable()) {
            Toast.makeText(mContext, "请先安装微信", Toast.LENGTH_LONG).show();
            return;
        }
        switch (v.getId()) {
            case R.id.share_text:
                WechatShareManager.ShareContentText mShareContentText = (WechatShareManager.ShareContentText) mShareManager.getShareContentText("微信文本分享");
                mShareManager.shareByWebchat(mShareContentText, WechatShareManager.WECHAT_SHARE_TYPE_FRENDS);
                break;
            case R.id.share_picture:
                WechatShareManager.ShareContentPicture mShareContentPicture = (WechatShareManager.ShareContentPicture) mShareManager.getShareContentPicture(R.drawable.icon);
                mShareManager.shareByWebchat(mShareContentPicture, WechatShareManager.WECHAT_SHARE_TYPE_FRENDS);
                break;
            case R.id.share_video:
                WechatShareManager.ShareContentVideo mShareContentVideo = (WechatShareManager.ShareContentVideo) mShareManager.getShareContentVideo("http://baidu.hz.letv.com/kan/agSlT?fr=v.baidu.com/");
                mShareManager.shareByWebchat(mShareContentVideo, WechatShareManager.WECHAT_SHARE_TYPE_FRENDS);
                break;
            default:
                break;
        }
    }

    private boolean isWebchatAvaliable() {
        //检测手机上是否安装了微信
        try {
            getPackageManager().getPackageInfo("com.tencent.mm", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
