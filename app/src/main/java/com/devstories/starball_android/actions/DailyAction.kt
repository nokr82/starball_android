package com.devstories.starball_android.actions

import com.devstories.starball_android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

object DailyAction {


    fun add_content(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/dailys/add_content", params, handler)
    }
    fun list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/dailys/list", params, handler)
    }

}