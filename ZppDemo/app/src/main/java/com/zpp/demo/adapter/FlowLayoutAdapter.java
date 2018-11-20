package com.zpp.demo.adapter;

import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;

import com.zpp.demo.R;
import com.zpp.demo.recycleview.BaseRecyclerViewAdapter;
import com.zpp.demo.recycleview.BaseRecyclerViewHolder;
import com.zpp.demo.bean.MainBean;

import java.util.List;

import com.zpp.tools.DensityUtils;

import static com.zpp.demo.base.MyApplication.mContext;

/**
 * Created by bstadmin on 2018/6/15.
 */

public class FlowLayoutAdapter extends BaseRecyclerViewAdapter {
    public FlowLayoutAdapter(int layoutId, List list) {
        super(layoutId, list);
    }
    private int i=0;
    private int[] colors={R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark};

    @Override
    protected void getView(BaseRecyclerViewHolder holder, Object item, int position) {
        MainBean bean= (MainBean) item;
        holder.setText(R.id.my_text,bean.getTitle());
        TextView view=holder.getView(R.id.my_text);

        view.setBackground(getDrawable(colors[i]));
        if(i>=2) {
            i = 0;
        }else {
            i++;
        }
    }


    private GradientDrawable getDrawable(int bgColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(bgColor);
        gradientDrawable.setCornerRadius(DensityUtils.dip2px(mContext, 4));
        return gradientDrawable;
    }
}
