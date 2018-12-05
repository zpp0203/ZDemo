package com.zandroid.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by 墨 on 2018/6/13.
 */

//抽取BaseHolder继承RecyclerView.ViewHolder
public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private BaseRecyclerViewAdapter.OnSubViewClickListener onSubViewClickListener;

    //不写死控件变量，而采用Map方式
    private HashMap<Integer, View> mViews = new HashMap<>();
    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }
    /**
     *获取控件的方法
     */
    public<T> T getView(Integer viewId){
        //根据保存变量的类型 强转为该类型
        View view = mViews.get(viewId);
        if(view==null){
            view= itemView.findViewById(viewId);
            //缓存
            mViews.put(viewId,view);
        }
        return (T)view;
    }
    /**
     * 传入子项点击事件所需参数
     * @param listener 自定义的接口
     * @param tagPosition tag
     */
    public void setSubViewClickListener(BaseRecyclerViewAdapter.OnSubViewClickListener listener, int tagPosition){
        this.onSubViewClickListener = listener;

    }

    /**
     * 实现子项点击事件的转发
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getTag() != null && onSubViewClickListener!=null) {
            int position = (int) v.getTag();
            onSubViewClickListener.onSubViewClick(v,position);
        }
    }
    /**
     *传入点击事件
     */
    public BaseRecyclerViewHolder setOnClick(Integer viewId,int tag){
        View view=getView(viewId);
        view.setTag(tag);
        view.setOnClickListener(this);
        return this;
    }

    /**
     *传入文本控件id和设置的文本值，设置文本
     */
    public BaseRecyclerViewHolder setText(Integer viewId, String value){
        TextView textView = getView(viewId);
        if (textView != null) {
            textView.setText(value);
        }
        return this;
    }
    /**
     * 传入图片控件id和资源id，设置图片
     */
    public BaseRecyclerViewHolder setImageResource(Integer viewId, Integer resId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            imageView.setImageResource(resId);
        }
        return this;
    }
    //...还可以扩展出各种控件。
    //Fluent API 链式api  obj.setxxx().setyyy()....
}
