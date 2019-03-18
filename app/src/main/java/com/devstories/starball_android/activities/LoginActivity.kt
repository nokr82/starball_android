package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.R
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        this.context = this
        progressDialog = ProgressDialog(context)

        findpasswordLL.setOnClickListener {
            val intent = Intent(context, FindPasswordActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        loginLL.setOnClickListener {
            login()
//            val intent = Intent(context, MainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
        }


        finishLL.setOnClickListener {
            finish()
            Utils.hideKeyboard(this)
        }
    }

    fun login() {
        var email = emailET.text.toString()
        var passwd = passwdET.text.toString()

        if (email.isEmpty()) {
            Utils.alert(context, "아이디는 필수 입력입니다.")
            return
        }

        if (passwd.isEmpty()) {
            Utils.alert(context, "비밀번호는 필수 입력입니다.")
            return
        }

        val params = RequestParams()
        params.put("email", email)
        params.put("passwd", passwd)
        params.put("join_type", 1)

        MemberAction.login(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                println(response)

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {

                        LoginActivity.processLoginData(context, response.getJSONObject("member"))

                        Utils.hideKeyboard(context)

                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    } else {


                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

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


    companion object {
        fun processLoginData(context: Context, data: JSONObject) {

            PrefUtils.setPreference(context, "member_id", Utils.getInt(data, "id"))
            PrefUtils.setPreference(context, "email", Utils.getString(data, "email"))
            PrefUtils.setPreference(context, "passwd", Utils.getString(data, "passwd"))
            PrefUtils.setPreference(context, "sns_key", Utils.getString(data, "sns_key"))
            PrefUtils.setPreference(context, "join_type", Utils.getInt(data, "join_type"))
            PrefUtils.setPreference(context, "savejoin_yn", Utils.getInt(data, "savejoin_yn"))
            PrefUtils.setPreference(context, "login_check", true)
            PrefUtils.setPreference(context, "autoLogin", true)

        }
    }

}
