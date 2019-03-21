package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.devstories.starball_android.actions.JoinAction
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.adapter.LanguageAdapter
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_search_setting.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import android.graphics.BitmapFactory
import android.support.v4.content.ContextCompat

class SearchSettingActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    private val SELECT_LANGUAGE_REQUST_CODE = 1004

    lateinit var languageAdapter: LanguageAdapter

    var adapterData = ArrayList<String>()

    var search_gender = ""
    var gender = ""
    var like_nation = ""
    var vvipview_yn = "N"
    var saveview_yn = "N"
    var other_nation = "N"
    private val SELECT_NATION = 1005


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_setting)

        this.context = this

        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        languageAdapter = LanguageAdapter(context, R.layout.item_language, adapterData)
        languageGV.adapter = languageAdapter
        languageGV.setOnItemClickListener { parent, view, position, id ->
            adapterData.removeAt(position)

            languageAdapter.notifyDataSetChanged()
        }


        nationRL.setOnClickListener {
            val intent = Intent(context, DlgSelectNationActivity::class.java)
            intent.putExtra("type",2)
            startActivityForResult(intent, SELECT_NATION)
        }

        ageSB.setOnRangeSeekbarChangeListener { minValue, maxValue ->

            minAgeTV.text = minValue.toString()
            maxAgeTV.text = maxValue.toString()

        }

        distanceSB.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            minDistanceTV.text = minValue.toString()
            maxDistanceTV.text = maxValue.toString()
        }

        heightSB.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            minHeightTV.text = minValue.toString()
            maxHeightTV.text = maxValue.toString()
        }

        seekBarSet()

        click()

    }

    fun seekBarSet() {

        var thump = Utils.convertToBitmap(ContextCompat.getDrawable(applicationContext, R.drawable.circle_background_ffffff_border_5_923b9f), Utils.dpToPx(18f).toInt(), Utils.dpToPx(18f).toInt())

        ageSB.setLeftThumbBitmap(thump).apply()
        ageSB.setLeftThumbHighlightBitmap(thump).apply()
        ageSB.setRightThumbBitmap(thump).apply()
        ageSB.setRightThumbHighlightBitmap(thump).apply()

        distanceSB.setLeftThumbBitmap(thump).apply()
        distanceSB.setLeftThumbHighlightBitmap(thump).apply()
        distanceSB.setRightThumbBitmap(thump).apply()
        distanceSB.setRightThumbHighlightBitmap(thump).apply()

        heightSB.setLeftThumbBitmap(thump).apply()
        heightSB.setLeftThumbHighlightBitmap(thump).apply()
        heightSB.setRightThumbBitmap(thump).apply()
        heightSB.setRightThumbHighlightBitmap(thump).apply()
    }

    fun click() {

        backIV.setOnClickListener {
            finish()
        }

        languageRL.setOnClickListener {
            val intent = Intent(context, DlgSelectLanguageActivity::class.java)
            startActivityForResult(intent, SELECT_LANGUAGE_REQUST_CODE)
        }


        languageLL.setOnClickListener {
            val intent = Intent(context, DlgSelectLanguageActivity::class.java)
            startActivityForResult(intent, SELECT_LANGUAGE_REQUST_CODE)
        }


        maleLL.setOnClickListener {
            setmenu()
            menIV.setImageResource(R.mipmap.comm_check_on)
            search_gender = "M"
        }

        femaleLL.setOnClickListener {
            setmenu()
            girlIV.setImageResource(R.mipmap.comm_check_on)
            search_gender = "F"
        }

        nationLL.setOnClickListener {
            if (other_nation == "Y") {
                other_nation = "N"
                nationIV.setImageResource(R.mipmap.comm_check_off)
            } else {
                other_nation = "Y"
                nationIV.setImageResource(R.mipmap.comm_check_on)
            }
        }

        saveLL.setOnClickListener {
            if (saveview_yn == "Y") {
                saveview_yn = "N"
                saveIV.setImageResource(R.mipmap.comm_check_off)
            } else {
                saveview_yn = "Y"
                saveIV.setImageResource(R.mipmap.comm_check_on)
            }
        }

        vvipLL.setOnClickListener {
            if (vvipview_yn == "Y") {
                vvipview_yn = "N"
                vvipIV.setImageResource(R.mipmap.comm_check_off)
            } else {
                vvipview_yn = "Y"
                vvipIV.setImageResource(R.mipmap.comm_check_on)
            }
        }

        nextTV.setOnClickListener {
            edit_search()
        }

        val icon = BitmapFactory.decodeResource(
            context.resources,
            com.devstories.starball_android.R.drawable.circle_background_ffffff_border_5_923b9f
        )



        get_info()
    }

    fun setmenu() {
        menIV.setImageResource(R.mipmap.comm_check_off)
        girlIV.setImageResource(R.mipmap.comm_check_off)
    }


    fun edit_search() {
        like_nation = Utils.getString(nationTV)
        var member_id = PrefUtils.getIntPreference(context, "member_id")

        val params = RequestParams()
        params.put("like_nation", like_nation)
        params.put("member_id", member_id)
        params.put("vvipview_yn", vvipview_yn)
        params.put("saveview_yn", saveview_yn)
        params.put("other_nation", other_nation)
        params.put("search_gender", search_gender)
        params.put("like_language", adapterData.joinToString())
        params.put("min_age", ageSB.selectedMinValue)
        params.put("max_age", ageSB.selectedMaxValue)
        params.put("min_distance", distanceSB.selectedMinValue)
        params.put("max_distance", distanceSB.selectedMaxValue)
        params.put("min_height", heightSB.selectedMinValue)
        params.put("max_height", heightSB.selectedMaxValue)

        JoinAction.final_join(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    Log.d("결과", result.toString())
                    if ("ok" == result) {
                        Toast.makeText(context, "변경되었습니다", Toast.LENGTH_SHORT).show()
                        get_info()

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

    fun get_info() {

        var member_id = PrefUtils.getIntPreference(context, "member_id")

        val params = RequestParams()
        params.put("member_id", member_id)

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

                        vvipview_yn = Utils.getString(member, "vvipview_yn")
                        saveview_yn = Utils.getString(member, "saveview_yn")
                        other_nation = Utils.getString(member, "other_nation")
                        gender = Utils.getString(member, "gender")
                        search_gender = Utils.getString(member, "search_gender")
                        like_nation = Utils.getString(member, "like_nation")
                        if (like_nation != ""){
                            nationTV.text = like_nation
                        }
                        adapterData.clear()

                        val like_languages = response.getJSONArray("like_languages")
                        for (i in 0..like_languages.length() - 1) {
                            var json = like_languages[i] as JSONObject
                            var language = Utils.getString(json, "language")
                            adapterData.add(language)
                        }
                        languageAdapter.notifyDataSetChanged()


                        if (search_gender == "F") {
                            girlIV.setImageResource(R.mipmap.comm_check_on)
                        } else if (search_gender == "M") {
                            menIV.setImageResource(R.mipmap.comm_check_on)
                        }

                        if (vvipview_yn == "Y") {
                            vvipIV.setImageResource(R.mipmap.comm_check_on)
                        } else {
                            vvipIV.setImageResource(R.mipmap.comm_check_off)
                        }
                        if (saveview_yn == "Y") {
                            saveIV.setImageResource(R.mipmap.comm_check_on)
                        } else {
                            saveIV.setImageResource(R.mipmap.comm_check_off)
                        }
                        if (other_nation == "Y") {
                            nationIV.setImageResource(R.mipmap.comm_check_on)
                        } else {
                            nationIV.setImageResource(R.mipmap.comm_check_off)
                        }

                        val min_age = Utils.getInt(member, "min_age")
                        val max_age = Utils.getInt(member, "max_age")

                        minAgeTV.text = min_age.toString()
                        maxAgeTV.text = max_age.toString()

                        ageSB.setMinStartValue(min_age.toFloat()).apply()
                        ageSB.setMaxStartValue(max_age.toFloat()).apply()

                        var min_distance = Utils.getInt(member, "min_distance")
                        var max_distance = Utils.getInt(member, "max_distance")

                        if (min_distance < 0) {
                            min_distance = 0
                        }

                        if (max_distance < 0) {
                            max_distance = 100
                        }

                        minDistanceTV.text = min_distance.toString()
                        maxDistanceTV.text = max_distance.toString()

                        distanceSB.setMinStartValue(min_distance.toFloat()).apply()
                        distanceSB.setMaxStartValue(max_distance.toFloat()).apply()

                        var min_height = Utils.getInt(member, "min_height")
                        var max_height = Utils.getInt(member, "max_height")

                        if (min_height < 0) {
                            min_height = 140
                        }

                        if (max_height < 0) {
                            max_height = 250
                        }

                        minHeightTV.text = min_height.toString()
                        maxHeightTV.text = max_height.toString()

                        println("min_height:::::::::::::::::::::::${min_height.toString()}")

                        heightSB.setMinStartValue(min_height.toFloat()).apply()
                        heightSB.setMaxStartValue(max_height.toFloat()).apply()

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

        when (requestCode) {
            SELECT_LANGUAGE_REQUST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        adapterData.add(data.getStringExtra("selectedLanguage"))
                        languageAdapter.notifyDataSetChanged()
                    }
                }
            }

            SELECT_NATION -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        nationTV.text = data.getStringExtra("selectedNation")

                    }
                }
            }
        }
    }


}
