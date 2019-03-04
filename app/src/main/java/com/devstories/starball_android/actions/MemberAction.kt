package com.devstories.starball_android.actions

import com.devstories.starball_android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

object MemberAction {

    fun login(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/login", params, handler)
    }
    fun get_info(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/info", params, handler)
    }
    fun list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/list", params, handler)
    }
}