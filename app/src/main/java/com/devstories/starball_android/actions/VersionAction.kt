package com.devstories.starball_android.actions

import com.devstories.starball_android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams


/**
 * Created by hooni
 */
object VersionAction {

    fun index(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/versions/index", params, handler)
    }

}