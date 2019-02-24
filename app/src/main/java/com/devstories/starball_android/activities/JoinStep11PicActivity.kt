package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.ProfileAdapter
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_join_picture.*

class JoinStep11PicActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var profileAdapter: ProfileAdapter
    var email = ""
    var name = ""
    var height = ""
    var language = ""
    var data = 9
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_picture)
        this.context = this
        progressDialog = ProgressDialog(context)

        val spanText = getString(R.string.picture_content)

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            descTV.text = Html.fromHtml(spanText);
        } else {
            descTV.text = Html.fromHtml(spanText, Html.FROM_HTML_MODE_LEGACY);
        }

        /*
        profileAdapter = ProfileAdapter(context, R.layout.item_profile_img, data)
        profileGV.adapter = profileAdapter
        profileGV.isExpanded = true



        profileGV.setOnItemClickListener { adapterView, view, i, l ->
            Log.d("로그",i.toString())
            Log.d("로그",view.toString())
            Log.d("로그",l.toString())
        }
        */

        nextTV.setOnClickListener {
            val intent = Intent(context, JoinResultActivity::class.java)

            startActivity(intent)
        }




    }



}
