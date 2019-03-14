package com.devstories.starball_android.actions

import com.devstories.starball_android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

object ReportAction {

    fun report(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/reports/report", params, handler)
    }

}