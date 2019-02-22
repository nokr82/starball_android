package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.ProfileAdapter
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_join_picture.*

class JoinStep11PicActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var ProfileAdapter: ProfileAdapter
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


        ProfileAdapter = ProfileAdapter(context, R.layout.item_profile_img, data)
        profileGV.adapter = ProfileAdapter



        profileGV.setOnItemClickListener { adapterView, view, i, l ->
            Log.d("로그",i.toString())
            Log.d("로그",view.toString())
            Log.d("로그",l.toString())
        }

        nextTV.setOnClickListener {
            val intent = Intent(context, JoinResultActivity::class.java)

            startActivity(intent)
        }




    }



}
