package com.zpp.demo.view.other;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zandroid.recycleview.expand.SectionedSpanSizeLookup;
import com.zandroid.tools.FileUtils;
import com.zpp.demo.R;
import com.zpp.demo.adapter.HotelEntityAdapter;
import com.zpp.demo.base.BaseActivity;
import com.zpp.demo.bean.HotelEntity;

import butterknife.Bind;

/**
 * 两级列表的实现
* */
public class RecycleExpandActivity extends BaseActivity {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Override
    protected int setLayoutView() {
        return R.layout.activity_recycle_expand;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        HotelEntityAdapter mAdapter = new HotelEntityAdapter(this);
        //GridLayoutManager manager = new GridLayoutManager(this,4);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        //设置header
        //manager.setSpanSizeLookup(new SectionedSpanSizeLookup(mAdapter,manager));
        //mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        HotelEntity entity = analysisJsonFile(this,"json");
        mAdapter.setData(entity.allTagsList);
        mAdapter.setOnItemClick(new HotelEntityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int section, int position) {
                Toast.makeText(mContext,section+"=="+position,Toast.LENGTH_SHORT).show();

            }
        });
    }

    public  HotelEntity analysisJsonFile(Context context, String fileName){
        String content = FileUtils.readJsonFile(context,fileName);
        Gson gson = new Gson();
        HotelEntity entity = gson.fromJson(content, HotelEntity.class);
        return  entity;

    }

}
