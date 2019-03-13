package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.MatchAdapter
import kotlinx.android.synthetic.main.fragment_chatting_send_crush.*

//메세지관리(메시지작성화면)

class ChattingSendCrushFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var sendcrushLV: ListView
    lateinit var header: View

    lateinit var MatchAdapter: MatchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_chatting_send_crush, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        header = View.inflate(myContext, R.layout.item_send_crush_header, null)
        sendcrushLV = header.findViewById(R.id.sendcrushLV)

        MatchAdapter = MatchAdapter(myContext, R.layout.item_chatting_match, 4)
        sendcrushLV.adapter = MatchAdapter

        sendlikeLV.addHeaderView(header)

        MatchAdapter = MatchAdapter(myContext, R.layout.item_chatting_match, 4)
        sendlikeLV.adapter = MatchAdapter


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}
