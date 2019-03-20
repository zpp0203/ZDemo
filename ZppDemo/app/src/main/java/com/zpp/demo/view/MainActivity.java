package com.zpp.demo.view;

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

import com.zandroid.falldown.FallObject;
import com.zandroid.falldown.FallingView;
import com.zandroid.widget.XRecyclerView;
import com.zpp.demo.R;
import com.zpp.demo.adapter.RecycleAdapter;
import com.zpp.demo.base.BaseActivity;
import com.zpp.demo.bean.MainBean;
import com.zpp.demo.view.other.OtherMainActivity;
import com.zpp.demo.view.system.SystemMainActivity;
import com.zpp.demo.view.third.ThirdMainActivity;
import com.zandroid.recycleview.BaseRecyclerViewAdapter;
import com.zandroid.tools.LogUtils;
import com.zandroid.tools.TimeUtils;
import com.zandroid.tools.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import static com.zpp.demo.view.MainActivity.SendReceiver.ACTION_SEND;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private XRecyclerView recyclerView;
    private RecycleAdapter recycleAdapter;
    private List<MainBean> list;

    private TextView main_menu;
    private PopupMenu popup;

    FallingView fallingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        registerBoradcastReceiver();

        initMainMenu();

//        PackageManager packageManager = getPackageManager();
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("bluetooth://com.ycc.blue"));
//        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
//        boolean isValid = !activities.isEmpty();
//        if (isValid) {
//            Log.e("--","打开其他应用");
//            startActivity(intent);
//        }


        fallingView=findViewById(R.id.falling_view);
        //循环落花
        FallObject.Builder builder = new FallObject.Builder(getResources().getDrawable(R.drawable.ly_home_stars));
        FallObject fallObject = builder
                .setSpeed(7,true)
                .setSize(50,50,true)
                .setWind(5,true,true)
                .build();
        fallingView.addFallObject(fallObject,100);
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
        recyclerView=  findViewById(R.id.recycle);
        initData();
        recycleAdapter=new RecycleAdapter(R.layout.item_main_btn,list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this );
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recyclerView.setAdapter(recycleAdapter);
        recyclerView.setNoMore(true);
        //设置分隔线
        recyclerView.addItemDecoration(new DividerItemDecoration(this ,OrientationHelper.VERTICAL));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recycleAdapter.setOnRecyclerViewItemClickListener(new BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                LogUtils.e("点击事件"+position);
                startActivity(new Intent(MainActivity.this, list.get(position).getActivity()));

            }
        });

        recycleAdapter.setOnRecyclerViewItemLongClickListener(new BaseRecyclerViewAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                ToastUtils.showLong(mContext,"长按事件");
            }
        });
//需要在Adapter中设置
//        recycleAdapter.setOnSubViewClickListener(new BaseRecyclerViewAdapter.OnSubViewClickListener() {
//            @Override
//            public void onSubViewClick(View v, int position) {
//                LogUtils.e("子view点击事件"+position);
//                ToastUtils.showLong(mContext,"子view点击事件"+position);
//                switch (v.getId()){
//                    case R.id.title:
//                        ToastUtils.showLong(mContext,"title点击事件");
//                        break;
//                    case R.id.state:
//                        ToastUtils.showLong(mContext,"state点击事件");
//                        break;
//                }
//            }
//        });


        main_menu=findViewById(R.id.main_menu);
        main_menu.setOnClickListener(this);
    }

    private void initData() {
        list=new ArrayList<>();
        list.add(new MainBean("System","有关系统功能的",SystemMainActivity.class));
        list.add(new MainBean("Third","有关第三方的",ThirdMainActivity.class));
        list.add(new MainBean("Other","自定义的、封装其他的",OtherMainActivity.class));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btn_volley:
//                Intent intent = new Intent(MainActivity.this, VolleyDemoActivity.class);
//                startActivity(intent);
//                break;
            case R.id.main_menu:
                popup.show();
                break;
        }
    }

    private void registerBoradcastReceiver(){
        String datatime=TimeUtils.timestamp2String(System.currentTimeMillis(),"yyyy-MM-dd");
        Long duration=TimeUtils.String2Timestamp(datatime+" 16:33:00","yyyy-MM-dd HH:mm:ss");
        LogUtils.e(datatime +"---"+duration);
        //注册的方法
        Intent i = new Intent(ACTION_SEND);
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, duration, pi);
    }

    public class SendReceiver extends BroadcastReceiver {
        public final static String ACTION_SEND = "alarm_service";

        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_SEND.equals(action)) {
                Log.i("SendReceiver", "send a message");
            }
        }
    }

    private void initMainMenu(){
        popup = new PopupMenu(this, main_menu);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.main_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.main_menu_1:
                        ToastUtils.showLong(mContext,"main_menu_1");
                        return true;
                    case R.id.main_menu_2:
                        ToastUtils.showLong(mContext,"main_menu_2");
                        return true;
                    case R.id.main_menu_3:
                        ToastUtils.showLong(mContext,"main_menu_3");
                        return true;
                    case R.id.main_menu_4:
                        ToastUtils.showLong(mContext,"main_menu_4");
                        return true;
                    case R.id.main_menu_4_1:
                        ToastUtils.showLong(mContext,"main_menu_41");
                        return true;
                    default:
                        return onOptionsItemSelected(item);
                }
            }
        });
    }
}
