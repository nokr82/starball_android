package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.DaillyAdapter
import com.devstories.starball_android.adapter.EventAdapter
import com.devstories.starball_android.adapter.GroupAdapter
import com.devstories.starball_android.adapter.TalkAdapter
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_lounge_main.*
import kotlinx.android.synthetic.main.activity_lounge_main.view.*

class ChattingActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var GroupAdapter: GroupAdapter

    lateinit var TalkAdapter: TalkAdapter

    lateinit var EventAdapter: EventAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lounge_main)
        this.context = this
        progressDialog = ProgressDialog(context)

        GroupAdapter = GroupAdapter(context, R.layout.item_chat_profile, 1)
        groupLV.adapter = GroupAdapter
        TalkAdapter = TalkAdapter(context, R.layout.item_chat_profile, 1)
        talkLV.adapter = TalkAdapter

        groupLV.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, GroupChattingActivity::class.java)
            startActivity(intent)
        }
        talkLV.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, FriendChattingActivity::class.java)
            startActivity(intent)
        }

        timeIV.setOnClickListener {
            val intent = Intent(context, DailyMomentListActivity::class.java)
            startActivity(intent)
        }
        plusIV.setOnClickListener {
            val intent = Intent(context, GrouptMakeActivity::class.java)
            startActivity(intent)
        }
        backIV.setOnClickListener {
            finish()
        }
        EventAdapter = EventAdapter(context, R.layout.item_chatting_match, 6)
        eventLV.adapter = EventAdapter





        eventLV.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, DlgCrushActivity::class.java)
            startActivity(intent)
        }

        storyLL.setOnClickListener {
            setmenu()
            storyLL.visibility = View.VISIBLE
            friendLL.visibility = View.GONE
            eventLV.visibility = View.VISIBLE
        }
        matchLL.setOnClickListener {

        }
        reciveLL.setOnClickListener {

        }
        sendLL.setOnClickListener {

        }

        moreIV.setOnClickListener {
            menuLL.visibility = View.GONE
            eventLL.visibility = View.VISIBLE
        }
        more2IV.setOnClickListener {
            menuLL.visibility = View.VISIBLE
            eventLL.visibility = View.GONE
            friendLL.visibility = View.VISIBLE
            eventLV.visibility = View.GONE
        }

        storyIV.setOnClickListener {
            setmenu()
            menuLL.visibility = View.GONE
            storyTV.visibility = View.GONE
            eventLL.visibility = View.VISIBLE
            storyLL.visibility = View.VISIBLE
            friendLL.visibility = View.GONE
            eventLV.visibility = View.VISIBLE
        }
        storymoreIV.setOnClickListener {
            menuLL.visibility = View.VISIBLE
            eventLL.visibility = View.GONE
            friendLL.visibility = View.VISIBLE
            eventLV.visibility = View.GONE
        }


    }

    fun setmenu(){
        event2LL.visibility = View.GONE
        storyLL.visibility = View.GONE
        matchLL.visibility = View.GONE
        reciveLL.visibility = View.GONE
        sendLL.visibility = View.GONE
    }


}
