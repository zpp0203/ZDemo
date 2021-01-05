package com.zpp.project

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.zpp.base.bean.Data
import com.zpp.RHttp
import com.zpp.base.BaseActivity
import com.zpp.base.bean.UserBean
import com.zpp.retrofit.callback.http.RHttpCallback

class MainActivity : BaseActivity() {
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_main
    }

    override fun initial() {
        var h =RHttp.Builder()
                .apiUrl("user")
                .lifecycle(this)
                .build()
        h.execute(object : RHttpCallback<Data<UserBean>>(){
            override fun convert(data: JsonElement?): Data<UserBean> {

                return Gson().fromJson(data, object: TypeToken<Data<UserBean>>(){}.type)
            }

            override fun onSuccess(value: Data<UserBean>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(code: Int, desc: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onCancel() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

}

