package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_setting_main.*

class SettingMainActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_main)
        this.context = this
        progressDialog = ProgressDialog(context)



        finishIV.setOnClickListener {
            finish()
        }

        centerIV.setOnClickListener {
            val intent = Intent(context, DailyMomentListActivity::class.java)
            startActivity(intent)
        }


        starballLL.setOnClickListener {
            val intent = Intent(context, DlgStarballHistoryActivity::class.java)
            startActivity(intent)
        }
        editIV.setOnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            startActivity(intent)
        }

        searchIV.setOnClickListener {
            val intent = Intent(context, SearchSettingActivity::class.java)
            startActivity(intent)
        }

        settingIV.setOnClickListener {
            val intent = Intent(context, SettingActivity::class.java)
            startActivity(intent)
        }

        moreTV.setOnClickListener {
            val intent = Intent(context, StarballMemberShipActivity::class.java)
            startActivity(intent)
        }
        payLL.setOnClickListener {
            val intent = Intent(context, StarballPayActivity::class.java)
            startActivity(intent)
        }

    }



}
