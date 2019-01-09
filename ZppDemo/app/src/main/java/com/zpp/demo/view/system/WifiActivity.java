package com.zpp.demo.view.system;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zpp.demo.R;
import com.zpp.demo.adapter.WifiListAdapter;
import com.zpp.demo.base.BaseActivity;
import com.zpp.demo.dialog.EditDialog;
import com.zpp.demo.tools.WifiBroadCastReceiver;
import com.zpp.demo.tools.WifiTools;
import com.zandroid.recycleview.BaseRecyclerViewAdapter;
import com.zandroid.tools.LogUtils;
import com.zandroid.tools.StringUtils;
import com.zandroid.tools.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.OnClick;

import static android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION;
import static android.net.wifi.WifiManager.SCAN_RESULTS_AVAILABLE_ACTION;
import static android.net.wifi.WifiManager.SUPPLICANT_STATE_CHANGED_ACTION;
import static android.net.wifi.WifiManager.WIFI_STATE_CHANGED_ACTION;

public class WifiActivity extends BaseActivity implements View.OnClickListener {
    Button wifi_state;
    Button wifi_scan;
    RecyclerView wifi_list;
    WifiListAdapter adapter;

    List<ScanResult> list;

    WifiBroadCastReceiver mWifiBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerBoradcastReceiver();
        initView();
        requestLocationPermission();

    }
    @Override
    protected int setLayoutView() {
        return R.layout.activity_wifi;
    }
    private void initData() {
        WifiTools wifiTools=WifiTools.getInstant(this);
        wifiTools.scanWifiAround();
        List<ScanResult> scanResults=wifiTools.getWifiScanResult();
        list.clear();
        list.addAll(WifiTools.getInstant(this).delEmptyName(scanResults));//去掉空的ssid;

        Collections.sort(list, new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult o1, ScanResult o2) {
                return o2.level - o1.level;
            }
        });
        Log.e("wifi","list:"+list.size()+" scan size:"+scanResults.size());


        WifiInfo wifiInfo=wifiTools.wifiConnectionInfo();
        Log.e("wifi 信息",wifiTools.getWifiIpAddress(wifiInfo));
        Log.e("网络 信息",wifiTools.intToIp(wifiTools.getDhcpInfo().ipAddress));

        adapter.notifyDataSetChanged();


    }
    public void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果 API level 是大于等于 23(Android 6.0) 时
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要向用户解释为什么需要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    ToastUtils.showLong(this, "自Android 6.0开始需要打开位置权限才可以搜索到WIFI设备");

                }
                //请求权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1111);
                return;
            }
        }
        initData();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1111:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    initData();
                } else {
                    // Permission Denied
                }

                break;
        }
    }
    private void initView() {
        wifi_state=findViewById(R.id.wifi_state);
        wifi_state.setOnClickListener(this);
        wifi_scan=findViewById(R.id.wifi_scan);
        wifi_scan.setOnClickListener(this);
        wifi_list=findViewById(R.id.wifi_list);
        setStateText();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this );
        //设置布局管理器
        wifi_list.setLayoutManager(layoutManager);
        list=new ArrayList<>();
        adapter=new WifiListAdapter(R.layout.item_flowlayout,list);
        wifi_list.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(new BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                itemClick(position);
            }
        });
    }
    //连接点击的wifi
    private void itemClick(int position){
        final ScanResult scanResult=list.get(position);
        if(WifiTools.getInstant(this).isExist(scanResult.SSID)!=null){
            createConfiguration(scanResult,"");
        }else {
            EditDialog dialog=new EditDialog(this) {
                @Override
                public void submitOnClick(String editText) {
                    //需要密码
                    createConfiguration(scanResult,editText);
                }
            };
            dialog.setTitle("请输入密码");
            dialog.show();
        }
    }
    private void createConfiguration(ScanResult scanResult,String password){
        LogUtils.e(scanResult.SSID+"--"+password);
        if(StringUtils.isEmpty(password)) {
            int networkId=WifiTools.getInstant(this).createWifiConfig(scanResult);
            WifiTools.getInstant(this).eableWifi(networkId);
        }else {
            int networkId=WifiTools.getInstant(this).createWifiConfig(scanResult,password);
            WifiTools.getInstant(this).eableWifi(networkId);
        }
    }

    @OnClick({R.id.wifi_reconnect})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wifi_state:
                WifiTools.getInstant(this).openOrCloseWifi();
                setStateText();
                break;
            case R.id.wifi_scan:
                initData();
                break;
            case R.id.wifi_reconnect:
//                WifiInfo wifiInfo=WifiTools.getInstant(mContext).wifiConnectionInfo();
//                WifiTools.getInstant(mContext).deleteWifi(wifiInfo.getNetworkId());
                WifiTools.getInstant(mContext).disConnectWifi();
                break;
        }
    }
    private void setStateText(){
        if(WifiTools.getInstant(this).isWifiEnable()){
            Log.e("wifiState","wifi open");
            wifi_state.setText("close");
        }else {
            Log.e("wifiState","wifi close");
            wifi_state.setText("open");
        }
    }
    //注册wifi状态变化广播
    public void registerBoradcastReceiver(){
        mWifiBroadcastReceiver=new WifiBroadCastReceiver();

        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(SUPPLICANT_STATE_CHANGED_ACTION);
        myIntentFilter.addAction(WIFI_STATE_CHANGED_ACTION);
        myIntentFilter.addAction(NETWORK_STATE_CHANGED_ACTION);
        myIntentFilter.addAction(SCAN_RESULTS_AVAILABLE_ACTION);
        //注册广播
        registerReceiver(mWifiBroadcastReceiver, myIntentFilter);
    }

    @Override
    protected void onDestroy() {
        //注销广播
        if (mWifiBroadcastReceiver != null) {
            unregisterReceiver(mWifiBroadcastReceiver);
            mWifiBroadcastReceiver = null;
        }
        super.onDestroy();
    }
}
