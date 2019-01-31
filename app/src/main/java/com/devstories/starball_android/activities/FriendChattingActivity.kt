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
import kotlinx.android.synthetic.main.activity_friend_chatting.*


class FriendChattingActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var GroupAdapter: GroupAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_chatting)
        this.context = this
        progressDialog = ProgressDialog(context)


        GroupAdapter = GroupAdapter(context, R.layout.item_chatting, 30)
        groupLV.adapter = GroupAdapter
        groupLV.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, DlgProposeActivity::class.java)
            startActivity(intent)
        }

        groupLV.setOnItemLongClickListener { parent, view, position, id ->


            return@setOnItemLongClickListener true
        }

        starballIV.setOnClickListener {
            val intent = Intent(context, DlgSendProposeActivity::class.java)
            startActivity(intent)
        }


        reportIV.setOnClickListener {
            val intent = Intent(context, ReportActivity::class.java)
            startActivity(intent)
        }


        globalIV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                globalIV.setImageResource(R.mipmap.global_on)
            } else {
                globalIV.setImageResource(R.mipmap.global)
            }
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
                languageIV.setImageResource(R.mipmap.bubble_on)
                languageLL.visibility = View.VISIBLE
            } else {
                languageIV.setImageResource(R.mipmap.bubble)
                languageLL.visibility = View.GONE
            }
        }

        backIV.setOnClickListener {
            finish()
        }





    }



}