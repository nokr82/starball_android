package com.devstories.starball_android.utils

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.activities.FriendChattingActivity
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Coomon {

    companion object {

        private var progressDialog: ProgressDialog? = null

        fun freeStraball(context: Context, cate: Int) {

            progressDialog = ProgressDialog(context)

            val params = RequestParams()
            params.put("member_id", PrefUtils.getIntPreference(context, "member_id"))
            params.put("starball", 1)
            params.put("cate", cate)

            MemberAction.free_starball(params, object : JsonHttpResponseHandler() {

                override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                    if (progressDialog != null) {
                        progressDialog!!.dismiss()
                    }

                    try {
                        val result = response!!.getString("result")

                        if ("ok" == result) {
                            Toast.makeText(context, context.getString(R.string.free_starball), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, context.getString(R.string.api_error), Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }

                override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                    super.onSuccess(statusCode, headers, response)
                }

                override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                    // System.out.println(responseString);
                }

                private fun error() {
                    Utils.alert(context, context.getString(R.string.api_error))
                }

                override fun onFailure(statusCode: Int, headers: Array<Header>?, responseString: String?, throwable: Throwable) {
                    if (progressDialog != null) {
                        progressDialog!!.dismiss()
                    }

                    System.out.println(responseString);

                    throwable.printStackTrace()
                    error()
                }

                override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONObject?) {
                    if (progressDialog != null) {
                        progressDialog!!.dismiss()
                    }
                    throwable.printStackTrace()
                    error()
                }

                override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONArray?) {
                    if (progressDialog != null) {
                        progressDialog!!.dismiss()
                    }
                    throwable.printStackTrace()
                    error()
                }

                override fun onStart() {
                    // show dialog
                    if (progressDialog != null) {

                        progressDialog!!.show()
                    }
                }

                override fun onFinish() {
                    if (progressDialog != null) {
                        progressDialog!!.dismiss()
                    }
                }
            })
        }

        fun pushCheck(context: Context) {
            val FROM_PUSH = PrefUtils.getBooleanPreference(context, "FROM_PUSH")

            if (FROM_PUSH) {

                val PUSH_TYPE = PrefUtils.getStringPreference(context, "PUSH_TYPE")

                if (PUSH_TYPE == "chatting") {
                    val intent = Intent(context, FriendChattingActivity::class.java)
                    context.startActivity(intent)

                    val intent1 = Intent(context, FriendChattingActivity::class.java)
                    intent1.putExtra("room_id", PrefUtils.getIntPreference(context, "room_id"))
                    context.startActivity(intent1)

                    prefPushDataClear(context)
                }

            } else {
                prefPushDataClear(context)
            }
        }

        fun prefPushDataClear(context: Context) {
            PrefUtils.removePreference(context, "FROM_PUSH")
            PrefUtils.removePreference(context, "PUSH_TYPE")
            PrefUtils.removePreference(context, "room_id")
        }

    }

}