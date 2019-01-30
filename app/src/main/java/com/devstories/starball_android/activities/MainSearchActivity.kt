package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_main_search.*

class MainSearchActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_search)
        this.context = this
        progressDialog = ProgressDialog(context)


        charmIV.setOnClickListener {
            val intent = Intent(context, DlgCrushActivity::class.java)
            startActivity(intent)
        }
        infoIV.setOnClickListener {
            val intent = Intent(context, DlgCharInfoActivity::class.java)
            startActivity(intent)
        }

        chatIV.setOnClickListener {
            val intent = Intent(context, ChattingActivity::class.java)
            startActivity(intent)
        }

        charIV.setOnClickListener {
            val intent = Intent(context, SettingMainActivity::class.java)
            startActivity(intent)
        }





    }



}
