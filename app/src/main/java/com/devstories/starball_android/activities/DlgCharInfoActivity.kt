package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity


class DlgCharInfoActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    private val _active = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_charactor_info)

        this.context = this
        progressDialog = ProgressDialog(context)







    }


}
