package com.volley;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;



public abstract class ZResponseListen{
    public void onErrorResponse(VolleyError volleyError) {

        int code=volleyError.hashCode();
        String msg=volleyError.getMessage();
        Log.e("response","失败返回："+code+":"+msg);
        if(volleyError.networkResponse==null) {
            if(volleyError.getMessage()!=null && volleyError.getMessage().contains("ConnectException"))
                msg = "服务器连接错误";
            else
                msg= "服务器未响应";
        }else{
            code=volleyError.networkResponse.statusCode;
            msg = HttpCode.getMsg(code);
        }
        errorListener(code,msg);
    }

    public void onResponse(Object o) {
        if(o instanceof String && !((String) o).isEmpty()){
            Log.e("response","成功返回："+o.toString());
            try {
                JSONObject object=new JSONObject((String) o);
                if(object.has("code")){
                    int code=object.getInt("code");
                    if(code==HttpCode.Success.getCode()) {
                        successListener((String) o);
                    }else {
                        String msg = object.optString("msg");
                        errorListener(code,msg!=null?msg:(code+"请求失败"));
                    }
                }else {
                    errorListener((String) o);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void successListener(String str){}
    protected void errorListener(String msg){}
    protected void errorListener(int code,String msg){}
}
