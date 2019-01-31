package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        this.context = this
        progressDialog = ProgressDialog(context)

        versionLL.setOnClickListener {
            val intent = Intent(context, VersionActivity::class.java)
            startActivity(intent)
        }
        alramLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if (it.isSelected) {
                alram_opLL.visibility = View.VISIBLE
            } else {
                alram_opLL.visibility = View.GONE
            }
        }

        logoutLL.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        backIV.setOnClickListener {
            finish()
        }




    }



}
