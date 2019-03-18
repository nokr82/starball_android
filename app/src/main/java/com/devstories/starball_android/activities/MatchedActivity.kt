package com.devstories.starball_android.activities

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_matched.*

class MatchedActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.devstories.starball_android.R.layout.activity_matched)

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        this.context = this
        progressDialog = ProgressDialog(context)


        val duration = 40L

        // val th01FadeIn = ObjectAnimator.ofFloat(th01IV, "alpha", 0f, 1f)
        // th01FadeIn.duration = duration

        val scaleDownX = ObjectAnimator.ofFloat(th01IV, "scaleX", 1.0f)
        val scaleDownY = ObjectAnimator.ofFloat(th01IV, "scaleY", 1.0f)

        val animators1 = ArrayList<Animator>()
        animators1.add(scaleDownX)
        animators1.add(scaleDownY)

        val firstSet = AnimatorSet()
        firstSet.duration = duration * 10
        firstSet.playTogether(animators1)

        // wings
        val wingScaleDownX = ObjectAnimator.ofFloat(wingStarIV, "scaleX", 1.0f)
        val wingScaleDownY = ObjectAnimator.ofFloat(wingStarIV, "scaleY", 1.0f)
        val wingRotateAnimator = ObjectAnimator.ofFloat(
            wingStarIV,
            "rotation", 200f, 0f
        )

        val wingAnimators = ArrayList<Animator>()
        wingAnimators.add(scaleDownX)
        wingAnimators.add(scaleDownY)
        wingAnimators.add(wingScaleDownX)
        wingAnimators.add(wingScaleDownY)
        wingAnimators.add(wingRotateAnimator)

        val wingSet = AnimatorSet()
        wingSet.duration = duration * 16
        wingSet.playTogether(wingAnimators)

        /*
        val th02FadeIn = ObjectAnimator.ofFloat(th02IV, "alpha", 0f, 1f)
        th02FadeIn.duration = duration

        val th03FadeIn = ObjectAnimator.ofFloat(th03IV, "alpha", 0f, 1f)
        th03FadeIn.duration = duration

        val th04FadeIn = ObjectAnimator.ofFloat(th04IV, "alpha", 0f, 1f)
        th04FadeIn.duration = duration

        val th05FadeIn = ObjectAnimator.ofFloat(th05IV, "alpha", 0f, 1f)
        th05FadeIn.duration = duration

        val th06FadeIn = ObjectAnimator.ofFloat(th06IV, "alpha", 0f, 1f)
        th06FadeIn.duration = duration

        val th07FadeIn = ObjectAnimator.ofFloat(th07IV, "alpha", 0f, 1f)
        th07FadeIn.duration = duration

        val th08FadeIn = ObjectAnimator.ofFloat(th08IV, "alpha", 0f, 1f)
        th08FadeIn.duration = duration

        val th09FadeIn = ObjectAnimator.ofFloat(th09IV, "alpha", 0f, 1f)
        th09FadeIn.duration = duration

        val th10FadeIn = ObjectAnimator.ofFloat(th10IV, "alpha", 0f, 1f)
        th10FadeIn.duration = duration

        val th11FadeIn = ObjectAnimator.ofFloat(th11IV, "alpha", 0f, 1f)
        th11FadeIn.duration = duration

        val th12FadeIn = ObjectAnimator.ofFloat(th12IV, "alpha", 0f, 1f)
        th12FadeIn.duration = duration

        val th13FadeIn = ObjectAnimator.ofFloat(th13IV, "alpha", 0f, 1f)
        th13FadeIn.duration = duration
        */

        val th14FadeIn = ObjectAnimator.ofFloat(th14IV, "alpha", 0f, 1f)
        th14FadeIn.duration = duration

        val th15FadeIn = ObjectAnimator.ofFloat(th15IV, "alpha", 0f, 1f)
        th15FadeIn.duration = duration

        val th16FadeIn = ObjectAnimator.ofFloat(th16IV, "alpha", 0f, 1f)
        th16FadeIn.duration = duration

        // val th17FadeIn = ObjectAnimator.ofFloat(th17IV, "alpha", 0f, 1f)
        // th17FadeIn.duration = duration


        th17IV.x = -(screenWidth / 1.8).toFloat()
        val th17FadeIn = ObjectAnimator.ofFloat(th17IV, "translationX", 0.toFloat())
        // th17FadeIn.duration = Random.nextInt(600, 1600).toLong()


        // val th18FadeIn = ObjectAnimator.ofFloat(th18IV, "alpha", 0f, 1f)
        // th17FadeIn.duration = duration * 20

        th18IV.x = (screenWidth / 1.8).toFloat()
        val th18FadeIn = ObjectAnimator.ofFloat(th18IV, "translationX", 0.toFloat())

        val animators1718 = ArrayList<Animator>()
        animators1718.add(th17FadeIn)
        animators1718.add(th18FadeIn)

        val firstSet1718 = AnimatorSet()
        firstSet1718.duration = duration * 25
        firstSet1718.playTogether(animators1718)

        val mAnimationSet = AnimatorSet()

        val animators = ArrayList<Animator>()
        // animators.add(firstSet)
        animators.add(wingSet)

        /*
        animators.add(th02FadeIn)
        animators.add(th03FadeIn)
        animators.add(th04FadeIn)
        animators.add(th05FadeIn)
        animators.add(th06FadeIn)
        animators.add(th07FadeIn)
        animators.add(th08FadeIn)
        animators.add(th09FadeIn)
        animators.add(th10FadeIn)
        animators.add(th11FadeIn)
        animators.add(th12FadeIn)
        animators.add(th13FadeIn)
        */

        animators.add(th14FadeIn)
        animators.add(th15FadeIn)
        animators.add(th16FadeIn)
        animators.add(firstSet1718)

        mAnimationSet.playSequentially(animators)
        mAnimationSet.start()
        mAnimationSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                Utils.delay(context, 500) {
                    finish()
                    overridePendingTransition(0, 0)
                }
            }

            override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                Utils.delay(context, 500) {
                    finish()
                    overridePendingTransition(0, 0)
                }
            }
        })


    }
}
