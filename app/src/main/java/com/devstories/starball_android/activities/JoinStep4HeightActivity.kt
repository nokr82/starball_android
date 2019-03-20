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
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_join_height.*


class JoinStep4HeightActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var height = ""

    var height1ETFirst = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.devstories.starball_android.R.layout.activity_join_height)
        this.context = this
        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

//        height= PrefUtils.getStringPreference(context,"height")

        nextTV.setBackgroundResource(R.drawable.background_border_strock2)
        height = PrefUtils.getStringPreference(context,"join_height", "")
        if(height.isNotEmpty()) {
            nextTV.setBackgroundColor(Color.BLACK)
            height1ET.setText(height.substring(0,1))

            if(height.length > 1) {
                height2ET.setText(height.substring(1,2))

                if(height.length > 2) {
                    height3ET.setText(height.substring(2,3))
                }

                height3ET.requestFocus()

            } else {
                height2ET.requestFocus()
            }

        } else {
            height1ET.requestFocus()
        }

        height1ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(height1ET.length()==1){
                    height2ET.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
        height1ET.setKeyImeChangeListener { event ->
            if (KeyEvent.KEYCODE_DEL === event.keyCode) {
                // do something
            }
        }

        height2ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(height2ET.length()==1){
                    height3ET.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
        height2ET.setKeyImeChangeListener { event ->
            if (KeyEvent.ACTION_DOWN == event.action && KeyEvent.KEYCODE_DEL === event.keyCode) {
                val height2 = Utils.getString(height2ET)
                if(height2.isEmpty()) {
                    height1ET.setSelection(height1ET.text.length)
                    height1ET.requestFocus()
                }
            }
        }

        height3ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (height1ET.length()>0&&height2ET.length()>0&&height3ET.length()>0){
                    nextTV.setBackgroundColor(Color.BLACK)
                }else{
                    nextTV.setBackgroundResource(R.drawable.background_border_strock2)
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
        height3ET.setKeyImeChangeListener { event ->
            if (KeyEvent.ACTION_DOWN == event.action && KeyEvent.KEYCODE_DEL === event.keyCode) {
                val height3 = Utils.getString(height3ET)
                if(height3.isEmpty()) {
                    height2ET.setSelection(height2ET.text.length)
                    height2ET.requestFocus()
                }
            }
        }






        nextTV.setOnClickListener {

            val height1 = Utils.getInt(height1ET)
            val height2 = Utils.getInt(height2ET)
            val height3 = Utils.getInt(height3ET)

            if (height1 < 1 || height2 < 0 || height3 < 0) {
                Toast.makeText(context, getString(com.devstories.starball_android.R.string.height_empty), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            height = height1.toString() + height2.toString() + height3.toString()
            PrefUtils.setPreference(context, "join_height", height)
            val intent = Intent(context, JoinStep5BirthActivity::class.java)

            startActivity(intent)
        }


    }

}
