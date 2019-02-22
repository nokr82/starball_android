package com.devstories.starball_android.Actions

import com.devstories.starball_android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

object MemberAction {

    fun login(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/login", params, handler)
    }
}