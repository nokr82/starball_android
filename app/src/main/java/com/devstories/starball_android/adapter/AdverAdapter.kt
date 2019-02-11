package com.devstories.starball_android.adapter

import android.app.Activity
import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.devstories.starball_android.R
import com.devstories.starball_android.base.Utils
import com.nostra13.universalimageloader.core.ImageLoader

class AdverAdapter(activity:Activity, imagePaths: ArrayList<String>) : PagerAdapter() {

    private val _activity: Activity = activity
    private val _imagePaths: ArrayList<String> = imagePaths
    private lateinit var inflater: LayoutInflater

    override fun getCount(): Int {
        return this._imagePaths.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val adverIV:ImageView
        val adverTV:TextView
        val adversubTV:TextView
        val addelLL:LinearLayout
        inflater = _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val viewLayout = inflater.inflate(R.layout.fragment_starballmembership1, container, false)
        adverIV = viewLayout.findViewById(R.id.adverIV)
        adverTV = viewLayout.findViewById(R.id.adverTV)
        adversubTV = viewLayout.findViewById(R.id.adversubTV)
        addelLL = viewLayout.findViewById(R.id.addelLL)



        if (position ==0){
            adverIV.setImageResource(R.mipmap.setting_ad_icon_01)
            addelLL.setBackgroundResource(R.drawable.background_gradient2)
        }else if (position ==1){
            adverIV.setImageResource(R.mipmap.setting_ad_icon_02)
            adverTV.text = "Starball을 cash로 전환 가능"
            adversubTV.text = "선물받는 Starball을 Cash화 할 수 있습니다.   \n" +
                    "현실에서도 소원을 이루어 보세요  (무료지급 Starball제외)"
            addelLL.setBackgroundResource(R.drawable.background_gradient3)
        }else if (position ==2){
            adverIV.setImageResource(R.mipmap.setting_ad_icon_03)
            adverTV.text = "X2 Starball"
            adversubTV.text = "매일 지급되는 Starball 개수가 두배로 늘어납니다.\n" +
                    "마음껏 좋아요를 해보세요\n"
            addelLL.setBackgroundResource(R.drawable.background_gradient4)
        }else if (position ==3){
            adverIV.setImageResource(R.mipmap.setting_ad_icon_04)
            adverTV.text = "X2 Starball"
            adversubTV.text = "추천 소개되는 등급이 up됩니다.\n보다 매력있는 분들을 보실 수 있습니다."
            addelLL.setBackgroundResource(R.drawable.background_gradient5)
        }else if (position ==4){
            adverIV.setImageResource(R.mipmap.setting_ad_icon_05)
            adverTV.text = "채팅번역 업그레이드"
            adversubTV.text = "채팅 자동번역 업그레이드!\n" +
                    "외국인 친구와 대화를 더 이상 두려워하지 마세요\n"
            addelLL.setBackgroundResource(R.drawable.background_gradient6)
        }else if (position ==5){
            adverIV.setImageResource(R.mipmap.setting_ad_icon_06)
            adverTV.text = "호감보내 온 사람 에게 ‘좋아요＇"

            adversubTV.text = "호감보내 온 사람에게 ‘좋아요＇를 보낼 수 있게 됩니다."
            addelLL.setBackgroundResource(R.drawable.background_gradient7)
        }else if (position ==6){
            adverIV.setImageResource(R.mipmap.setting_ad_icon_07)
            adverTV.text = "유령모드 설정"
            adversubTV.text = "휴면상태로 전환 할 수 있는 기능설정이 가능합니다."
            addelLL.setBackgroundResource(R.drawable.background_gradient8)
        }else if (position ==7){
            adverIV.setImageResource(R.mipmap.setting_ad_icon_08)
            adverTV.text = "나를 보는 회원 제한"
            adversubTV.text = "회원님이 좋아요 한 사람들에게만\n" +
                    "보이기 기능설정이 가능합니다."
            addelLL.setBackgroundResource(R.drawable.background_gradient9)
        }else if (position ==8){
            adverIV.setImageResource(R.mipmap.setting_ad_icon_09)
            adverTV.text = "X3 프로필 부스터"
            adversubTV.text = "일반 회원대비 노출도 3배 이상 올라갑니다.\n" +
                    "인기도가 대폭 올라 갑니다"
            addelLL.setBackgroundResource(R.drawable.background_gradient10)
        }else if (position ==9){
            adverIV.setImageResource(R.mipmap.setting_ad_icon_10)
            adverTV.text = "되돌리기"
            adversubTV.text = "무제한으로 되돌리기가 가능합니다.\n" +
                    "순간의 선택이 평생을 좌우할지도 모릅니다."
            addelLL.setBackgroundResource(R.drawable.background_gradient11)
        }else if (position ==10){
            adverIV.setImageResource(R.mipmap.setting_ad_icon_11)
            adverTV.text = "그룹 발송참여인원 제한 해제"
            adversubTV.text = "그룹발송 인원제한이 해제됩니다.\n" +
                    "편리하게 친구대화를 관리하세요"
            addelLL.setBackgroundResource(R.drawable.background_gradient12)
        }else if (position ==11){
            adverIV.setImageResource(R.mipmap.setting_ad_icon_12)
            adverTV.text = "크로스필터링"
            adversubTV.text = "나와 동일한 검색조건을 가진 회원님만이 \n" +
                    "나에게 호감을 보낼 수 있는 기능설정이 가능합니다."
            addelLL.setBackgroundResource(R.drawable.background_gradient13)
        }



        (container as ViewPager).addView(viewLayout)

        return viewLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as LinearLayout)

    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

}