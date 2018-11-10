package com.zpp.demo.adapter;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zpp.demo.R;
import com.zpp.demo.bean.Person;
import com.zpp.demo.volley.MyVolley;

public class VolleyImagerLoaderAdapter extends BaseAdapter {
    private Context context;
    private List<Person> list;
    private LayoutInflater mInflater;
    public ViewHolder holder;

    public VolleyImagerLoaderAdapter(Context context, List<Person> list) {
        this.context = context;
        this.list = list;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_imageloader, null);
            holder = new ViewHolder();
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Person bean = list.get(position);
        holder.tv_name.setText(bean.getName());
        MyVolley.getImage(bean.getImgUrl(), holder.iv_image, R.mipmap.ic_launcher, R.mipmap.ic_launcher, 150, 150);
        return convertView;
    }

    class ViewHolder {
        private TextView tv_name;
        private ImageView iv_image;
    }
}
