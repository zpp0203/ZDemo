package com.zpp.demo.view.third;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zpp.demo.R;
import com.zpp.demo.manager.QQShareManager;
import com.zandroid.tools.ToastUtils;

import static com.zpp.demo.manager.QQShareManager.QQ_SHARE_TYPE_ZONE;

public class QQDemoActivity extends AppCompatActivity {
    private QQShareManager qqShareManager;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_qqdemo);
    }


    private void shareToQQ(){
        qqShareManager=new QQShareManager();
        qqShareManager.registShare(this);
        QQShareManager.ShareContentWebpage shareContent=qqShareManager.getShareContentWebpag("标题","这是分享主体","https://hao.qq.com/?unc=Af31026&s=o400493_1","http://politics.people.com.cn/n1/2018/0613/c1024-30055946.html");

        qqShareManager.shareByQQ(this,shareContent,QQ_SHARE_TYPE_ZONE);
        qqShareManager.setOnQQShareResponse(new QQShareManager.QQShareResponse() {
            @Override
            public void respCode(int code) {
                switch (code){
                    case 0:
                        ToastUtils.showLong(mContext,"分享成功");
                        break;
                    case 1:
                        ToastUtils.showLong(mContext,"取消分享");
                        break;
                    case 2:
                        ToastUtils.showLong(mContext,"拒绝访问");
                        break;
                    case 3:
                        ToastUtils.showLong(mContext,"分享失败");
                        break;
                }
            }
        });
    }
}
