package com.zpp.demo.adapter;


import com.zpp.demo.R;
import com.zpp.recycleview.BaseRecyclerViewAdapter;
import com.zpp.recycleview.BaseRecyclerViewHolder;
import com.zpp.demo.bean.MainBean;

import java.util.List;

/**
 * Created by bstadmin on 2018/6/13.
 */

public class RecycleAdapter extends BaseRecyclerViewAdapter {

    public RecycleAdapter(int layoutId, List list) {
        super(layoutId, list);
    }
    //给item内控件填充数据及设置点击监听事件
    @Override
    protected void getView(BaseRecyclerViewHolder holder,  Object item, int position) {
        MainBean bean= (MainBean) item;
        holder.setText(R.id.title,bean.getTitle());
        holder.setText(R.id.state,bean.getState());

//        holder.setOnClick(R.id.title,position);

    }


}
