package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import com.devstories.starball_android.Actions.JoinAction
import com.devstories.starball_android.R
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_join_result.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JoinResultActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_result)
        this.context = this
        progressDialog = ProgressDialog(context)

        val name = PrefUtils.getStringPreference(context, "join_name")

        val title = String.format(getString(R.string.final_join_title), name);
        titleTV.text = title

        val desc1 = getString(R.string.join_result_content)
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            desc1TV.text = Html.fromHtml(desc1);
        } else {
            desc1TV.text = Html.fromHtml(desc1, Html.FROM_HTML_MODE_LEGACY);
        }

        val manner = String.format(getString(R.string.join_result_manner), name)
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            mannerTV.text = Html.fromHtml(manner);
        } else {
            mannerTV.text = Html.fromHtml(manner, Html.FROM_HTML_MODE_LEGACY);
        }

        val lession = String.format(getString(R.string.result_lesson), name)
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            lessionTV.text = Html.fromHtml(lession);
        } else {
            lessionTV.text = Html.fromHtml(lession, Html.FROM_HTML_MODE_LEGACY);
        }

        okTV.setOnClickListener {
            join()
        }


        noLL.setOnClickListener {
            val intent = Intent(context, JoinActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


    }


    fun join() {

        val email = PrefUtils.getStringPreference(context, "join_email")
        val passwd = PrefUtils.getStringPreference(context, "join_passwd")
        val name = PrefUtils.getStringPreference(context, "join_name")
        val gender = PrefUtils.getStringPreference(context, "join_gender")
        val height = PrefUtils.getStringPreference(context, "join_height")
        val birth = PrefUtils.getStringPreference(context, "join_birth")
        val language = PrefUtils.getStringPreference(context, "join_language")
        val job = PrefUtils.getStringPreference(context, "join_job")
        val school = PrefUtils.getStringPreference(context, "join_school")
        val intro = PrefUtils.getStringPreference(context, "join_intro")

        val params = RequestParams()
        params.put("name", name)
        params.put("email", email)
        params.put("passwd", passwd)
        params.put("gender", gender)
        params.put("height", height)
        params.put("language", language)
        params.put("birth", birth)
        params.put("job", job)
        params.put("intro", intro)


        JoinAction.join(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    Log.d("결과",result.toString())
                    if ("ok" == result) {
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
