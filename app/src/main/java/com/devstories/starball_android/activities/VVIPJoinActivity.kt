package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_vvip_join.*

class VVIPJoinActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    private val heightitems = arrayOf(
        "vvip1",
        "vvip2",
        "vvip3",
        "vvip4",
        "vvip5",
        "vvip6",
        "vvip7"
    )
    var adPosition = 0

    private var adTime = 0
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vvip_join)
        this.context = this
        progressDialog = ProgressDialog(context)

        for (i in 0..(heightitems.size - 1)) {
            val userView = View.inflate(context, R.layout.item_vvip, null)
            var adverTV: TextView = userView.findViewById(R.id.adverTV)
            var adverIV: ImageView = userView.findViewById(R.id.adverIV)
            var adverLL: LinearLayout = userView.findViewById(R.id.adverLL)
            var adversubTV: TextView = userView.findViewById(R.id.adversubTV)
            if (heightitems[i]=="vvip1"){
                adverLL.setBackgroundResource(R.drawable.background_gradient3)
                adverTV.text = "Premium 이상\n" +
                        "회원에게만 노출"
                adversubTV.text ="아무나 소개되지 않습니다.\n최상급 회원분들에게만 소개됩니다."
                adverIV.setImageResource(R.mipmap.setting_vvip_icon_01)
            }else if (heightitems[i]=="vvip2"){
                adverLL.setBackgroundResource(R.drawable.background_gradient2)
                adverTV.text = "매일 최상위 \n" +
                        "10명의 인기회원 추천"
                adversubTV.text ="VVIP회원님에게만\n" +
                        "매일 최고 인기 10명의 인기회원을\n" +
                        "추천 해 드립니다."
                adverIV.setImageResource(R.mipmap.setting_vvip_icon_02)
            }else if (heightitems[i]=="vvip3"){
                adverLL.setBackgroundResource(R.drawable.background_gradient9)
                adverTV.text = "신고된 회원 원천차단"
                adversubTV.text ="VVIP회원님의 안전을 위하여\n" +
                        "한번이라도 신고접수된 \n" +
                        "회원은 자동으로 필터링 됩니다."
                adverIV.setImageResource(R.mipmap.setting_vvip_icon_03)
            }else if (heightitems[i]=="vvip4"){
                adverLL.setBackgroundResource(R.drawable.background_gradient4)
                adverTV.text = "최상위 프로필 부스터\n" +
                        "상시 적용"
                adversubTV.text ="VVIP회원분은 모든 검색순위에\n" +
                        "최상위로 등록되어 적용됩니다."
                adverIV.setImageResource(R.mipmap.setting_vvip_icon_04)
            }else if (heightitems[i]=="vvip5"){
                adverLL.setBackgroundResource(R.drawable.background_gradient3)
                adverTV.text = "VVIP 전용 검색에 노출"
                adversubTV.text ="VVIP전용 검색에 노출되어\n" +
                        "인기도를 높힙니다."
                adverIV.setImageResource(R.mipmap.setting_vvip_icon_05)
            }else if (heightitems[i]=="vvip6"){
                adverLL.setBackgroundResource(R.drawable.background_gradient12)
                adverTV.text = "매일 X4 Starball 지급"
                adversubTV.text ="일반회원대비 4배많은\n" +
                        "Starball이 매일 지급됩니다."
                adverIV.setImageResource(R.mipmap.setting_vvip_icon_06)
            }else if (heightitems[i]=="vvip7"){
                adverLL.setBackgroundResource(R.drawable.background_gradient6)
                adverTV.text = "신입회원에게 0순위로 소개"
                adversubTV.text ="신입회원에게는 최우선으로\n" +
                        "VVIP회원님이 먼저 소개됩니다. "
                adverIV.setImageResource(R.mipmap.setting_vvip_icon_07)
            }




            userLL.addView(userView)

        }

        backIV.setOnClickListener {
            finish()
        }


        noTV.setOnClickListener {
            finish()
        }




    }



}
