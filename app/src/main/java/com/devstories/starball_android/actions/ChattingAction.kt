package com.devstories.starball_android.actions

import com.devstories.starball_android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

object ChattingAction {

    fun cancel_group_chatting(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/cancel_group_chatting", params, handler)
    }

    fun del_group_member(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/del_group_member", params, handler)
    }
    fun group_chatting(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/group_chatting", params, handler)
    }
    fun group(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/group", params, handler)
    }
    fun index(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/index", params, handler)
    }

    fun chatting(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/chatting", params, handler)
    }
    fun group_detail(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/group_detail", params, handler)
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

    fun add_group(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/add_group", params, handler)
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

    fun del_chatting(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/del_chatting", params, handler)
    }

    fun saveTranslate(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/chattings/save_translate", params, handler)
    }
}