package com.devstories.starball_android.activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_find_password.*

class FindPasswordActivity : RootActivity() {

    private var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password)

        context = this

        finishLL.setOnClickListener {
            finish()
            Utils.hideKeyboard(this)
        }
    }
}
