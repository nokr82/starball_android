package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        this.context = this
        progressDialog = ProgressDialog(context)

        joinTV.setOnClickListener {

            val email = Utils.getString(emailET)

            if (email.count() < 1) {
                dlg(getString(R.string.email_empty))
                return@setOnClickListener
            }

            if (!Utils.isValidEmail(email)) {
                dlg(getString(R.string.email_fail))
                return@setOnClickListener
            }

            val intent = Intent(context, JoinStep1PasswdActivity::class.java)
            intent.putExtra("email", email)
            startActivity(intent)

        }

        loginTV.setOnClickListener {
//            val intent = Intent(context, MainSearchActivity::class.java)
//           val intent = Intent(context, MainActivity::class.java)
            val intent = Intent(context, LoginActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }





    }

    fun dlg(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }


}
