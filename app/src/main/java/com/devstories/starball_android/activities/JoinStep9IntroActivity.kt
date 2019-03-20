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
import kotlinx.android.synthetic.main.activity_join_step9_intro.*

class JoinStep9IntroActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var intro = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_step9_intro)

        this.context = this
        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)


        nextTV.setBackgroundResource(R.drawable.background_border_strock2)
        var intro = PrefUtils.getStringPreference(context,"join_intro", "")
        if (intro.isNotEmpty()){
            nextTV.setBackgroundColor(Color.BLACK)
            introET.setText(intro)
            limitTV.text = intro.length.toString()+"/500"
        }

        introET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length>0){
                    nextTV.setBackgroundColor(Color.BLACK)
                }else{
                    nextTV.setBackgroundResource(R.drawable.background_border_strock2)
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length <= 500){
                    limitTV.text = s.length.toString()+"/500"
                } else{
                    Toast.makeText(context,"더이상 입력할수 없습니다.",Toast.LENGTH_SHORT).show()
                }
            }
        })

        nextTV.setOnClickListener {
            intro = Utils.getString(introET)

            if (intro.isEmpty()) {
                Toast.makeText(context, "당신을 표현할 수 있는 말을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            PrefUtils.setPreference(context, "join_intro", intro)
            val intent = Intent(context, JoinStep11PicActivity::class.java)
            startActivity(intent)
        }

        skipTV.setOnClickListener {
            PrefUtils.removePreference(context, "join_intro")
            val intent = Intent(context, JoinStep11PicActivity::class.java)
            startActivity(intent)
        }
    }
}
