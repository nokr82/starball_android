package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.base.DateUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_join_birth.*

class JoinStep5BirthActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var email = ""
    var passwd = ""
    var name = ""
    var gender = ""
    var height = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_birth)

        this.context = this
        progressDialog = ProgressDialog(context)

        email = intent.getStringExtra("email")
        passwd = intent.getStringExtra("passwd")
        name = intent.getStringExtra("name")
        gender = intent.getStringExtra("gender")
        height = intent.getStringExtra("height")

        year1ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setYearTV()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        year2ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setYearTV()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        year3ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setYearTV()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        year4ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setYearTV()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        month1ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMonthTV()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        month2ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMonthTV()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        day1ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setDayTV()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        day2ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setDayTV()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        moveedit()


        nextTV.setOnClickListener {

            val full_date = Utils.getString(yearTV) + "-" + Utils.getString(monthTV) + "-" + Utils.getString(dayTV)

            val checkDate = DateUtils.checkDate(full_date, "yyyy-MM-dd")

            if (!checkDate) {
                Toast.makeText(context, getString(R.string.birthday_fail), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val intent = Intent(context, JoinStep6LanguageActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("passwd", passwd)
            intent.putExtra("name", name)
            intent.putExtra("gender", gender)
            intent.putExtra("height", height)
            intent.putExtra("birth", full_date)
            startActivity(intent)
        }

    }

    fun moveedit(){
        year1ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(year1ET.length()==1){
                    year2ET.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
        year2ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(year2ET.length()==1){
                    year3ET.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
        year3ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(year3ET.length()==1){
                    year4ET.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
        year4ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(year4ET.length()==1){
                    month1ET.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
        month1ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(month1ET.length()==1){
                    month2ET.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
        month2ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(month2ET.length()==1){
                    day1ET.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
        day1ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(day1ET.length()==1){
                    day2ET.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

    }


    fun setYearTV() {
        yearTV.text = Utils.getString(year1ET) + Utils.getString(year2ET) + Utils.getString(year3ET) + Utils.getString(year4ET)
    }

    fun setMonthTV() {

//        var month1 = Utils.getInt(month1ET)
//        var month2 = Utils.getInt(month2ET)
//
//        if (month1 < 0 || month1 > 2) {
//            month1 = 0
//        }
//
//        if (month2 < 0 || month1 > 3) {
//            month2 = 0
//        }
//        month1ET.setText(month1.toString())
//        month2ET.setText(month2.toString())
        monthTV.text =  Utils.getString(month1ET) + Utils.getString(month2ET)
    }

    fun setDayTV() {

//        var day1 = Utils.getInt(day1ET)
//        var day2 = Utils.getInt(day2ET)
//
//        if (day1 < 0 || day1 > 3) {
//            day1 = 0
//        }
//
//        if (day2 < 0) {
//            day2 = 0
//        }
//        day1ET.setText(day1.toString())
//        day2ET.setText(day2.toString())

        dayTV.text =  Utils.getString(day1ET) + Utils.getString(day2ET)
    }

}
