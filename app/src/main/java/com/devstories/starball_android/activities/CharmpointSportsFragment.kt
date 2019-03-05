package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devstories.starball_android.actions.JoinAction
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.R
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_charmpoint_sports.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

//메세지관리(메시지작성화면)

class CharmpointSportsFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    var sports = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_charmpoint_sports, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sport1TV.setOnClickListener {
            setmenu()
            sports = Utils.getString(sport1TV)
            sport1TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "SPORTS_CHANGE"
            myContext.sendBroadcast(intent)
        }
        sport2TV.setOnClickListener {
            setmenu()
            sports = Utils.getString(sport2TV)
            edit_info()
            sport2TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "SPORTS_CHANGE"
            myContext.sendBroadcast(intent)
        }
        sport3TV.setOnClickListener {
            setmenu()
            sports = Utils.getString(sport3TV)
            edit_info()
            sport3TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "SPORTS_CHANGE"
            myContext.sendBroadcast(intent)
        }
        sport4TV.setOnClickListener {
            setmenu()
            sports = Utils.getString(sport4TV)
            edit_info()
            sport4TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "SPORTS_CHANGE"
            myContext.sendBroadcast(intent)
        }
        sport5TV.setOnClickListener {
            setmenu()
            sports = Utils.getString(sport5TV)
            edit_info()
            sport5TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "SPORTS_CHANGE"
            myContext.sendBroadcast(intent)
        }
        sport6TV.setOnClickListener {
            setmenu()
            sports = Utils.getString(sport6TV)
            edit_info()
            sport6TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "SPORTS_CHANGE"
            myContext.sendBroadcast(intent)
        }
        sport7TV.setOnClickListener {
            setmenu()
            sports = Utils.getString(sport7TV)
            edit_info()
            sport7TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "SPORTS_CHANGE"
            myContext.sendBroadcast(intent)
        }


        skipTV.setOnClickListener {
            var intent = Intent()
            intent.action = "SPORTS_CHANGE"
            myContext.sendBroadcast(intent)
        }
        get_info()
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

                        var sport = Utils.getString(member,"sport")

                        if (sport == Utils.getString(sport1TV)){
                            sport1TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (sport == Utils.getString(sport2TV)){
                            sport2TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (sport ==Utils.getString(sport3TV)){
                            sport3TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (sport == Utils.getString(sport4TV)){
                            sport4TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (sport == Utils.getString(sport5TV)){
                            sport5TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (sport ==Utils.getString(sport6TV)){
                            sport6TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (sport == Utils.getString(sport7TV)){
                            sport7TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else{

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
                Utils.alert(myContext, "조회중 장애가 발생하였습니다.")
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
    fun setmenu(){
        sport1TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        sport2TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        sport3TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        sport4TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        sport5TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        sport6TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        sport7TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
    }
    fun edit_info() {
        var member_id = PrefUtils.getIntPreference(myContext, "member_id")
        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("sport", sports)

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
                Utils.alert(myContext, "조회중 장애가 발생하였습니다.")
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
    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}
