package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.AdverAdapter
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_vvip_join.*

class VVIPJoinActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    private val heightitems = arrayOf(
        "170cm",
        "171cm",
        "172cm",
        "173cm",
        "174cm",
        "175cm",
        "176cm",
        "177cm",
        "178cm",
        "179cm",
        "180cm",
        "181cm"
    )
    private lateinit var adverAdapter: AdverAdapter
    var adPosition = 0

    private var adTime = 0
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vvip_join)
        this.context = this
        progressDialog = ProgressDialog(context)

        for (i in 0..(heightitems.size - 1)) {

            val userView = View.inflate(context, R.layout.item_vvip, null)



            userLL.addView(userView)

        }

        backIV.setOnClickListener {
            finish()
        }


        noTV.setOnClickListener {
            finish()
        }




    }



}
