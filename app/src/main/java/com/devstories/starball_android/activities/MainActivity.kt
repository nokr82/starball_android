package com.devstories.starball_android.activities

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.*
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.base.*
import com.devstories.starball_android.swipestack.SwipeStack
import com.devstories.starball_android.swipestack.SwipeStackAdapter
import com.google.android.gms.location.*
import com.google.firebase.iid.FirebaseInstanceId
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MainActivity : RootActivity() {

    val REQUEST_ACCESS_COARSE_LOCATION = 101
    val REQUEST_FINE_LOCATION = 100

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

    var latitude = 37.5203175
    var longitude = 126.9107831

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    internal var usestarballreciver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                val intent = Intent(context, MatchedActivity::class.java)
                startActivity(intent)
            }
        }
    }

    var member_id = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.devstories.starball_android.R.layout.activity_main)

        mContext = this

        progressDialog = ProgressDialog(mContext, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        member_id = PrefUtils.getIntPreference(mContext, "member_id")

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {

                fusedLocationClient.removeLocationUpdates(locationCallback)

                locationResult ?: return
                onLocationUpdated(locationResult?.lastLocation)
            }
        }


        updateToken()

        initGPS()

        get_info()

        AdmobUtils.loadAd(mContext) {
            println("admob closed")
            // finish()
        }

        get_proposes()

    }


    private fun get_info() {

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

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("latitude", latitude)
        params.put("longitude", longitude)

        MemberAction.list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {

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

    private fun initGPS() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION)
        } else {
            checkGPs()
        }
    }

    private fun checkGPs() {
        if (Utils.availableLocationService(mContext)) {
            startLocation()
        } else {
            gpsCheckAlert.sendEmptyMessage(0)
        }
    }

    private var gpsCheckAlert: Handler = Handler {
        latitude = -99999.0
        longitude = -99999.0

        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("확인")
        builder.setMessage("위치 서비스 이용이 제한되어 있습니다.\n설정에서 위치 서비스 이용을 허용해주세요.")
        builder.setCancelable(true)
        builder.setNegativeButton("취소") { dialog, id ->
            dialog.cancel()

            latitude = 37.5203175
            longitude = 126.9107831

        }
        builder.setPositiveButton(getString(R.string.settings)) { dialog, id ->
            dialog.cancel()
            startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        val alert = builder.create()
        alert.show()

        return@Handler true
    }

    @SuppressLint("MissingPermission")
    private fun startLocation() {

        if (progressDialog != null) {
            // show dialog
            //progressDialog.setMessage("현재 위치 확인 중...");
            progressDialog!!.show()
        }

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10 * 1000  /* 10 secs */
        locationRequest.fastestInterval = 2000 /* 2 sec */

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

    }

    private fun loadPermissions(perm: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(mContext, perm) !== PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(perm), requestCode)
        } else {
            if (Manifest.permission.ACCESS_FINE_LOCATION == perm) {
                loadPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_ACCESS_COARSE_LOCATION)
            } else if (Manifest.permission.ACCESS_COARSE_LOCATION == perm) {
                checkGPs()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_FINE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadPermissions(android.Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_ACCESS_COARSE_LOCATION)
            }
            REQUEST_ACCESS_COARSE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkGPs()
            }
        }

    }

    fun onLocationUpdated(location: Location?) {

        println("location : $location")

        if(isFinishing || isDestroyed) {
            return
        }

        if (location != null) {

            latitude = location.latitude
            longitude = location.longitude

            loadData()
        }

    }

    fun get_proposes() {

        val params = RequestParams()
        params.put("member_id", member_id)

        MemberAction.get_proposes(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                Log.d("스타볼",response.toString())
                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {

                        var list = response.getJSONArray("list")

                        for (i in 0 until list.length()) {
                            val data = list[i] as JSONObject

                            var intent = Intent(mContext, DlgProposeActivity::class.java)
                            intent.putExtra("propose_id", Utils.getInt(data, "id"))
                            startActivity(intent)

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

    private fun updateToken() {

        val member_id = PrefUtils.getIntPreference(mContext, "member_id")
        val member_token = FirebaseInstanceId.getInstance().getToken()

        if (member_id == -1 || null == member_token || "" == member_token || member_token.isEmpty()) {
            return
        }

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("token", member_token)
        params.put("device", Config.device)

        println("member_token : $member_token")
        println("device : ${Config.device}")

        MemberAction.regist_token(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {

                PrefUtils.setPreference(mContext, "token", member_token)

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                super.onSuccess(statusCode, headers, response)
            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {}

            private fun error() {

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseString: String?,
                throwable: Throwable
            ) {
                throwable.printStackTrace()
                error()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                throwable: Throwable,
                errorResponse: JSONObject?
            ) {
                throwable.printStackTrace()
                error()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                throwable: Throwable,
                errorResponse: JSONArray?
            ) {
                throwable.printStackTrace()
                error()
            }

            override fun onStart() {
            }

            override fun onFinish() {
            }
        })
    }

}
