package com.devstories.starball_android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.devstories.starball_android.swipestack.SwipeStack
import com.devstories.starball_android.swipestack.SwipeStackAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : FragmentActivity() {

    lateinit var mContext:Context
    internal var MAX_PAGE = 3                         //View Pager의 총 페이지 갯수를 나타내는 변수 선언
    internal var cur_fragment = Fragment()   //현재 Viewpager가 가리키는 Fragment를 받을 변수 선언

    var data = ArrayList<JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.devstories.starball_android.R.layout.activity_main)

        mContext = this

        chatIV.setOnClickListener {
            val intent = Intent(this, ChattingExActivity::class.java)
            startActivity(intent)
        }



        charIV.setOnClickListener {
            val intent = Intent(this, SettingMainActivity::class.java)
            startActivity(intent)
        }

        loadData()

        val swipeStack = swipeStack as SwipeStack
        swipeStack.adapter = SwipeStackAdapter(mContext, data, swipeStack.getmSwipeHelper())
        swipeStack.setListener(object : SwipeStack.SwipeStackListener {
            override fun onStackEmpty() {
                loadData()
            }

            override fun onViewSwipedToTop(position: Int) {

            }

            override fun onViewSwipedToBottom(position: Int) {

            }

            override fun onViewSwipedToLeft(position: Int) {

            }

            override fun onViewSwipedToRight(position: Int) {

            }
        })
    }

    private fun loadData() {

        for (idx in 0 until 10) {
            val item = JSONObject()

            val pages = JSONArray()
            for(pageIdx in 0 until 7) {
                pages.put(JSONObject())
            }

            item.put("pages", pages)

            data.add(item)
        }
    }

}
