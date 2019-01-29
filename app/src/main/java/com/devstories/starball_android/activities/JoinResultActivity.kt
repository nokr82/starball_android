package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_join_result.*

class JoinResultActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_result)
        this.context = this
        progressDialog = ProgressDialog(context)

        checkTV.setOnClickListener {
            val intent = Intent(context, MainSearchActivity::class.java)
            startActivity(intent)
        }




    }



}
