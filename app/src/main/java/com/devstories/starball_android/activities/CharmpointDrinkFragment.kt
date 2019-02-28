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
import kotlinx.android.synthetic.main.fragment_charmpoint_drink.*

//메세지관리(메시지작성화면)

class CharmpointDrinkFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    var drink = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_charmpoint_drink, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        drink1TV.setOnClickListener {
            drink = Utils.getString(drink1TV)
            drink1TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "DRINK_CHANGE"
            myContext.sendBroadcast(intent)
        }
        drink2TV.setOnClickListener {
            drink = Utils.getString(drink2TV)
            drink2TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "DRINK_CHANGE"
            myContext.sendBroadcast(intent)
        }
        drink3TV.setOnClickListener {
            drink = Utils.getString(drink3TV)
            drink3TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "DRINK_CHANGE"
            myContext.sendBroadcast(intent)
        }
        drink4TV.setOnClickListener {
            drink = Utils.getString(drink4TV)
            drink4TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "DRINK_CHANGE"
            myContext.sendBroadcast(intent)
        }

        skipTV.setOnClickListener {
            var intent = Intent()
            intent.action = "DRINK_CHANGE"
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
