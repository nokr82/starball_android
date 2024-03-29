package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_join_name.*

class JoinStep2NameActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var name  = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_name)

        this.context = this
        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)


        Log.d("패스",name)
        if (PrefUtils.getStringPreference(context,"join_name")!=null){
            if (PrefUtils.getStringPreference(context,"join_name").length>2){
                nextTV.setBackgroundColor(Color.BLACK)
            }else{
                nextTV.setBackgroundResource(R.drawable.background_border_strock2)
            }

            nameET.setText(PrefUtils.getStringPreference(context,"join_name"))
            nameTV.text = PrefUtils.getStringPreference(context,"join_name")
        }
        nameET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                nameTV.text = Utils.getString(nameET)
                if (s.length>2){
                    nextTV.setBackgroundColor(Color.BLACK)
                }else{
                    nextTV.setBackgroundResource(R.drawable.background_border_strock2)
                }

            }

            override fun afterTextChanged(s: Editable) {}
        })

        nextTV.setOnClickListener {

            name = Utils.getString(nameET)

            if (name.count() < 1) {
                errorMsg(getString(R.string.name_empty))
                return@setOnClickListener
            }
            PrefUtils.setPreference(context, "join_name", name)
            val intent = Intent(context, JoinStep3GenderActivity::class.java)
            startActivity(intent)

        }

    }

    fun errorMsg(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

}
