package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_join_school.*

class JoinStep8Activity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_school)
        this.context = this
        progressDialog = ProgressDialog(context)

        nextTV.setOnClickListener {
            val intent = Intent(context, JoinStep9Activity::class.java)
            startActivity(intent)
        }




    }



}
