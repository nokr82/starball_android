package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.base.PrefUtils
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

        if (PrefUtils.getStringPreference(context,"job")!=null){
            job = PrefUtils.getStringPreference(context,"job")
            jobET.setText(job)
        }

        nextTV.setOnClickListener {

            job =  Utils.getString(jobET)

            if (job.count() < 1) {
                Toast.makeText(context, getString(R.string.job_empty), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            PrefUtils.setPreference(context, "job", job)

            val intent = Intent(context, JoinStep8SchoolActivity::class.java)

            startActivity(intent)
        }

    }

}
