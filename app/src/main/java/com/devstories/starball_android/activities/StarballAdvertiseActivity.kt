package com.devstories.starball_android.activities

import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_starball_advertise.*

class StarballAdvertiseActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starball_advertise)


        closeIV.setOnClickListener {
            finish()
        }

    }

}
