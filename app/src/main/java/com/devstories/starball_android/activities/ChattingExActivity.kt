package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.EventAdapter
import com.devstories.starball_android.adapter.ChattingAdapter
import com.devstories.starball_android.adapter.ChattingRoomAdapter
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_chatting_ex.*

class ChattingExActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var GroupAdapter: ChattingAdapter

    lateinit var TalkAdapter: ChattingRoomAdapter

    lateinit var EventAdapter: EventAdapter

    var type = -1
    lateinit var header: View
    lateinit var footer: View
    lateinit var backIV: ImageView
    lateinit var timeIV: ImageView
    lateinit var plusIV: ImageView
    lateinit var matchRL: RelativeLayout
    lateinit var sendRL: RelativeLayout
    lateinit var reciveRL: RelativeLayout
    lateinit var storyRL: RelativeLayout
    lateinit var matchmoreTV: ImageView
    lateinit var storymoreIV: ImageView
    lateinit var recivemoreIV: ImageView
    lateinit var sendmoreIV: ImageView
    lateinit var more2IV: ImageView
    lateinit var storyLL: LinearLayout
    lateinit var moreIV: ImageView
    lateinit var friendLL: LinearLayout
    lateinit var sendLL: LinearLayout
    lateinit var reciveLL: LinearLayout
    lateinit var matchLL: LinearLayout
    lateinit var recive2LL: LinearLayout
    lateinit var send2LL: LinearLayout
    lateinit var menuLL: LinearLayout
    lateinit var eventLL: LinearLayout
    lateinit var event2LL: RelativeLayout
    lateinit var storyTV: TextView
    lateinit var groupLL: LinearLayout
    lateinit var story_op2LL: LinearLayout
    lateinit var group2LL: LinearLayout
    lateinit var story_opLL: LinearLayout
    lateinit var star_opIV: ImageView
    lateinit var titleTV: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting_ex)
        this.context = this
        progressDialog = ProgressDialog(context)
        footer = View.inflate(this, R.layout.item_group_make_footer, null)
        header = View.inflate(this, R.layout.item_lounge_head, null)

        groupLL = header.findViewById(R.id.groupLL)
        story_op2LL = header.findViewById(R.id.story_op2LL)
        group2LL = header.findViewById(R.id.group2LL)
        story_opLL = header.findViewById(R.id.story_opLL)
        backIV = header.findViewById(R.id.backIV)
        timeIV = header.findViewById(R.id.timeIV)
        plusIV = header.findViewById(R.id.plusIV)
        matchRL = header.findViewById(R.id.matchRL)
        sendRL = header.findViewById(R.id.sendRL)
        reciveRL = header.findViewById(R.id.reciveRL)
        storyRL = header.findViewById(R.id.storyRL)
        matchmoreTV = header.findViewById(R.id.matchmoreTV)
        storymoreIV = header.findViewById(R.id.storymoreIV)
        recivemoreIV = header.findViewById(R.id.recivemoreIV)
        sendmoreIV = header.findViewById(R.id.sendmoreIV)
        more2IV = header.findViewById(R.id.more2IV)
        storyLL = header.findViewById(R.id.storyLL)
        moreIV = header.findViewById(R.id.moreIV)
        friendLL = header.findViewById(R.id.friendLL)
        sendLL = header.findViewById(R.id.sendLL)
        reciveLL = header.findViewById(R.id.reciveLL)
        matchLL = header.findViewById(R.id.matchLL)
        recive2LL = header.findViewById(R.id.recive2LL)
        send2LL = header.findViewById(R.id.send2LL)
        menuLL = header.findViewById(R.id.menuLL)
        eventLL = header.findViewById(R.id.eventLL)
        storyTV = header.findViewById(R.id.storyTV)
        event2LL = header.findViewById(R.id.event2LL)
//        star_opIV = footer.findViewById(R.id.star_opIV)
        titleTV = footer.findViewById(R.id.titleTV)


        EventAdapter = EventAdapter(context, R.layout.item_chatting_match, 0, type)
        eventLV.adapter = EventAdapter
        eventLV.addHeaderView(header)



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

        groupLL.setOnClickListener {
            val intent = Intent(context, GroupChattingActivity::class.java)
            startActivity(intent)
        }
        story_op2LL.setOnClickListener {
            val intent = Intent(context, FriendChattingActivity::class.java)
            startActivity(intent)
        }
        group2LL.setOnClickListener {
            val intent = Intent(context, GroupChattingActivity::class.java)
            startActivity(intent)
        }
        story_opLL.setOnClickListener {
            val intent = Intent(context, FriendChattingActivity::class.java)
            startActivity(intent)
        }



        storyLL.setOnClickListener {
            setmenu()
            storyLL.visibility = View.VISIBLE
            friendLL.visibility = View.VISIBLE
            eventLV.visibility = View.VISIBLE

        }
        matchLL.setOnClickListener {
            setmenu()
            storyLL.visibility = View.VISIBLE
            friendLL.visibility = View.GONE
            eventLV.visibility = View.VISIBLE
        }
        reciveLL.setOnClickListener {
            setmenu()
            reciveLL.visibility = View.VISIBLE
            friendLL.visibility = View.GONE
            eventLV.visibility = View.VISIBLE
            recive2LL.visibility = View.VISIBLE
        }
        sendLL.setOnClickListener {
            setmenu()
            sendLL.visibility = View.VISIBLE
            friendLL.visibility = View.GONE
            eventLV.visibility = View.VISIBLE
            send2LL.visibility = View.VISIBLE
        }


        moreIV.setOnClickListener {
            eventLV.removeFooterView(footer)
            EventAdapter = EventAdapter(context, R.layout.item_chatting_match, 0, type)
            eventLV.adapter = EventAdapter
            menuLL.visibility = View.GONE
            eventLL.visibility = View.VISIBLE
        }
        more2IV.setOnClickListener {
            setmenu()
            eventLV.removeFooterView(footer)
            menuLL.visibility = View.VISIBLE
            eventLL.visibility = View.GONE
            friendLL.visibility = View.VISIBLE
        }



        sendmoreIV.setOnClickListener {
            setmenu()
            eventLV.removeFooterView(footer)
            EventAdapter = EventAdapter(context, R.layout.item_chatting_match, 0, type)
            eventLV.adapter = EventAdapter
            menuLL.visibility = View.VISIBLE
            eventLL.visibility = View.GONE
            friendLL.visibility = View.VISIBLE
        }
        recivemoreIV.setOnClickListener {
            setmenu()
            EventAdapter = EventAdapter(context, R.layout.item_chatting_match, 0, type)
            eventLV.adapter = EventAdapter
            menuLL.visibility = View.VISIBLE
            eventLL.visibility = View.GONE
            friendLL.visibility = View.VISIBLE
        }
        matchmoreTV.setOnClickListener {
            setmenu()
            eventLV.removeFooterView(footer)
            EventAdapter = EventAdapter(context, R.layout.item_chatting_match, 0, type)
            eventLV.adapter = EventAdapter
            menuLL.visibility = View.VISIBLE
            eventLL.visibility = View.GONE
            friendLL.visibility = View.VISIBLE
        }


        storymoreIV.setOnClickListener {
            eventLV.removeFooterView(footer)

            EventAdapter = EventAdapter(context, R.layout.item_chatting_match, 0, type)
            eventLV.adapter = EventAdapter
            menuLL.visibility = View.VISIBLE
            eventLL.visibility = View.GONE
            friendLL.visibility = View.VISIBLE
        }
        storyRL.setOnClickListener {
            setmenu()
            eventLV.removeFooterView(footer)
            menuLL.visibility = View.GONE
            storyTV.visibility = View.GONE
            eventLL.visibility = View.VISIBLE
            storyLL.visibility = View.VISIBLE
            friendLL.visibility = View.VISIBLE
        }


        reciveRL.setOnClickListener {
            setmenu()
            eventLV.addFooterView(footer)
            star_opIV.setImageResource(R.mipmap.recive_star)
            type = 2
            EventAdapter = EventAdapter(context, R.layout.item_chatting_match, 3, type)
            eventLV.adapter = EventAdapter
            menuLL.visibility = View.GONE
            storyTV.visibility = View.GONE
            eventLL.visibility = View.VISIBLE
            reciveLL.visibility = View.VISIBLE
            friendLL.visibility = View.GONE
            recive2LL.visibility = View.VISIBLE
        }

        sendRL.setOnClickListener {
            setmenu()
            eventLV.addFooterView(footer)
            star_opIV.setImageResource(R.mipmap.lounge_heart_like)
            titleTV.text = "보낸 Like"
            type = 3
            EventAdapter = EventAdapter(context, R.layout.item_chatting_match, 3, type)
            eventLV.adapter = EventAdapter
            menuLL.visibility = View.GONE
            storyTV.visibility = View.GONE
            eventLL.visibility = View.VISIBLE
            sendLL.visibility = View.VISIBLE
            friendLL.visibility = View.GONE
            eventLV.visibility = View.VISIBLE
            send2LL.visibility = View.VISIBLE
        }
        matchRL.setOnClickListener {
            setmenu()
            eventLV.removeFooterView(footer)
            type = 1
            EventAdapter = EventAdapter(context, R.layout.item_chatting_match, 1, type)
            eventLV.adapter = EventAdapter
            menuLL.visibility = View.GONE
            storyTV.visibility = View.GONE
            eventLL.visibility = View.VISIBLE
            matchLL.visibility = View.VISIBLE
            friendLL.visibility = View.GONE
            eventLV.visibility = View.VISIBLE
        }


    }

    fun setmenu() {
        recive2LL.visibility = View.GONE
        send2LL.visibility = View.GONE
        event2LL.visibility = View.GONE
        storyLL.visibility = View.GONE
        matchLL.visibility = View.GONE
        reciveLL.visibility = View.GONE
        sendLL.visibility = View.GONE
    }


}
