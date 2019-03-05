package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.devstories.starball_android.BuildConfig
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.VersionAction
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_version_info.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class VersionActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    private var update = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_version_info)
        this.context = this
        progressDialog = ProgressDialog(context)

        backIV.setOnClickListener {
            finish()
        }

        nextTV.setOnClickListener {
            if (update) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=$packageName")
                startActivity(intent)
            }
        }

        loadData()

    }

    fun loadData() {

        val params = RequestParams()
        params.put("device", "A")
        params.put("my_version", BuildConfig.VERSION_NAME)

        VersionAction.index(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result || "update" == result) {

                        val versionObj = response.getJSONObject("version")
                        val version = Utils.getString(versionObj, "version")
                        val myVersion = BuildConfig.VERSION_NAME

                        versionTV.text = "version. $version"
                        myVersionTV.text = "version. $myVersion"

                        if ("update" == result) {

                            update = true

                            nextTV.text = getString(R.string.version_update)
                            updateTV.visibility = View.VISIBLE
                        } else {
                            nextTV.text = getString(R.string.new_version_is)
                            updateTV.visibility = View.GONE
                        }

                    } else {
                        Utils.alert(context, "조회중 장애가 발생하였습니다.")
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
