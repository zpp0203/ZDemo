package com.zpp.demo.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/26.
 * 店铺信息
 */

public class StoreInfo {
    private String id;
    private String name;
    private boolean isChoosed;
    private boolean isEditor; //自己对该组的编辑状态
    private boolean ActionBarEditor;// 全局对该组的编辑状态
    private int flag;

    private List<GoodsInfo> products;

    public List<GoodsInfo> getProducts() {
        return products;
    }

    public void setProducts(List<GoodsInfo> products) {
        this.products = products;
    }

    public StoreInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public boolean isEditor() {
        return isEditor;
    }

    public void setEditor(boolean editor) {
        isEditor = editor;
    }
    public boolean isActionBarEditor() {
        return ActionBarEditor;
    }

    public void setActionBarEditor(boolean actionBarEditor) {
        ActionBarEditor = actionBarEditor;
    }


    @Override
    public int hashCode() {
        String code=this.id+this.getName();
        return code.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if(this == object)
            return true;
        if(object == null)
            return false;
        if(this.getClass() != object.getClass())
            return false;
        final StoreInfo storeInfo = (StoreInfo) object;
        if(!this.getId().equals(storeInfo.getId()))
            return false;
        if(!this.getName().equals(storeInfo.getName()))
            return false;

        return true;
    }
}
