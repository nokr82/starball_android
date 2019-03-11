package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.JoinAction
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.devstories.starball_android.billing.IAPHelper
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_save_join.*
import kotlinx.android.synthetic.main.fragment_charmpoint_animal.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.File

class SaveJoinActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var savejoin_yn = "N"

    var member_id = -1

    private var iapHelper: IAPHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_join)

        this.context = this
        progressDialog = ProgressDialog(context)

        member_id = PrefUtils.getIntPreference(context, "member_id")

        iapHelper = IAPHelper(this, object : IAPHelper.BuyListener {

            override fun bought(sku: String, purchaseToken: String) {

                println("$sku bought!!! $purchaseToken")

                join_safe(purchaseToken)

//                if ("1gb" == sku) {
//                    setCharge(1024*1024*1024, purchaseToken)
//                } else if ("600mb" == sku) {
//                    setCharge(1024*1024*600, purchaseToken)
//                }

            }

            override fun failed(e: Exception) {
                e.printStackTrace()

                Utils.alert(context, "구매 중 장애가 발생하였습니다. " + e.localizedMessage)
            }
        })

        get_info()

        backIV.setOnClickListener {
            val intent = Intent()
            intent.putExtra("result", "result")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        yesTV.setOnClickListener {

            if (savejoin_yn == "Y") {
                val intent = Intent(context, SaveJoinOverActivity::class.java)
                startActivity(intent)
            } else {

                iapHelper?.buy("safety")
//                join_safe()
            }

        }
        noTV.setOnClickListener {
            finish()
        }

    }


    fun get_info() {

        val params = RequestParams()
        params.put("member_id", member_id)

        MemberAction.get_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    Log.d("결과", result.toString())
                    if ("ok" == result) {

                        val member = response.getJSONObject("member")

                        savejoin_yn = Utils.getString(member, "savejoin_yn")

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


            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                throwable: Throwable,
                errorResponse: JSONObject?
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                throwable.printStackTrace()
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
                throwable.printStackTrace()
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

    fun join_safe(purchaseToken: String) {

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("price", 50000)
        params.put("pay_type", "1")
        params.put("purchaseToken", purchaseToken)

        JoinAction.join_safety(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    Log.d("결과", result.toString())
                    if ("ok" == result) {
                        Toast.makeText(context, "가입되었습니다", Toast.LENGTH_SHORT).show()

                        iapHelper?.consume(purchaseToken)

                        get_info()
                        val intent = Intent()
                        intent.putExtra("result", "result")
                        setResult(Activity.RESULT_OK, intent)
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

    override fun onDestroy() {
        super.onDestroy()

        if (iapHelper != null) {
            iapHelper!!.destroy()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        iapHelper!!.onActivityResult(requestCode, resultCode, data)
    }

}
