package com.devstories.starball_android.activities

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_school)

        this.context = this

        var school = PrefUtils.getStringPreference(context,"school", "")
        if (school.isNotEmpty()){
            schoolET.setText(school)
        }

        nextTV.setOnClickListener {
            school = Utils.getString(schoolET)

            if (school.isEmpty()) {
                Toast.makeText(context, getString(R.string.school_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            PrefUtils.setPreference(context, "school", school)

            val intent = Intent(context, JoinStep9ExpActivity::class.java)
            startActivity(intent)
        }
    }
}
