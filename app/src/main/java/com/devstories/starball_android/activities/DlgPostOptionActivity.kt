package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.DailyAction
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.dlg_post_option.*
import org.json.JSONException
import org.json.JSONObject

class DlgPostOptionActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    private var REPORT = 1001
    var content_id = -1
    var like_member_id = -1
    var like_id = -1
    var daily_member_id = -1
    var member_id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_post_option)


        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        this.context = this
        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        member_id = PrefUtils.getIntPreference(context, "member_id")
        daily_member_id = intent.getIntExtra("daily_member_id", -1)
        content_id = intent.getIntExtra("content_id", -1)
        like_member_id = intent.getIntExtra("like_member_id", -1)
        like_id = intent.getIntExtra("like_id", -1)

        if (like_id != -1){
            delTV.text = "매치 취소"

            reportTV.setOnClickListener {
                val intent = Intent(context, ReportActivity::class.java)
                intent.putExtra("report_member_id",like_member_id)
                startActivityForResult(intent, REPORT)
            }

            delTV.setOnClickListener {
                match_cancel()
            }
        }
        else if (daily_member_id != -1){
            delTV.visibility = View.GONE
            reportTV.visibility = View.VISIBLE
            blockTV.visibility = View.VISIBLE
            reportTV.setOnClickListener {
                val intent = Intent(context, ReportActivity::class.java)
                intent.putExtra("report_member_id",daily_member_id)
                startActivityForResult(intent, REPORT)
            }
            blockTV.setOnClickListener {
                blocking()
            }

        }
        else{
            if (like_member_id==member_id){
                delTV.visibility = View.VISIBLE
                reportTV.visibility = View.GONE
            }else{
                delTV.visibility = View.GONE
                reportTV.visibility = View.VISIBLE
            }
            reportTV.setOnClickListener {
                val intent = Intent(context, ReportActivity::class.java)
                intent.putExtra("report_member_id",like_member_id)
                startActivityForResult(intent, REPORT)
            }

            delTV.setOnClickListener {
                del_content()
            }
        }

    }


    fun blocking() {
        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("block_member_id", daily_member_id)

        MemberAction.blocking(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                Log.d("아우스0",response.toString())
                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        Toast.makeText(context,"차단되었습니다.",Toast.LENGTH_SHORT).show()
                        finish()
                    } else if ("already" == result){
                        Toast.makeText(context,"이미 차단상태입니다.",Toast.LENGTH_SHORT).show()
                        finish()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseString: String?,
                throwable: Throwable
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                System.out.println(responseString);

                throwable.printStackTrace()
                error()
            }


            override fun onStart() {
                // show dialog
//                if (progressDialog != null) {
//                    progressDialog!!.show()
//                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }

    fun match_cancel() {
        val params = RequestParams()
        params.put("like_id", like_id)

        MemberAction.match_cancel(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                Log.d("아우스0",response.toString())
                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        var intent = Intent()
                        intent.action = "DEL_MATCH"
                        sendBroadcast(intent)
                        finish()
                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseString: String?,
                throwable: Throwable
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                System.out.println(responseString);

                throwable.printStackTrace()
                error()
            }


            override fun onStart() {
                // show dialog
//                if (progressDialog != null) {
//                    progressDialog!!.show()
//                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }

    fun del_content() {
        val params = RequestParams()
        params.put("content_id", content_id)

        DailyAction.del_content(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                Log.d("아우스0",response.toString())
                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        var intent = Intent()
                        intent.action = "DEL_POST"
                        sendBroadcast(intent)
                        finish()
                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseString: String?,
                throwable: Throwable
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                System.out.println(responseString);

                throwable.printStackTrace()
                error()
            }


            override fun onStart() {
                // show dialog
//                if (progressDialog != null) {
//                    progressDialog!!.show()
//                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }

}
