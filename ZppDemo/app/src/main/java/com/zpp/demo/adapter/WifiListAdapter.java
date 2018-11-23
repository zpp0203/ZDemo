package com.zpp.demo.adapter;

import android.net.wifi.ScanResult;
import android.util.Log;

import com.zpp.demo.R;
import com.zpp.recycleview.BaseRecyclerViewAdapter;
import com.zpp.recycleview.BaseRecyclerViewHolder;
import com.zpp.demo.tools.WifiTools;

import java.util.List;

public class WifiListAdapter extends BaseRecyclerViewAdapter {

    public WifiListAdapter(int layoutId, List list) {
        super(layoutId, list);
    }


    @Override
    protected void getView(BaseRecyclerViewHolder holder, Object item, int position) {
        super.getView(holder, item, position);
        ScanResult result= (ScanResult) item;
        Log.d("wifiadapter","SSID:"+result.SSID+" BSSID:"+result.BSSID);
        holder.setText(R.id.my_text,result.SSID);
    }
}
