package com.zpp.demo.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zpp.demo.R;
import com.zpp.demo.adapter.ShopcatAdapter;
import com.zpp.demo.base.BaseActivity;
import com.zpp.demo.bean.GoodsInfo;
import com.zpp.demo.bean.StoreInfo;

import java.util.ArrayList;
import java.util.List;

import com.zpp.tools.LogUtils;
import com.zpp.tools.ToastUtils;


public class ShoppingCartActivity extends BaseActivity implements View.OnClickListener, ShopcatAdapter.CheckInterface, ShopcatAdapter.ModifyCountInterface, ShopcatAdapter.GroupEditorListener {

    ExpandableListView listView;

    CheckBox allCheckBox;
    TextView totalPrice;
    TextView goPay;
    LinearLayout orderInfo;
    TextView shareGoods;
    TextView collectGoods;
    TextView delGoods;
    LinearLayout shareInfo;
    LinearLayout llCart;


    TextView shoppingcatNum;
    Button actionBarEdit;
    LinearLayout empty_shopcart;
    private double mtotalPrice = 0.00;
    private int mtotalCount = 0;
    //false就是编辑，ture就是完成
    private boolean flag = false;
    private ShopcatAdapter adapter;

    private List<StoreInfo> storeInfos;
//    private List<StoreInfo> groups; //组元素的列表
//    private Map<String, List<GoodsInfo>> childs; //子元素的列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvents();
    }
    @Override
    protected int setLayoutView() {
        return R.layout.activity_shopping_cart;
    }
    private void initView() {
        listView= (ExpandableListView) findViewById(R.id.listView);
        allCheckBox= (CheckBox) findViewById(R.id.all_checkBox);
        allCheckBox.setOnClickListener(this);
        totalPrice= (TextView) findViewById(R.id.total_price);
        goPay= (TextView) findViewById(R.id.go_pay);
        goPay.setOnClickListener(this);
        orderInfo= (LinearLayout) findViewById(R.id.order_info);
        shareGoods= (TextView) findViewById(R.id.share_goods);
        shareGoods.setOnClickListener(this);
        collectGoods= (TextView) findViewById(R.id.collect_goods);
        collectGoods.setOnClickListener(this);
        delGoods= (TextView) findViewById(R.id.del_goods);
        delGoods.setOnClickListener(this);
        shareInfo= (LinearLayout) findViewById(R.id.share_info);
        llCart= (LinearLayout) findViewById(R.id.ll_cart);

        shoppingcatNum = (TextView) findViewById(R.id.shoppingcat_num);
        actionBarEdit = (Button) findViewById(R.id.actionBar_edit);
        actionBarEdit.setOnClickListener(this);
    }


    private void initEvents() {

        adapter = new ShopcatAdapter(storeInfos, mContext);
        adapter.setCheckInterface(this);//关键步骤1：设置复选框的接口
        adapter.setModifyCountInterface(this); //关键步骤2:设置增删减的接口
        adapter.setGroupEditorListener(this);//关键步骤3:监听组列表的编辑状态
        listView.setGroupIndicator(null); //设置属性 GroupIndicator 去掉向下箭头
        listView.setAdapter(adapter);
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            listView.expandGroup(i); //关键步骤4:初始化，将ExpandableListView以展开的方式显示
        }
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int firstVisiablePostion=view.getFirstVisiblePosition();
                int top=-1;
                View firstView=view.getChildAt(firstVisibleItem);
                LogUtils.i("childCount="+view.getChildCount());//返回的是显示层面上的所包含的子view的个数
                if(firstView!=null){
                    top=firstView.getTop();
                }
                LogUtils.i("firstVisiableItem="+firstVisibleItem+",fistVisiablePosition="+firstVisiablePostion+",firstView="+firstView+",top="+top);
                if(firstVisibleItem==0&&top==0){

                }else{

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartNum();
    }

    /**
     * 设置购物车的数量
     */
    private void setCartNum() {
        int count = 0;
        for (int i = 0; i < storeInfos.size(); i++) {
            StoreInfo group = storeInfos.get(i);
            group.setChoosed(allCheckBox.isChecked());
            List<GoodsInfo> Childs = storeInfos.get(i).getProducts();
            for (GoodsInfo childs : Childs) {
                count++;
            }
        }

        //购物车已经清空
        if (count == 0) {
            clearCart();
        } else {
            shoppingcatNum.setText("购物车(" + count + ")");
        }

    }

    private void clearCart() {
        shoppingcatNum.setText("购物车(0)");
        actionBarEdit.setVisibility(View.GONE);
        llCart.setVisibility(View.GONE);
        empty_shopcart.setVisibility(View.VISIBLE);//这里发生过错误
    }

    /**
     * 模拟数据<br>
     * 遵循适配器的数据列表填充原则，组元素被放在一个list中，对应着组元素的下辖子元素被放在Map中
     * 其Key是组元素的Id
     */
    private void initData() {
        storeInfos=new ArrayList<>();
        StoreInfo storeInfo=new StoreInfo("1","一号店铺");
        List<GoodsInfo> goods = new ArrayList<>();
        for (int j = 0; j <= 2; j++) {
            //i-j 就是商品的id， 对应着第几个店铺的第几个商品，1-1 就是第一个店铺的第一个商品
            goods.add(new GoodsInfo(j+"", "商品", storeInfo.getName() + "的第" + (j + 1) + "个商品", "22.22", "33.33", "第一排", "出头天者", 1));
        }
        storeInfo.setProducts(goods);
        storeInfos.add(storeInfo);

        StoreInfo storeInfo1=new StoreInfo("2","二号店铺");
        List<GoodsInfo> goods1 = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            //i-j 就是商品的id， 对应着第几个店铺的第几个商品，1-1 就是第一个店铺的第一个商品
            goods1.add(new GoodsInfo(i + "", "商品", storeInfo1.getName() + "的第" + (i + 1) + "个商品", "22.22", "33.33", "第一排", "出头天者", 1));
        }
        storeInfo1.setProducts(goods1);
        storeInfos.add(storeInfo1);

        //都不存在
        StoreInfo storeInfo2=new StoreInfo("3","三号店铺");
        GoodsInfo goodsInfo2=new GoodsInfo(2 +"123", "添加商品", storeInfo2.getName() + "的第" + (2323 + 1) + "个商品", "22.22", "33.33", "第一排", "出头天者", 1);
        addToCart(storeInfo2,goodsInfo2);
        //存在店铺，不存在商品
        StoreInfo storeInfo3=new StoreInfo("1","一号店铺");
        GoodsInfo goodsInfo3=new GoodsInfo(2 +"1433", "添加商品", storeInfo3.getName() + "的第" + (2323 + 1) + "个商品", "22.22", "33.33", "第一排", "出头天者", 1);
        addToCart(storeInfo3,goodsInfo3);
        //都存在
        StoreInfo storeInfo4=new StoreInfo("2","二号店铺");
        GoodsInfo goodsInfo4=new GoodsInfo(0 + "", "商品", storeInfo4.getName() + "的第" + 1 + "个商品", "22.22", "33.33", "第一排", "出头天者", 1);
        addToCart(storeInfo4,goodsInfo4);
    }
    private void addToCart(StoreInfo storeInfo,GoodsInfo goodsInfo){
        int[] options=isAdd(storeInfo,goodsInfo);
        if(options[0]==-1){//购物车里没有该店铺
            List<GoodsInfo> goods = new ArrayList<>();
            goods.add(goodsInfo);
            storeInfo.setProducts(goods);
            storeInfos.add(storeInfo);
        }else if(options[0]!=-1 && options[1]==-1){//存在店铺但没有该商品
            StoreInfo stf=storeInfos.get(options[0]);
            stf.getProducts().add(goodsInfo);
        }else {
            StoreInfo stf=storeInfos.get(options[0]);
            GoodsInfo gif=stf.getProducts().get(options[1]);
            gif.setCount(gif.getCount()+goodsInfo.getCount());
        }
    }
    private int[] isAdd(StoreInfo storeInfo,GoodsInfo goodsInfo){
        int[] options={-1,-1};
        if(storeInfos.contains(storeInfo)){
            for(int i=0;i<storeInfos.size();i++) {
                StoreInfo storeInfo1=storeInfos.get(i);
                if (storeInfo1.equals(storeInfo)) {
                    Log.e("TAG", "已有店铺 " + storeInfo.getName());
                    options[0]=i;
                    List<GoodsInfo> goods=storeInfo1.getProducts();
                    if (goods.contains(goodsInfo)) {
                        for (int j = 0; j < goods.size(); j++) {
                            GoodsInfo goodsInfo1 = goods.get(j);
                            if (goodsInfo1.equals(goodsInfo)) {
                                Log.e("TAG", "已有商品 " + storeInfo.getName());
                                options[1] = j;
                                break;
                            }
                        }
                    } else {
                        Log.e("TAG", "么有 " + goodsInfo.getName());
                    }
                    break;
                }
            }
        } else {
            Log.e("TAG", "么有 " + storeInfo.getName());
        }

        return options;
    }
    /**
     * 删除操作
     * 1.不要边遍历边删除,容易出现数组越界的情况
     * 2.把将要删除的对象放进相应的容器中，待遍历完，用removeAll的方式进行删除
     */
    private void doDelete() {
        List<StoreInfo> toBeDeleteGroups = new ArrayList<StoreInfo>(); //待删除的组元素
        for (int i = 0; i < storeInfos.size(); i++) {
            StoreInfo group = storeInfos.get(i);
            if (group.isChoosed()) {
                toBeDeleteGroups.add(group);
            }
            List<GoodsInfo> toBeDeleteChilds = new ArrayList<GoodsInfo>();//待删除的子元素
            List<GoodsInfo> child = storeInfos.get(i).getProducts();
            for (int j = 0; j < child.size(); j++) {
                if (child.get(j).isChoosed()) {
                    toBeDeleteChilds.add(child.get(j));
                }
            }
            child.removeAll(toBeDeleteChilds);
        }
        storeInfos.removeAll(toBeDeleteGroups);
        //重新设置购物车
        setCartNum();
        adapter.notifyDataSetChanged();

    }


    /**
     * @param groupPosition 组元素的位置
     * @param isChecked     组元素的选中与否
     *                      思路:组元素被选中了，那么下辖全部的子元素也被选中
     */
    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        StoreInfo group = storeInfos.get(groupPosition);
        List<GoodsInfo> child = group.getProducts();
        for (int i = 0; i < child.size(); i++) {
            child.get(i).setChoosed(isChecked);
        }
        if (isCheckAll()) {
            allCheckBox.setChecked(true);//全选
        } else {
            allCheckBox.setChecked(false);//反选
        }
        adapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * @return 判断组元素是否全选
     */
    private boolean isCheckAll() {
        for (StoreInfo group : storeInfos) {
            if (!group.isChoosed()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param groupPosition 组元素的位置
     * @param childPosition 子元素的位置
     * @param isChecked     子元素的选中与否
     */
    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        boolean allChildSameState = true; //判断该组下面的所有子元素是否处于同一状态
        StoreInfo group = storeInfos.get(groupPosition);
        List<GoodsInfo> child = group.getProducts();
        for (int i = 0; i < child.size(); i++) {
            //不选全中
            if (child.get(i).isChoosed() != isChecked) {
                allChildSameState = false;
                break;
            }
        }

        if (allChildSameState) {
            group.setChoosed(isChecked);//如果子元素状态相同，那么对应的组元素也设置成这一种的同一状态
        } else {
            group.setChoosed(false);//否则一律视为未选中
        }

        if (isCheckAll()) {
            allCheckBox.setChecked(true);//全选
        } else {
            allCheckBox.setChecked(false);//反选
        }

        adapter.notifyDataSetChanged();
        calulate();

    }

    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        GoodsInfo good = (GoodsInfo) adapter.getChild(groupPosition, childPosition);
        int count = good.getCount();
        count++;
        good.setCount(count);
        ((TextView) showCountView).setText(String.valueOf(count));
        adapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * @param groupPosition
     * @param childPosition
     * @param showCountView
     * @param isChecked
     */
    @Override
    public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        GoodsInfo good = (GoodsInfo) adapter.getChild(groupPosition, childPosition);
        int count = good.getCount();
        if (count == 1) {
            return;
        }
        count--;
        good.setCount(count);
        ((TextView) showCountView).setText("" + count);
        adapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * @param groupPosition
     * @param childPosition 思路:当子元素=0，那么组元素也要删除
     */
    @Override
    public void childDelete(int groupPosition, int childPosition) {
        StoreInfo group = storeInfos.get(groupPosition);
        List<GoodsInfo> child = group.getProducts();
        child.remove(childPosition);
        if (child.size() == 0) {
            storeInfos.remove(groupPosition);
        }
        adapter.notifyDataSetChanged();
        calulate();


    }

    public void doUpdate(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        GoodsInfo good = (GoodsInfo) adapter.getChild(groupPosition, childPosition);
        int count = good.getCount();
        LogUtils.i("进行更新数据，数量" + count + "");
        ((TextView) showCountView).setText(String.valueOf(count));
        adapter.notifyDataSetChanged();
        calulate();


    }

    @Override
    public void groupEditor(int groupPosition) {

    }

    public void onClick(View view) {
        AlertDialog dialog;
        int i = view.getId();
        if (i == R.id.all_checkBox) {
            doCheckAll();

        } else if (i == R.id.go_pay) {
            if (mtotalCount == 0) {
                ToastUtils.showLong(mContext, "请选择要支付的商品");
                return;
            }
            dialog = new AlertDialog.Builder(mContext).create();
            dialog.setMessage("总计:" + mtotalCount + "种商品，" + mtotalPrice + "元");
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "支付", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            dialog.show();

        } else if (i == R.id.share_goods) {
            if (mtotalCount == 0) {
                ToastUtils.showLong(mContext, "请选择要分享的商品");
                return;
            }
            ToastUtils.showLong(mContext, "分享成功");

        } else if (i == R.id.collect_goods) {
            if (mtotalCount == 0) {
                ToastUtils.showLong(mContext, "请选择要收藏的商品");
                return;
            }
            ToastUtils.showLong(mContext, "收藏成功");

        } else if (i == R.id.del_goods) {
            if (mtotalCount == 0) {
                ToastUtils.showLong(mContext, "请选择要删除的商品");
                return;
            }
            dialog = new AlertDialog.Builder(mContext).create();
            dialog.setMessage("确认要删除该商品吗?");
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doDelete();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            dialog.show();

        } else if (i == R.id.actionBar_edit) {
            flag = !flag;
            setActionBarEditor();
            setVisiable();

        }
    }

    /**
     * ActionBar标题上点编辑的时候，只显示每一个店铺的商品修改界面
     * ActionBar标题上点完成的时候，只显示每一个店铺的商品信息界面
     */
    private void setActionBarEditor() {
        for (int i = 0; i < storeInfos.size(); i++) {
            StoreInfo group = storeInfos.get(i);
            if (group.isActionBarEditor()) {
                group.setActionBarEditor(false);
            } else {
                group.setActionBarEditor(true);
            }
        }
        adapter.notifyDataSetChanged();
    }


    /**
     * 全选和反选
     * 错误标记：在这里出现过错误
     */
    private void doCheckAll() {
        for (int i = 0; i < storeInfos.size(); i++) {
            StoreInfo group = storeInfos.get(i);
            group.setChoosed(allCheckBox.isChecked());
            List<GoodsInfo> child = group.getProducts();
            for (int j = 0; j < child.size(); j++) {
                child.get(j).setChoosed(allCheckBox.isChecked());//这里出现过错误
            }
        }
        adapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * 计算商品总价格，操作步骤
     * 1.先清空全局计价,计数
     * 2.遍历所有的子元素，只要是被选中的，就进行相关的计算操作
     * 3.给textView填充数据
     */
    private void calulate() {
        mtotalPrice = 0.00;
        mtotalCount = 0;
        for (int i = 0; i < storeInfos.size(); i++) {
            StoreInfo group = storeInfos.get(i);
            List<GoodsInfo> child = group.getProducts();
            for (int j = 0; j < child.size(); j++) {
                GoodsInfo good = child.get(j);
                if (good.isChoosed()) {
                    mtotalCount++;
                    mtotalPrice += Double.parseDouble(good.getPrice()) * good.getCount();
                }
            }
        }
        totalPrice.setText("￥" + mtotalPrice + "");
        goPay.setText("去支付(" + mtotalCount + ")");
        if (mtotalCount == 0) {
            setCartNum();
        } else {
            shoppingcatNum.setText("购物车(" + mtotalCount + ")");
        }


    }

    private void setVisiable() {
        if (flag) {
            orderInfo.setVisibility(View.GONE);
            shareInfo.setVisibility(View.VISIBLE);
            actionBarEdit.setText("完成");
        } else {
            orderInfo.setVisibility(View.VISIBLE);
            shareInfo.setVisibility(View.GONE);
            actionBarEdit.setText("编辑");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter = null;
        storeInfos.clear();
        mtotalPrice = 0.00;
        mtotalCount = 0;
    }

}
