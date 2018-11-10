package com.zpp.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheUtils {

    private static final String CACHE_FILE_NAME = "com.zpp.spf";
    private static SharedPreferences mSharedPreferences;

    public static void putBoolean(Context context, String key, boolean value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key,
                                     boolean defValue) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getBoolean(key, defValue);
    }

    /**
     * 存入SharedPreferences数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * 读取SharedPreferences数据
     *
     * @param context
     * @param key
     * @param defValue
     */
    public static String getString(Context context, String key, String defValue) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getString(key, defValue);
    }

    /**
     * 存入SharedPreferences数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putInt(Context context, String key, int value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putInt(key, value).commit();
    }

    /**
     * 读取SharedPreferences数据
     *
     * @param context
     * @param key
     * @param defValue
     */
    public static int getInt(Context context, String key, int defValue) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getInt(key, defValue);
    }

}