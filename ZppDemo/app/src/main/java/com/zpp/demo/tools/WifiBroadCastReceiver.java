package com.zpp.demo.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

import com.zandroid.tools.LogUtils;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class WifiBroadCastReceiver extends BroadcastReceiver {
    private String TAG="WifiBroadCastReceiver";
    public static final int PASS_ERROR=0;
    public static final int CONNECT_SUCCESS=1;
    public static final int CONNECT_FALSE=2;
    public static final int NETEABLE=3;

    public static final int NETSTATUS_INAVAILABLE = 0;
    public static final int NETSTATUS_WIFI = 1;
    public static final int NETSTATUS_MOBILE = 2;
    public static int netStatus = 0;
    public static boolean updateSuccess = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo allNetInfo = cm.getActiveNetworkInfo();

        if (allNetInfo == null) {
            if (mobileNetInfo != null && (mobileNetInfo.isConnected() || mobileNetInfo.isConnectedOrConnecting())) {
                netStatus = NETSTATUS_MOBILE;
            } else if (wifiNetInfo != null && wifiNetInfo.isConnected() || wifiNetInfo.isConnectedOrConnecting()) {
                netStatus = NETSTATUS_WIFI;
            } else {
                netStatus = NETSTATUS_INAVAILABLE;
            }
        } else {
            if (allNetInfo.isConnected() || allNetInfo.isConnectedOrConnecting()) {
                if (mobileNetInfo.isConnected() || mobileNetInfo.isConnectedOrConnecting()) {
                    netStatus = NETSTATUS_MOBILE;
                } else {
                    netStatus = NETSTATUS_WIFI;
                }
            } else {
                netStatus = NETSTATUS_INAVAILABLE;
            }
        }

        switch (intent.getAction()) {
            case WifiManager.SUPPLICANT_STATE_CHANGED_ACTION:
                //密码错误广播,是不是正在获得IP地址
                int linkWifiResult = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
                if (linkWifiResult == WifiManager.ERROR_AUTHENTICATING) {
                    //密码错误
                    WifiTools.getInstant(context).reMoveWifi(linkWifiResult);
                    Log.e(TAG,"密码错误");
                    if(wifiConnectListen!=null){
                        wifiConnectListen.connectListen(PASS_ERROR,ConnectivityManager.TYPE_WIFI);
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
                            wifiConnectListen.connectListen(isConnected?CONNECT_SUCCESS:CONNECT_FALSE,ConnectivityManager.TYPE_WIFI);
                        }
                    }
                    break;
            case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                break;

            // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
            case CONNECTIVITY_ACTION:
                //获取联网状态的NetworkInfo对象
                NetworkInfo info = intent
                        .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    //如果当前的网络连接成功并且网络连接可用
                    if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                        if (info.getType() == ConnectivityManager.TYPE_WIFI
                                || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                            Log.i("TAG", getConnectionType(info.getType()) + "连上");
                            wifiConnectListen.connectListen(NETEABLE,info.getType());
                        }
                    } else {
                        Log.i("TAG", getConnectionType(info.getType()) + "断开");
                    }
                }
                break;
        }
    }
    private WifiConnectListen wifiConnectListen;
    public void setWifiConnectListen(WifiConnectListen wifiConnectListen){
        this.wifiConnectListen=wifiConnectListen;
    }
    public abstract class WifiConnectListen{
        public abstract void connectListen(int listenCode,int netType);
    }

    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "移动网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        }
        return connType;
    }
}