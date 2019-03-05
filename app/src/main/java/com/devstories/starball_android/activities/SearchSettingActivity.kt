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

class SearchSettingActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    private val SELECT_LANGUAGE_REQUST_CODE = 1004


    lateinit var languageAdapter: LanguageAdapter


    var adapterData = ArrayList<String>()

    var search_gender = ""
    var gender = ""


    var vvipview_yn = "N"
    var saveview_yn = "N"
    var other_nation = "N"




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_setting)
        this.context = this
        progressDialog = ProgressDialog(context)

        languageAdapter = LanguageAdapter(context, R.layout.item_language, adapterData)
        languageGV.adapter = languageAdapter
        languageGV.setOnItemClickListener { parent, view, position, id ->
            adapterData.removeAt(position)

            languageAdapter.notifyDataSetChanged()
        }

        click()

    }
    fun click(){

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


        menIV.setOnClickListener {
            setmenu()
            menIV.setImageResource(R.mipmap.comm_check_on)
            search_gender = "M"
        }
        girlIV.setOnClickListener {
            setmenu()
            girlIV.setImageResource(R.mipmap.comm_check_on)
            search_gender = "F"
        }


        nationLL.setOnClickListener {
            if (other_nation =="Y"){
                other_nation = "N"
                nationIV.setImageResource(R.mipmap.comm_check_off)
            }else{
                other_nation = "Y"
                nationIV.setImageResource(R.mipmap.comm_check_on)
            }
        }
        saveLL.setOnClickListener {
            if (saveview_yn =="Y"){
                saveview_yn = "N"
                saveIV.setImageResource(R.mipmap.comm_check_off)
            }else{
                saveview_yn = "Y"
                saveIV.setImageResource(R.mipmap.comm_check_on)
            }
        }
        vvipLL.setOnClickListener {
            if (vvipview_yn =="Y"){
                vvipview_yn = "N"
                vvipIV.setImageResource(R.mipmap.comm_check_off)
            }else{
                vvipview_yn = "Y"
                vvipIV.setImageResource(R.mipmap.comm_check_on)
            }
        }


        nextTV.setOnClickListener {
            edit_search()
        }

        get_info()
    }

    fun setmenu(){
        menIV.setImageResource(R.mipmap.comm_check_off)
        girlIV.setImageResource(R.mipmap.comm_check_off)
    }



    fun edit_search() {
        var member_id = PrefUtils.getIntPreference(context, "member_id")

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("vvipview_yn", vvipview_yn)
        params.put("saveview_yn", saveview_yn)
        params.put("other_nation", other_nation)
        params.put("search_gender", search_gender)

        JoinAction.final_join(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    Log.d("결과",result.toString())
                    if ("ok" == result) {
                        Toast.makeText(context,"변경되었습니다",Toast.LENGTH_SHORT).show()
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

                        vvipview_yn = Utils.getString(member,"vvipview_yn")
                        saveview_yn = Utils.getString(member,"saveview_yn")
                        other_nation = Utils.getString(member,"other_nation")
                        gender = Utils.getString(member,"gender")
                        search_gender = Utils.getString(member,"search_gender")




                        if (search_gender =="F"){
                            girlIV.setImageResource(R.mipmap.comm_check_on)
                        }else if (search_gender =="M"){
                            menIV.setImageResource(R.mipmap.comm_check_on)
                        }


                        if (vvipview_yn =="Y"){
                            vvipIV.setImageResource(R.mipmap.comm_check_on)
                        }else{
                            vvipIV.setImageResource(R.mipmap.comm_check_off)
                        }
                        if (saveview_yn =="Y"){
                            saveIV.setImageResource(R.mipmap.comm_check_on)
                        }else{
                            saveIV.setImageResource(R.mipmap.comm_check_off)
                        }
                        if (other_nation =="Y"){
                            nationIV.setImageResource(R.mipmap.comm_check_on)
                        }else{
                            nationIV.setImageResource(R.mipmap.comm_check_off)
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
                    if(data != null) {
                        adapterData.add(data.getStringExtra("selectedLanguage"))
                        languageAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }


}
