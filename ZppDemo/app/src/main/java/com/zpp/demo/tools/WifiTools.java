package com.zpp.demo.tools;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.DhcpInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.zpp.tools.ToastUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WifiTools {

    private String TAG="WifiTools";
    private static WifiTools sInstant = null;
    private final WifiManager mWifiManager;
    private final Context mContext;


    private WifiTools(Context context) {
        //拿到wifi管理器
        mWifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        this.mContext = context;
    }

    /**
     * 获取 一个单例对象
     *
     * @param context 上下文
     * @return 返回实例对象
     */
    public static WifiTools getInstant(Context context) {
        if (sInstant == null) {
            synchronized (WifiTools.class) {
                if (sInstant == null) {
                    sInstant = new WifiTools(context);
                }
            }
        }
        return sInstant;
    }

    /**
     * 获取当前wifi链接信息
     *
     * @return 返回实例对象
     */
    public WifiInfo wifiConnectionInfo() {
        return mWifiManager.getConnectionInfo();
    }

    public DhcpInfo getDhcpInfo(){
        return mWifiManager.getDhcpInfo();
    }

    /**
     * 这个方法用于扫描周围的wifi
     */
    public void scanWifiAround() {
        if (!isWifiEnable()) {
            Toast.makeText(mContext, "wifi没有打开,请打开wifi", Toast.LENGTH_SHORT).show();
            return;
        }
        mWifiManager.startScan();
    }

    /**
     * @return true表示wifi已经打开, false表示wifi没有打开, 状态为:
     * 打开中,或者关闭,或者关闭中...
     */
    public boolean isWifiEnable() {
        return mWifiManager.isWifiEnabled();
    }

    /**
     * 这个方法用于打开或关闭wifi
     * 当wifi打开的时候,那么就会关闭wifi
     * 当wifi关闭的时候,那么就会打开wifi
     */
    public void openOrCloseWifi() {
        //判断当前wifi的状态,是关闭还是打开
        if (this.isWifiEnable()) {
            mWifiManager.setWifiEnabled(false);
        } else {
            mWifiManager.setWifiEnabled(true);
        }
    }

    /**
     * @return 返回扫描的wifi结果, 是一个list集合
     * @call 当收到扫描结果的广播以后就可以调用这个方法去获取扫描结果
     */
    public List<ScanResult> getWifiScanResult() {
        return mWifiManager.getScanResults();
    }

    /**
     * @param ssid wifi的bssid
     * @return 返回该wifi是否已经配置过
     * @call 判断连接时是否需要密码
     */
    public WifiConfiguration isExist(String ssid) {
        List<WifiConfiguration> configs = mWifiManager.getConfiguredNetworks();

        for (WifiConfiguration config : configs) {
            if (config.SSID.equals("\"" + ssid + "\"")) {
                return config;
            }
        }

        return null;
    }

    public void reMoveWifi(int networkId){
        mWifiManager.removeNetwork(networkId);
    }
    public void eableWifi(int networkId){

        if (networkId == -1) {
            ToastUtils.showLong(mContext, "操作失败,需要您到手机wifi列表中取消对设备连接的保存...");
            Log.e(TAG, "操作失败,需要您到手机wifi列表中取消对设备连接的保存");
        } else {
            boolean enableNetwork = mWifiManager.enableNetwork(networkId, true);
            if (!enableNetwork) {
                ToastUtils.showLong(mContext, "切换到指定wifi失败...");
                Log.e(TAG, "切换到指定wifi失败");
            } else {
                ToastUtils.showLong(mContext, "切换到指定wifi成功...");
                Log.e(TAG, "切换到指定wifi成功");
            }
        }
        mWifiManager.enableNetwork(networkId,true);
    }
    /**
    * return networkId
    * */
    public int createWifiConfig(ScanResult scanResult) {
        return createWifiConfig(scanResult, "");
    }
    public int createWifiConfig(ScanResult scanResult, String password) {
        String capabilities = scanResult.capabilities;
        String ssid=scanResult.SSID;
        //初始化WifiConfiguration
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        //指定对应的SSID
        config.SSID = "\"" + ssid + "\"";

        //以WEP加密的场景
        if(capabilities.contains("WEP") || capabilities.contains("wep")) {
            config.hiddenSSID = true;
            config.wepKeys[0]= "\""+password+"\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
            //以WPA加密的场景，自己测试时，发现热点以WPA2建立时，同样可以用这种配置连接
        } else if(capabilities.contains("WPA") || capabilities.contains("wpa")) {
            config.preSharedKey = "\""+password+"\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }else {//不需要密码的场景
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        int networkId = mWifiManager.addNetwork(config);
        return networkId;
    }


    /**
     * 断开WIFI
     *
     * @param netId netId
     * @return 是否断开
     */
    public boolean disconnectWifi ( int netId){
        boolean isDisable = mWifiManager.disableNetwork(netId);
        boolean isDisconnect = mWifiManager.disconnect();
        return isDisable && isDisconnect;
    }


    /**
     * 关闭Wifi
     */
    public void closeWifi () {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 去掉空的ssid
     *
     * @return
     */
    public List<ScanResult> delEmptyName (List < ScanResult > sr) {
        List<ScanResult> newlist = new ArrayList<ScanResult>();
        for (ScanResult result : sr) {
            if (!TextUtils.isEmpty(result.SSID))
                newlist.add(result);
        }
        return newlist;
    }

    public String getWifiIpAddress(WifiInfo wifiInfo){
        return intToIp(wifiInfo.getIpAddress());
    }

    public String intToIp(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    //判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
    public boolean isLocation(){
        LocationManager locManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        if(locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        }
        return false;
    }
    /**
     * 强制帮用户打开GPS
     */
    public void openGPS() {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(mContext, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

        Log.e("wifiTool","强制帮用户打开GPS");
    }
    //连接过当前热点的设备。自己手机
    public void j(){
        String MacAddr = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/net/arp"));
            String line = reader.readLine();//读取第一行信息,就是IP address HW type Flags HW address Mask Device
             while ((line = reader.readLine()) != null) {
                 String[] tokens = line.split("[ ]+");
                 if (tokens.length < 6) {
                     continue;
                 }
                 String ip = tokens[0]; //ip
                 String mac = tokens[3]; //mac 地址
                 String flag = tokens[2];//表示连接状态
             }
         } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            }catch (IOException e) {

            }
        }
    }
}
