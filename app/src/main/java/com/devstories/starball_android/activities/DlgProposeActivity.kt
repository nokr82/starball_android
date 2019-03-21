package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.base.Config
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.nostra13.universalimageloader.core.ImageLoader
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.dlg_propose.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class DlgProposeActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    private val _active = true

    private var propose_id = -1
    private var member_id = -1

    val PROPOSE_ANIMATION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_propose)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        this.context = this
        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        member_id = PrefUtils.getIntPreference(context, "member_id")

        propose_id = intent.getIntExtra("propose_id", -1)

        noTV.setOnClickListener {
            propose_confirm("N")
        }

        doneTV.setOnClickListener {
            propose_confirm("Y")
        }

        getPropose()

    }

    fun getPropose() {

        val params = RequestParams()
        params.put("propose_id", propose_id)

        MemberAction.get_propose(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {

                        val member = response.getJSONObject("Member")
                        val profile = response.getJSONObject("Profile")
                        val propose = response.getJSONObject("Propose")

                        ImageLoader.getInstance().displayImage(Config.url + Utils.getString(profile, "image_uri"), proposeMemberIV, Utils.UILoptionsProfile)

                        textTV.text = getString(R.string.dlg_prose_title1) + Utils.getString(member, "name") + getString(R.string.dlg_prose_title2)
                        text2TV.text = getString(R.string.dlg_prose_content1) + Utils.getString(propose, "starball") + getString(R.string.dlg_prose_content3)

                    } else if ("confirm_done" == result){
                        finish()
                    } else {
                        textTV.text = getString(R.string.dlg_prose_title1)+ " " + getString(R.string.dlg_prose_title2)
                        text2TV.text = getString(R.string.dlg_prose_content1) + " " + getString(R.string.dlg_prose_content3)

                        Toast.makeText(context, getString(R.string.api_error), Toast.LENGTH_LONG).show()
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

                // System.out.println(responseString);

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

    fun propose_confirm(accept_yn: String) {

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("propose_id", propose_id)
        params.put("accept_yn", accept_yn)

        MemberAction.propose_confirm(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {

                        if ("Y" == accept_yn) {
                            var intent = Intent(context, ProposedActivity::class.java)
                            startActivityForResult(intent, PROPOSE_ANIMATION)
                        } else {
                            finish()
                        }

                    } else if (result == "proposed") {
                        Toast.makeText(context, getString(R.string.proposed), Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, getString(R.string.api_error), Toast.LENGTH_LONG).show()
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

                // System.out.println(responseString);

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                PROPOSE_ANIMATION -> {
                    finish()
                }
            }
        }

    }

}
