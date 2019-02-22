package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_join_school.*

class JoinStep8SchoolActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    var email = ""
    var passwd = ""
    var name = ""
    var gender = ""
    var height = ""
    var language = ""
    var school = ""
    var birth = ""
    var job = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_school)
        this.context = this
        progressDialog = ProgressDialog(context)


        if (PrefUtils.getStringPreference(context,"school")!=null){
            school = PrefUtils.getStringPreference(context,"school")
            schoolET.setText(school)
        }

        nextTV.setOnClickListener {
            school = Utils.getString(schoolET)
            if (school.count() < 1) {
                Toast.makeText(context, getString(R.string.school_empty), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            PrefUtils.setPreference(context, "school", school)
            val intent = Intent(context, JoinStep9ExpActivity::class.java)

            startActivity(intent)
        }




    }



}
