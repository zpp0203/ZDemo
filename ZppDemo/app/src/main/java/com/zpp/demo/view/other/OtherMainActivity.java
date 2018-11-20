package com.zpp.demo.view.other;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.zpp.demo.R;
import com.zpp.demo.adapter.RecycleAdapter;
import com.zpp.demo.base.BaseActivity;
import com.zpp.demo.bean.MainBean;
import com.zpp.demo.recycleview.BaseRecyclerViewAdapter;
import com.zpp.tools.ActivityUtils;
import com.zpp.tools.AppUtils;
import com.zpp.tools.LogUtils;
import com.zpp.tools.TimeUtils;
import com.zpp.tools.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import static com.zpp.demo.view.MainActivity.SendReceiver.ACTION_SEND;


public class OtherMainActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecycleAdapter recycleAdapter;
    private List<MainBean> list;

    private TextView main_menu;
    private PopupMenu popup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();


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
                startActivity(new Intent(OtherMainActivity.this, list.get(position).getActivity()));

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
        main_menu.setOnClickListener(this);
        main_menu.setVisibility(View.GONE);
    }

    private void initData() {
        list=new ArrayList<>();
        list.add(new MainBean("Volley Demo","volley 使用方法",VolleyDemoActivity.class));
        list.add(new MainBean("RecycleView Demo","RecycleView的布局设置等",RecycleViewDemoActivity.class));
        list.add(new MainBean("购物车 Demo","防淘宝购物车",ShoppingCartActivity.class));
        list.add(new MainBean("自定义View Demo","自定义View 在设置了横竖屏不同布局时，设置android:screenOrientation=\"sensor\"而不设置android:configChanges",CustomViewActivity.class));
        list.add(new MainBean("TCP Demo","TCP连接",TCPActivity.class));
        list.add(new MainBean("动画轨迹 Demo","动画轨迹",ValueAnimatorActivity.class));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_menu:
                popup.show();
                break;
        }
    }

}
