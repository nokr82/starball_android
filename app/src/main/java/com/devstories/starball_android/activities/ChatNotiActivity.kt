package com.devstories.starball_android.activities

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_chat_noti.*
import kotlin.random.Random

class ChatNotiActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.devstories.starball_android.R.layout.activity_chat_noti)

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        this.context = this
        progressDialog = ProgressDialog(context)


        val screenWidth = Utils.getScreenWidth(context)
        val screenHeight = Utils.getScreenHeight(context)
        val containerHeight = screenHeight / 16

        println("screenHeight : $screenHeight, containerHeight : $containerHeight")

        val widthPadding = 150
        val topPadding = 0

        val unitWidth = screenWidth / Random.nextInt(13, 18 )

        var x = -100

        val animators = ArrayList<Animator>()

        val totalCnt = Random.nextInt(8, 18)
        // for(idx in 0..totalCnt) {
        while(true) {
            val item = View.inflate(context, com.devstories.starball_android.R.layout.item_matched, null)
            val starIV = item.findViewById<ImageView>(R.id.starIV)

            val img = Random.nextInt(1, 500) % 3
            when (img) {
                0 -> starIV.setImageResource(R.mipmap.star_01)
                1 -> starIV.setImageResource(R.mipmap.star_02)
                2 -> starIV.setImageResource(R.mipmap.star_03)
            }

            val width = Random.nextInt(50, 250)
            x += Random.nextInt(unitWidth / 2, unitWidth)
            // params.leftMargin = Random.nextInt(0, screenWidth - widthPadding)
            // params.topMargin = 0
            // x += Random.nextInt(unitWidth / 2, unitWidth)
            // x += (width / 1.6).toInt()

            val params = RelativeLayout.LayoutParams(width, width)
            // params.leftMargin = Random.nextInt(0, screenWidth - widthPadding)
            params.leftMargin = x
            params.topMargin = Random.nextInt(topPadding, containerHeight) - width
            // params.topMargin = 0
            rootRL.addView(item, params);

            val animation = ObjectAnimator.ofFloat(item, "translationY", (screenHeight + width).toFloat())
            animation.duration = Random.nextInt(600, 1600).toLong()

            animators.add(animation)

            if(x > screenWidth) {
                break
            }
        }

        val mAnimationSet = AnimatorSet()
        mAnimationSet.playTogether(animators)
        mAnimationSet.start()
        mAnimationSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {

            }

            override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                finish()
            }
        })

    }



}
