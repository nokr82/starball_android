package com.devstories.starball_android.Actions

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

    // 회원가입
    fun join(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/members/join", params, handler)
    }

    // 닉네임 체크
    fun check_nick_name(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/join/check_nick_name.json", params, handler)
    }

}