package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_join_job.*

class JoinStep7JobActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var email = ""
    var passwd = ""
    var name = ""
    var gender = ""
    var height = ""
    var birth = ""
    var language = ""
    var job =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_job)

        this.context = this
        progressDialog = ProgressDialog(context)

        email = intent.getStringExtra("email")
        passwd = intent.getStringExtra("passwd")
        name = intent.getStringExtra("name")
        gender = intent.getStringExtra("gender")
        height = intent.getStringExtra("height")
        birth = intent.getStringExtra("birth")
        language = intent.getStringExtra("language")
        Log.d("언어",email)
        Log.d("언어",passwd)
        Log.d("언어",language)
        print("language:::::::::::::::::::::::::::::::::::::::::::$language")

        nextTV.setOnClickListener {

            job =  Utils.getString(jobET)

            if (job.count() < 1) {
                Toast.makeText(context, getString(R.string.job_empty), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val intent = Intent(context, JoinStep8SchoolActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("passwd", passwd)
            intent.putExtra("name", name)
            intent.putExtra("gender", gender)
            intent.putExtra("height", height)
            intent.putExtra("birth", birth)
            intent.putExtra("language", language)
            intent.putExtra("job", job)
            startActivity(intent)
        }

    }

}
