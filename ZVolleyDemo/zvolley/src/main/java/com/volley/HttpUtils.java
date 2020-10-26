package com.volley;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class HttpUtils {
    private static final String TAG = HttpUtils.class.getName();
    private int method;
    private static Map<String,String> params = new HashMap();


    private HttpUtils(){}
    private static HttpUtils instance;
    public static HttpUtils getInstance(){
        if(instance==null){
            synchronized (HttpUtils.class){
                if(instance==null){
                    instance=new HttpUtils();
                }
            }
        }
        params.clear();
        return instance;
    }

    public void httpRequest(String url, int method, final ZResponseListen responseListen){
        String mUrl=getUrl(url,method,getParams());
        StringRequest request = new StringRequest(method,mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                responseListen.onResponse(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                responseListen.onErrorResponse(volleyError);
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                return params;
            }
        };
        Log.e(TAG,mUrl);
        for(Map.Entry<String, String> entry : params.entrySet()){
            Log.e(TAG,entry.getKey()+":"+entry.getValue());
        }

        ZVolley.addRequest(request);
    }

    //单个文件上传
    public void httpRequest(String url, String filePartName, File file, final ZResponseListen responseListen){
        this.method=Request.Method.POST;
        String mUrl=getUrl(url,method,getParams());

        MultipartRequest request=new MultipartRequest(mUrl,new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                responseListen.onErrorResponse(volleyError);
            }
        },new Response.Listener<String>(){
            @Override
            public void onResponse(String str) {
                responseListen.onResponse(str);
            }
        },filePartName,file,params);
        Log.e(TAG,mUrl);
        for(Map.Entry<String, String> entry : params.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
        ZVolley.addRequest(request);
    }
    //多文件上传
    public void httpRequest(String url, String filePartName, List<File> files, final ZResponseListen responseListen){
        this.method=Request.Method.POST;
        String mUrl=getUrl(url,method,getParams());

        MultipartRequest request=new MultipartRequest(mUrl,new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                responseListen.onErrorResponse(volleyError);
            }
        },new Response.Listener<String>(){
            @Override
            public void onResponse(String str) {
                responseListen.onResponse(str);
            }
        },filePartName,files,params);
        Log.e(TAG,mUrl);
        for(Map.Entry<String, String> entry : params.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
        ZVolley.addRequest(request);
    }
    public void httpRequest(String url, final ZResponseListen responseListen){
        getParams();
        String mUrl=getUrl(url);
        StringRequest request = new StringRequest(mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                responseListen.onResponse(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                responseListen.onErrorResponse(volleyError);
            }
        });
        ZVolley.addRequest(request);
    }
    public void httpRequest(String url, Map<String,String> map, final ZResponseListen responseListen){
        getParams().putAll(map);
        int method=Request.Method.POST;
        String mUrl=getUrl(url,method,params);
        StringRequest request = new StringRequest(Request.Method.POST, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                responseListen.onResponse(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                responseListen.onErrorResponse(volleyError);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        Log.e(TAG,mUrl);
        for(Map.Entry<String, String> entry : params.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
        ZVolley.addRequest(request);
    }


    private boolean isUrlRequest(int method) {
        return this.params.size() > 0
                && (method == Request.Method.GET || method == Request.Method.HEAD || method == Request.Method.DELETE);
    }

    public void addParam(String key, String value){
        params.put(key,value);
    }
    public void addParams(Map params) {
        this.params.putAll(params);
    }
    public String getUrl(String url) {
        return getUrl(url,Request.Method.GET,null);
    }

    public String getUrl(String url, int method, Map params) {
        String mUrl=ZVolley.getBaseUrl()+url;
        if (isUrlRequest(method)) {//非post请求
            //接口后自带了 ?
            if (ZVolley.getBaseUrl()!=null && !mUrl.contains("?")) {
                mUrl += "?";
            }
            return mUrl += encodeParameters(params, "UTF-8");
        }
        return mUrl;
    }


    /**
     * Converts <code>params</code> 使用于URL参数格式化，或者body参数格式化
     */
    public String encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            int i=0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (TextUtils.isEmpty(entry.getKey()) || TextUtils.isEmpty(entry.getValue())) {
                    continue;
                }
                encodedParams.append(URLEncoder.encode(dealParamKey(entry.getKey()), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue().toString(), paramsEncoding));
                i++;
                if(i<params.size()) {
                    encodedParams.append('&');
                }
            }
            return encodedParams.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }
    /**
     * name[0]
     * to
     * name[]
     *
     * @param key
     * @return
     */
    private String dealParamKey(String key) {
        String content = key;
        String regex = "\\[[0-9]\\d*\\]$";

        Matcher matcher = Pattern.compile(regex).matcher(content);
        if (matcher.find()) {
            content = content.replace(matcher.group(), "[]");
        }
        return content;
    }

    private Map getParams() {
        params.clear();
        if(ZVolley.getPublicParam()!=null)
            params.putAll(ZVolley.getPublicParam());
        return params;
    }

}
