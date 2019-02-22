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
import kotlinx.android.synthetic.main.activity_join_height.*

class JoinStep4HeightActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var email = ""
    var passwd = ""
    var name = ""
    var gender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_height)
        this.context = this
        progressDialog = ProgressDialog(context)

        email = intent.getStringExtra("email")
        passwd = intent.getStringExtra("passwd")
        name = intent.getStringExtra("name")
        gender = intent.getStringExtra("gender")

        height1ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(height1ET.length()==1){
                    height2ET.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
        height2ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(height2ET.length()==1){
                    height3ET.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })





        nextTV.setOnClickListener {

            val height1 = Utils.getInt(height1ET)
            val height2 = Utils.getInt(height2ET)
            val height3 = Utils.getInt(height3ET)

            if (height1 < 1 || height2 < 0 || height3 < 0) {
                Toast.makeText(context, getString(R.string.height_empty), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val height = height1.toString() + height2.toString() + height3.toString()

            val intent = Intent(context, JoinStep5BirthActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("passwd", passwd)
            intent.putExtra("name", name)
            intent.putExtra("gender", gender)
            intent.putExtra("height", height)
            startActivity(intent)
        }

    }

}
