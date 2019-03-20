package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.base.DateUtils
import com.devstories.starball_android.base.PrefUtils
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
    var birth = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_birth)

        this.context = this
        progressDialog = ProgressDialog(context)

        nextTV.setBackgroundResource(R.drawable.background_border_strock2)

        birth = PrefUtils.getStringPreference(context, "join_birth", "")
        if (birth.isNotEmpty()) {

            year1ET.setText(birth.substring(0, 1))

            if(birth.length > 1) {
                year2ET.setText(birth.substring(1, 2))

                if(birth.length > 2) {
                    year3ET.setText(birth.substring(2, 3))

                    if(birth.length > 3) {
                        year4ET.setText(birth.substring(3, 4))

                        if(birth.length > 5) {
                            month1ET.setText(birth.substring(5, 6))

                            if(birth.length > 6) {
                                month2ET.setText(birth.substring(6, 7))

                                if(birth.length > 8) {
                                    day1ET.setText(birth.substring(8, 9))
                                    if(birth.length > 9) {
                                        nextTV.setBackgroundColor(Color.BLACK)
                                        day2ET.setText(birth.substring(9, 10))
                                    }

                                    day2ET.requestFocus()

                                } else {
                                    day1ET.requestFocus()
                                }

                            } else {
                                month2ET.requestFocus()
                            }

                        } else {
                            month1ET.requestFocus()
                        }
                    } else {
                        year4ET.requestFocus()
                    }
                } else {
                    year3ET.requestFocus()
                }
            } else {
                year2ET.requestFocus()
            }

            setYearTV()
            setMonthTV()
            setDayTV()

        } else {
            year1ET.requestFocus()
        }


        year1ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {

                if(s.isEmpty()) {
                    return
                }

                if(s.toString().toInt() < 1) {
                    year1ET.setText("")
                    Toast.makeText(context, getString(R.string.birthday_fail), Toast.LENGTH_SHORT).show()
                    return
                }

                if(validateYear()) {
                    if (year1ET.length() == 1) {
                        year2ET.requestFocus()
                    }

                    setYearTV()

                } else {
                    year1ET.setText("")
                }
            }
        })
        year1ET.setKeyImeChangeListener { event ->
            if (KeyEvent.ACTION_DOWN == event.action && KeyEvent.KEYCODE_DEL === event.keyCode) {

            }
        }

        year2ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {

                if(s.isEmpty()) {
                    return
                }

                if(validateYear()) {
                    if (year2ET.length() == 1) {
                        year3ET.requestFocus()
                    }

                    setYearTV()

                } else {
                    year2ET.setText("")
                }
            }
        })
        year2ET.setKeyImeChangeListener { event ->
            if (KeyEvent.ACTION_DOWN == event.action && KeyEvent.KEYCODE_DEL === event.keyCode) {
                val year2 = Utils.getString(year2ET)
                if (year2.isEmpty()) {
                    year1ET.setSelection(year1ET.text.length)
                    year1ET.requestFocus()
                }
            }
        }

        year3ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setYearTV()
            }

            override fun afterTextChanged(s: Editable) {

                if(s.isEmpty()) {
                    return
                }

                if(validateYear()) {
                    if (year3ET.length() == 1) {
                        year4ET.requestFocus()
                    }

                    setYearTV()

                } else {
                    year3ET.setText("")
                }
            }
        })
        year3ET.setKeyImeChangeListener { event ->
            if (KeyEvent.ACTION_DOWN == event.action && KeyEvent.KEYCODE_DEL === event.keyCode) {
                val year3 = Utils.getString(year3ET)
                if (year3.isEmpty()) {
                    year2ET.setSelection(year2ET.text.length)
                    year2ET.requestFocus()
                }
            }
        }

        year4ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setYearTV()
            }

            override fun afterTextChanged(s: Editable) {

                if(s.isEmpty()) {
                    return
                }

                if(validateYear()) {
                    if (year4ET.length() == 1) {
                        month1ET.requestFocus()
                    }

                    setYearTV()

                } else {
                    year4ET.setText("")
                }
            }
        })
        year4ET.setKeyImeChangeListener { event ->
            if (KeyEvent.ACTION_DOWN == event.action && KeyEvent.KEYCODE_DEL === event.keyCode) {
                val year4 = Utils.getString(year4ET)
                if (year4.isEmpty()) {
                    year3ET.setSelection(year3ET.text.length)
                    year3ET.requestFocus()
                }
            }
        }

        month1ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {

                if(s.isEmpty()) {
                    return
                }

                if(s.toString().toInt() > 1) {
                    month1ET.setText("")
                    Toast.makeText(context, getString(R.string.birthday_fail), Toast.LENGTH_SHORT).show()
                    return
                }

                setMonthTV()

                if (month1ET.length() == 1) {
                    month2ET.requestFocus()
                }
            }
        })
        month1ET.setKeyImeChangeListener { event ->
            if (KeyEvent.ACTION_DOWN == event.action && KeyEvent.KEYCODE_DEL === event.keyCode) {
                val month1 = Utils.getString(month1ET)
                if (month1.isEmpty()) {
                    year4ET.setSelection(year4ET.text.length)
                    year4ET.requestFocus()
                }
            }
        }

        month2ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMonthTV()
            }

            override fun afterTextChanged(s: Editable) {

                if(s.isEmpty()) {
                    return
                }

                if (validateMonth()) {
                    setMonthTV()

                    if (month2ET.length() == 1) {
                        day1ET.requestFocus()
                    }

                } else {
                    month2ET.setText("")
                }
            }
        })
        month2ET.setKeyImeChangeListener { event ->
            if (KeyEvent.ACTION_DOWN == event.action && KeyEvent.KEYCODE_DEL === event.keyCode) {
                val month2 = Utils.getString(month2ET)
                if (month2.isEmpty()) {
                    month1ET.setSelection(month1ET.text.length)
                    month1ET.requestFocus()
                }
            }
        }

        day1ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setDayTV()
            }

            override fun afterTextChanged(s: Editable) {

                if(s.isEmpty()) {
                    return
                }

                if(s.toString().toInt() > 3) {
                    Log.d("생일1","")
                    day1ET.setText("")
                    Toast.makeText(context, getString(R.string.birthday_fail), Toast.LENGTH_SHORT).show()
                    return
                }

                val day2 = Utils.getInt(day2ET, 0)
                if(day2 > 0) {
                    if(validateDate()) {
                        setDayTV()

                        if (day1ET.length() == 1) {
                            day2ET.requestFocus()
                        }

                    } else {
                        Log.d("생일","")
                        day1ET.setText("")
                    }
                } else {
                    setDayTV()

                    if (day1ET.length() == 1) {
                        day2ET.requestFocus()
                    }
                }
            }
        })
        day1ET.setKeyImeChangeListener { event ->
            if (KeyEvent.ACTION_DOWN == event.action && KeyEvent.KEYCODE_DEL === event.keyCode) {
                val day1 = Utils.getString(day1ET)
                if (day1.isEmpty()) {
                    month2ET.setSelection(month2ET.text.length)
                    month2ET.requestFocus()
                }
            }
        }

        day2ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (year1ET.length()>0&&year2ET.length()>0&&year3ET.length()>0 &&year4ET.length()>0
                    &&month1ET.length()>0&&month2ET.length()>0
                    &&day1ET.length()>0&&day2ET.length()>0){
                    nextTV.setBackgroundColor(Color.BLACK)
                }else{
                    nextTV.setBackgroundResource(R.drawable.background_border_strock2)
                }
            }

            override fun afterTextChanged(s: Editable) {

                if(s.isEmpty()) {
                    return
                }

                if(validateDate()) {
                    setDayTV()

                } else {
                    day2ET.setText("")
                }
            }
        })
        day2ET.setKeyImeChangeListener { event ->
            if (KeyEvent.ACTION_DOWN == event.action && KeyEvent.KEYCODE_DEL === event.keyCode) {
                val day2 = Utils.getString(day2ET)
                if (day2.isEmpty()) {
                    day1ET.setSelection(day1ET.text.length)
                    day1ET.requestFocus()
                }
            }
        }

        // moveedit()


        nextTV.setOnClickListener {

            val full_date = Utils.getString(yearTV) + "-" + Utils.getString(monthTV) + "-" + Utils.getString(dayTV)

            val checkDate = DateUtils.checkDate(full_date, "yyyy-MM-dd")
            if (!checkDate) {
                Toast.makeText(context, getString(R.string.birthday_fail), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            birth = full_date
            PrefUtils.setPreference(context, "join_birth", birth)


            val intent = Intent(context, JoinStep6LanguageActivity::class.java)
            startActivity(intent)
        }

    }

    private fun validateYear(): Boolean {
        val year = "${Utils.getInt(year1ET, 0)}${Utils.getInt(year2ET, 0)}${Utils.getInt(year3ET, 0)}${Utils.getInt(year4ET, 0)}".toInt()
        val nowYear = DateUtils.getToday("yyyy").toInt()

        if ((nowYear - year) < 0) {
            Toast.makeText(context, getString(R.string.birthday_fail), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validateMonth(): Boolean {
        val month = "${Utils.getInt(month1ET, 0)}${Utils.getInt(month2ET, 0)}".toInt()
        if (month > 12 || month == 0) {
            Toast.makeText(context, getString(R.string.birthday_fail), Toast.LENGTH_SHORT).show()
            return false
        }

        val ym = "${Utils.getInt(year1ET, 0)}${Utils.getInt(year2ET, 0)}${Utils.getInt(year3ET, 0)}${Utils.getInt(year4ET, 0)}-${Utils.getInt(month1ET, 0)}${Utils.getInt(month2ET, 0)}"
        val diff = DateUtils.getDiffDayCount(ym, DateUtils.getToday("yyyy-MM"), "yyyy-MM")
        if(diff < 0) {
            Toast.makeText(context, getString(R.string.birthday_fail), Toast.LENGTH_SHORT).show()
            return false
        }

        return return true
    }

    private fun validateDate(): Boolean {
        val full_date = Utils.getString(yearTV) + "-" + Utils.getString(monthTV) + "-" + "${Utils.getInt(day1ET, 0)}${Utils.getInt(day2ET, 0)}"
        val validDate = DateUtils.isValidDate(full_date, "yyyy-MM-dd")
        if(!validDate) {
            Toast.makeText(context, getString(R.string.birthday_fail), Toast.LENGTH_SHORT).show()
            return false
        }

        val diff = DateUtils.getDiffDayCount(full_date, DateUtils.getToday("yyyy-MM-dd"), "yyyy-MM-dd")
        if(diff < 0) {
            Toast.makeText(context, getString(R.string.birthday_fail), Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    fun setYearTV() {
        yearTV.text = Utils.getString(year1ET) + Utils.getString(year2ET) + Utils.getString(year3ET) + Utils.getString(year4ET)
    }

    fun setMonthTV() {
        monthTV.text = Utils.getString(month1ET) + Utils.getString(month2ET)
    }

    fun setDayTV() {
        dayTV.text = Utils.getString(day1ET) + Utils.getString(day2ET)
    }

}
