package com.volley;

import android.widget.ImageView;

public class ZImageLoader {

    /**
     * 加载网络图片
     */
    public static void loadImage(String requestUrl, ImageView imageView) {
        loadImage(requestUrl, imageView, 0, 0);
    }

    /**
     * 加载网络图片
     */
    public static void loadImage(String requestUrl, ImageView imageView,
                                int defaultImageResId, int errorImageResId) {
        loadImage(requestUrl, imageView, defaultImageResId, errorImageResId, 0,
                0);
    }

    /**
     * 加载网络图片
     */
    public static void loadImage(String requestUrl, ImageView imageView,
                                int defaultImageResId, int errorImageResId, int maxWidth,
                                int maxHeight) {
        imageView.setTag(requestUrl);
        try {
            ZVolley.getImageLoader().get(
                    requestUrl,
                    ImageListenerFactory.getImageListener(imageView,
                            defaultImageResId, errorImageResId), maxWidth,
                    maxHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
