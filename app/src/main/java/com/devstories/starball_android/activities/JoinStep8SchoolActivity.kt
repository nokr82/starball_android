package com.devstories.starball_android.activities

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
import kotlinx.android.synthetic.main.activity_join_school.*

class JoinStep8SchoolActivity : RootActivity() {

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_school)

        this.context = this
        nextTV.setBackgroundResource(R.drawable.background_border_strock2)
        var school = PrefUtils.getStringPreference(context,"join_school", "")
        if (school.isNotEmpty()){
            nextTV.setBackgroundColor(Color.BLACK)
            schoolET.setText(school)
        }

        schoolET.addTextChangedListener(object : TextWatcher {
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
            school = Utils.getString(schoolET)

            if (school.isEmpty()) {
                Toast.makeText(context, getString(R.string.school_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            PrefUtils.setPreference(context, "join_school", school)

            val intent = Intent(context, JoinStep9IntroActivity::class.java)
            startActivity(intent)
        }
    }
}
