package com.zpp.demo.view.third;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.zpp.demo.R;
import com.zpp.demo.base.BaseActivity;
import com.zpp.demo.bean.AuthResult;
import com.zpp.demo.tools.OrderInfoUtil2_0;
import com.zpp.tools.LogUtils;
import com.zpp.tools.StringUtils;

import java.util.Map;

import butterknife.OnClick;

public class AliActivity extends BaseActivity {
    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2016092200568739";//应用APPID

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "2088102176738184";//合作伙伴身份PID  开发平台密匙管理中可查看
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "1024";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    //应用私钥
    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCD2Vs6Egh9EVk6xugxJLQGVwveQSr357fzhavJcNF/gr7ZsXHpCIa0IC4dcdhVr+Rk4Jn8kWdI7gPvt/Fi3OvtPOiXhCyrM1BIttS1nV/2GRrcqoqZnm3sskvKpug0h0xYg3BmeZAYjf7LQmJbT6dZTJ1B+vtUXF2sKnk8otxGC+zji2XdQ2JOZrMuR0oEcm1UNMwGbAbv0Ui5rQwnhQuNCBiQ/6GsOnYabB+DMpILmxsjx24e/8fuHjobzAFwUZOTZx/6QjdyrhshfDPePADyeD1MntFxGL5it/Hg3aGmTIyvamXmvvn3biN3WJhwrwOGPVIqg7J+uXu8KVR30R3HAgMBAAECggEAfwt4gPsRXNnTnfQ9leTk5tsHX5Bxw9BuiuN5hTAFXhVwqrj1pvMFQRlk9hBVwTMsIxvcpObCnB5kgp8+o+F2H/G5uQ2uMQTPzlEFoz5SUgMcxBY8WQu0pgejSlELnFnmcb+tFcD63P4tzlNYDN4yxbQyL3qUpFQgGp4OliPjlkXTT6btApPG0MKtDogWx0109X3OQ3/RpUJggHzozfwCzF9sEyk75VeMM8GdGj2B4VuWCCpe9n6kdP71N6AhY7Q+3z7dJvjzzfJYu9zRUJ6fz4IP0xh80PUi8CbQU2HS5AjmFiw3C/Br4mGkdiQ/NZ4uMU8nH/usCMNWxk7DmsgxqQKBgQC65M6si83BXoe44YTZS8BaIDYrbQXef94Fp36j7Z6sLLEEhJ+GS/WnyNmYNvtCeopI9UxhR1pn5xDwcNsIl94FZoagwS4OgTgCCtJES4XGtmUWE6qo7n9eaXF09toqK+X2vf0fGYmupnIlIMgx3SwYDJ622nLu14QDCnPRNbw06wKBgQC0mhDNoVnDu8jR4KU25zFkD/82S+d/R5wYjeFk2Hw+AtonyuQ2FsUKdxaEHrua/XcYa6cGwgxjmbqEsbv2kFeOiLs7W3KD9TPUgwfRyYuRBi/RKE6hLD0uci4T0/jLtOhTjU93omF+oJ365Kic/P8Yggak8sYYaAgEVAqYr1KzlQKBgB7jF6D+jeeqRsXwR3NeCJEch7dPkku+WCQZFV3kNHgB/tNfU7nvLc8n64Yzd/z5oYcDOzdMgo1va6ZBEIHwD1pXImLI6mKtSmNUBIIS9S3tEPHAGFFI8TXaOUz6Sv3zOVM4/O852j5J9c1bCJrtEY977nML0TjV11RlUtS9i7g3AoGATuvQkfuDfYxvGoAf0pk/NSnkbfbHiBBj/zrpFyefS331lVQNXDGhE4ys8zl25CkFRU+t4r8jqNbB8kC1Ee9Lw8augybYKSAa8S/9V8jKS6q3dKA3lCj/528QAaws2eAJcpGa+32jOzG3N696m3fXhf80JhrNRFcZwcJ0Cvr6me0CgYEAk3n6r/xpIbzZiyau7nfC5OG6UUBORvlYEgQBg5I46igUyqAh2ng7RqYpwNZIap4WhIxrp1HSbQmx6sWHgxG1W87TS24k+aZlz/jub3x5Yrsj6C2YavuvlyXOZ3mQ82nUMXJEh+974bCRIRMrJap+X8CkxGX8pY/0kIaQc2/jMnE=";
    public static final String RSA_PRIVATE = "";
    private static final int SDK_AUTH_FLAG = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_ali;
    }
    @OnClick({R.id.ali_login})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.ali_login:
                LogUtils.e("ali login-----");
                authV2("");
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value

                        // 传入，则支付账户为该授权账户
                        Toast.makeText(AliActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(AliActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    /**
     * 支付宝账户授权业务
     *
     */
    public void authV2(String authInfo) {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
                || TextUtils.isEmpty(TARGET_ID)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
            return;
        }
        if(StringUtils.isEmpty(authInfo))
            authInfo=getAuthInfo();

        final String finalAuthInfo = authInfo;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(AliActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(finalAuthInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    private String getAuthInfo(){
        /**
         * authInfo//签名
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;
        return authInfo;
    }
}
