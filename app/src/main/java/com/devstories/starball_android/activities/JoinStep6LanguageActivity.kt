package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.LanguageAdapter
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_join_language.*

class JoinStep6LanguageActivity : RootActivity() {

    private val SELECT_LANGUAGE_REQUST_CODE = 1001

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var adapter:ArrayAdapter<String>
    var languages =  Resources.getSystem().getAssets().getLocales();


    lateinit var languageAdapter:LanguageAdapter
    var adapterData = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_language)

        this.context = this
        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        nextTV.setBackgroundResource(R.drawable.background_border_strock2)
        languageTV.setOnClickListener {
            val intent = Intent(context, DlgSelectLanguageActivity::class.java)
            startActivityForResult(intent, SELECT_LANGUAGE_REQUST_CODE)
        }

        languageAdapter = LanguageAdapter(context, R.layout.item_language, adapterData)
        languageLV.adapter = languageAdapter
        languageLV.setOnItemClickListener { parent, view, position, id ->
            adapterData.removeAt(position)

            languageAdapter.notifyDataSetChanged()
        }

        val joinLanguage = PrefUtils.getStringPreference(context, "join_language", "")
        if(joinLanguage.isNotEmpty()) {
            nextTV.setBackgroundColor(Color.BLACK)
            val splited = joinLanguage.split(",")
            for (language in splited) {
                adapterData.add(language.trim())
            }

            languageAdapter.notifyDataSetChanged()
        }

        nextTV.setOnClickListener {

            if(adapterData.count() < 1) {
                Toast.makeText(context, getString(R.string.language_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            PrefUtils.setPreference(context, "join_language", adapterData.joinToString())

            val intent = Intent(context, JoinStep7JobActivity::class.java)
            startActivity(intent)
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SELECT_LANGUAGE_REQUST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if(data != null) {
                        nextTV.setBackgroundColor(Color.BLACK)
                        adapterData.add(data.getStringExtra("selectedLanguage"))
                        languageAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

}
