package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.DaillyAdapter
import com.devstories.starball_android.adapter.GroupAdapter
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_group_chatting.*

class GroupChattingActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var GroupAdapter: GroupAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chatting)
        this.context = this
        progressDialog = ProgressDialog(context)


        GroupAdapter = GroupAdapter(context, R.layout.item_group_chatting, 30)
        groupLV.adapter = GroupAdapter
        groupLV.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, DlgProposeActivity::class.java)
            startActivity(intent)
        }
        starballIV.setOnClickListener {
            val intent = Intent(context, DlgSendProposeActivity::class.java)
            startActivity(intent)
        }

        plusLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                emoLL.visibility = View.VISIBLE
            } else {
                emoLL.visibility = View.GONE
            }
        }
        languageIV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                languageLL.visibility = View.VISIBLE
            } else {
                languageLL.visibility = View.GONE
            }
        }



        reportIV.setOnClickListener {
            val intent = Intent(context, ReportActivity::class.java)
            startActivity(intent)
        }
        starballIV.setOnClickListener {
            val intent = Intent(context, DlgCrushActivity::class.java)
            startActivity(intent)
        }

        backIV.setOnClickListener {
            finish()
        }





    }



}
