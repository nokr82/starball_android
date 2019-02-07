package com.devstories.starball_android.activities

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.devstories.starball_android.R


class MainActivity : FragmentActivity() {

    internal var MAX_PAGE = 3                         //View Pager의 총 페이지 갯수를 나타내는 변수 선언
    internal var cur_fragment = Fragment()   //현재 Viewpager가 가리키는 Fragment를 받을 변수 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById(R.id.viewpager) as ViewPager        //Viewpager 선언 및 초기화
        viewPager.adapter = adapter(getSupportFragmentManager()) //선언한 viewpager에 adapter를 연결
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
