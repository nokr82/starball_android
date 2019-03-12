package com.devstories.starball_android.actions

import com.devstories.starball_android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

object ChattingAction {

    fun index(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/index", params, handler)
    }

    fun chatting(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/chatting", params, handler)
    }

    fun detail(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/detail", params, handler)
    }

    fun send_chatting(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/send_chatting", params, handler)
    }
}