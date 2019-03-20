package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.adapter.infoAdapter
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.dlg_charactor_info.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class DlgCharInfoActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    private val _active = true

    lateinit var infoAdapter: infoAdapter
    var email = ""
    var height = -1
    var intro = ""
    var nation = ""
    var region = ""
    var policy = ""
    var baby = ""
    var animal = ""
    var smoke = ""
    var drink = ""
    var health = ""
    var sport = ""
    var work = ""

    var vvip_yn = ""
    var know_yn = ""
    var save_yn = ""
    var limit_yn = ""
    var cross_yn = ""
    var ghost_yn = ""
    var savejoin_yn = ""
    var phone_yn = "N"
    var email_yn = ""
    var facebook_yn = ""
    var insta_yn = ""
    var travel = ""
    var travel_cal = ""
    var phone = ""
    var like_member_id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_charactor_info)

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        this.context = this
        progressDialog = ProgressDialog(context)

        like_member_id = intent.getIntExtra("like_member_id",-1)

        get_info()
        reportTV.setOnClickListener {
            val intent = Intent(context, ReportActivity::class.java)
            intent.putExtra("report_member_id",like_member_id)
            startActivity(intent)
        }

        emailLL.setOnClickListener {
            val intent = Intent(context, EmailConnectActivity::class.java)
            startActivity(intent)
        }

        phoneLL.setOnClickListener {
            val intent = Intent(context, PhoneCertiActivity::class.java)
            startActivity(intent)
        }



    }

    fun get_info() {

        if (like_member_id<0){
            return
        }

        val params = RequestParams()
        params.put("member_id", like_member_id)

        MemberAction.get_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    Log.d("결과", result.toString())
                    if ("ok" == result) {

                        val member = response.getJSONObject("member")

                        email = Utils.getString(member, "email")
                        height = Utils.getInt(member, "height")
                        intro = Utils.getString(member, "intro")
                        nation = Utils.getString(member, "nation")
                        region = Utils.getString(member, "region")
                        policy = Utils.getString(member, "policy")
                        baby = Utils.getString(member, "baby")
                        drink = Utils.getString(member, "drink")
                        smoke = Utils.getString(member, "smoke")
                        health = Utils.getString(member, "health")
                        sport = Utils.getString(member, "sport")
                        animal = Utils.getString(member, "animal")
                        work = Utils.getString(member, "work")
                        phone = Utils.getString(member, "phone")

                        vvip_yn = Utils.getString(member, "vvip_yn")
                        know_yn = Utils.getString(member, "know_yn")
                        save_yn = Utils.getString(member, "save_yn")
                        cross_yn = Utils.getString(member, "cross_yn")
                        limit_yn = Utils.getString(member, "limit_yn")
                        ghost_yn = Utils.getString(member, "ghost_yn")
                        savejoin_yn = Utils.getString(member, "savejoin_yn")
                        phone_yn = Utils.getString(member, "phone_yn")
                        email_yn = Utils.getString(member, "email_yn")
                        facebook_yn = Utils.getString(member, "facebook_yn")
                        insta_yn = Utils.getString(member, "insta_yn")
                        travel = Utils.getString(member, "travel")
                        travel_cal = Utils.getString(member, "travel_cal")


                        if (email.length>0){
                            emailTV.text = " : "+getString(R.string.certification)
                        }else{
                            emailTV.text = " : "+getString(R.string.certification)
                        }
                        if (phone_yn=="N"){
                            phoneTV.text = " : "+getString(R.string.unauthorized)
                        }else{
                            phoneTV.text = " : "+getString(R.string.certification)
                        }
                        if (region != ""){
                            regionTV.text = " : "+region
                        }else{
                            regionTV.text = " : "+getString(R.string.none)
                        }
                        if (policy != ""){
                            policyTV.text = " : "+policy
                        }else{
                            policyTV.text = " : "+getString(R.string.none)
                        }
                        if (baby != ""){
                            babyTV.text = " : "+baby
                        }else{
                            babyTV.text = " : "+getString(R.string.none)
                        }
                        if (animal != ""){
                            dogTV.text = " : "+animal
                        }else{
                            dogTV.text = " : "+getString(R.string.none)
                        }
                        if (smoke != ""){
                            smokeTV.text = " : "+animal
                        }else{
                            smokeTV.text = " : "+getString(R.string.none)
                        }
                        if (drink != ""){
                            drinkTV.text = " : "+animal
                        }else{
                            drinkTV.text = " : "+getString(R.string.none)
                        }
                        if (health != ""){
                            healthTV.text = " : "+health
                        }else{
                            healthTV.text = " : "+getString(R.string.none)
                        }

                        if (sport != ""){
                            sportTV.text = " : "+sport
                        }else{
                            sportTV.text = " : "+getString(R.string.none)
                        }
                        if (height != -1){
                            heightTV.text = " : "+height
                        }else{
                            heightTV.text = " : "+getString(R.string.none)
                        }
                        if (facebook_yn =="Y"){
                            facebookTV.text = " : "+getString(R.string.certification)
                        }else{
                            facebookTV.text = " : "+getString(R.string.unauthorized)
                        }
                        if (insta_yn =="Y"){
                            instaTV.text = " : "+getString(R.string.certification)
                        }else{
                            instaTV.text = " : "+getString(R.string.unauthorized)
                        }
                        if (work != ""){
                            workTV.text = " : "+work
                        }else{
                            workTV.text = " : "+getString(R.string.none)
                        }
                        if (travel_cal != ""&&travel != null){
                            travelTV.text = " : "+travel_cal + "."+travel
                        }else{
                            travelTV.text = " : "+getString(R.string.none)
                        }

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
                Log.d("에러", responseString)
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
                Log.d("에러2", errorResponse.toString())
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
                Log.d("에러3", errorResponse.toString())
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
