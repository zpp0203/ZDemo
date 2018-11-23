package com.zpp.volley;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 自定义返回错误信息监听
 *
 */
public abstract class ZErrorListener implements Response.ErrorListener {


    public void onErrorResponse(VolleyError error) {
        Log.e("error", error.getMessage());

    }

}
