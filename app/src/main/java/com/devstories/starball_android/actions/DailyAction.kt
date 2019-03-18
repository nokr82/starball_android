package com.devstories.starball_android.actions

import com.devstories.starball_android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

object DailyAction {


    fun add_content(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/dailys/add_content", params, handler)
    }
    fun list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/dailys/list", params, handler)
    }
    fun del_content(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/dailys/del_content", params, handler)
    }

}