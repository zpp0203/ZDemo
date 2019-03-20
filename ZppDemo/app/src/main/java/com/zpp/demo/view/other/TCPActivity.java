package com.zpp.demo.view.other;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zpp.demo.R;
import com.zpp.demo.base.BaseActivity;
import com.zpp.demo.bean.MainBean;
import com.zpp.demo.bean.TCPClientEVent;
import com.zpp.demo.bean.TCPServerEVent;
import com.zpp.demo.tools.TcpClient;
import com.zpp.demo.tools.TcpServer;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class TCPActivity extends BaseActivity {

    Button btnStartServer;
    Button btnSrartClient;

    Button btnSendToServer;
    Button btnSendToClient;

    TextView tv_server;
    TextView tv_client;

    @Bind(R.id.tv_back_server)
    TextView tv_back;

    @Override
    protected int setLayoutView() {
        return R.layout.activity_tcp;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnStartServer = findViewById(R.id.btn_start_server);
        btnSrartClient = findViewById(R.id.btn_start_client);
        btnSendToServer = findViewById(R.id.btn_send_server);
        btnSendToClient = findViewById(R.id.btn_send_client);
        tv_server = findViewById(R.id.tv_server);
        tv_client = findViewById(R.id.tv_client);

        btnStartServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开启服务器
                TcpServer.startServer();
            }
        });

        btnSrartClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //连接服务器
                TcpClient.startClient( getIPAddress(getApplicationContext()) , 8080);
            }
        });

        btnSendToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送数据给服务器
                TcpClient.sendTcpMessage("321");
            }
        });

        btnSendToClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送数据给客户端
                TcpServer.sendTcpMessage("321");
            }
        });

        Log.i("tcp" ,"ip地址:" + getIPAddress(this));

    }
    StringBuilder stringBuilderC=new StringBuilder();
    StringBuilder stringBuilderS=new StringBuilder();
    @Subscribe
    //收到客户端的消息
    public void onEventMainThread(TCPClientEVent tcpClientEVent) {
        stringBuilderC.append(tcpClientEVent.getMsg()+"\n");
        tv_client.setText("客户端收到:\n" +stringBuilderC.toString());
    }

    //收到服务器的消息
    @Subscribe
    public void onEventMainThread(TCPServerEVent messageEvent) {
        stringBuilderS.append(messageEvent.getMsg()+"\n");
        tv_server.setText("服务器收到:\n" +stringBuilderS.toString());
    }
    @Subscribe
    public void onEventMainThread(MainBean mainBean){
        tv_back.setText(mainBean.getState());
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
