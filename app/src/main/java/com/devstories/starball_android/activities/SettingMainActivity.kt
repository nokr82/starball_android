package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.adapter.AdverAdapter
import com.devstories.starball_android.base.Config
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.nostra13.universalimageloader.core.ImageLoader
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_setting_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SettingMainActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    var adverImagePaths = ArrayList<String>()
    private lateinit var adverAdapter: AdverAdapter
    var adPosition = 0
    var popular_average: Double = 0.0

    var profiledata = ArrayList<JSONObject>()

    private var adTime = 0
    private var handler: Handler? = null

    var vip_yn = "N"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_main)
        this.context = this
        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        adverImagePaths.add("1")
        adverImagePaths.add("2")
        adverImagePaths.add("3")
        adverImagePaths.add("4")
        adverImagePaths.add("5")
        adverImagePaths.add("6")
        adverImagePaths.add("7")
        adverImagePaths.add("8")
        adverImagePaths.add("9")
        adverImagePaths.add("10")
        adverImagePaths.add("11")
        adverImagePaths.add("12")
        adverAdapter = AdverAdapter(this, adverImagePaths)
        adverVP.adapter = adverAdapter
        adverVP.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                adPosition = position
            }

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {
                circleLL.removeAllViews()
                for (i in adverImagePaths.indices) {
                    if (i == adPosition) {
                        addDot(circleLL, true)
                    } else {
                        addDot(circleLL, false)
                    }
                }
            }
        })
        timer()




        popularLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if (it.isSelected) {
                popular1LL.visibility = View.VISIBLE
                popular2LL.visibility = View.VISIBLE
            } else {
                popular1LL.visibility = View.GONE
                popular2LL.visibility = View.GONE
            }
        }

        finishIV.setOnClickListener {
            finish()
        }

        centerIV.setOnClickListener {
            val intent = Intent(context, DailyMomentListActivity::class.java)
            startActivity(intent)
        }


        starballLL.setOnClickListener {
            val intent = Intent(context, DlgStarballHistoryActivity::class.java)
            startActivity(intent)
        }
        editIV.setOnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            startActivity(intent)
        }

        searchIV.setOnClickListener {
            val intent = Intent(context, SearchSettingActivity::class.java)
            startActivity(intent)
        }

        settingIV.setOnClickListener {
            val intent = Intent(context, SettingActivity::class.java)
            startActivity(intent)
        }

        moreTV.setOnClickListener {
            val intent = Intent(context, StarballMemberShipActivity::class.java)
            startActivity(intent)
        }
        payLL.setOnClickListener {
            val intent = Intent(context, StarballPayActivity::class.java)
            startActivity(intent)
        }
        get_info()
    }

    override fun onResume() {
        super.onResume()
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
                        val my_membership = response.getString("my_membership")
                        val member = response.getJSONObject("member")
                        val name = Utils.getString(member, "name")
                        var profiles = response.getJSONArray("profiles")
                        popular_average = response.getDouble("popular_average")
                        ratting_bar.rating = popular_average.toFloat()

                        if (my_membership == "member") {
                            vip_yn = "Y"
                        } else {
                            vip_yn = "N"
                        }


//                         like_count = response.getInt("like_count")
                        for (i in 0 until profiles.length()) {
                            profiledata.add(profiles[i] as JSONObject)
                        }
                        var image_uri = Utils.getString(profiledata[0], "image_uri")
                        Log.d("이미지", profiledata[0].toString())

                        ImageLoader.getInstance()
                            .displayImage(Config.url + image_uri, profileIV, Utils.UILoptionsProfile)


                        nameTV.text = name

                        starballTV.text = Utils.getInt(response, "starball").toString()

                    } else {

                    }
                    updatePopular()
                    updateGrades()
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

    private fun updatePopular() {
        // Basic – 최소 3장만 채웠을 경우
        // Plus – 3장 이상 채웠을 경우
        // Premium – 9장 이상을 채웠을 경우
        // VVIP – 9장에 사진과 동영상으로 구성했을 경우
        popular_average
        if (popular_average <= 2) {
            drawBasic()
        } else if (popular_average <= 3) {
            drawPlus()
        } else if (popular_average <= 4) {

            drawPremium()
        } else if (popular_average <= 4.5 || vip_yn.equals("Y")) {
            drawVVIP()

        } else {
            initGrades()
        }

    }


    private fun updateGrades() {
        // Basic – 최소 3장만 채웠을 경우
        // Plus – 3장 이상 채웠을 경우
        // Premium – 9장 이상을 채웠을 경우
        // VVIP – 9장에 사진과 동영상으로 구성했을 경우

        val pictureCnt = profiledata.size
        if (pictureCnt == 3) {
            drawBasic2()
        } else if (pictureCnt in 4..8) {
            drawPlus2()
        } else if (pictureCnt > 8) {

            var videoContains = false
            for (picture in profiledata) {
                val mediaType = Utils.getInt(picture!!, "mediaType")
                if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                    videoContains = true
                    break
                }
            }

            if (videoContains) {
                drawVVIP2()
            } else {
                drawPremium2()
            }
        } else {
            initGrades2()
        }

    }

    private fun drawBasic() {
        initGrades()
        basicV.setBackgroundColor(Color.parseColor("#903ba0"))
        plusV.setBackgroundColor(Color.parseColor("#903ba0"))
    }

    private fun drawPlus() {
        initGrades()
        plusV.setBackgroundColor(Color.parseColor("#903ba0"))
        premiumV.setBackgroundColor(Color.parseColor("#903ba0"))
    }

    private fun drawPremium() {
        initGrades()
        premiumV.setBackgroundColor(Color.parseColor("#903ba0"))
        vvipV.setBackgroundColor(Color.parseColor("#903ba0"))
    }

    private fun drawVVIP() {
        initGrades()
        premiumV.setBackgroundColor(Color.parseColor("#903ba0"))
        vvipV.setBackgroundColor(Color.parseColor("#903ba0"))
    }

    private fun initGrades() {
        basicV.setBackgroundColor(Color.parseColor("#d4d4d4"))
        plusV.setBackgroundColor(Color.parseColor("#d4d4d4"))
        premiumV.setBackgroundColor(Color.parseColor("#d4d4d4"))
        vvipV.setBackgroundColor(Color.parseColor("#d4d4d4"))
    }

    private fun drawBasic2() {
        initGrades2()
        basicV2.setBackgroundColor(Color.parseColor("#903ba0"))
        plusV2.setBackgroundColor(Color.parseColor("#903ba0"))
    }

    private fun drawPlus2() {
        initGrades2()
        plusV2.setBackgroundColor(Color.parseColor("#903ba0"))
        premiumV2.setBackgroundColor(Color.parseColor("#903ba0"))
    }

    private fun drawPremium2() {
        initGrades2()
        premiumV2.setBackgroundColor(Color.parseColor("#903ba0"))
        vvipV2.setBackgroundColor(Color.parseColor("#903ba0"))
    }

    private fun drawVVIP2() {
        initGrades2()
        premiumV2.setBackgroundColor(Color.parseColor("#903ba0"))
        vvipV2.setBackgroundColor(Color.parseColor("#903ba0"))
    }

    private fun initGrades2() {
        basicV2.setBackgroundColor(Color.parseColor("#d4d4d4"))
        plusV2.setBackgroundColor(Color.parseColor("#d4d4d4"))
        premiumV2.setBackgroundColor(Color.parseColor("#d4d4d4"))
        vvipV2.setBackgroundColor(Color.parseColor("#d4d4d4"))
    }


    private fun timer() {

        if (handler != null) {
            handler!!.removeCallbacksAndMessages(null);
        }

        handler = object : Handler() {
            override fun handleMessage(msg: Message) {

                adTime++

                val index = adverVP.getCurrentItem()
                val last_index = adverImagePaths.size - 1

                if (adTime % 2 == 0) {
                    if (index < last_index) {
                        adverVP.setCurrentItem(index + 1)
                    } else {
                        adverVP.setCurrentItem(0)
                    }
                }

                handler!!.sendEmptyMessageDelayed(0, 4000) // 1초에 한번 업, 1000 = 1 초
            }
        }
        handler!!.sendEmptyMessage(0)
    }

    private fun addDot(circleLL: LinearLayout, selected: Boolean) {
        val iv = ImageView(context)
        if (selected) {
            iv.setBackgroundResource(R.drawable.circle_background1)
        } else {
            iv.setBackgroundResource(R.drawable.circle_background2)
        }

        val width = Utils.pxToDp(6.0f).toInt()
        val height = Utils.pxToDp(6.0f).toInt()

        iv.layoutParams = LinearLayout.LayoutParams(width, height)
        iv.scaleType = ImageView.ScaleType.CENTER_CROP

        val lpt = iv.layoutParams as ViewGroup.MarginLayoutParams
        val marginRight = Utils.pxToDp(7.0f).toInt()
        lpt.setMargins(0, 0, marginRight, 0)
        iv.layoutParams = lpt

        circleLL.addView(iv)
    }


}
