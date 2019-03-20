package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.dlg_select_language.*
import java.util.*


class DlgSelectLanguageActivity : RootActivity() {

    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    private var ticketAdapter: ArrayAdapter<String>? = null

    private var selectedLanguage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.devstories.starball_android.R.layout.dlg_select_language)


        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        this.myContext = this
        progressDialog = ProgressDialog(myContext, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        val locales = Locale.getAvailableLocales()
        val languages = ArrayList<String>()
        for (locale in locales) {
            val country = locale.displayCountry
            val language = locale.displayLanguage
            if (country.isNotEmpty() && !languages.contains(country)) {
                languages.add("$language ($country)")
            }
        }
        Collections.sort(languages)

        selectedLanguage = languages.first()
        selectedLanguageTV.text = extractLanguage()

        val array = arrayOfNulls<String>(languages.size)
        languages.toArray(array)

        ticketAdapter = ArrayAdapter<String>(myContext, android.R.layout.simple_spinner_item, languages)
        //set min value zero
        ticketSP.minValue = 0
        //set max value from length array string reduced 1
        ticketSP.maxValue = languages.size - 1
        //implement array string to number picker
        ticketSP.displayedValues = array
        //disable soft keyboard
        ticketSP.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        //set wrap true or false, try it you will know the difference
        ticketSP.wrapSelectorWheel = false
        ticketSP.setOnValueChangedListener { picker, oldVal, newVal ->
            selectedLanguage = languages.get(newVal)
            selectedLanguageTV.text = extractLanguage()
        }

        selectTV.setOnClickListener {
            val intent = Intent()
            val language = extractLanguage()
            intent.putExtra("selectedLanguage", language)
            setResult(Activity.RESULT_OK, intent)

            finish()
        }
    }

    private fun extractLanguage(): String {
        val splited = selectedLanguage.split(" (")
        return splited[0]
    }


}
