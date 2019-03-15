package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.dlg_chatting.*

class DlgChattingActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var chatting_id = -1
    var chatting_contents = ""
    var delete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_chatting)

        this.context = this
        progressDialog = ProgressDialog(context)

        chatting_id = intent.getIntExtra("chatting_id", -1)
        chatting_contents = intent.getStringExtra("chatting_contents")
        delete = intent.getBooleanExtra("delete", false)

        if (delete) {
            sendCancelTV.visibility = View.VISIBLE
        } else {
            sendCancelTV.visibility = View.GONE
        }

        copyTV.setOnClickListener {
            set_result(1)
        }

        sendCancelTV.setOnClickListener {
            set_result(2)
        }

        editTV.setOnClickListener {
            set_result(3)
        }

        addAdverbTV.setOnClickListener {
            set_result(4)
        }

    }

    fun set_result(type: Int){
        var intent = Intent()
        intent.putExtra("type", type)
        intent.putExtra("chatting_id", chatting_id)
        intent.putExtra("chatting_contents", chatting_contents)
        setResult(Activity.RESULT_OK, intent)

        finish()
    }

}
