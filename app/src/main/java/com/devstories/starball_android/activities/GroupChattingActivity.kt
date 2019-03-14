package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.GroupChattingAdapter
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_group_chatting.*
import org.json.JSONObject
import java.util.ArrayList

class GroupChattingActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var GroupChattingAdapter: GroupChattingAdapter
    var adapterData = ArrayList<JSONObject>()
    var member_list  = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chatting)
        this.context = this
        progressDialog = ProgressDialog(context)


        member_list =  intent.getStringExtra("member_list")

        GroupChattingAdapter = GroupChattingAdapter(context, R.layout.item_group_chatting, adapterData,this)
        groupLV.adapter = GroupChattingAdapter

        groupLV.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, DlgProposeActivity::class.java)
            startActivity(intent)
        }

        cancelIV.setOnClickListener {
            cancelTV.visibility = View.VISIBLE
        }


        starballIV.setOnClickListener {
            val intent = Intent(context, DlgSendProposeActivity::class.java)
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



        reportIV.setOnClickListener {
            val intent = Intent(context, ReportActivity::class.java)
            startActivity(intent)
        }


        backIV.setOnClickListener {
            finish()
        }





    }



}
