package com.zpp.demo.bean;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/3/26.
 * 商品信息
 */

public class GoodsInfo {
    private String id;
    private String name;
    private boolean isChoosed;
    private String imageUrl;
    private String desc;
    private String price;
    private String prime_price;
    private int postion;
    private int count;
    private String color;
    private String size;
    private String goodsImg;

    public GoodsInfo(String id, String name, String desc,String price, String prime_price,
                     String color, String size,int count) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.prime_price = prime_price;
        this.count = count;
        this.color = color;
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrime_price() {
        return prime_price;
    }

    public void setPrime_price(String prime_price) {
        this.prime_price = prime_price;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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
        final GoodsInfo goodsInfo = (GoodsInfo) object;
        if(!this.getId().equals(goodsInfo.getId()) )
            return false;
        if(!this.getName().equals(goodsInfo.getName()))
            return false;

        return true;
    }

}
