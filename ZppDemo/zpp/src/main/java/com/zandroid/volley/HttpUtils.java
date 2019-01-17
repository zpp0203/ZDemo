package com.zandroid.volley;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HttpUtils {
    private static final String TAG = HttpUtils.class.getName();
    private int method;
    private String url;
    private Map params = new HashMap();

    private ZResponseListen baseResponseListen;

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
        return instance;
    }


    public void httpRequest(String url,ZResponseListen responseListen){
        this.method=Request.Method.GET;
        this.url=url;
        this.baseResponseListen=responseListen;
        StringRequest request = new StringRequest(getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                baseResponseListen.onResponse(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                baseResponseListen.onErrorResponse(volleyError);
            }
        });
        MyVolley.mRequestQueue.add(request);
    }
    public void httpRequest(String url, Map map, ZResponseListen responseListen){
        this.method=Request.Method.POST;
        this.url=url;
        this.params.putAll(map);
        this.baseResponseListen=responseListen;
        StringRequest request = new StringRequest(Request.Method.POST, getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                baseResponseListen.onResponse(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                baseResponseListen.onErrorResponse(volleyError);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return getmParams();
            }
        };
        MyVolley.mRequestQueue.add(request);
    }


    public Map getmParams() {
        return params;
    }

    private boolean isUrlRequest() {
        return this.params.size() > 0
                && (getMethod() == Request.Method.GET || getMethod() == Request.Method.HEAD || getMethod() == Request.Method.DELETE);
    }

    public void addParams(Map params) {
        this.params.putAll(params);
    }

    public String getUrl() {
        if (isUrlRequest()) {//非post请求
            //接口后自带了 ?
            if (!url.contains("?")) {
                url += "?";
            }
            return url += encodeParameters(getmParams(), "UTF-8");
        }

        return url;
    }

    public int getMethod() {
        return method;
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
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                i++;
                if(i<params.size()) {
                    encodedParams.append('&');
                }
            }
            Log.d(TAG, encodedParams.toString());
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
        System.out.println(content);

        Matcher matcher = Pattern.compile(regex).matcher(content);
        if (matcher.find()) {
            content = content.replace(matcher.group(), "[]");
        }

        return content;
    }
}
