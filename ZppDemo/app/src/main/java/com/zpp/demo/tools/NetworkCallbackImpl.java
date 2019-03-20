package com.zpp.demo.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.zandroid.tools.ToastUtils;

    //注册
    /*NetworkRequest.Builder builder = new NetworkRequest.Builder();
      NetworkRequest request = builder.build();
      connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
      connectivityManager.registerNetworkCallback(request, networkCallback);
    */
    //解绑 onDestroy()
    //connectivityManager.unregisterNetworkCallback(networkCallback);

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {
    private Context context;
    private  ConnectivityManager connectivityManager;

    public NetworkCallbackImpl(Context context) {
        this.context = context;
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        NetworkRequest request = builder.build();
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.registerNetworkCallback(request, this);
    }


    public void destroy() {
        if (connectivityManager != null)
            connectivityManager.unregisterNetworkCallback(this);
    }

    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        ToastUtils.showToast(context, "onAvailable");

//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(WifiTools.getInstant(context).getCurrentNetwork(context));
//                    //通过如下这个判断，可以达到预期的效果
//                    if (nc !=null && !nc.hasCapability(nc.NET_CAPABILITY_VALIDATED)) {
//                        Log.d("MyReceiver","已连接，但无法上网");
//                    }
//                }
    }
 @Override
    public void onLosing(Network network, int maxMsToLive) {
        super.onLosing(network, maxMsToLive);
    Toast.makeText(context, "onLosing", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onLost(Network network) {
        super.onLost(network);
    Toast.makeText(context, "onLost", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        Toast.makeText(context, "onCapabilitiesChanged", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties);
        Toast.makeText(context, "onLinkPropertiesChanged", Toast.LENGTH_SHORT).show();
    }
}
