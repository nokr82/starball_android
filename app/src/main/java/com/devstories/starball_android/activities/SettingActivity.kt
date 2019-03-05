package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.JoinAction
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_setting.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SettingActivity : RootActivity() {

    val LOGOUT_CONFIRM = 100

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var message_yn = "N"
    var match_yn = "N"
    var crush_yn = "N"
    var propose_yn = "N"
    var daily_yn = "N"
    var starball_yn = "N"
    var vibe_yn = "N"
    var sound_yn = "N"

    var member_id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        this.context = this
        progressDialog = ProgressDialog(context)

        member_id = PrefUtils.getIntPreference(context, "member_id")

        suggestionLL.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND)

            try {
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("koreacap@starball.me"))

                emailIntent.type = "text/html"
                emailIntent.setPackage("com.google.android.gm")
                if (emailIntent.resolveActivity(packageManager) != null)
                    startActivity(emailIntent)

                startActivity(emailIntent)
            } catch (e: Exception) {
                e.printStackTrace()

                emailIntent.type = "text/html"
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("koreacap@starball.me"))

                startActivity(Intent.createChooser(emailIntent, "Send Email"))
            }

        }

        secessionLL.setOnClickListener {
            var intent = Intent(context, DlgSecessionActivity::class.java)
            startActivity(intent)
        }

        versionLL.setOnClickListener {
            val intent = Intent(context, VersionActivity::class.java)
            startActivity(intent)
        }
        alramLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if (it.isSelected) {
                alram_opLL.visibility = View.VISIBLE
            } else {
                alram_opLL.visibility = View.GONE
            }
        }

        logoutLL.setOnClickListener {
            var intent = Intent(context, DlgLogoutActivity::class.java)
            startActivityForResult(intent, LOGOUT_CONFIRM)
        }

        backIV.setOnClickListener {
            finish()
        }

        click()

        loadData()

    }

    fun click() {

        messageLL.setOnClickListener {
            if (message_yn == "Y") {
                message_yn = "N"
            } else {
                message_yn = "Y"
            }

            editInfo()

        }
        matchLL.setOnClickListener {

            if (match_yn == "Y") {
                match_yn = "N"
            } else {
                match_yn = "Y"
            }

            editInfo()
        }
        crushLL.setOnClickListener {

            if (crush_yn == "Y") {
                crush_yn = "N"
            } else {
                crush_yn = "Y"
            }

            editInfo()
        }
        proposeLL.setOnClickListener {

            if (crush_yn == "Y") {
                propose_yn = "N"
            } else {
                propose_yn = "Y"
            }

            editInfo()
        }
        dailyLL.setOnClickListener {

            if (crush_yn == "Y") {
                daily_yn = "N"
            } else {
                daily_yn = "Y"
            }

            editInfo()

        }
        starballLL.setOnClickListener {

            if (crush_yn == "Y") {
                starball_yn = "N"
            } else {
                starball_yn = "Y"
            }

            editInfo()

        }
        vibeLL.setOnClickListener {

            if (crush_yn == "Y") {
                vibe_yn = "N"
            } else {
                vibe_yn = "Y"
            }

            editInfo()

        }
        soundLL.setOnClickListener {

            if (crush_yn == "Y") {
                sound_yn = "N"
            } else {
                sound_yn = "Y"
            }

            editInfo()

        }
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

                        message_yn = Utils.getString(member, "message_yn")
                        match_yn = Utils.getString(member, "match_yn")
                        crush_yn = Utils.getString(member, "crush_yn")
                        propose_yn = Utils.getString(member, "propose_yn")
                        daily_yn = Utils.getString(member, "daily_yn")
                        starball_yn = Utils.getString(member, "starball_yn")
                        vibe_yn = Utils.getString(member, "vibe_yn")
                        sound_yn = Utils.getString(member, "sound_yn")

                        if (message_yn == "Y") {
                            messageIV.setImageResource(R.mipmap.setting_toggle_on)
                        } else {
                            messageIV.setImageResource(R.mipmap.off)
                        }

                        if (match_yn == "Y") {
                            matchIV.setImageResource(R.mipmap.setting_toggle_on)
                        } else {
                            matchIV.setImageResource(R.mipmap.off)
                        }

                        if (crush_yn == "Y") {
                            crushIV.setImageResource(R.mipmap.setting_toggle_on)
                        } else {
                            crushIV.setImageResource(R.mipmap.off)
                        }

                        if (propose_yn == "Y") {
                            proposeIV.setImageResource(R.mipmap.setting_toggle_on)
                        } else {
                            proposeIV.setImageResource(R.mipmap.off)
                        }

                        if (daily_yn == "Y") {
                            dailyIV.setImageResource(R.mipmap.setting_toggle_on)
                        } else {
                            dailyIV.setImageResource(R.mipmap.off)
                        }

                        if (starball_yn == "Y") {
                            starballIV.setImageResource(R.mipmap.setting_toggle_on)
                        } else {
                            starballIV.setImageResource(R.mipmap.off)
                        }

                        if (vibe_yn == "Y") {
                            vibeIV.setImageResource(R.mipmap.setting_toggle_on)
                        } else {
                            vibeIV.setImageResource(R.mipmap.off)
                        }

                        if (sound_yn == "Y") {
                            soundIV.setImageResource(R.mipmap.setting_toggle_on)
                        } else {
                            soundIV.setImageResource(R.mipmap.off)
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

    fun editInfo() {

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("message_yn", message_yn)
        params.put("match_yn", match_yn)
        params.put("crush_yn", crush_yn)
        params.put("propose_yn", propose_yn)
        params.put("daily_yn", daily_yn)
        params.put("starball_yn", starball_yn)
        params.put("vibe_yn", vibe_yn)
        params.put("sound_yn", sound_yn)

        MemberAction.edit(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        val member = response.getJSONObject("member")

                        message_yn = Utils.getString(member, "message_yn")
                        match_yn = Utils.getString(member, "match_yn")
                        crush_yn = Utils.getString(member, "crush_yn")
                        propose_yn = Utils.getString(member, "propose_yn")
                        daily_yn = Utils.getString(member, "daily_yn")
                        starball_yn = Utils.getString(member, "starball_yn")
                        vibe_yn = Utils.getString(member, "vibe_yn")
                        sound_yn = Utils.getString(member, "sound_yn")

                        if (message_yn == "Y") {
                            messageIV.setImageResource(R.mipmap.setting_toggle_on)
                        } else {
                            messageIV.setImageResource(R.mipmap.off)
                        }

                        if (match_yn == "Y") {
                            matchIV.setImageResource(R.mipmap.setting_toggle_on)
                        } else {
                            matchIV.setImageResource(R.mipmap.off)
                        }

                        if (crush_yn == "Y") {
                            crushIV.setImageResource(R.mipmap.setting_toggle_on)
                        } else {
                            crushIV.setImageResource(R.mipmap.off)
                        }

                        if (propose_yn == "Y") {
                            proposeIV.setImageResource(R.mipmap.setting_toggle_on)
                        } else {
                            proposeIV.setImageResource(R.mipmap.off)
                        }

                        if (daily_yn == "Y") {
                            dailyIV.setImageResource(R.mipmap.setting_toggle_on)
                        } else {
                            dailyIV.setImageResource(R.mipmap.off)
                        }

                        if (starball_yn == "Y") {
                            starballIV.setImageResource(R.mipmap.setting_toggle_on)
                        } else {
                            starballIV.setImageResource(R.mipmap.off)
                        }

                        if (vibe_yn == "Y") {
                            vibeIV.setImageResource(R.mipmap.setting_toggle_on)
                        } else {
                            vibeIV.setImageResource(R.mipmap.off)
                        }

                        if (sound_yn == "Y") {
                            soundIV.setImageResource(R.mipmap.setting_toggle_on)
                        } else {
                            soundIV.setImageResource(R.mipmap.off)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                LOGOUT_CONFIRM -> {

                    PrefUtils.clear(context)

                    val intent = Intent(context, JoinActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                }
            }
        }

    }

}
