package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_join_gender.*

class JoinStep3GenderActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var email = ""
    var passwd = ""
    var name = ""
    var gender = "F"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_gender)

        this.context = this
        progressDialog = ProgressDialog(context)

        email = intent.getStringExtra("email")
        passwd = intent.getStringExtra("passwd")
        name = intent.getStringExtra("name")

        maleTV.setOnClickListener {

            gender = "M"
            setGenderView()

        }

        femaleTV.setOnClickListener {

            gender = "F"
            setGenderView()

        }

        nextTV.setOnClickListener {
            val intent = Intent(context, JoinStep4HeightActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("passwd", passwd)
            intent.putExtra("name", name)
            intent.putExtra("gender", gender)
            startActivity(intent)
        }

    }

    fun setGenderView() {

        maleTV.setBackgroundResource(R.drawable.background_gender_male_border)
        femaleTV.setBackgroundResource(R.drawable.background_gender_female_border)
        maleTV.setTextColor(Color.parseColor("#555555"))
        femaleTV.setTextColor(Color.parseColor("#555555"))

        if (gender == "M") {
            maleTV.setBackgroundResource(R.drawable.background_gender_male)
            maleTV.setTextColor(Color.parseColor("#FFFFFF"))
        } else {
            femaleTV.setBackgroundResource(R.drawable.background_gender_female)
            femaleTV.setTextColor(Color.parseColor("#FFFFFF"))
        }

    }



}
