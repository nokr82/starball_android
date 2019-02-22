package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
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
        progressDialog = ProgressDialog(context)

//        passwd= PrefUtils.getStringPreference(context,"passwd")
        Log.d("패스",passwd)
        if (passwd!=null){
            passwdET.setText(passwd)
        }

        passwdET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val passwd = Utils.getString(passwdET)

                if (Utils.containsAlpha(passwd) && Utils.containsNumber(passwd) && passwd.count() > 5 && passwd.count() < 13) {
                    passwdConfirmTV.visibility = View.VISIBLE
                    passwdConfirmTV.text = getString(R.string.pwd_confirm)
                } else {
                    passwdConfirmTV.visibility = View.GONE
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

            val intent = Intent(context, JoinStep2NameActivity::class.java)
            startActivity(intent)

        }

    }

}
