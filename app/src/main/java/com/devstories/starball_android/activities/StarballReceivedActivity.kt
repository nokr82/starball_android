package com.devstories.starball_android.activities

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_matched.*

class StarballReceivedActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setContentView(com.devstories.starball_android.R.layout.activity_starball_received)

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        this.context = this
        progressDialog = ProgressDialog(context)


        val duration = 50L

        // val th01FadeIn = ObjectAnimator.ofFloat(th01IV, "alpha", 0f, 1f)
        // th01FadeIn.duration = duration

        val th01FadeOut = ObjectAnimator.ofFloat(th01IV, "alpha", 1f, 0f)
        val scaleDownX = ObjectAnimator.ofFloat(th01IV, "scaleX", 1.0f)
        val scaleDownY = ObjectAnimator.ofFloat(th01IV, "scaleY", 1.0f)

        val animators1 = ArrayList<Animator>()
        animators1.add(scaleDownX)
        animators1.add(scaleDownY)

        val firstSet = AnimatorSet()
        firstSet.duration = duration * 10
        firstSet.playTogether(animators1)

        // wings
        val wingScaleDownX = ObjectAnimator.ofFloat(wingStarIV, "scaleX", 3.0f)
        val wingScaleDownY = ObjectAnimator.ofFloat(wingStarIV, "scaleY", 3.0f)
        val wingRotateAnimator = ObjectAnimator.ofFloat(
            wingStarIV,
            "rotation", 360f, 0f
        )
        val wingFadeOutAnimator = ObjectAnimator.ofFloat(wingStarIV, "alpha", 1.0f, 0f)

        val th14FadeOut = ObjectAnimator.ofFloat(th14IV, "alpha", 1f, 0f)
        val th14FadeIn = ObjectAnimator.ofFloat(th14IV, "alpha", 0f, 1f)
        th14FadeIn.duration = duration * 5

        val th15FadeOut = ObjectAnimator.ofFloat(th15IV, "alpha", 1f, 0f)
        val th15FadeIn = ObjectAnimator.ofFloat(th15IV, "alpha", 0f, 1f)
        th15FadeIn.duration = duration * 5

        val th16FadeOut = ObjectAnimator.ofFloat(th16IV, "alpha", 1f, 0f)
        val th16FadeIn = ObjectAnimator.ofFloat(th16IV, "alpha", 0f, 1f)
        th16FadeIn.duration = duration * 5

        val wingAnimators = ArrayList<Animator>()
        wingAnimators.add(scaleDownX)
        wingAnimators.add(scaleDownY)

        val wingSet = AnimatorSet()
        wingSet.duration = duration * 10
        wingSet.playTogether(wingAnimators)

        val wingScaleAnimators = ArrayList<Animator>()
        wingScaleAnimators.add(wingScaleDownX)
        wingScaleAnimators.add(wingScaleDownY)
        wingScaleAnimators.add(wingRotateAnimator)
        wingScaleAnimators.add(wingFadeOutAnimator)

        val wingScaleSet = AnimatorSet()
        wingScaleSet.duration = duration * 40
        wingScaleSet.playTogether(wingScaleAnimators)

        val fadeoutAnimators = ArrayList<Animator>()
        fadeoutAnimators.add(th01FadeOut)
        fadeoutAnimators.add(th14FadeOut)
        fadeoutAnimators.add(th15FadeOut)
        fadeoutAnimators.add(th16FadeOut)

        val fadeoutSet = AnimatorSet()
        fadeoutSet.duration = duration * 40
        fadeoutSet.playTogether(fadeoutAnimators)

        val mAnimationSet = AnimatorSet()

        val animators = ArrayList<Animator>()
        animators.add(wingSet)

        animators.add(th14FadeIn)
        animators.add(th15FadeIn)
        animators.add(th16FadeIn)

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
                fadeoutSet.start()
            }

            override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                fadeoutSet.start()
            }
        })

        wingScaleSet.start()

        fadeoutSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                finish()
                overridePendingTransition(0, 0)
            }

            override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                finish()
                overridePendingTransition(0, 0)
            }
        })
    }
}
