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
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_daily_mement_list.*

class DailyMomentListActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var DaillyAdapter: DaillyAdapter


    lateinit var header: View
    lateinit var backIV: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_mement_list)
        this.context = this
        progressDialog = ProgressDialog(context)


        DaillyAdapter = DaillyAdapter(context, R.layout.item_daily_list, 6)
        dailyLV.adapter = DaillyAdapter



        dailyLV.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, DlgAlbumPayActivity::class.java)
            startActivity(intent)
        }


        header = View.inflate(this, R.layout.item_daily_momenthead, null)
        backIV = header.findViewById(R.id.backIV)

        dailyLV.addHeaderView(header)


        backIV.setOnClickListener {
            finish()
        }





    }



}
