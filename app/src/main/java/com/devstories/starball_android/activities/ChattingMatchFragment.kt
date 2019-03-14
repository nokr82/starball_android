package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.MatchAdapter
import kotlinx.android.synthetic.main.fragment_chatting_match.*

//채팅 매칭

class ChattingMatchFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var MatchAdapter: MatchAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_chatting_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        MatchAdapter = MatchAdapter(myContext, R.layout.item_chatting_match, 8)
        matchLV.adapter = MatchAdapter
    }


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}
