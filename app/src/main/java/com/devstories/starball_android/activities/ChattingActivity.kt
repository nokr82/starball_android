package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import com.devstories.starball_android.R
import kotlinx.android.synthetic.main.activity_chatting.*


class ChattingActivity : FragmentActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var type = -1
    val ChattingFragment: ChattingFragment = ChattingFragment()
    val ChattingMatchFragment: ChattingMatchFragment = ChattingMatchFragment()
    val ChattingCrushFragment: ChattingCrushFragment = ChattingCrushFragment()
    val ChattingSendCrushFragment: ChattingSendCrushFragment = ChattingSendCrushFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)
        this.context = this
        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        supportFragmentManager.beginTransaction().replace(R.id.chattingFL, ChattingFragment).commit()

        click()
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
        more2IV.setOnClickListener {
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


}
