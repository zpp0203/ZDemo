package com.zpp.demo.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

import com.zpp.tools.LogUtils;
import com.zpp.tools.ToastUtils;

public class WifiBroadCastReceiver extends BroadcastReceiver {
    private String TAG="WifiBroadCastReceiver";
    public static final int PASS_ERROR=0;
    public static final int CONNECT_SUCCESS=1;
    public static final int CONNECT_FALSE=2;
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case WifiManager.SUPPLICANT_STATE_CHANGED_ACTION:
                //密码错误广播,是不是正在获得IP地址
                int linkWifiResult = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
                if (linkWifiResult == WifiManager.ERROR_AUTHENTICATING) {
                    //密码错误
                    WifiTools.getInstant(context).reMoveWifi(linkWifiResult);
                    Log.e(TAG,"密码错误");
                    if(wifiConnectListen!=null){
                        wifiConnectListen.connectListen(PASS_ERROR);
                    }
                }
            case WifiManager.WIFI_STATE_CHANGED_ACTION:

                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                        WifiManager.WIFI_STATE_DISABLED);
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_DISABLED://wifi断开

                        break;
                    case WifiManager.WIFI_STATE_ENABLED://wifi开启

                        break;
                }

                break;

            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    // 监听wifi的连接状态即是否连上了一个有效无线路由
                    Parcelable parcelableExtra = intent
                            .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (null != parcelableExtra) {
                        // 获取联网状态的NetWorkInfo对象
                        NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                        //获取的State对象则代表着连接成功与否等状态
                        NetworkInfo.State state = networkInfo.getState();
                        //判断网络是否已经连接
                        boolean isConnected = state == NetworkInfo.State.CONNECTED;
                        LogUtils.e(TAG,"连接："+state);
                        if(wifiConnectListen!=null){
                            wifiConnectListen.connectListen(isConnected?CONNECT_SUCCESS:CONNECT_FALSE);
                        }
                    }
                    break;
            case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                break;
        }
    }
    private WifiConnectListen wifiConnectListen;
    public void setWifiConnectListen(WifiConnectListen wifiConnectListen){
        this.wifiConnectListen=wifiConnectListen;
    }
    public abstract class WifiConnectListen{
        public abstract void connectListen(int listenCode);
    }
}