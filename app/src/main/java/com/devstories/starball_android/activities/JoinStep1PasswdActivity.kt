package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_join_pwstep.*

class JoinStep1PasswdActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var email = ""
    var passwd = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_pwstep)

        this.context = this
        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        Log.d("패스",passwd)
        if (PrefUtils.getStringPreference(context,"passwd") != null){
            passwdET.setText(PrefUtils.getStringPreference(context,"passwd"))
            val passwd = Utils.getString(passwdET)
            if (Utils.containsAlpha(passwd) && Utils.containsNumber(passwd) && passwd.count() > 5 && passwd.count() < 13) {
                passwdConfirmTV.visibility = View.VISIBLE
                passwdConfirmTV.text = getString(R.string.pwd_confirm)
                nextTV.setBackgroundColor(Color.BLACK)
            } else {
                passwdConfirmTV.visibility = View.GONE
                nextTV.setBackgroundResource(R.drawable.background_border_strock2)

            }
        }

        passwdET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val passwd = Utils.getString(passwdET)

                if (Utils.containsAlpha(passwd) && Utils.containsNumber(passwd) && passwd.count() > 5 && passwd.count() < 13) {
                    passwdConfirmTV.visibility = View.VISIBLE
                    passwdConfirmTV.text = getString(R.string.pwd_confirm)
                    nextTV.setBackgroundColor(Color.BLACK)
                } else {
                    passwdConfirmTV.visibility = View.GONE
                    nextTV.setBackgroundResource(R.drawable.background_border_strock2)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        nextTV.setOnClickListener {

            if(passwdConfirmTV.visibility == View.GONE) {
                Toast.makeText(context,"올바르지 않은 비밀번호입니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val passwd = Utils.getString(passwdET)
            PrefUtils.setPreference(context, "passwd", passwd)

            println("passwd::::::::::::::::::::::::::::::::::::::$passwd")

            val intent = Intent(context, JoinStep2NameActivity::class.java)
            startActivity(intent)

        }

    }

}
