package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_phone_confirm.*

class PhoneCertiActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_confirm)
        this.context = this
        progressDialog = ProgressDialog(context)


        okTV.setOnClickListener {
            val intent = Intent(context, PhoneCerti2Activity::class.java)
            startActivity(intent)
        }

        backIV.setOnClickListener {
            finish()
        }






    }



}
