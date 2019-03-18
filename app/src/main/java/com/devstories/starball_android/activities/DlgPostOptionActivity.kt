package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.dlg_post_option.*

class DlgPostOptionActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    private var REPORT = 1001
    var content_id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_post_option)

        this.context = this
        progressDialog = ProgressDialog(context)

        content_id = intent.getIntExtra("content_id", -1)

        reportTV.setOnClickListener {
            val intent = Intent(context, ReportActivity::class.java)
            startActivityForResult(intent, REPORT)
        }

       delTV.setOnClickListener {
           set_result()
       }


    }

    fun set_result(){
        var intent = Intent()
        intent.action = "DEL_POST"
        sendBroadcast(intent)
        finish()
    }

}
