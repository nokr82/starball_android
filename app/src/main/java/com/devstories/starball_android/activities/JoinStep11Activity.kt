package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.ProfileAdapter
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_join_picture.*

class JoinStep11Activity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var ProfileAdapter: ProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_picture)
        this.context = this
        progressDialog = ProgressDialog(context)


        ProfileAdapter = ProfileAdapter(context, R.layout.item_profile_img, 9)
        profileGV.adapter = ProfileAdapter


        nextTV.setOnClickListener {
            val intent = Intent(context, JoinResultActivity::class.java)
            startActivity(intent)
        }




    }



}
