package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.GroupAdapter
import com.devstories.starball_android.adapter.TalkAdapter
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_lounge_main.*

class ChattingActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var GroupAdapter: GroupAdapter

    lateinit var TalkAdapter: TalkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lounge_main)
        this.context = this
        progressDialog = ProgressDialog(context)

        GroupAdapter = GroupAdapter(context, R.layout.item_chat_profile, 2)
        groupLV.adapter = GroupAdapter
        TalkAdapter = TalkAdapter(context, R.layout.item_chat_profile, 2)
        talkLV.adapter = TalkAdapter


        timeIV.setOnClickListener {
            val intent = Intent(context, DailyMomentListActivity::class.java)
            startActivity(intent)
        }
        plusIV.setOnClickListener {
            val intent = Intent(context, DailyMomentViewListActivity::class.java)
            startActivity(intent)
        }
        backIV.setOnClickListener {
            finish()
        }








    }



}
