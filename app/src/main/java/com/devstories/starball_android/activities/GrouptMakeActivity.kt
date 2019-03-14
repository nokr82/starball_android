package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.GroupAdapter
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_group_make.*

class GrouptMakeActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var GroupAdapter: GroupAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_make)
        this.context = this
        progressDialog = ProgressDialog(context)


        GroupAdapter = GroupAdapter(context, R.layout.item_group_profile, 12)
        groupLV.adapter = GroupAdapter
        groupLV.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, GroupChattingActivity::class.java)
            startActivity(intent)
        }

        backIV.setOnClickListener {
            finish()
        }





    }



}
