package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_join_name.*

class JoinStep2NameActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var email = ""
    var passwd = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_name)

        this.context = this
        progressDialog = ProgressDialog(context)

        email = intent.getStringExtra("email")
        passwd = intent.getStringExtra("passwd")

        nameET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                nameTV.text = Utils.getString(nameET)

            }

            override fun afterTextChanged(s: Editable) {}
        })

        nextTV.setOnClickListener {

            val name = Utils.getString(nameET)

            if (name.count() < 1) {
                errorMsg(getString(R.string.name_empty))
                return@setOnClickListener
            }

            val intent = Intent(context, JoinStep3GenderActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("passwd", passwd)
            intent.putExtra("name", name)
            startActivity(intent)

        }

    }

    fun errorMsg(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

}
