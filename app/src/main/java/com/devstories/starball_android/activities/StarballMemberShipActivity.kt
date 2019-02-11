package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.view.ViewPager
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.AdverAdapter
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_starball_membership.*

class StarballMemberShipActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    var adverImagePaths = ArrayList<String>()
    private lateinit var adverAdapter: AdverAdapter
    var adPosition = 0;

    private var adTime = 0
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starball_membership)
        this.context = this
        progressDialog = ProgressDialog(context)
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


        backIV.setOnClickListener {
            finish()
        }

        vipTV.setOnClickListener {
            val intent = Intent(context, VVIPJoinActivity::class.java)
            startActivity(intent)
        }





    }

    private fun timer() {

        if(handler != null) {
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

                handler!!.sendEmptyMessageDelayed(0, 2000) // 1초에 한번 업, 1000 = 1 초
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
