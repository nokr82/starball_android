package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_join_pwstep.*

class JoinStep1PasswdActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_pwstep)

        this.context = this
        progressDialog = ProgressDialog(context)

        email = intent.getStringExtra("email")

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
                return@setOnClickListener
            }

            val passwd = Utils.getString(passwdET)

            val intent = Intent(context, JoinStep2NameActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("passwd", passwd)
            startActivity(intent)

        }

    }

}
