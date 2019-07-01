package com.zpp.demo.view.customView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.zandroid.recycleview.BaseRecyclerViewAdapter;
import com.zandroid.tools.LogUtils;
import com.zandroid.tools.ToastUtils;
import com.zandroid.widget.GuideView;
import com.zpp.demo.R;
import com.zpp.demo.adapter.RecycleAdapter;
import com.zpp.demo.base.BaseActivity;
import com.zpp.demo.bean.MainBean;
import com.zpp.demo.view.other.DateActivity;

import java.util.ArrayList;
import java.util.List;


public class CustomMainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RecycleAdapter recycleAdapter;
    private List<MainBean> list;

    private TextView main_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        showGuide();
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        recyclerView= (RecyclerView) findViewById(R.id.recycle);
        initData();
        recycleAdapter=new RecycleAdapter(R.layout.item_main_btn,list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this );
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recyclerView.setAdapter(recycleAdapter);
        //设置分隔线
        recyclerView.addItemDecoration(new DividerItemDecoration(this ,OrientationHelper.VERTICAL));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recycleAdapter.setOnRecyclerViewItemClickListener(new BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                LogUtils.e("点击事件"+position);
                startActivity(new Intent(CustomMainActivity.this, list.get(position).getActivity()));

            }
        });

        recycleAdapter.setOnRecyclerViewItemLongClickListener(new BaseRecyclerViewAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                ToastUtils.showLong(mContext,"长按事件");
            }
        });

        recycleAdapter.setOnSubViewClickListener(new BaseRecyclerViewAdapter.OnSubViewClickListener() {
            @Override
            public void onSubViewClick(View v, int position) {
                LogUtils.e("子view点击事件"+position);
                ToastUtils.showLong(mContext,"子view点击事件"+position);
                switch (v.getId()){
                    case R.id.title:
                        ToastUtils.showLong(mContext,"title点击事件");
                        break;
                    case R.id.state:
                        ToastUtils.showLong(mContext,"state点击事件");
                        break;
                }
            }
        });


        main_menu=findViewById(R.id.main_menu);
    }

    private void initData() {
        list=new ArrayList<>();
        list.add(new MainBean("自定义View Demo","自定义View 在设置了横竖屏不同布局时，设置android:screenOrientation=\"sensor\"而不设置android:configChanges",CustomViewActivity.class));
        list.add(new MainBean("时间选择器 Demo","时间选择器",DateActivity.class));
        list.add(new MainBean("自定义绘制","自定义绘制",MyShapeActivity.class));

    }


    public void showGuide(){
        GuideView guideView=new GuideView(this);
        guideView.setView(R.layout.guide_person)
                .addHightLight(main_menu,5)
                .setCancelTouchout(true)
                .show();
    }
}
