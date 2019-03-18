package com.devstories.starball_android.actions

import com.devstories.starball_android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

object MemberAction {

    fun match_list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/match_list", params, handler)
    }
    fun login(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/login", params, handler)
    }
    fun get_info(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/info", params, handler)
    }
    fun profile_del(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/profile_del", params, handler)
    }
    fun list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/list", params, handler)
    }
    fun edit(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/edit", params, handler)
    }
    fun secession(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/secession", params, handler)
    }
    fun starball_history(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/starball_history", params, handler)
    }
    fun cash_request(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/cash_request", params, handler)
    }
    fun buy_starball(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/buy_starball", params, handler)
    }
    fun like(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/like", params, handler)
    }

    fun like_list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/like_list", params, handler)
    }

    fun sns_connect(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/sns_connect", params, handler)
    }

}