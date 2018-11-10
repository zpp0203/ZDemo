package com.zpp.demo.bean;


/**
 * Created by 墨 on 2018/6/13.
 */

public class MainBean {
    public MainBean(){}

    public MainBean(String state) {
        this.state = state;
    }

    public MainBean(String title, String state) {
        this.title = title;
        this.state = state;
    }

    public MainBean(String title, String state, Class activity){
        this.title=title;
        this.state=state;
        this.activity=activity;
    }
    private String title;
    private String state;
    private Class activity;

    public Class getActivity() {
        return activity;
    }

    public void setActivity(Class activity) {
        this.activity = activity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
