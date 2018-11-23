package com.zpp.demo.view.other;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.zpp.demo.R;
import com.zpp.demo.adapter.FlowLayoutAdapter;
import com.zpp.demo.base.BaseActivity;
import com.zpp.recycleview.BaseRecyclerViewAdapter;
import com.zpp.recycleview.FlowLayoutManager;
import com.zpp.demo.bean.MainBean;
import com.zpp.recycleview.GridDividerItemDecoration;
import com.zpp.tools.LogUtils;
import com.zpp.tools.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

public class RecycleViewDemoActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private FlowLayoutAdapter recycleAdapter;
    private List<MainBean> list;

    @Bind(R.id.grid_recycle)
    RecyclerView gridRecyclerView;
    ItemTouchHelper itemTouchHelper;//用于处理拖拽、侧滑删除等功能的
    @Override
    protected int setLayoutView() {
        return R.layout.activity_recycle_view_demo;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        flowLayout();
        gridLayout();
    }
    int fromPosition,toPosition;
    private void gridLayout() {
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        gridRecyclerView.setAdapter(recycleAdapter);
        gridRecyclerView.setLayoutManager(gridLayoutManager);
        //设置分隔线
        GridDividerItemDecoration gridDividerItemDecoration=new GridDividerItemDecoration(this);

        gridRecyclerView.addItemDecoration(gridDividerItemDecoration);

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback(){
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                //设置移动方式
                final int dragFlags;
                final int swipeFlags;
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    swipeFlags = 0;
                } else {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                }
                return makeMovementFlags(dragFlags, swipeFlags);

            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                //移动过程中调用
//                int fromPosition = viewHolder.getAdapterPosition();
//                int toPosition = viewHolder1.getAdapterPosition();
//                list.add(toPosition, list.remove(fromPosition));
//                recycleAdapter.notifyItemMoved(fromPosition, toPosition);


                fromPosition = viewHolder.getAdapterPosition();
                toPosition = viewHolder1.getAdapterPosition();

                return false;
            }
            //侧滑过程中调用
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//                int position = viewHolder.getAdapterPosition();
//                recycleAdapter.notifyItemRemoved(position);
//                list.remove(position);
            }

            //当长按的时候调用
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            //当手指松开的时候调用
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundResource(0);

                LogUtils.e(fromPosition+"--"+toPosition);
                // 交换在list中的指定位置的元素
                Collections.swap(list, fromPosition, toPosition);
                recycleAdapter.notifyDataSetChanged();

            }

        });
        itemTouchHelper.attachToRecyclerView(gridRecyclerView);
    }

    private void flowLayout() {
        recyclerView= (RecyclerView) findViewById(R.id.recycle);
        recycleAdapter=new FlowLayoutAdapter(R.layout.item_flowlayout,list);
        recycleAdapter.setOnRecyclerViewItemClickListener(new BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                LogUtils.e("点击："+position);
                ToastUtils.showLong(mContext,"点击："+position);
            }
        });
        //流式布局管理器
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL);
        FlowLayoutManager layoutManager = new FlowLayoutManager();
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);

        //设置Adapter
        recyclerView.setAdapter(recycleAdapter);

//        //设置分隔线
        recyclerView.addItemDecoration(new SpaceItemDecoration(5));

        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initData() {
        list=new ArrayList<>();
        list.add(new MainBean("测试11","volley 使用方法"));
        list.add(new MainBean("测试11111","微信分享"));
        list.add(new MainBean("测试22222222222","RecycleView的布局设置等"));
        list.add(new MainBean("测试5555555","RecycleView的布局设置等"));
        list.add(new MainBean("测试3333","RecycleView的布局设置等"));
        list.add(new MainBean("测试5ewwfsdfs5555555","RecycleView的布局设置等"));
    }

    //设置分割线
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;
        public SpaceItemDecoration(int space) {
            this.space = space;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if(parent.getChildAdapterPosition(view) > 0) {
                //从第二个条目开始，距离上方Item的距离
                outRect.top = space;
            }

        }

    }

}
