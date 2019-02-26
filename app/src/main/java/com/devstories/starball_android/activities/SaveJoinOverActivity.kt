package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_save_join_over.*

class SaveJoinOverActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_join_over)
        this.context = this
        progressDialog = ProgressDialog(context)

        backIV.setOnClickListener {
            finish()
        }
        noTV.setOnClickListener {
            val intent = Intent(context, SaveJoinCancleActivity::class.java)
            startActivity(intent)
        }





    }



}
