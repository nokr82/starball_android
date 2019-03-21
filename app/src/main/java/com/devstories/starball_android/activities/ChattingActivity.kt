package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_chatting.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class ChattingActivity : FragmentActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var type = -1
    val ChattingFragment: ChattingFragment = ChattingFragment()
    val ChattingMatchFragment: ChattingMatchFragment = ChattingMatchFragment()
    val ChattingCrushFragment: ChattingCrushFragment = ChattingCrushFragment()
    val ChattingSendCrushFragment: ChattingSendCrushFragment = ChattingSendCrushFragment()

    var member_id = -1

    internal var loungeCountReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {

                println("LOUNGE_COUNT::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")

                loadData()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        var filter1 = IntentFilter("LOUNGE_COUNT")
        registerReceiver(loungeCountReceiver, filter1)

        this.context = this
        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        member_id = PrefUtils.getIntPreference(context, "member_id")

        supportFragmentManager.beginTransaction().replace(R.id.chattingFL, ChattingFragment).commit()

        click()

        loadData()
    }

    fun loadData() {

        val params = RequestParams()
        params.put("member_id", member_id)

        MemberAction.lounge_count(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        val send_like_read = Utils.getInt(response, "send_like_read")
                        val receive_like_read = Utils.getInt(response, "receive_like_read")
                        val match_read = Utils.getInt(response, "match_read")
                        val chatting_read = Utils.getInt(response, "chatting_read")

                        if (send_like_read > 0) {
                            newSendIV.visibility = View.VISIBLE
                        } else {
                            newSendIV.visibility = View.GONE
                        }

                        if (receive_like_read > 0) {
                            newReceiveIV.visibility = View.VISIBLE
                        } else {
                            newReceiveIV.visibility = View.GONE
                        }

                        if (match_read > 0) {
                            newMatchIV.visibility = View.VISIBLE
                        } else {
                            newMatchIV.visibility = View.GONE
                        }

                        if (chatting_read > 0) {
                            newChattingIV.visibility = View.VISIBLE
                        } else {
                            newChattingIV.visibility = View.GONE
                        }

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
                Utils.alert(context, getString(R.string.api_error))
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseString: String?, throwable: Throwable) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

//                 System.out.println(responseString);

                throwable.printStackTrace()
                error()
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                throwable.printStackTrace()
                error()
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONArray?) {
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

    fun click() {

        backIV.setOnClickListener {
            finish()
        }

        timeIV.setOnClickListener {
            val intent = Intent(context, DailyMomentListActivity::class.java)
            startActivity(intent)
        }

        clickmoreIV.setOnClickListener {
            menuLL.visibility = View.GONE
            clickLL.visibility = View.GONE
            eventLL.visibility = View.VISIBLE
        }


        storyLL.setOnClickListener {
            eventLL.visibility = View.GONE
            clickLL.visibility = View.VISIBLE
            clickIV.setImageResource(R.mipmap.lounge_menu_chat)
            clickTV.text = "대화"
            menuLL.visibility = View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.chattingFL, ChattingFragment).commit()
        }
        matchLL.setOnClickListener {
            eventLL.visibility = View.GONE
            clickLL.visibility = View.VISIBLE
            clickIV.setImageResource(R.mipmap.lounge_menu_match)
            clickTV.text = "매치"
            menuLL.visibility = View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.chattingFL, ChattingMatchFragment).commit()
        }
        reciveLL.setOnClickListener {
            eventLL.visibility = View.GONE
            clickLL.visibility = View.VISIBLE
            clickIV.setImageResource(R.mipmap.lounge_menu_send)
            clickTV.text = "받은 호감"
            menuLL.visibility = View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.chattingFL, ChattingCrushFragment).commit()
        }
        sendLL.setOnClickListener {
            eventLL.visibility = View.GONE
            clickLL.visibility = View.VISIBLE
            clickIV.setImageResource(R.mipmap.lounge_menu_rece)
            clickTV.text = "보낸 호감"
            menuLL.visibility = View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.chattingFL, ChattingSendCrushFragment).commit()
        }
        event2LL.setOnClickListener {
            menuLL.visibility = View.VISIBLE
            eventLL.visibility = View.GONE
            clickLL.visibility = View.GONE
        }


        storyRL.setOnClickListener {
            menuLL.visibility = View.GONE
            eventLL.visibility = View.GONE
            clickLL.visibility = View.VISIBLE
            clickIV.setImageResource(R.mipmap.lounge_menu_chat)
            clickTV.text = "대화"
            supportFragmentManager.beginTransaction().replace(R.id.chattingFL, ChattingFragment).commit()
        }
        reciveRL.setOnClickListener {
            menuLL.visibility = View.GONE
            eventLL.visibility = View.GONE
            clickLL.visibility = View.VISIBLE
            clickIV.setImageResource(R.mipmap.lounge_menu_send)
            clickTV.text = "받은 호감"
            supportFragmentManager.beginTransaction().replace(R.id.chattingFL, ChattingCrushFragment).commit()
        }

        sendRL.setOnClickListener {
            menuLL.visibility = View.GONE
            eventLL.visibility = View.GONE
            clickLL.visibility = View.VISIBLE
            clickIV.setImageResource(R.mipmap.lounge_menu_rece)
            clickTV.text = "보낸 호감"
            supportFragmentManager.beginTransaction().replace(R.id.chattingFL, ChattingSendCrushFragment).commit()
        }
        matchRL.setOnClickListener {
            menuLL.visibility = View.GONE
            eventLL.visibility = View.GONE
            clickLL.visibility = View.VISIBLE
            clickIV.setImageResource(R.mipmap.lounge_menu_match)
            clickTV.text = "매치"
            supportFragmentManager.beginTransaction().replace(R.id.chattingFL, ChattingMatchFragment).commit()
        }

        moreRL.setOnClickListener {
            menuLL.visibility = View.GONE
            clickLL.visibility = View.GONE
            eventLL.visibility = View.VISIBLE
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        try {

            if (loungeCountReceiver != null) {
                unregisterReceiver(loungeCountReceiver)
            }

        } catch (e: IllegalArgumentException) {

        }

    }

}
