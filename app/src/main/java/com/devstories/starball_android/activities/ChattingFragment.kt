package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.EventAdapter
import kotlinx.android.synthetic.main.fragment_chatting.*

//메세지관리(메시지작성화면)

class ChattingFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var header: View
    lateinit var footer: View
    lateinit var plusIV: ImageView
    lateinit var groupLV: ListView
    lateinit var storyLV: ListView
    var type = -1
    lateinit var EventAdapter: EventAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_chatting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        footer = View.inflate(myContext, R.layout.item_chatting_foot, null)
        header = View.inflate(myContext, R.layout.item_chatting_head, null)

        plusIV = header.findViewById(R.id.plusIV)
        groupLV = header.findViewById(R.id.groupLV)
        storyLV = footer.findViewById(R.id.storyLV)

        EventAdapter = EventAdapter(myContext, R.layout.item_chatting_match, 0,type)
        chattingLV.adapter = EventAdapter
        chattingLV.addHeaderView(header)

        chattingLV.addHeaderView(footer)


    }


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}
