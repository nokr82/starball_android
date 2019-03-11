package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.adapter.StarballHistoryAdapter
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.dlg_starball_history.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class DlgStarballHistoryActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    private val _active = true

    private lateinit var adapter: StarballHistoryAdapter
    private var adapterData = ArrayList<JSONObject>()

    private var member_id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_starball_history)

        this.context = this
        progressDialog = ProgressDialog(context)

        member_id = PrefUtils.getIntPreference(context, "member_id")

        cashTV.setOnClickListener {
            val intent = Intent(context, CashRequestActivity::class.java)
            startActivity(intent)
        }

        closeTV.setOnClickListener {
            finish()
        }
        payTV.setOnClickListener {
            val intent = Intent(context, StarballPayActivity::class.java)
            startActivity(intent)
        }

        adapter = StarballHistoryAdapter(context, R.layout.item_starball_history, adapterData)
        listLV.adapter = adapter


        loadData()

    }

    fun loadData() {

        val params = RequestParams()
        params.put("member_id", member_id)

        MemberAction.starball_history(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        var page = Utils.getInt(response, "page")
                        var totalPage = Utils.getInt(response, "totalPage")

                        if (page == 1) {
                            adapterData.clear()
                            adapter.notifyDataSetChanged()
                        }

                        val list = response.getJSONArray("history")

                        for (i in 0 until list.length()) {
                            adapterData.add(list[i] as JSONObject)
                        }

                        adapter.notifyDataSetChanged()

                    } else if ("empty" == result) {

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

}
