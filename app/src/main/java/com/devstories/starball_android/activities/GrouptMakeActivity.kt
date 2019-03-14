package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.ChattingAction
import com.devstories.starball_android.actions.ChattingAction.chat_user_list
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

class GrouptMakeActivity : RootActivity()  {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var GroupAdapter: GroupAdapter
    var adapterdata  = ArrayList<JSONObject>()
    var member_list  = ArrayList<String>()
    var size = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_make)
        this.context = this
        progressDialog = ProgressDialog(context)

        chat_user_list()
        GroupAdapter = GroupAdapter(context, R.layout.item_group_profile, adapterdata)
        groupLV.adapter = GroupAdapter

        nextTV.setOnClickListener {

            for (i in 0 until adapterdata.size) {
                var json = adapterdata[i] as JSONObject
                val Member = json.getJSONObject("Member")
                var member_id = Utils.getString(Member, "id")
                val isSelectedOp = json.getBoolean("isSelectedOp")
                if (isSelectedOp){
                    member_list.add(member_id)
                }else{

                }
            }
            Log.d("아뒤",member_list.toString())

            val intent = Intent(context, GroupChattingActivity::class.java)
            intent.putExtra("member_list",member_list)
            startActivity(intent)

        }

        groupLV.setOnItemClickListener { parent, view, position, id ->

            val json = adapterdata.get(position)
            Log.d("클릭",json.toString())
            val Member = json.getJSONObject("Member")
            val isSelectedOp = json.getBoolean("isSelectedOp")
            var member_id = Utils.getString(Member, "id")


            if (isSelectedOp){
                adapterdata[position].put("isSelectedOp",false)
                size--
            }else{
                if (size>2){
                    Toast.makeText(context,"그룹당 3명까지만 선택할수있습니다",Toast.LENGTH_SHORT).show()
                    return@setOnItemClickListener
                }
                adapterdata[position].put("isSelectedOp",true)
                size++
            }

            Log.d("사이즈",size.toString())
            Log.d("클릭",member_list.toString())
            GroupAdapter.notifyDataSetChanged()

        }


        backIV.setOnClickListener {
            finish()
        }





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

                        val chats = response.getJSONArray("chat")
                        adapterdata.clear()
                        for (i in 0..chats.length() - 1) {
                            var json = chats[i] as JSONObject
                            Log.d("제이슨",json.toString())
                            adapterdata.add(json)
                            adapterdata[i].put("isSelectedOp",false)
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
