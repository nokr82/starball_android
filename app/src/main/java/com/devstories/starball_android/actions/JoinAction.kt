package com.devstories.starball_android.actions

import com.devstories.starball_android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams


/**
 * Created by hooni
 */
object JoinAction {

    // 핸드폰 인증
    fun send_sms(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/join/sms_code.json", params, handler)
    }
    // 최종회원가입
    fun final_join(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/edit", params, handler)
    }
    // 안전회원가입
    fun join_safety(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/join_safety", params, handler)
    }
    // 회원가입
    fun join(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/join", params, handler)
    }

    // 닉네임 체크
    fun check_nick_name(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/join/check_nick_name.json", params, handler)
    }

}