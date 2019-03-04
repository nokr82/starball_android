package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var message = "N"
    var match = "N"
    var crush = "N"
    var propose = "N"
    var daily = "N"
    var starball = "N"
    var vibe = "N"
    var sound = "N"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        this.context = this
        progressDialog = ProgressDialog(context)

        versionLL.setOnClickListener {
            val intent = Intent(context, VersionActivity::class.java)
            startActivity(intent)
        }
        alramLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if (it.isSelected) {
                alram_opLL.visibility = View.VISIBLE
            } else {
                alram_opLL.visibility = View.GONE
            }
        }

        logoutLL.setOnClickListener {
            val intent = Intent(context, JoinActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        backIV.setOnClickListener {
            finish()
        }

        click()
    }

    fun click() {

        messageLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                message = "Y"
                messageIV.setImageResource(R.mipmap.setting_toggle_on)
            } else {
                message = "N"
                messageIV.setImageResource(R.mipmap.off)
            }
        }
        matchLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                match = "Y"
                matchIV.setImageResource(R.mipmap.setting_toggle_on)
            } else {
                match = "N"
                matchIV.setImageResource(R.mipmap.off)
            }
        }
        crushLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                crush = "Y"
                crushIV.setImageResource(R.mipmap.setting_toggle_on)
            } else {
                crush = "N"
                crushIV.setImageResource(R.mipmap.off)
            }
        }
        proposeLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                propose = "Y"
                proposeIV.setImageResource(R.mipmap.setting_toggle_on)
            } else {
                propose = "N"
                proposeIV.setImageResource(R.mipmap.off)
            }
        }
        dailyLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                daily = "Y"
                dailyIV.setImageResource(R.mipmap.setting_toggle_on)
            } else {
                daily = "N"
                dailyIV.setImageResource(R.mipmap.off)
            }
        }
        starballLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                starball = "Y"
                starballIV.setImageResource(R.mipmap.setting_toggle_on)
            } else {
                starball = "N"
                starballIV.setImageResource(R.mipmap.off)
            }
        }
        vibeLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                vibe = "Y"
                vibeIV.setImageResource(R.mipmap.setting_toggle_on)
            } else {
                vibe = "N"
                vibeIV.setImageResource(R.mipmap.off)
            }
        }
        soundLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                sound = "Y"
                soundIV.setImageResource(R.mipmap.setting_toggle_on)
            } else {
                sound = "N"
                soundIV.setImageResource(R.mipmap.off)
            }
        }
    }


}
