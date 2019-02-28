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
import kotlinx.android.synthetic.main.fragment_charmpoint_baby.*

//메세지관리(메시지작성화면)

class CharmpointBabyFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    var baby = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_charmpoint_baby, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        baby1TV.setOnClickListener {
            baby = Utils.getString(baby1TV)
            baby1TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "BABY_CHANGE"
            myContext.sendBroadcast(intent)
        }


        baby2TV.setOnClickListener {
            baby = Utils.getString(baby2TV)
            baby2TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "BABY_CHANGE"
            myContext.sendBroadcast(intent)
        }

        baby3TV.setOnClickListener {
            baby = Utils.getString(baby3TV)
            baby3TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "BABY_CHANGE"
            myContext.sendBroadcast(intent)
        }

        baby4TV.setOnClickListener {
            baby = Utils.getString(baby4TV)
            baby4TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "BABY_CHANGE"
            myContext.sendBroadcast(intent)
        }



        skipTV.setOnClickListener {
            var intent = Intent()
            intent.action = "BABY_CHANGE"
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
