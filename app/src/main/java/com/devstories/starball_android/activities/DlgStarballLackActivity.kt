package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.devstories.starball_android.Actions.ChargeAction
import com.devstories.starball_android.R
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.devstories.starball_android.billing.IAPHelper
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.dlg_starball_lack.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DlgStarballLackActivity : RootActivity() {

    lateinit var mContext: Context
    private lateinit var progressDialog: ProgressDialog

    private var iapHelper: IAPHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_starball_lack)

        this.mContext = this
        progressDialog = ProgressDialog(mContext)


        iapHelper = IAPHelper(this, object : IAPHelper.BuyListener {

            override fun bought(sku: String, purchaseToken: String) {

                println("$sku bought!!!");

                if ("startball_02" == sku) {
                    setCharge(1024*1024*1024, purchaseToken)
                } else if ("startball_20" == sku) {
                    setCharge(1024*1024*600, purchaseToken)
                } else if ("startball_24h" == sku) {
                    setCharge(1024*1024*600, purchaseToken)
                }
            }

            override fun failed(e: Exception) {
                e.printStackTrace()

                Utils.alert(mContext, "구매 중 장애가 발생하였습니다. " + e.localizedMessage)
            }
        })

        starballO2LL.setOnClickListener {
            iapHelper?.buy("startball_02")
        }

        starball20LL.setOnClickListener {
            iapHelper?.buy("startball_20")
        }

        starball24hLL.setOnClickListener {
            iapHelper?.buy("startball_24h")
        }

    }

    private fun setCharge(quota: Int, purchaseToken: String) {

        val params = RequestParams()
        params.put("quota", quota)
        params.put("member_id", PrefUtils.getIntPreference(mContext, "member_id"))

        ChargeAction.setCharge(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null && !isFinishing()) {
                    progressDialog.dismiss()
                }

                try {

                    // System.out.println(response);

                    val result = Utils.getInt(response, "return")

                    if (result == 1) {
                        // ok

                        iapHelper?.consume(purchaseToken)

                        val intent = Intent()
                        intent.action = "MY_QUOTA_UPDATED"
                        sendBroadcast(intent)

                    } else if (result == 0) {
                        // error
                        Toast.makeText(mContext, Utils.getString(response, "error"), Toast.LENGTH_LONG).show()
                        return
                    } else {
                        Toast.makeText(mContext, "오류가 발생하였습니다.", Toast.LENGTH_LONG).show()
                        return
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                super.onSuccess(statusCode, headers, response)
            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {}

            private fun error() {
                if (progressDialog != null && !isFinishing()) {
                    Utils.alert(mContext, "처리중 장애가 발생하였습니다.")
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseString: String?, throwable: Throwable) {
                if (progressDialog != null && !isFinishing()) {
                    progressDialog.dismiss()
                }

                throwable.printStackTrace()
                error()
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONObject?) {
                if (progressDialog != null && !isFinishing()) {
                    progressDialog.dismiss()
                }
                throwable.printStackTrace()
                error()
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONArray?) {
                if (progressDialog != null && !isFinishing()) {
                    progressDialog.dismiss()
                }
                throwable.printStackTrace()
                error()
            }

            override fun onStart() {
                // show dialog
                if (progressDialog != null && !isFinishing()) {
                    progressDialog.setMessage("처리 중...")
                    progressDialog.show()
                }
            }

            override fun onFinish() {
                if (progressDialog != null && !isFinishing()) {
                    progressDialog.dismiss()
                }
            }
        })
    }


}
