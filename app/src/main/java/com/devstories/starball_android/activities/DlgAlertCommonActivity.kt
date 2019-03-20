package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.dlg_alert_common.*

class DlgAlertCommonActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var contents = ""
    var cancel = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_alert_common)


        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        this.context = this
        progressDialog = ProgressDialog(context)

        contents = intent.getStringExtra("contents")
        cancel = intent.getBooleanExtra("cancel", false)

        contentTV.text = contents

        if(cancel) {
            cancelTV.visibility = View.VISIBLE
        }

        doneTV.setOnClickListener {
            var intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        cancelTV.setOnClickListener {
            var intent = Intent()
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }

    }

}
