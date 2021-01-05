package com.zpp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zpp.base.MyApplication;
import com.zpp.project.BuildConfig;


/**
 * Created by Ric on 2017/3/12.
 */

public class SharedPreUtils {

    private static SharedPreferences getUserPreferences() {
        return MyApplication.getInstance().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
    }

    public static void setData(String key, Object object) {
        SharedPreferences.Editor editor = getUserPreferences().edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, ((Integer) object).intValue());
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, ((Boolean) object).booleanValue());
        } else if (object instanceof Long) {
            editor.putLong(key, ((Long) object).longValue());
        } else if (object instanceof Float) {
            editor.putFloat(key, ((Float) object).floatValue());
        }
        editor.commit();
    }

    public static void removeData(String key) {
        SharedPreferences.Editor editor = getUserPreferences().edit();
        editor.remove(key);
        editor.commit();
    }

    public static String getStringData(String key,String def) {
        return getUserPreferences().getString(key, def);
    }
    public static String getStringData(String key) {
        return getUserPreferences().getString(key, "");
    }

    public static int getIntData(String key) {
        return getUserPreferences().getInt(key, 0);
    }
    public static int getIntData(String key,int def) {
        return getUserPreferences().getInt(key, def);
    }

    public static int getSpecialIntData(String key, int special) {
        try {
            return getUserPreferences().getInt(key, special);
        } catch (Exception e) {
            return special;
        }
    }

    public static long getLongData(String key) {
        try {
            return getUserPreferences().getLong(key, 0);
        } catch (Exception e) {
            return 0;
        }
    }

    public static Float getFloatData(String key) {
        try {
            return getUserPreferences().getFloat(key, 0);
        } catch (Exception e) {
            return 0f;
        }
    }

    public static boolean getBooleanData(String key) {
        try {
            return getUserPreferences().getBoolean(key, false);
        } catch (Exception e) {
            return false;
        }
    }

    public static void cleanUserPreference() {
        SharedPreferences.Editor editor = getUserPreferences().edit();
        editor.clear();
        editor.commit();
    }
}
