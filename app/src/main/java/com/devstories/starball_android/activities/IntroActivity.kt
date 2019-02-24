package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class IntroActivity : RootActivity() {

    protected var _splashTime = 2000 // time to display the splash screen in ms
    private val _active = true
    private var splashThread: Thread? = null

    private var progressDialog: ProgressDialog? = null

    private var context: Context? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        this.context = this
        progressDialog = ProgressDialog(context)


        getHash()

        val buldle = intent.extras
        if (buldle != null) {
            try {

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        splashThread = object : Thread() {
            override fun run() {
                try {
                    var waited = 0
                    while (waited < _splashTime && _active) {
                        Thread.sleep(100)
                        waited += 100
                    }
                } catch (e: InterruptedException) {
                    // do nothing
                } finally {
                    stopIntro()
                }
            }
        }
        (splashThread as Thread).start()

    }

    private fun stopIntro() {
        // val intent = Intent(context, JoinResultActivity::class.java)
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

    }

    private fun getHash() {


        try {
            val info = packageManager.getPackageInfo("com.devstories.starball_android", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }

        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }


    }

}
