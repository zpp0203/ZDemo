package com.zpp.base.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 用户实体类
 *
 * @author ZhongDaFeng
 */

public class UserBean implements Serializable {
    //token	string	用户登录生成的token
    //uid	string	用户Id
    @SerializedName("token")
    private String token;
    @SerializedName("uid")
    private String uid;
    private String time;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
