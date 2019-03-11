package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_cash_request.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CashRequestActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var member_id = -1
    var myCashStarball = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_request)

        this.context = this
        progressDialog = ProgressDialog(context)

        member_id = PrefUtils.getIntPreference(context, "member_id")

        cashStarballET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val cashStarball = Utils.getInt(cashStarballET)

                if (cashStarball > 0) {

                    if (myCashStarball < cashStarball) {
                        return
                    }

                    var price = ((2000 * cashStarball) * 0.4).toInt()

                    cashTV.text = "$ " + price
                } else {
                    cashTV.text = "$ 0"
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        historyTV.setOnClickListener {
            val intent = Intent(context, DlgStarballHistoryActivity::class.java)
            startActivity(intent)
        }

        backIV.setOnClickListener {
            finish()
        }

        nextTV.setOnClickListener {

            val cashStarball = Utils.getInt(cashStarballET)
            val email = Utils.getString(emailET)

            if (cashStarball < 1) {
                Toast.makeText(context, getString(R.string.cash_starball_empty), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (myCashStarball < cashStarball) {
                Toast.makeText(context, getString(R.string.cash_starball_confirm), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (email == "") {
                Toast.makeText(context, getString(R.string.email_empty), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            request_cash()

        }

        get_info()

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

                    if ("ok" == result) {

                        var starball = Utils.getInt(response, "starball")
                        myCashStarball = Utils.getInt(response, "cashStarball")

                        if (starball < 1) {
                            starball = 0
                        }

                        if (myCashStarball < 1) {
                            myCashStarball = 0
                        }

                        myStarballTV.text = starball.toString()
                        cashStarballTV.text = myCashStarball.toString()

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
                responseString: String?,
                throwable: Throwable
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                // System.out.println(responseString);

                throwable.printStackTrace()
                error()
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

    fun request_cash() {

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("starball", Utils.getInt(cashStarballET))
        params.put("email", Utils.getString(emailET))

        MemberAction.cash_request(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        var starball = Utils.getInt(response, "starball")
                        myCashStarball = Utils.getInt(response, "cashStarball")

                        if (starball < 1) {
                            starball = 0
                        }

                        if (myCashStarball < 1) {
                            myCashStarball = 0
                        }

                        myStarballTV.text = starball.toString()
                        cashStarballTV.text = myCashStarball.toString()

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
                responseString: String?,
                throwable: Throwable
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                // System.out.println(responseString);

                throwable.printStackTrace()
                error()
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
