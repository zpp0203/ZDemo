package com.zpp.demo.adapter;

import com.zandroid.recycleview.BaseRecyclerViewAdapter;
import com.zandroid.recycleview.BaseRecyclerViewHolder;
import com.zpp.demo.R;
import com.zpp.demo.bean.MainBean;

import java.util.List;

public class SlideRecyclerAdapter extends BaseRecyclerViewAdapter {
    public SlideRecyclerAdapter(int layoutId, List list) {
        super(layoutId, list);
    }

    @Override
    protected void getView(BaseRecyclerViewHolder holder, Object item, int position) {
        holder.setText(R.id.text,((MainBean) item).getTitle());
    }
}
