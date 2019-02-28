package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devstories.starball_android.R
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.fragment_charmpoint_sports.*

//메세지관리(메시지작성화면)

class CharmpointSportsFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    var sports = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_charmpoint_sports, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sport1TV.setOnClickListener {
            sports = Utils.getString(sport1TV)
            sport1TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "SPORTS_CHANGE"
            myContext.sendBroadcast(intent)
        }
        sport2TV.setOnClickListener {
            sports = Utils.getString(sport2TV)
            sport2TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "SPORTS_CHANGE"
            myContext.sendBroadcast(intent)
        }
        sport1TV.setOnClickListener {
            sports = Utils.getString(sport1TV)
            sport1TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "SPORTS_CHANGE"
            myContext.sendBroadcast(intent)
        }
        sport3TV.setOnClickListener {
            sports = Utils.getString(sport3TV)
            sport3TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "SPORTS_CHANGE"
            myContext.sendBroadcast(intent)
        }
        sport4TV.setOnClickListener {
            sports = Utils.getString(sport4TV)
            sport4TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "SPORTS_CHANGE"
            myContext.sendBroadcast(intent)
        }
        sport5TV.setOnClickListener {
            sports = Utils.getString(sport5TV)
            sport5TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "SPORTS_CHANGE"
            myContext.sendBroadcast(intent)
        }
        sport6TV.setOnClickListener {
            sports = Utils.getString(sport6TV)
            sport6TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "SPORTS_CHANGE"
            myContext.sendBroadcast(intent)
        }
        sport7TV.setOnClickListener {
            sports = Utils.getString(sport7TV)
            sport7TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "SPORTS_CHANGE"
            myContext.sendBroadcast(intent)
        }


        skipTV.setOnClickListener {
            var intent = Intent()
            intent.action = "SPORTS_CHANGE"
            myContext.sendBroadcast(intent)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}
