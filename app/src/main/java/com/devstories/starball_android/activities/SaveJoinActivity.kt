package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.JoinAction
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_save_join.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.File

class SaveJoinActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_join)
        this.context = this
        progressDialog = ProgressDialog(context)
        val savejoin_yn = PrefUtils.getStringPreference(context, "savejoin_yn")

        backIV.setOnClickListener {
            finish()
        }


        yesTV.setOnClickListener {

            if (savejoin_yn == "N") {
                val intent = Intent(context, SaveJoinOverActivity::class.java)
                startActivity(intent)
            } else {

            }

        }
        noTV.setOnClickListener {
            val intent = Intent(context, SaveJoinCancleActivity::class.java)
            startActivity(intent)
        }

    }

    fun edit_info() {



        var member_id = PrefUtils.getIntPreference(context, "member_id")

        val params = RequestParams()

        params.put("member_id", member_id)
        params.put("price", 50000)



        JoinAction.final_join(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    Log.d("결과", result.toString())
                    if ("ok" == result) {
                        Toast.makeText(context, "저장되었습니다", Toast.LENGTH_SHORT).show()
                    } else {

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
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
            }

            /* override fun onFailure(
                 statusCode: Int,
                 headers: Array<Header>?,
                 responseString: String?,
                 throwable: Throwable
             ) {
                 if (progressDialog != null) {
                     progressDialog!!.dismiss()
                 }
                 Log.d("에러", responseString.toString())
                 // System.out.println(responseString);

                 throwable.printStackTrace()
                 error()
             }*/

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                throwable: Throwable,
                errorResponse: JSONObject?
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                Log.d("에러2", errorResponse.toString())
                throwable.printStackTrace()
                error()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                throwable: Throwable,
                errorResponse: JSONArray?
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                Log.d("에러3", errorResponse.toString())
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

}
