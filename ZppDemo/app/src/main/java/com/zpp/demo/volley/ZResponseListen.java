package com.zpp.demo.volley;

import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ZResponseListen {

    public void onErrorResponse(VolleyError volleyError) {
        errorListener(volleyError.hashCode(),volleyError.getMessage());
    }


    public void onResponse(Object o) {
        if(o instanceof String && !((String) o).isEmpty()){
            Log.e("response","返回："+o.toString());
            try {
                JSONObject object=new JSONObject((String) o);
                if(object.has("code")){
                    int code=object.getInt("code");

                    if(code==200) {
                        successListener(o);
                    }else {
                        errorListener(code,"msg");
                    }
                }else {
                    errorListener(-1,"返回数据格式错误");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void successListener(Object object);
    protected abstract void errorListener(int code,String msg);
}
