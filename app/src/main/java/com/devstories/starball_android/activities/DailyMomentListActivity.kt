package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.DaillyAdapter
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_daily_mement_list.*
import kotlinx.android.synthetic.main.item_daily_momenthead.*

class DailyMomentListActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var DaillyAdapter: DaillyAdapter


    lateinit var header: View
    lateinit var backIV: ImageView
    lateinit var videoLL: LinearLayout
    lateinit var photoLL: LinearLayout
    lateinit var headRL: RelativeLayout

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
        headRL = header.findViewById(R.id.headRL)
        videoLL = header.findViewById(R.id.videoLL)
        photoLL = header.findViewById(R.id.photoLL)
        dailyLV.addHeaderView(header)
        headRL.setOnClickListener {

        }
        videoLL.setOnClickListener {
            val intent = Intent(context, DailyMomentViewListActivity::class.java)
            startActivity(intent)
        }
        photoLL.setOnClickListener {
            val intent = Intent(context, DailyMomentSubVIewListActivity::class.java)
            startActivity(intent)
        }



        backIV.setOnClickListener {
            finish()
        }


    }


}
