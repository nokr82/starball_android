package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.DaillyAdapter
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_daily_view_subscriblist.*

class DailyMomentSubVIewListActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var DaillyAdapter: DaillyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_view_subscriblist)
        this.context = this
        progressDialog = ProgressDialog(context)



        DaillyAdapter = DaillyAdapter(context,R.layout.item_view_daily_sub_list, 6)
        dailyGV.adapter = DaillyAdapter

        timelineTV.setOnClickListener {
            val intent = Intent(context, DailyMomentListActivity::class.java)
            startActivity(intent)
        }
        backIV.setOnClickListener {
            finish()
        }



    }



}
