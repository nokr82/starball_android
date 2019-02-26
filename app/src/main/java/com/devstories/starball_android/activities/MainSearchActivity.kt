package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.fragment_main_search1.*

class MainSearchActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_main_search1)
        this.context = this
        progressDialog = ProgressDialog(context)



        safeIV.setOnClickListener {
            val intent = Intent(context, SaveJoinActivity::class.java)
            startActivity(intent)
        }


        charmIV.setOnClickListener {
            val intent = Intent(context, DlgStarballLackActivity::class.java)
            startActivity(intent)
        }
        infoIV.setOnClickListener {
            val intent = Intent(context, DlgCharInfoActivity::class.java)
            startActivity(intent)
        }


    }



}
