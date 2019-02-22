package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.LanguageAdapter
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_join_language.*
import org.json.JSONObject

class JoinStep6LanguageActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var adapter:ArrayAdapter<String>
    var languages =  Resources.getSystem().getAssets().getLocales();


    lateinit var languageAdapter:LanguageAdapter
    var adapterData = ArrayList<JSONObject>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_language)

        this.context = this
        progressDialog = ProgressDialog(context)


        adapter = ArrayAdapter(context, R.layout.spinner_item, languages)
        languageSP.adapter = adapter
        if (PrefUtils.getStringPreference(context,"language")!=null){
          Log.d("언어",PrefUtils.getStringPreference(context,"language"))
        }


        languageAdapter = LanguageAdapter(context, R.layout.item_language, adapterData)
        languageLV.adapter = languageAdapter

        addLanguageLL.setOnClickListener {
            val addData = languageSP.selectedItem as String

            for(i in 0 until adapterData.size) {
                val data = adapterData[i]
                val data_name = Utils.getString(data, "name")

                if (data_name == addData) {
                    return@setOnClickListener
                }

            }

            val json = JSONObject()
            json.put("name", addData)

            adapterData.add(json)
            languageAdapter.notifyDataSetChanged()

        }

        nextTV.setOnClickListener {

            if(adapterData.count() < 1) {
                Toast.makeText(context, getString(R.string.language_empty), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            println("adapterData.toString():::::::::::::::::::::::::::::${adapterData.toString()}")
            println("adapterData:::::::::::::::::::::::::::::${adapterData}")

            val intent = Intent(context, JoinStep7JobActivity::class.java)

            PrefUtils.setPreference(context, "language", adapterData.toString())

            startActivity(intent)
        }


    }



}
