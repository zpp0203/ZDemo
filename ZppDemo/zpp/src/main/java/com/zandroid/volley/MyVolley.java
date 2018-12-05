package com.zandroid.volley;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


/**
 * 管理类
 *
 * @author //初始化MyVolley
 *         MyVolley.init(getApplicationContext());
 */
public class MyVolley {
    private static MyVolley instance;
    //请求队列
    public static RequestQueue mRequestQueue;
    //创建ImageLoader
    private static ImageLoader mImageLoader;
    //默认分配最大空间的几分之几
    private final static int RATE = 8;

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    public MyVolley(Context context) {
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
    public static void init(Context context) {
        if (instance == null) {
            instance = new MyVolley(context);
        }
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


        getRequestQueue().add(request);
    }

    /**
     * 加载网络图片
     */
    public static void getImage(String requestUrl, ImageView imageView) {
        getImage(requestUrl, imageView, 0, 0);
    }

    /**
     * 加载网络图片
     */
    public static void getImage(String requestUrl, ImageView imageView,
                                int defaultImageResId, int errorImageResId) {
        getImage(requestUrl, imageView, defaultImageResId, errorImageResId, 0,
                0);
    }

    /**
     * 加载网络图片
     */
    public static void getImage(String requestUrl, ImageView imageView,
                                int defaultImageResId, int errorImageResId, int maxWidth,
                                int maxHeight) {
        imageView.setTag(requestUrl);
        try {
            getImageLoader().get(
                    requestUrl,
                    ImageListenerFactory.getImageListener(imageView,
                            defaultImageResId, errorImageResId), maxWidth,
                    maxHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查是否完成初始化
     */
    private static void throwIfNotInit() {
        if (instance == null) {
            throw new IllegalStateException("MyVolley尚未初始化，在使用前应该执行init()");
        }
    }
}
