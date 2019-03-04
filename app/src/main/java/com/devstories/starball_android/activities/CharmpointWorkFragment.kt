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
import com.devstories.starball_android.Actions.JoinAction
import com.devstories.starball_android.Actions.MemberAction
import com.devstories.starball_android.R
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_charmpoint_work.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

//메세지관리(메시지작성화면)

class CharmpointWorkFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    var work = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_charmpoint_work, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        work1TV.setOnClickListener {
            setmenu()
            work = Utils.getString(work1TV)
            edit_info()
            work1TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "WORK_CHANGE"
            myContext.sendBroadcast(intent)
        }
        work2TV.setOnClickListener {
            setmenu()
            work = Utils.getString(work2TV)
            edit_info()
            work2TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "WORK_CHANGE"
            myContext.sendBroadcast(intent)
        }
        work3TV.setOnClickListener {
            setmenu()
            work = Utils.getString(work3TV)
            edit_info()
            work3TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "WORK_CHANGE"
            myContext.sendBroadcast(intent)
        }
        work4TV.setOnClickListener {
            setmenu()
            work = Utils.getString(work4TV)
            edit_info()
            work4TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "WORK_CHANGE"
            myContext.sendBroadcast(intent)
        }
        work5TV.setOnClickListener {
            setmenu()
            work = Utils.getString(work5TV)
            edit_info()
            work5TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "WORK_CHANGE"
            myContext.sendBroadcast(intent)
        }
        work6TV.setOnClickListener {
            setmenu()
            work = Utils.getString(work6TV)
            edit_info()
            work6TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "WORK_CHANGE"
            myContext.sendBroadcast(intent)
        }


        skipTV.setOnClickListener {
            var intent = Intent()
            intent.action = "WORK_CHANGE"
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

                        var work = Utils.getString(member,"work")

                        if (work == Utils.getString(work1TV)){
                            work1TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (work == Utils.getString(work2TV)){
                            work2TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (work ==Utils.getString(work3TV)){
                            work3TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (work == Utils.getString(work4TV)){
                            work4TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (work == Utils.getString(work5TV)){
                            work5TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (work ==Utils.getString(work6TV)){
                            work6TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
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
        work1TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        work2TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        work3TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        work4TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        work5TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        work6TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
    }


    fun edit_info() {
        var member_id = PrefUtils.getIntPreference(context, "member_id")
        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("work", work)

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
