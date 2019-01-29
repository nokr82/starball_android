package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_profile_pre.*

class JoinStep12Activity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_pre)
        this.context = this
        progressDialog = ProgressDialog(context)

        nextIV.setOnClickListener {
            val intent = Intent(context, JoinResultActivity::class.java)
            startActivity(intent)
        }




    }



}
