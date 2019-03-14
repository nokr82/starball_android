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

    fun edit_room(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/edit_room", params, handler)
    }

    fun send_chatting(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/send_chatting", params, handler)
    }

    fun adverb(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/adverb", params, handler)
    }

    fun del_adverb(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/del_adverb", params, handler)
    }

    fun add_adverb(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/add_adverb", params, handler)
    }
    fun chat_user_list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/chat_user_list", params, handler)
    }

    fun like(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/like", params, handler)
    }
}