package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.DaillyAdapter
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_daily_moment_view_list.*

class DailyMomentViewListActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var DaillyAdapter: DaillyAdapter


    lateinit var header: View
    lateinit var backIV: ImageView
    lateinit var timelineTV: TextView
    lateinit var secretTV: TextView
    lateinit var profileIV: ImageView
    lateinit var starballIV: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_moment_view_list)
        this.context = this
        progressDialog = ProgressDialog(context)


        DaillyAdapter = DaillyAdapter(context,R.layout.item_view_daily_list, 6)
        dailyLV.adapter = DaillyAdapter


        header = View.inflate(this, R.layout.item_daily_moment_view_head, null)
        backIV = header.findViewById(R.id.backIV)
        timelineTV = header.findViewById(R.id.timelineTV)
        secretTV = header.findViewById(R.id.secretTV)
        profileIV= header.findViewById(R.id.profileIV)
        starballIV =  header.findViewById(R.id.starballIV)
        dailyLV.addHeaderView(header)
        timelineTV.setOnClickListener {
            val intent = Intent(context, DailyMomentListActivity::class.java)
            startActivity(intent)
        }

        starballIV.setOnClickListener {
            val intent = Intent(context, DlgCrushActivity::class.java)
            startActivity(intent)
        }

        secretTV.setOnClickListener {
            val intent = Intent(context, DailyMomentSubVIewListActivity::class.java)
            startActivity(intent)
        }
        profileIV.setOnClickListener {
            val intent = Intent(context, DlgAlbumPayActivity::class.java)
            startActivity(intent)
        }

        backIV.setOnClickListener {
            finish()
        }

    }



}
