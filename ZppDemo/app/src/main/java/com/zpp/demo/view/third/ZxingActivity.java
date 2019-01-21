package com.zpp.demo.view.third;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zpp.demo.R;
import com.zpp.demo.base.BaseActivity;
import com.zpp.zxinglibrary.bean.ZxingConfig;
import com.zpp.zxinglibrary.common.Constant;
import com.zpp.zxinglibrary.encode.CodeCreator;


import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author: yzq
 * @date: 2017/10/26 15:17
 * @declare :
 */

public class ZxingActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.scanBtn)
    Button scanBtn;
    /*扫描结果*/
    @Bind(R.id.result)
    TextView result;
    /*要生成二维码的输入框*/
    @Bind(R.id.contentEt)
    EditText contentEt;
    /*生成按钮*/
    @Bind(R.id.encodeBtn)
    Button encodeBtn;
    /*生成的图片*/
    @Bind(R.id.contentIv)
    ImageView contentIv;
    private int REQUEST_CODE_SCAN = 111;
    /**
     * 生成带logo的二维码
     */
    @Bind(R.id.encodeBtnWithLogo)
    Button encodeBtnWithLogo;
    @Bind(R.id.contentIvWithLogo)
    ImageView contentIvWithLogo;
    private String contentEtString;

    @Override
    protected int setLayoutView() {
        return R.layout.activity_zxing;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }




    @OnClick({R.id.scanBtn,R.id.encodeBtn,R.id.encodeBtnWithLogo})
    public void onClick(View v) {
        Bitmap bitmap = null;
        switch (v.getId()) {
            case R.id.scanBtn:
                checkCameraAndroid6Permission();
                break;
            case R.id.encodeBtn:
                contentEtString = contentEt.getText().toString().trim();
                if (TextUtils.isEmpty(contentEtString)) {
                    Toast.makeText(this, "请输入要生成二维码图片的字符串", Toast.LENGTH_SHORT).show();
                    return;
                }

                bitmap = CodeCreator.createQRCode(contentEtString, 400, 400, null);
                if (bitmap != null) {
                    contentIv.setImageBitmap(bitmap);
                }

                break;

            case R.id.encodeBtnWithLogo:

                contentEtString = contentEt.getText().toString().trim();
                if (TextUtils.isEmpty(contentEtString)) {
                    Toast.makeText(this, "请输入要生成二维码图片的字符串", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                bitmap = CodeCreator.createQRCode(contentEtString, 400, 400, logo);

                if (bitmap != null) {
                    contentIvWithLogo.setImageBitmap(bitmap);
                }
                break;
            default:
        }
    }
    //Android6.0中打开蓝牙需要动态申请定位权限
    private final int REQUEST_CODE_CONTACT = 101;
    private void checkCameraAndroid6Permission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};
            //权限检测
            if((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=
                    PackageManager.PERMISSION_GRANTED)||(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=
                    PackageManager.PERMISSION_GRANTED) ){
                ActivityCompat.requestPermissions(this,permissions,REQUEST_CODE_CONTACT);
                return;
            }
        }
        startZxing();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_CONTACT: {
                // 授权被允许
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("-------->", "请求被允许");
                    startZxing();

                } else {
                    Log.v("-------->", "请求被拒绝");
                    Uri packageURI = Uri.parse("package:" + getPackageName());
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);

                    Toast.makeText(ZxingActivity.this, "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
    private void startZxing(){
        Log.e("-","------");
        Intent intent = new Intent(ZxingActivity.this, CaptureActivity.class);
        /*ZxingConfig是配置类
         *可以设置是否显示底部布局，闪光灯，相册，
         * 是否播放提示音  震动
         * 设置扫描框颜色等
         * 也可以不传这个参数
         * */
        ZxingConfig config = new ZxingConfig();
        // config.setPlayBeep(false);//是否播放扫描声音 默认为true
        //  config.setShake(false);//是否震动  默认为true
        // config.setDecodeBarCode(false);//是否扫描条形码 默认为true
//                                config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
//                                config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
//                                config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                result.setText("扫描结果为：" + content);
            }
        }
    }

}
