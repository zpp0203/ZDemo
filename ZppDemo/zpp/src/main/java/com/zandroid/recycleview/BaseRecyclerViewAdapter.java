package com.zandroid.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 墨 on 2018/6/13.
 */

//封装的时候，部分参数可以选择由外部的构造函数或者set方法
public class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> implements View.OnClickListener,View.OnLongClickListener {
    private List<T> mList;
    private int layoutId;

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener;
    private OnSubViewClickListener onSubViewClickListener;

    public BaseRecyclerViewAdapter(int layoutId, List<T> list){
        this.layoutId=layoutId;
        if(list==null)
            list=new ArrayList<T>();
        mList = list;
    }

    public void addItems(List<T> items){
        if(mList!=null && items!=null){
            mList.addAll(items);
        }else {
            mList=items;
        }
        notifyDataSetChanged();
    }

    public List<T> getItems(){
        return mList;
    }

    public void removeItem(int position){
        mList.remove(position);
        notifyDataSetChanged();
    }
    public void clearAll(){
        mList.clear();
        notifyDataSetChanged();
    }
    //onCreateViewHolder用来给rv创建缓存
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //参数3 判断条件 true  1.打气 2.添加到paraent
        // false 1.打气 （参考parent的宽度）
        View view =   LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        BaseRecyclerViewHolder holder = new BaseRecyclerViewHolder(view);
        return holder;
    }
    //onBindViewHolder给缓存控件设置数据
    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        T item = mList.get(position);
        getView(holder, item,position);
        //点击事件
        holder.itemView.setTag(position);
        if (onRecyclerViewItemClickListener != null) {
            holder.itemView.setOnClickListener(this);
        }
        if (onRecyclerViewItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(this);
        }
        if (onSubViewClickListener != null) {
            holder.setSubViewClickListener(onSubViewClickListener,position);
        }
    }
    //给item内控件填充数据
    protected void getView(BaseRecyclerViewHolder holder, T item, int position) {
        //什么都没有做
    }

    //获取记录数据
    @Override
    public int getItemCount() {
        return mList.size();
    }


    @Override
    public void onClick(View view) {
        if (view.getTag() != null && onRecyclerViewItemClickListener!=null) {
            int position = (int) view.getTag();
            onRecyclerViewItemClickListener.onItemClick(position);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getTag() != null && onRecyclerViewItemLongClickListener!=null){
            int position = (int)view.getTag();
            onRecyclerViewItemLongClickListener.onItemLongClick(position);
        }
        return true;
    }


    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public void setOnRecyclerViewItemLongClickListener(OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener) {
        this.onRecyclerViewItemLongClickListener = onRecyclerViewItemLongClickListener;
    }

    public void setOnSubViewClickListener(OnSubViewClickListener listener){
        this.onSubViewClickListener = listener;
    }


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int position);
    }

    public interface OnSubViewClickListener{
        void onSubViewClick(View v, int position);
    }

    public interface OnRecyclerViewItemLongClickListener {
        void onItemLongClick(int position);
    }
}
