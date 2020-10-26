package com.volley;

/**
 * Created by 墨 on 2018/6/11.
 */

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


/**
 * 管理类
 *
 * @author //初始化MyVolley
 *         ZVolley.init(getApplicationContext());
 */
public class ZVolley {
    private static ZVolley instance;
    //请求队列
    public static RequestQueue mRequestQueue;
    //创建ImageLoader
    private static ImageLoader mImageLoader;
    //默认分配最大空间的几分之几
    private final static int RATE = 8;

    private static String baseUrl;//服务器地址
    private static Map publicParam;//公共参数

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    private ZVolley(Context context) {
        //初始化请求队列(默认创建5个线程)
        mRequestQueue = Volley.newRequestQueue(context);
        //获取ActivityManager管理者
        @SuppressLint("WrongConstant") ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int maxSize = manager.getMemoryClass() / RATE;
        //初始化ImageLoader对象
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(1024 * 1024 * maxSize));
        Log.e("TAG", "MyVolley初始化完成");

    }

    /**
     * Volley的初始化操作，使用volley前必须调用此方法
     */
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    public static ZVolley init(Context context) {
        if (instance == null) {
            instance = new ZVolley(context);
        }
        return instance;
    }

    /**
     * 获取消息队列
     */
    public static RequestQueue getRequestQueue() {
        throwIfNotInit();
        return mRequestQueue;
    }

    /**
     * 获取ImageLoader
     */
    public static ImageLoader getImageLoader() {
        throwIfNotInit();
        return mImageLoader;
    }

    /**
     * 加入请求队列
     */
    public static void addRequest(Request<?> request) {
//        参数一DEFAULT_TIMEOUT_MS是超时时间，时间应该设置的稍微大一点
//        参数二DEFAULT_MAX_RETRIES，超时后重复请求的次数，如果不希望有2次请求的情况，则设置为0
//        参数三DEFAULT_BACKOFF_MULT是backoff因子，对于请求失败之后的请求，并不会隔相同的时间去请求Server，不会以线性的时间增长去请求，而是一个曲线增长，一次比一次长，如果backoff因子是2，当前超时为3，即下次再请求隔6S。
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 0, 0f));
        request.setShouldCache(false);
        getRequestQueue().add(request);
    }



    /**
     * 检查是否完成初始化
     */
    private static void throwIfNotInit() {
        if (instance == null) {
            throw new IllegalStateException("MyVolley尚未初始化，在使用前应该执行init()");
        }
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public ZVolley setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }
    public ZVolley addParam(String key,String value) {
        if(publicParam==null)
            publicParam=new HashMap();
        publicParam.put(key,value);
        return this;
    }

    public static Map getPublicParam(){
        return publicParam;
    }

}
