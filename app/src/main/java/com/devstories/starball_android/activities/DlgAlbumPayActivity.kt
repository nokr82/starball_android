package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.dlg_crush.*


class DlgAlbumPayActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    private val _active = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_album_pay)

        this.context = this
        progressDialog = ProgressDialog(context)



        payTV.setOnClickListener {
            val intent = Intent(context, StarballPayActivity::class.java)
            startActivity(intent)
        }

        giftTV.setOnClickListener {
            val intent = Intent(context, StarballPayActivity::class.java)
            startActivity(intent)
        }




    }


}
