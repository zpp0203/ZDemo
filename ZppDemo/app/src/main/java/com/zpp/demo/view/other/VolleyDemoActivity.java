package com.zpp.demo.view.other;

import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zpp.demo.R;
import com.zpp.demo.bean.PersonInfoBean;
import com.zpp.demo.volley.ZResponseListen;
import com.zpp.demo.volley.FormImage;
import com.zpp.demo.volley.GsonRequest;
import com.zpp.demo.volley.VolleyUtils;
import com.zpp.demo.volley.ZErrorListener;
import com.zpp.demo.volley.ZSuccessListener;
import com.zpp.demo.volley.MyVolley;
import com.zpp.demo.volley.PostUploadRequest;
import com.zpp.demo.volley.XMLRequest;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zpp.demo.base.BaseActivity;

public class VolleyDemoActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_get;
    private Button btn_post;
    private Button btn_get2;
    private Button btn_post2;
    private Button btn_get3;
    private Button btn_post3;
    private Button btn_xml;
    private Button btn_image;
    private Button btn_uploadfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }
    @Override
    protected int setLayoutView() {
        return R.layout.activity_volley_demo;
    }
    private void initView() {
        btn_get = (Button) findViewById(R.id.btn_get);
        btn_post = (Button) findViewById(R.id.btn_post);
        btn_get2 = (Button) findViewById(R.id.btn_get2);
        btn_post2 = (Button) findViewById(R.id.btn_post2);
        btn_get3 = (Button) findViewById(R.id.btn_get3);
        btn_post3 = (Button) findViewById(R.id.btn_post3);
        btn_uploadfile = (Button) findViewById(R.id.btn_uploadfile);
        btn_xml = (Button) findViewById(R.id.btn_xml);
        btn_image = (Button) findViewById(R.id.btn_image);
        btn_get.setOnClickListener(this);
        btn_post.setOnClickListener(this);
        btn_get2.setOnClickListener(this);
        btn_post2.setOnClickListener(this);
        btn_get3.setOnClickListener(this);
        btn_post3.setOnClickListener(this);
        btn_xml.setOnClickListener(this);
        btn_image.setOnClickListener(this);
        btn_uploadfile.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get:
                getStringRequest();
                break;
            case R.id.btn_post:
                postStringRequest();
                break;
            case R.id.btn_get2:
                getJsonRequest();
                break;
            case R.id.btn_post2:
                postJsonRequest();
                break;
            case R.id.btn_uploadfile:
                uploadfile();
                break;
            case R.id.btn_get3:
                getMyVolley();
                break;
            case R.id.btn_post3:
                postMyVolley();
                break;
            case R.id.btn_xml:
                getXml();
                break;
            case R.id.btn_image:
                Intent intent = new Intent(VolleyDemoActivity.this, ImageLoaderActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getXml() {
        String url = "http://flash.weather.com.cn/wmaps/xml/china.xml";
        XMLRequest request = new XMLRequest(url, new Response.Listener<XmlPullParser>() {
            @Override
            public void onResponse(XmlPullParser xmlPullParser) {
                try {
                    int eventType = xmlPullParser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                String nodeName = xmlPullParser.getName();
                                if ("city".equals(nodeName)) {
                                    String pName = xmlPullParser.getAttributeValue(0);
                                    Log.e("TAG", "city is " + pName);
                                }
                                break;
                        }
                        eventType = xmlPullParser.next();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyVolley.addRequest(request);
    }

    private void postMyVolley() {
        String url = "http://api.k780.com:88/?app=idcard.get";
        Map<String, String> map = new HashMap<>();
        map.put("appkey", "10003");
        map.put("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4");
        map.put("format", "json");
        map.put("idcard", "110101199001011114");
        GsonRequest request = new GsonRequest(url, map, PersonInfoBean.class, new ZSuccessListener() {

            @Override
            public void onResponse(Object vo) {
                super.onResponse(vo);
                PersonInfoBean bean = (PersonInfoBean) vo;
                Log.e("sucess", bean.toString());
            }

        }, new ZErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }

        });
        MyVolley.addRequest(request);
    }

    private void getMyVolley() {
        String url = "http://api.k780.com:88/?app=idcard.get&idcard=110101199001011114&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
        GsonRequest request = new GsonRequest(url, PersonInfoBean.class, new ZSuccessListener() {

            @Override
            public void onResponse(Object t) {
                super.onResponse(t);
                PersonInfoBean bean = (PersonInfoBean) t;
                Log.e("success", bean.toString());
            }

        }, new ZErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }

        });
        MyVolley.addRequest(request);
    }

    /**
     * 上传文件
     */
    private void uploadfile() {
        //上传文件路径
        String url = "http://192.168.1.107:8080/FileUpload/FileServlet";
        List<FormImage> list = new ArrayList<>();
        String path1 = Environment.getExternalStorageDirectory().getPath() + File.separator + "Download" + File.separator + "ss.png";
        String path2 = Environment.getExternalStorageDirectory().getPath() + File.separator + "Download" + File.separator + "ic_launcher.png";
        File file1 = new File(path1);
        File file2 = new File(path2);

        FormImage f1 = new FormImage();
        f1.setFile(file1);
        f1.setFileName("t1");
        f1.setName("file1");
        f1.setMime("image/png");
        list.add(f1);

        FormImage f2 = new FormImage();
        f2.setFile(file2);
        f2.setFileName("t2");
        f2.setName("file2");
        f2.setMime("image/png");
        list.add(f2);


        PostUploadRequest request = new PostUploadRequest(url, list, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("success", s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyVolley.addRequest(request);

    }

    private void postJsonRequest() {
        String url = "http://api.k780.com:88/?app=phone.get";
        Map<String, String> map = new HashMap<>();
        map.put("phone", "13800138000");
        map.put("appkey", "10003");
        map.put("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4");
        map.put("format", "json");
        map.put("idcard", "110101199001011114");
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject params=new JSONObject(map);
        JsonObjectRequest request = new JsonObjectRequest(url,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("success", jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) ;
        queue.add(request);
    }

    /**
     * 通过JsonRequest发送get请求
     */
    private void getJsonRequest() {
        String url = "http://api.k780.com:88/?app=phone.get&phone=13800138000&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(url, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("success", jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
    }


    /**
     * 通过StringRequest发送post请求
     */
    private void postStringRequest() {
        String url = "http://api.k780.com:88/?app=phone.get";
        Map<String, String> map = new HashMap<>();
        map.put("phone", "13800138000");
        map.put("appkey", "10003");
        map.put("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4");
        map.put("format", "json");
        map.put("idcard", "110101199001011114");

        VolleyUtils.getInstance().httpRequest(url,map, new ZResponseListen() {
            @Override
            protected void successListener(Object object) {

            }

            @Override
            protected void errorListener(int code, String msg) {

            }
        });



//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                Log.e("success", s);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        }){
//            // 需要重写获取参数的函数,可以向服务器提交参数
//            protected Map<String,String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<>();
//                map.put("phone", "13800138000");
//                map.put("appkey", "10003");
//                map.put("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4");
//                map.put("format", "json");
//                map.put("idcard", "110101199001011114");
//                return map;
//            }
//        };
//        queue.add(request);
    }

    /**
     * 通过StringRequest发送get请求
     */
    private void getStringRequest() {
        String url = "https://www.jianshu.com/p/6ce99e03080f";
        VolleyUtils.getInstance().httpRequest(url, new ZResponseListen() {
            @Override
            protected void successListener(Object object) {

            }

            @Override
            protected void errorListener(int code, String msg) {

            }
        });
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                Log.e("success", s);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });
//        queue.add(request);
    }


}
