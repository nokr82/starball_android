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
    var passwd = ""
    var name = ""
    var gender = ""
    var height = ""
    var language = ""
    var school = ""
    var birth = ""
    var job = ""
    var exp = ""
    var data = 9
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_picture)
        this.context = this
        progressDialog = ProgressDialog(context)


        ProfileAdapter = ProfileAdapter(context, R.layout.item_profile_img, data)
        profileGV.adapter = ProfileAdapter

        email = intent.getStringExtra("email")
        passwd = intent.getStringExtra("passwd")
        name = intent.getStringExtra("name")
        gender = intent.getStringExtra("gender")
        height = intent.getStringExtra("height")
        language = intent.getStringExtra("language")
        birth = intent.getStringExtra("birth")
        job = intent.getStringExtra("job")
        exp = intent.getStringExtra("exp")

        profileGV.setOnItemClickListener { adapterView, view, i, l ->
            Log.d("로그",i.toString())
            Log.d("로그",view.toString())
            Log.d("로그",l.toString())
        }

        nextTV.setOnClickListener {
            val intent = Intent(context, JoinResultActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("passwd", passwd)
            intent.putExtra("name", name)
            intent.putExtra("gender", gender)
            intent.putExtra("height", height)
            intent.putExtra("birth", birth)
            intent.putExtra("language", language)
            intent.putExtra("job", job)
            intent.putExtra("school", school)
            intent.putExtra("exp", exp)
            startActivity(intent)
        }




    }



}
