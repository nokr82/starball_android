package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.DailyAction
import com.devstories.starball_android.adapter.DaillyAdapter
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_daily_moment_view_list.*
import org.json.JSONException
import org.json.JSONObject

class DailyMomentViewListActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var DaillyAdapter: DaillyAdapter

    var page = 1
    var totalPage = 1
    var daily_member_id = -1

    var adapterdata = ArrayList<JSONObject>()

    lateinit var header: View
    lateinit var backIV: ImageView
    lateinit var timelineTV: TextView
    lateinit var secretTV: TextView
    lateinit var profileIV: ImageView
    lateinit var starballIV: ImageView
    lateinit var nameTV: TextView
    lateinit var createdTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_moment_view_list)
        this.context = this
        progressDialog = ProgressDialog(context)

        daily_member_id = intent.getIntExtra("daily_member_id", -1)


//        DaillyAdapter = DaillyAdapter(context,R.layout.item_view_daily_list, 6)
//        dailyLV.adapter = DaillyAdapter


        header = View.inflate(this, R.layout.item_daily_moment_view_head, null)
        backIV = header.findViewById(R.id.backIV)
        nameTV  = header.findViewById(R.id.nameTV)
        timelineTV = header.findViewById(R.id.timelineTV)
        secretTV = header.findViewById(R.id.secretTV)
        profileIV= header.findViewById(R.id.profileIV)
        starballIV =  header.findViewById(R.id.starballIV)
        createdTV=  header.findViewById(R.id.starballIV)
        dailyLV.addHeaderView(header)

        my_daily_list()

        DaillyAdapter = DaillyAdapter(context, R.layout.item_daily_list, adapterdata,DailyMomentListActivity(),this,2)
        dailyLV.adapter = DaillyAdapter

        timelineTV.setOnClickListener {
            val intent = Intent(context, DailyMomentListActivity::class.java)
            startActivity(intent)
        }

        starballIV.setOnClickListener {
            val intent = Intent(context, DlgCrushActivity::class.java)
            startActivity(intent)
        }

        secretTV.setOnClickListener {
            val intent = Intent(context, DailyMomentSubVIewListActivity::class.java)
            startActivity(intent)
        }
        profileIV.setOnClickListener {
            val intent = Intent(context, DlgAlbumPayActivity::class.java)
            startActivity(intent)
        }

        backIV.setOnClickListener {
            finish()
        }

    }


    fun my_daily_list() {
        var member_id = PrefUtils.getIntPreference(context, "member_id")
        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("daily_member_id", daily_member_id)
        params.put("page", page)

        DailyAction.daily(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                Log.d("아우스0",response.toString())
                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        totalPage = Utils.getInt(response, "totalPage")
                        if (page == 1) {
                            adapterdata.clear()
                        }
                        val Like = response.getJSONObject("Like")
                        var created_at = Utils.getString(Like,"created_at")

                        createdTV.text = created_at.substring(0, 10).replace("-", ".")

                        val list = response.getJSONArray("list")
                        for (i in 0..list.length() - 1) {
                            var json = list[i] as JSONObject
                            Log.d("제이슨", json.toString())
                            adapterdata.add(json)
                        }
                        DaillyAdapter.notifyDataSetChanged()

                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                System.out.println(responseString);
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

                System.out.println(responseString);

                throwable.printStackTrace()
                error()
            }


            override fun onStart() {
                // show dialog
//                if (progressDialog != null) {
//                    progressDialog!!.show()
//                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }

    fun like(content_id:Int) {
        var member_id = PrefUtils.getIntPreference(context, "member_id")
        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("content_id", content_id)

        DailyAction.like(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                Log.d("아우스0",response.toString())
                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        my_daily_list()
                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                System.out.println(responseString);
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

                System.out.println(responseString);

                throwable.printStackTrace()
                error()
            }


            override fun onStart() {
                // show dialog
//                if (progressDialog != null) {
//                    progressDialog!!.show()
//                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }

}
