package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.infoAdapter
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.dlg_charactor_info.*


class DlgCharInfoActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    private val _active = true

    lateinit var infoAdapter: infoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_charactor_info)

        this.context = this
        progressDialog = ProgressDialog(context)




        infoAdapter = infoAdapter(context,R.layout.item_char_info, 12)
        infoGV.adapter = infoAdapter



    }


}