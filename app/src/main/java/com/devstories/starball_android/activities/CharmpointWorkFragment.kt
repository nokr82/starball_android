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
import kotlinx.android.synthetic.main.fragment_charmpoint_work.*

//메세지관리(메시지작성화면)

class CharmpointWorkFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    var work = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_charmpoint_work, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        work1TV.setOnClickListener {
            work = Utils.getString(work1TV)
            work1TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "WORK_CHANGE"
            myContext.sendBroadcast(intent)
        }
        work2TV.setOnClickListener {
            work = Utils.getString(work2TV)
            work2TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "WORK_CHANGE"
            myContext.sendBroadcast(intent)
        }
        work3TV.setOnClickListener {
            work = Utils.getString(work3TV)
            work3TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "WORK_CHANGE"
            myContext.sendBroadcast(intent)
        }
        work4TV.setOnClickListener {
            work = Utils.getString(work4TV)
            work4TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "WORK_CHANGE"
            myContext.sendBroadcast(intent)
        }
        work5TV.setOnClickListener {
            work = Utils.getString(work5TV)
            work5TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "WORK_CHANGE"
            myContext.sendBroadcast(intent)
        }
        work6TV.setOnClickListener {
            work = Utils.getString(work6TV)
            work6TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "WORK_CHANGE"
            myContext.sendBroadcast(intent)
        }


        skipTV.setOnClickListener {
            var intent = Intent()
            intent.action = "WORK_CHANGE"
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
