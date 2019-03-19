package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_join_job.*

class JoinStep7JobActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_job)

        this.context = this
        progressDialog = ProgressDialog(context)
        nextTV.setBackgroundResource(R.drawable.background_border_strock2)
        var job = PrefUtils.getStringPreference(context,"join_job", "")
        if (job.isNotEmpty()){
            nextTV.setBackgroundColor(Color.BLACK)
            jobET.setText(job)
        }

        jobET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
             if (s.length>0){
                 nextTV.setBackgroundColor(Color.BLACK)
             }else{
                 nextTV.setBackgroundResource(R.drawable.background_border_strock2)
             }
            }
            override fun afterTextChanged(s: Editable) {}
        })



        nextTV.setOnClickListener {

            job =  Utils.getString(jobET)

            if (job.isEmpty()) {
                Toast.makeText(context, getString(R.string.job_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            PrefUtils.setPreference(context, "join_job", job)

            val intent = Intent(context, JoinStep8SchoolActivity::class.java)
            startActivity(intent)
        }

    }

}
