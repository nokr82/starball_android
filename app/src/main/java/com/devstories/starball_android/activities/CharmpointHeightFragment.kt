package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.devstories.starball_android.R
import kotlinx.android.synthetic.main.fragment_charmpoint_height.*

//메세지관리(메시지작성화면)

class CharmpointHeightFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var charmpointSettingAcitivity: CharmpointSettingAcitivity
    private var ticketAdapter: ArrayAdapter<String>? = null
    private val heightitems = arrayOf(
        "170cm",
        "171cm",
        "172cm",
        "173cm",
        "174cm",
        "175cm",
        "176cm",
        "177cm",
        "178cm",
        "179cm",
        "180cm",
        "181cm"
    )
    private var select_height = "0"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_charmpoint_height, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        charmpointSettingAcitivity = activity as CharmpointSettingAcitivity

        ticketAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, heightitems)
        ticketSP.adapter = ticketAdapter
        ticketSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                select_height = heightitems[position]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        skipTV.setOnClickListener {
            var intent = Intent()
            intent.action = "HEIGHT_CHANGE"
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
