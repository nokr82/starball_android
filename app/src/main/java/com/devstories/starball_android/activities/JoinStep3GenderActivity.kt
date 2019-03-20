package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.devstories.starball_android.R
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_join_gender.*

class JoinStep3GenderActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var email = ""
    var passwd = ""
    var name = ""
    var gender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_gender)

        this.context = this
        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        nextTV.setBackgroundResource(R.drawable.background_border_strock2)

//        gender= PrefUtils.getStringPreference(context,"gender")
        Log.d("패스",gender)
        if (PrefUtils.getStringPreference(context,"join_gender")!=null){
            gender = PrefUtils.getStringPreference(context,"join_gender")
            nextTV.setBackgroundColor(Color.BLACK)
                setGenderView()
        }


        maleTV.setOnClickListener {

            gender = "M"
            nextTV.setBackgroundColor(Color.BLACK)
            setGenderView()

        }

        femaleTV.setOnClickListener {

            gender = "F"
            nextTV.setBackgroundColor(Color.BLACK)
            setGenderView()

        }

        nextTV.setOnClickListener {
            PrefUtils.setPreference(context, "join_gender", gender)
            val intent = Intent(context, JoinStep4HeightActivity::class.java)
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
        } else if (gender=="F"){
            femaleTV.setBackgroundResource(R.drawable.background_gender_female)
            femaleTV.setTextColor(Color.parseColor("#FFFFFF"))
        }

    }



}
