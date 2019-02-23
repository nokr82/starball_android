package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_join_expression.*

class JoinStep9ExpActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var exp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_expression)

        this.context = this
        progressDialog = ProgressDialog(context)

        var exp = PrefUtils.getStringPreference(context,"join_exp", "")
        if (exp.isNotEmpty()){
            expET.setText(exp)
            limitTV.text = exp.length.toString()+"/500"
        }

        expET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (s.length <= 500){
                    limitTV.text = s.length.toString()+"/500"
                } else{
                    Toast.makeText(context,"더이상 입력할수 없습니다.",Toast.LENGTH_SHORT).show()
                }
            }
        })

        nextTV.setOnClickListener {
            exp = Utils.getString(expET)

            if (exp.isEmpty()) {
                Toast.makeText(context, "당신을 표현할 수 있는 말을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            PrefUtils.setPreference(context, "join_exp", exp)

            val intent = Intent(context, JoinStep10Activity::class.java)
            startActivity(intent)
        }

        skipTV.setOnClickListener {

            PrefUtils.removePreference(context, "join_exp")

            val intent = Intent(context, JoinStep10Activity::class.java)
            startActivity(intent)
        }
    }
}
