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
import com.devstories.starball_android.R
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
            region = Utils.getString(region1TV)
            edit_info()
            region1TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region2TV.setOnClickListener {
            region = Utils.getString(region2TV)
            edit_info()
            region2TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region3TV.setOnClickListener {
            region = Utils.getString(region3TV)
            edit_info()
            region3TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region4TV.setOnClickListener {
            region = Utils.getString(region4TV)
            edit_info()
            region4TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region5TV.setOnClickListener {
            region = Utils.getString(region5TV)
            edit_info()
            region5TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region6TV.setOnClickListener {
            region = Utils.getString(region6TV)
            edit_info()
            region6TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region7TV.setOnClickListener {
            region = Utils.getString(region7TV)
            edit_info()
            region7TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region8TV.setOnClickListener {
            region = Utils.getString(region8TV)
            edit_info()
            region8TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region9TV.setOnClickListener {
            region = Utils.getString(region9TV)
            edit_info()
            region9TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region10TV.setOnClickListener {
            region = Utils.getString(region10TV)
            edit_info()
            region10TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region11TV.setOnClickListener {
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

    }
    fun edit_info() {
        val params = RequestParams()
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

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}
