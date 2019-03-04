package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.devstories.starball_android.Actions.JoinAction
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.LanguageAdapter
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

    var gender = ""

    var option = -1
    var option_list= ArrayList<String>()

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
            gender = "M"
        }
        girlIV.setOnClickListener {
            setmenu()
            girlIV.setImageResource(R.mipmap.comm_check_on)
            gender = "F"
        }
        nationLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected) {
                option_list.add("1")
                nationIV.setImageResource(R.mipmap.comm_check_on)
            } else {
                option_list.remove("1")
                nationIV.setImageResource(R.mipmap.comm_check_off)
            }
        }
        saveLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected) {
                option_list.add("2")
                saveIV.setImageResource(R.mipmap.comm_check_on)
            } else {
                option_list.remove("2")
                saveIV.setImageResource(R.mipmap.comm_check_off)
            }
        }
        vvipLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected) {
                option_list.add("3")
                vvipIV.setImageResource(R.mipmap.comm_check_on)
            } else {
                option_list.remove("3")
                vvipIV.setImageResource(R.mipmap.comm_check_off)
            }
        }


    }

    fun setmenu(){
        menIV.setImageResource(R.mipmap.comm_check_off)
        girlIV.setImageResource(R.mipmap.comm_check_off)
    }



    fun edit_search() {
        val params = RequestParams()
        params.put("gender", gender)
        params.put("option_list", option_list)

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
