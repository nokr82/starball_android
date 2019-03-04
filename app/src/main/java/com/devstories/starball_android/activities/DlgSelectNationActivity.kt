package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.dlg_select_language.*
import java.util.*


class DlgSelectNationActivity : RootActivity() {

    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    private var ticketAdapter: ArrayAdapter<String>? = null

    private var selectedNation = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.devstories.starball_android.R.layout.dlg_select_language)

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        this.myContext = this
        progressDialog = ProgressDialog(myContext)

        val locales = Locale.getAvailableLocales()
        val nations = ArrayList<String>()
        for (locale in locales) {
            val country = locale.displayCountry
            if (country.isNotEmpty()) {
                nations.add("$country")
            }
        }
        Collections.sort(nations)

        selectedNation = nations.first()
        selectedLanguageTV.text = selectedNation

        val array = arrayOfNulls<String>(nations.size)
        nations.toArray(array)

        ticketAdapter = ArrayAdapter<String>(myContext, android.R.layout.simple_spinner_item, nations)
        //set min value zero
        ticketSP.minValue = 0
        //set max value from length array string reduced 1
        ticketSP.maxValue = nations.size - 1
        //implement array string to number picker
        ticketSP.displayedValues = array
        //disable soft keyboard
        ticketSP.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        //set wrap true or false, try it you will know the difference
        ticketSP.wrapSelectorWheel = false
        ticketSP.setOnValueChangedListener { picker, oldVal, newVal ->
            selectedNation = nations.get(newVal)
            selectedLanguageTV.text = selectedNation
        }

        selectTV.setOnClickListener {
            val intent = Intent()
            val nation = selectedNation
            intent.putExtra("selectedNation", nation)
            setResult(Activity.RESULT_OK, intent)

            finish()
        }
    }




}
