package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_search_setting.*
import kotlinx.android.synthetic.main.dlg_crush.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class DlgCrushActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var starball = -1

    var use_starball = -1

    var like_member_id = -1
    private val _active = true




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_crush)

        this.context = this
        progressDialog = ProgressDialog(context)

        like_member_id = intent.getIntExtra("like_member_id",-1)

        Log.d("라이크",like_member_id.toString())

        get_info()
        noTV.setOnClickListener {
            finish()
        }
        payTV.setOnClickListener {
            val intent = Intent(context, StarballPayActivity::class.java)
            startActivity(intent)
        }

        giftTV.setOnClickListener {
            val intent = Intent(context, StarballPayActivity::class.java)
            startActivity(intent)
        }
        starball2IV.setOnClickListener {
            setmenu()
            starball2IV.setImageResource(R.mipmap.radio_on)
            use_starball = 2
        }
        starball5IV.setOnClickListener {
            setmenu()
            starball5IV.setImageResource(R.mipmap.radio_on)
            use_starball = 5
        }
        starball10IV.setOnClickListener {
            setmenu()
            starball10IV.setImageResource(R.mipmap.radio_on)
            use_starball = 10
        }
        starball20IV.setOnClickListener {
            setmenu()
            starball20IV.setImageResource(R.mipmap.radio_on)
            use_starball = 20
        }
        starball50IV.setOnClickListener {
            setmenu()
            starball50IV.setImageResource(R.mipmap.radio_on)
            use_starball = 50
        }

        giftTV.setOnClickListener {
            if (use_starball<0){
                Toast.makeText(context,"스타볼을 입력해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (like_member_id>0){
                use_starball()
            }else{
                Toast.makeText(context,"오류 발생",Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun setmenu() {
        starball2IV.setImageResource(R.mipmap.radio_off)
        starball5IV.setImageResource(R.mipmap.radio_off)
        starball10IV.setImageResource(R.mipmap.radio_off)
        starball20IV.setImageResource(R.mipmap.radio_off)
        starball50IV.setImageResource(R.mipmap.radio_off)
    }
    fun get_info() {
        val member_id = PrefUtils.getIntPreference(context,"member_id")
        val params = RequestParams()
        params.put("member_id", member_id)

        MemberAction.get_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                Log.d("스타볼",response.toString())
                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        starball = Utils.getInt(response, "starball")
                        left_starballTV.text = starball.toString()

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

    fun use_starball() {
        val member_id = PrefUtils.getIntPreference(context,"member_id")
        if (use_starballET.length()>0){
            use_starball = Utils.getInt(use_starballET)
        }

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("like_member_id",like_member_id)
        params.put("type",2)
        params.put("starball", use_starball)

        MemberAction.like(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                Log.d("스타볼사용",response.toString())
                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {

                        var intent = Intent()
                        intent.action = "STARBALL_USE"
                        sendBroadcast(intent)
                        finish()
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
