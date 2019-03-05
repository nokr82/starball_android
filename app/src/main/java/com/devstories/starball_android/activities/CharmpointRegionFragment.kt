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
import kotlinx.android.synthetic.main.fragment_charmpoint_region.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

//메세지관리(메시지작성화면)

class CharmpointRegionFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    var region = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_charmpoint_region, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        region1TV.setOnClickListener {
            setmenu()
            region = Utils.getString(region1TV)
            edit_info()
            region1TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region2TV.setOnClickListener {
            setmenu()
            region = Utils.getString(region2TV)
            edit_info()
            region2TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region3TV.setOnClickListener {
            setmenu()
            region = Utils.getString(region3TV)
            edit_info()
            region3TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region4TV.setOnClickListener {
            setmenu()
            region = Utils.getString(region4TV)
            edit_info()
            region4TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region5TV.setOnClickListener {
            setmenu()
            region = Utils.getString(region5TV)
            edit_info()
            region5TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region6TV.setOnClickListener {
            setmenu()
            region = Utils.getString(region6TV)
            edit_info()
            region6TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region7TV.setOnClickListener {
            setmenu()
            region = Utils.getString(region7TV)
            edit_info()
            region7TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region8TV.setOnClickListener {
            setmenu()
            region = Utils.getString(region8TV)
            edit_info()
            region8TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region9TV.setOnClickListener {
            setmenu()
            region = Utils.getString(region9TV)
            edit_info()
            region9TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region10TV.setOnClickListener {
            setmenu()
            region = Utils.getString(region10TV)
            edit_info()
            region10TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region11TV.setOnClickListener {
            setmenu()
            region = Utils.getString(region11TV)
            edit_info()
            region11TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }






        skipTV.setOnClickListener {
            var intent = Intent()
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        get_info()
    }


    fun setmenu(){
        region1TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        region2TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        region3TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        region4TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        region5TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        region6TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        region7TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        region8TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        region9TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        region10TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        region11TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
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

                        var region = Utils.getString(member,"region")

                        if (region == Utils.getString(region1TV)){
                            region1TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (region == Utils.getString(region2TV)){
                            region2TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (region ==Utils.getString(region3TV)){
                            region3TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (region == Utils.getString(region4TV)){
                            region4TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (region == Utils.getString(region5TV)){
                            region5TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (region == Utils.getString(region6TV)){
                            region6TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (region == Utils.getString(region7TV)){
                            region7TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (region == Utils.getString(region8TV)){
                            region8TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (region == Utils.getString(region9TV)){
                            region9TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (region == Utils.getString(region10TV)){
                            region10TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (region == Utils.getString(region11TV)){
                            region11TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
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


    fun edit_info() {
        var member_id = PrefUtils.getIntPreference(context, "member_id")
        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("region", region)

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

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}
