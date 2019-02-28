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
import kotlinx.android.synthetic.main.fragment_charmpoint_region.*

//메세지관리(메시지작성화면)

class CharmpointRegionFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    var region = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_charmpoint_region, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        region1TV.setOnClickListener {
            region = Utils.getString(region1TV)
            region1TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region2TV.setOnClickListener {
            region = Utils.getString(region2TV)
            region2TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region3TV.setOnClickListener {
            region = Utils.getString(region3TV)
            region3TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region4TV.setOnClickListener {
            region = Utils.getString(region4TV)
            region4TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region5TV.setOnClickListener {
            region = Utils.getString(region5TV)
            region5TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region6TV.setOnClickListener {
            region = Utils.getString(region6TV)
            region6TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region7TV.setOnClickListener {
            region = Utils.getString(region7TV)
            region7TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region8TV.setOnClickListener {
            region = Utils.getString(region8TV)
            region8TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region9TV.setOnClickListener {
            region = Utils.getString(region9TV)
            region9TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region10TV.setOnClickListener {
            region = Utils.getString(region10TV)
            region10TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }
        region11TV.setOnClickListener {
            region = Utils.getString(region11TV)
            region11TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.putExtra("region",region)
            intent.action = "REGION_CHANGE"
            myContext.sendBroadcast(intent)
        }






        skipTV.setOnClickListener {
            var intent = Intent()
            intent.action = "REGION_CHANGE"
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
