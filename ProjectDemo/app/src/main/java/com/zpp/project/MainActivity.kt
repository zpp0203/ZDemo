package com.zpp.project

import android.os.Looper
import com.zpp.RetrofitHttpUtils
import com.zpp.base.BaseActivity
import com.zpp.retrofit.callback.HttpCallback
import com.zpp.retrofit.utils.LogUtils

class MainActivity : BaseActivity() {
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_main
    }

    override fun initial() {
        var params= mutableMapOf<String,Any>()
        params.put("key", "f908271a6155ab8a0f08566c7ad888c5")
        params.put("city","惠州")

        var h = RetrofitHttpUtils.Builder()
                .apiUrl("/simpleWeather/query")
                .addParameter(params)
                .lifecycle(this)
                .build()
        h.execute(object : HttpCallback<String>(){
            override fun onSuccess(value: String?) {
                LogUtils.e("onSuccess=="+(Looper.getMainLooper().getThread() == Thread.currentThread()))
            }

            override fun onError(code: Int, desc: String?) {
                LogUtils.e("onError=="+(Looper.getMainLooper().getThread() == Thread.currentThread()))
            }

        })
    }

}

