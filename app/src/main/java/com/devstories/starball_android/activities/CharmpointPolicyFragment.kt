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
import kotlinx.android.synthetic.main.fragment_charmpoint_policy.*

//메세지관리(메시지작성화면)

class CharmpointPolicyFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    var policy = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_charmpoint_policy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        policy1TV.setOnClickListener {
            policy = Utils.getString(policy1TV)
            policy1TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "POLICY_CHANGE"
            myContext.sendBroadcast(intent)
        }

        policy2TV.setOnClickListener {
            policy = Utils.getString(policy2TV)
            policy2TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "POLICY_CHANGE"
            myContext.sendBroadcast(intent)
        }

        policy3TV.setOnClickListener {
            policy = Utils.getString(policy3TV)
            policy3TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "POLICY_CHANGE"
            myContext.sendBroadcast(intent)
        }

        policy4TV.setOnClickListener {
            policy = Utils.getString(policy4TV)
            policy4TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "POLICY_CHANGE"
            myContext.sendBroadcast(intent)
        }


        skipTV.setOnClickListener {

            var intent = Intent()
            intent.action = "POLICY_CHANGE"
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
