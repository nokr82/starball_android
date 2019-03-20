package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.ChattingAction
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.adapter.GroupAdapter
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_group_make.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class GrouptMakeActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var GroupAdapter: GroupAdapter
    var adapterdata = ArrayList<JSONObject>()
    var member_list = ArrayList<String>()
    var size = 0

    var page = 1
    var totalPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_make)
        this.context = this
        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        chat_user_list()
        GroupAdapter = GroupAdapter(context, R.layout.item_group_profile, adapterdata)
        groupLV.adapter = GroupAdapter
        groupLV.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(p0: AbsListView?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onScrollStateChanged(absListView: AbsListView, newState: Int) {
                if (!groupLV.canScrollVertically(-1)) {

                } else if (!groupLV.canScrollVertically(1)) {
                    if (totalPage > page) {
                        page++
                        chat_user_list()
                    }
                } else {
                }
            }
        })
        nextTV.setOnClickListener {
            group_make()
        }

        groupLV.setOnItemClickListener { parent, view, position, id ->

            val json = adapterdata.get(position)
            Log.d("클릭", json.toString())
            val Member = json.getJSONObject("Member")
            val isSelectedOp = json.getBoolean("isSelectedOp")
            var member_id = Utils.getString(Member, "id")


            if (isSelectedOp) {
                adapterdata[position].put("isSelectedOp", false)
                size--
            } else {
                if (size > 2) {
                    Toast.makeText(context, "그룹당 3명까지만 선택할수있습니다", Toast.LENGTH_SHORT).show()
                    return@setOnItemClickListener
                }
                adapterdata[position].put("isSelectedOp", true)
                size++
            }

            GroupAdapter.notifyDataSetChanged()

        }



        backIV.setOnClickListener {
            finish()
        }

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

    fun group_make() {

        for (i in 0 until adapterdata.size) {
            var json = adapterdata[i] as JSONObject
            val Member = json.getJSONObject("Member")
            var member_id = Utils.getString(Member, "id")
            val isSelectedOp = json.getBoolean("isSelectedOp")
            if (isSelectedOp) {
                member_list.add(member_id)
            } else {

            }
        }

        var member_id = PrefUtils.getIntPreference(context, "member_id")
        var title = Utils.getString(titleET)
        if (title.length < 1) {
            Toast.makeText(context, "그룹명을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (member_list.size < 2) {
            Toast.makeText(context, "두명이상 선택해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("아뒤", member_list.toString())
        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("title", title)
        params.put("attend_member_id", member_list.joinToString())

        ChattingAction.add_group(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                Log.d("그룹", response.toString())
                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        val intent = Intent(context, GroupChattingActivity::class.java)
                        startActivity(intent)
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

    fun chat_user_list() {

        var member_id = PrefUtils.getIntPreference(context, "member_id")

        val params = RequestParams()
        params.put("member_id", member_id)

        ChattingAction.chat_user_list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    Log.d("결과", response.toString())
                    if ("ok" == result) {

                        page = Utils.getInt(response, "page")
                        totalPage = Utils.getInt(response, "totalPage")

                        val chats = response.getJSONArray("chat")
                        if (page == 1) {
                            adapterdata.clear()
                            GroupAdapter.notifyDataSetChanged()
                        }
                        for (i in 0..chats.length() - 1) {
                            var json = chats[i] as JSONObject
                            Log.d("제이슨", json.toString())
                            adapterdata.add(json)
                            adapterdata[i].put("isSelectedOp", false)
                        }
                        GroupAdapter.notifyDataSetChanged()


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
