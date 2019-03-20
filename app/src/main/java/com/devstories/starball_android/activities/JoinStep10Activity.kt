package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_join_shape.*

class JoinStep10Activity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_shape)
        this.context = this
        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        val spanText = getString(R.string.shape_content)

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            descTV.text = Html.fromHtml(spanText);
        } else {
            descTV.text = Html.fromHtml(spanText, Html.FROM_HTML_MODE_LEGACY);
        }

        nextTV.setOnClickListener {
            val intent = Intent(context, JoinStep11PicActivity::class.java)
            startActivity(intent)
        }
    }
}
