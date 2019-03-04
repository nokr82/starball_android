package com.devstories.starball_android.Actions

import com.devstories.starball_android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

object ChargeAction {

    fun setCharge(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/member/charge", params, handler)
    }

}