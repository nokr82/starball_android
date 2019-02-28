package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.devstories.starball_android.Actions.JoinAction
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.CharmAdapter
import com.devstories.starball_android.adapter.LanguageAdapter
import com.devstories.starball_android.adapter.ProfileAdapter
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_edit_profile.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class EditProfileActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var ProfileAdapter: ProfileAdapter

    private val SELECT_LANGUAGE_REQUST_CODE = 1004
    private val SELECT_PICTURE_REQUEST = 1002
    private var pictures = arrayListOf<JSONObject>()

    private val CHARM_POINT = 111


    private val HEIGHT_SELECT = 101
    private val REGION_SELECT = 102
    private val POLICY_SELECT = 103
    private val BABY_SELECT = 104
    private val ANIMAL_SELECT = 105
    private val SMOKE_SELECT = 106
    private val DRINK_SELECT = 107
    private val HEALTH_SELECT = 108
    private val SPORTS_SELECT = 109
    private val WORK_SELECT = 110


    lateinit var languageAdapter: LanguageAdapter
    var adapterData = ArrayList<String>()

    lateinit var charmAdapter: CharmAdapter
    var adapterData2 = ArrayList<String>()

    var step = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        this.context = this
        progressDialog = ProgressDialog(context)


        ProfileAdapter = ProfileAdapter(context, R.layout.item_profile_img, 20)
        profileGV.adapter = ProfileAdapter


        languageAdapter = LanguageAdapter(context, R.layout.item_language, adapterData)
        languageGV.adapter = languageAdapter
        languageGV.setOnItemClickListener { parent, view, position, id ->
            adapterData.removeAt(position)

            languageAdapter.notifyDataSetChanged()
        }

        charmAdapter = CharmAdapter(context,R.layout.item_charm_point, adapterData2)
        charmpointGV.adapter = charmAdapter



        val joinLanguage = PrefUtils.getStringPreference(context, "join_language", "")
        if(joinLanguage.isNotEmpty()) {
            val splited = joinLanguage.split(",")
            for (language in splited) {
                adapterData.add(language.trim())
            }

            languageAdapter.notifyDataSetChanged()
        }

        click()
    }

    fun click(){
        languageLL.setOnClickListener {
            val intent = Intent(context, DlgSelectLanguageActivity::class.java)
            startActivityForResult(intent, SELECT_LANGUAGE_REQUST_CODE)
        }

        heightLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            startActivityForResult(intent,HEIGHT_SELECT)
        }
        regionLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 2
            intent.putExtra("step", step)
            startActivityForResult(intent,REGION_SELECT)

        }
        policyLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 3
            intent.putExtra("step", step)
            startActivityForResult(intent,POLICY_SELECT)
        }
        babyLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 4
            intent.putExtra("step", step)
            startActivityForResult(intent,BABY_SELECT)
        }
        animalLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 5
            intent.putExtra("step", step)
            startActivityForResult(intent,ANIMAL_SELECT)
        }
        smokeLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 6
            intent.putExtra("step", step)
            startActivityForResult(intent,SMOKE_SELECT)
        }
        drinkLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 7
            intent.putExtra("step", step)
            startActivityForResult(intent,DRINK_SELECT)
        }
        healthLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 8
            intent.putExtra("step", step)
            startActivityForResult(intent,HEALTH_SELECT)
        }
        sportLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 9
            intent.putExtra("step", step)
            startActivityForResult(intent,SPORTS_SELECT)
        }
        workLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 10
            intent.putExtra("step", step)
            startActivityForResult(intent,WORK_SELECT)
        }




        backIV.setOnClickListener {
            finish()
        }

        charmRL.setOnClickListener {
            val intent = Intent(context, CharmPointActivity::class.java)
            startActivityForResult(intent,CHARM_POINT)
        }


        phoneLL.setOnClickListener {
            val intent = Intent(context, PhoneCertiActivity::class.java)
            startActivity(intent)
        }
        saveLL.setOnClickListener {
            val intent = Intent(context, SaveJoinActivity::class.java)
            startActivity(intent)
        }
        emailLL.setOnClickListener {
            val intent = Intent(context, EmailConnectActivity::class.java)
            startActivity(intent)
        }

    }


    fun edit_info() {
        var intro = Utils.getString(introET)
       var join_language =   PrefUtils.setPreference(context, "join_language", adapterData.joinToString())

        val params = RequestParams()
        params.put("intro", intro)
        params.put("language", join_language)



        JoinAction.final_join(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    Log.d("결과",result.toString())
                    if ("ok" == result) {

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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CHARM_POINT    -> {
                if (resultCode == Activity.RESULT_OK) {
                    if(data != null) {
                        adapterData2.add( data.getStringExtra("charmPoint"))

                        charmAdapter.notifyDataSetChanged()
                    }
                }
            }
            SELECT_LANGUAGE_REQUST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if(data != null) {
                        adapterData.add(data.getStringExtra("selectedLanguage"))
                        languageAdapter.notifyDataSetChanged()
                    }
                }
            }
            HEIGHT_SELECT  -> {
                if (resultCode == Activity.RESULT_OK) {
                    if(data != null) {

                    }
                }
            }
            REGION_SELECT   -> {
                if (resultCode == Activity.RESULT_OK) {
                    if(data != null) {

                    }
                }
            }
            POLICY_SELECT   -> {
                if (resultCode == Activity.RESULT_OK) {
                    if(data != null) {

                    }
                }
            }
            BABY_SELECT    -> {
                if (resultCode == Activity.RESULT_OK) {
                    if(data != null) {

                    }
                }
            }
            ANIMAL_SELECT   -> {
                if (resultCode == Activity.RESULT_OK) {
                    if(data != null) {

                    }
                }
            }
            SMOKE_SELECT   -> {
                if (resultCode == Activity.RESULT_OK) {
                    if(data != null) {

                    }
                }
            }
            DRINK_SELECT   -> {
                if (resultCode == Activity.RESULT_OK) {
                    if(data != null) {

                    }
                }
            }
            HEALTH_SELECT   -> {
                if (resultCode == Activity.RESULT_OK) {
                    if(data != null) {

                    }
                }
            }
            SPORTS_SELECT    -> {
                if (resultCode == Activity.RESULT_OK) {
                    if(data != null) {

                    }
                }
            }
            WORK_SELECT    -> {
                if (resultCode == Activity.RESULT_OK) {
                    if(data != null) {

                    }
                }
            }


        }
    }

}
