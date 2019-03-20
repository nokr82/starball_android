package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.base.AdmobUtils
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.devstories.starball_android.billing.IAPHelper
import com.devstories.starball_android.utils.Coomon
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_pay_starball.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class StarballPayActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    private lateinit var iapHelper: IAPHelper

    var member_id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_starball)

        this.context = this
        progressDialog = ProgressDialog(context)

        member_id = PrefUtils.getIntPreference(context, "member_id")

        iapHelper = IAPHelper(this, object : IAPHelper.BuyListener {

            override fun bought(sku: String, purchaseToken: String) {

                println("$sku bought!!! $purchaseToken")

                var starball = 0

                var starballs = sku.split("_")

                if (starballs.count() > 1) {
                    starball = starballs[1].toInt()
                }

                buyStarball(starball, purchaseToken)

            }

            override fun failed(e: Exception) {
                e.printStackTrace()

                Utils.alert(context, "구매 중 장애가 발생하였습니다. " + e.localizedMessage)
            }
        })

        payTV.setOnClickListener {
            val intent = Intent(context, DlgStarballLackActivity::class.java)
            startActivity(intent)
        }
        starball500LL.setOnClickListener {
            val intent = Intent(context, CashRequestActivity::class.java)
            startActivity(intent)
        }

        backIV.setOnClickListener {
            finish()
        }

        starball5TV.setOnClickListener {
            iapHelper.buy("starball_05")
        }

        starball10TV.setOnClickListener {
            iapHelper.buy("starball_10")
        }

        starball30TV.setOnClickListener {
            iapHelper.buy("starball_30")
        }

        starball50TV.setOnClickListener {
            iapHelper.buy("starball_50")
        }

        starball100TV.setOnClickListener {
            iapHelper.buy("starball_100")
        }

        starball500TV.setOnClickListener {
            iapHelper.buy("starball_500")
        }

        recommendTV.setOnClickListener {

            var intent = Intent(context, DlgRecommendActivity::class.java)
            startActivity(intent)

        }

        adverTV.setOnClickListener {

            AdmobUtils.loadAd(context) {
                // freeStarball(2)
                Coomon.freeStraball(context, 2)
            }

        }
    }

    fun buyStarball(starball: Int, purchaseToken: String) {

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("starball", starball)
        params.put("price", 2000 * starball)
        params.put("purchaseToken", purchaseToken)

        MemberAction.buy_starball(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        Toast.makeText(context, getString(R.string.starball_buy_done), Toast.LENGTH_SHORT).show()

                        iapHelper.consume(purchaseToken)

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

            /* override fun onFailure(statusCode: Int, headers: Array<Header>?, responseString: String?, throwable: Throwable) {
                 if (progressDialog != null) {
                     progressDialog!!.dismiss()
                 }
                 Log.d("에러", responseString.toString())
                 // System.out.println(responseString);

                 throwable.printStackTrace()
                 error()
             }*/

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

    fun freeStarball(cate: Int) {

        val params = RequestParams()
        params.put("member_id", member_id)
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
                        Toast.makeText(context, getString(R.string.free_starball), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, getString(R.string.api_error), Toast.LENGTH_SHORT).show()
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
                Utils.alert(context, getString(R.string.api_error))
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

    override fun onDestroy() {
        super.onDestroy()

        iapHelper.destroy()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        iapHelper.onActivityResult(requestCode, resultCode, data)
    }

}
