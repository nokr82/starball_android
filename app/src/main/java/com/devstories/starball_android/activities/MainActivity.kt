package com.devstories.starball_android.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.devstories.starball_android.swipestack.SwipeStack
import com.devstories.starball_android.swipestack.SwipeStackAdapter
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_cash_request.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MainActivity : RootActivity() {

    lateinit var mContext:Context
    private var progressDialog: ProgressDialog? = null

    internal var MAX_PAGE = 3                         //View Pager의 총 페이지 갯수를 나타내는 변수 선언
    internal var cur_fragment = Fragment()   //현재 Viewpager가 가리키는 Fragment를 받을 변수 선언

    var data = ArrayList<JSONObject>()
    lateinit var swipeStackAdapter:SwipeStackAdapter

    private val topLogoTimer = Timer()
    private val rightBottomStarballTimer = Timer()
    //현재보유스타볼
    var starball = -1

    internal var usestarballreciver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                val intent = Intent(context, MatchedActivity::class.java)
                startActivity(intent)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.devstories.starball_android.R.layout.activity_main)

        mContext = this

        progressDialog = ProgressDialog(mContext, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        var filter1 = IntentFilter("STARBALL_USE")
        registerReceiver(usestarballreciver, filter1)

        chatIV.setOnClickListener {
            val intent = Intent(this, ChattingActivity::class.java)
            startActivity(intent)
        }

        charIV.setOnClickListener {
            val intent = Intent(this, SettingMainActivity::class.java)
            startActivity(intent)
        }

        prevIV.setOnClickListener {
            val intent = Intent(this, StarballReceivedActivity::class.java)
            startActivity(intent)
        }

        get_info()

        val swipeStack = swipeStack as SwipeStack
        swipeStackAdapter = SwipeStackAdapter(mContext, this, data, swipeStack.getmSwipeHelper())
        swipeStack.adapter = swipeStackAdapter
        swipeStack.setListener(object : SwipeStack.SwipeStackListener {
            override fun onStackEmpty() {
                loadData()
            }

            override fun onViewSwipedToTop(position: Int) {

            }

            override fun onViewSwipedToBottom(position: Int) {

            }

            override fun onViewSwipedToLeft(position: Int) {

            }

            override fun onViewSwipedToRight(position: Int) {

            }
        })

        startAnimation()

        loadData()

    }


    private fun get_info() {
        val member_id = PrefUtils.getIntPreference(mContext,"member_id")
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
                        Log.d("스타볼",starball.toString())

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
                Utils.alert(mContext, "조회중 장애가 발생하였습니다.")
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

    private fun loadData() {

        val member_id = PrefUtils.getIntPreference(mContext,"member_id")

        val params = RequestParams()
        params.put("member_id", member_id)

        MemberAction.list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {

                    println(response)

                    val result =   Utils.getString(response,"result")
                    if ("ok" == result) {

                        val members = response!!.getJSONArray("members")
                        for (idx in 0 until members.length()){
                            val member = members.get(idx) as JSONObject
                            member.put("starball",starball)
                            member.put("pages", member.getJSONArray("profiles"))
                            data.add(member)
                        }
                    }

                    swipeStackAdapter.notifyDataSetChanged()

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
                Utils.alert(mContext, "조회중 장애가 발생하였습니다.")
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
                    // progressDialog!!.show()
                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })

    }

    private fun startAnimation() {

        val topLogoPeriod = 1000 * 10L
        topLogoTimer.scheduleAtFixedRate(object:TimerTask() {
            override fun run() {
                runOnUiThread {
                    animateTopLogo()
                }
            }
        }, topLogoPeriod, topLogoPeriod)

        rightBottomStarballTimer.scheduleAtFixedRate(object:TimerTask() {
            override fun run() {
                runOnUiThread {
                    // val intent = Intent("ROTATE_RIGHT_BOTTOM_STARBALL")
                    // sendBroadcast(intent)
                }
            }
        }, 0, 1000 * 5)

    }

    fun animateTopLogo() {

        val oa1 = ObjectAnimator.ofFloat(logoIV, "scaleY", 1f, 0f)
        val oa2 = ObjectAnimator.ofFloat(logoIV, "scaleY", 0f, 1f)
        oa1.interpolator = DecelerateInterpolator()
        oa2.interpolator = AccelerateDecelerateInterpolator()
        oa1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                oa2.start()
            }
        })
        oa1.start()
    }

}
