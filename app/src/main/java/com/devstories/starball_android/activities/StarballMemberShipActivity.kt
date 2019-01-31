package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_starball_membership.*

class StarballMemberShipActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starball_membership)
        this.context = this
        progressDialog = ProgressDialog(context)

        backIV.setOnClickListener {
            finish()
        }

        vipTV.setOnClickListener {
            val intent = Intent(context, VVIPJoinActivity::class.java)
            startActivity(intent)
        }





    }



}
