package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
    var language = ""

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
        language = intent.getStringExtra("language")

        print("language:::::::::::::::::::::::::::::::::::::::::::$language")

        nextTV.setOnClickListener {

            val job = Utils.getString(jobET)

            if (job.count() < 1) {
                Toast.makeText(context, getString(R.string.job_empty), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val intent = Intent(context, JoinStep8Activity::class.java)
            startActivity(intent)
        }

    }

}
