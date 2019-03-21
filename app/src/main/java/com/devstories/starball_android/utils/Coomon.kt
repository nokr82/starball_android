package com.devstories.starball_android.utils

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.activities.*
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

        fun pushCheck(context: Context, intent: Intent) {
            val FROM_PUSH = intent.getBooleanExtra("FROM_PUSH", false)

            if (FROM_PUSH) {

                val PUSH_TYPE = intent.getStringExtra("PUSH_TYPE")

                if (PUSH_TYPE == "chatting") {

                    val intent1 = Intent(context, FriendChattingActivity::class.java)
                    intent1.putExtra("room_id", intent.getIntExtra("room_id", -1))
                    context.startActivity(intent1)

                    val chatting_animation = intent.getBooleanExtra("chatting_animation", false)

                    if (chatting_animation) {
                        val intent2 = Intent(context, ChatNotiActivity::class.java)
                        context.startActivity(intent2)
                    }
                } else if (PUSH_TYPE == "like") {
                    var intent1 = Intent(context, LikedNotiActivity::class.java)
                    context.startActivity(intent1)
                } else if (PUSH_TYPE == "matching") {

                    val man_url = intent.getStringExtra("man_url")
                    val woman_url = intent.getStringExtra("woman_url")

                    val intent = Intent(context, MatchedActivity::class.java)
                    intent.putExtra("man_url", man_url)
                    intent.putExtra("woman_url", woman_url)
                    context.startActivity(intent)
                }

            }
        }

    }

}