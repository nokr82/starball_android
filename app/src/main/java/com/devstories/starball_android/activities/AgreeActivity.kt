package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.base.Config
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_agree.*

class AgreeActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agree)

        this.context = this
        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        type = intent.getStringExtra("type")

        if (type == "1") {
            titleTV.text = getString(R.string.setting_solo_method)
        }

        webWV.loadUrl(Config.url + "/agrees/agree?type=" + type)

        backLL.setOnClickListener {
            finish()
        }

    }

}
