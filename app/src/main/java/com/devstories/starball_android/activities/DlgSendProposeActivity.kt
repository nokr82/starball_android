package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
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
import kotlinx.android.synthetic.main.dlg_send_propose.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DlgSendProposeActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var propose_member_id = -1
    var name = ""
    var profile = ""

    var starball = 50

    var member_id = -1

    var my_starball = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_send_propose)


        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        this.context = this
        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        member_id = PrefUtils.getIntPreference(context, "member_id")

        propose_member_id = intent.getIntExtra("propose_member_id", -1)
        name = intent.getStringExtra("name")
        profile = intent.getStringExtra("profile")

        userNameTV.text = name
        ImageLoader.getInstance().displayImage(Config.url + profile, proposeMemberIV,  Utils.UILoptionsProfile)

        noTV.setOnClickListener {
            finish()
        }

        payTV.setOnClickListener {
            var intent = Intent(context, StarballPayActivity::class.java)
            startActivity(intent)
        }

        starball2LL.setOnClickListener {
            starball = 2
            starballSetView()
        }

        starball5LL.setOnClickListener {
            starball = 5
            starballSetView()
        }

        starball10LL.setOnClickListener {
            starball = 10
            starballSetView()
        }

        starball30LL.setOnClickListener {
            starball = 30
            starballSetView()
        }

        starball50LL.setOnClickListener {
            starball = 50
            starballSetView()
        }

        giftTV.setOnClickListener {

            var starballText= Utils.getInt(starballET)

            if (starballText > 0) {
                starball = starballText
            }

            if (my_starball < starball) {
                Toast.makeText(context, getString(R.string.fall_starball), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            propose()

        }

        loadData()

    }

    fun loadData() {

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

                        val member = response.getJSONObject("member")
                        val name = Utils.getString(member, "name")
                        var profiles = response.getJSONArray("profiles")

                        my_starball = Utils.getInt(response, "starball")

                        myStarballTV.text = my_starball.toString()

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
                Utils.alert(context, getString(R.string.api_error))
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

    fun propose() {

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("propose_member_id", propose_member_id)
        params.put("starball", starball)

        MemberAction.propose(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        Toast.makeText(context, name + getString(R.string.propose_ok), Toast.LENGTH_LONG).show()
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

    fun starballSetView() {
        starball2IV.setImageResource(R.mipmap.radio_off)
        starball5IV.setImageResource(R.mipmap.radio_off)
        starball10IV.setImageResource(R.mipmap.radio_off)
        starball30IV.setImageResource(R.mipmap.radio_off)
        starball50IV.setImageResource(R.mipmap.radio_off)

        if (starball == 2) {
            starball2IV.setImageResource(R.mipmap.radio_on)
        } else if (starball == 5) {
            starball5IV.setImageResource(R.mipmap.radio_on)
        } else if (starball == 10) {
            starball10IV.setImageResource(R.mipmap.radio_on)
        } else if (starball == 30) {
            starball30IV.setImageResource(R.mipmap.radio_on)
        } else {
            starball50IV.setImageResource(R.mipmap.radio_on)
        }
    }

}
