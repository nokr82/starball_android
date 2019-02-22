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
import kotlinx.android.synthetic.main.activity_join_expression.*

class JoinStep9ExpActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    var email = ""
    var passwd = ""
    var name = ""
    var gender = ""
    var height = ""
    var language = ""
    var school = ""
    var birth = ""
    var job = ""
    var exp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_expression)
        this.context = this
        progressDialog = ProgressDialog(context)
        email = intent.getStringExtra("email")
        passwd = intent.getStringExtra("passwd")
        name = intent.getStringExtra("name")
        gender = intent.getStringExtra("gender")
        height = intent.getStringExtra("height")
        language = intent.getStringExtra("language")
        birth = intent.getStringExtra("birth")
        job = intent.getStringExtra("job")


        expET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (s.length<500){
                    limitTV.text = s.length.toString()+"/500"
                }else{
                    Toast.makeText(context,"더이상 입력할수 없습니다.",Toast.LENGTH_SHORT).show()
                }


            }
        })
        
        
        nextTV.setOnClickListener {
            exp = Utils.getString(expET)

            val intent = Intent(context, JoinStep10Activity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("passwd", passwd)
            intent.putExtra("name", name)
            intent.putExtra("gender", gender)
            intent.putExtra("height", height)
            intent.putExtra("birth", birth)
            intent.putExtra("language", language)
            intent.putExtra("job", job)
            intent.putExtra("school", school)
            intent.putExtra("exp", exp)
            startActivity(intent)
        }




    }



}
