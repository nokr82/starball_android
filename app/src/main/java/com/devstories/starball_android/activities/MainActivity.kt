package com.devstories.starball_android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
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

        val viewPager = findViewById(com.devstories.starball_android.R.id.viewpager) as ViewPager        //Viewpager 선언 및 초기화
        viewPager.adapter = adapter(getSupportFragmentManager()) //선언한 viewpager에 adapter를 연결


        chatIV.setOnClickListener {
            val intent = Intent(this, ChattingExActivity::class.java)
            startActivity(intent)
        }



        charIV.setOnClickListener {
            val intent = Intent(this, SettingMainActivity::class.java)
            startActivity(intent)
        }

        for (idx in 0 until 10) {
            val item = JSONObject()

            val pages = JSONArray()
            for(pageIdx in 0 until 7) {
                pages.put(JSONObject())
            }

            item.put("pages", pages)

            data.add(item)
        }

        val swipeStack = swipeStack as SwipeStack
        swipeStack.adapter = SwipeStackAdapter(mContext, data, swipeStack.getmSwipeHelper())
    }

    private inner class adapter//adapter클래스
        (fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            if (position < 0 || MAX_PAGE <= position)
            //가리키는 페이지가 0 이하거나 MAX_PAGE보다 많을 시 null로 리턴
                return null
            when (position) {
                0 -> cur_fragment = MainSearchExActivity()

                1 -> cur_fragment = MainSearchEx2Activity()

                2 -> cur_fragment = MainSearchEx3Activity()
            }

            return cur_fragment
        }

        override fun getCount(): Int {
            return MAX_PAGE
        }
    }
}
