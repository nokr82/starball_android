package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.devstories.starball_android.Actions.JoinAction
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_join.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JoinActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    var email = ""
    var join_type = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        this.context = this
        progressDialog = ProgressDialog(context)




        joinTV.setOnClickListener {

            email = Utils.getString(emailET)

           /* if (email.count() < 1) {
                dlg(getString(R.string.email_empty))
                return@setOnClickListener
            }

            if (!Utils.isValidEmail(email)) {
                dlg(getString(R.string.email_fail))
                return@setOnClickListener
            }*/
            val intent = Intent(context, JoinStep1PasswdActivity::class.java)

            startActivity(intent)
            join_type = 1
//            join()


        }

        loginTV.setOnClickListener {
            //            val intent = Intent(context, MainSearchActivity::class.java)
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }





    }

    fun dlg(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }


    fun join() {
        val params = RequestParams()
        params.put("email", email)
        params.put("join_type", join_type)

        JoinAction.join(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    Log.d("결과",response.toString())
                    if ("ok" == result) {
                        val intent = Intent(context, JoinStep1PasswdActivity::class.java)

                        startActivity(intent)
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

                System.out.println(responseString);

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
