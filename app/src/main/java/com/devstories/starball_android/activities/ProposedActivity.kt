package com.devstories.starball_android.activities

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_proposed.*

class ProposedActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.devstories.starball_android.R.layout.activity_proposed)

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        this.context = this
        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        val duration = 100L

        val showUp = ObjectAnimator.ofPropertyValuesHolder(
            heartIV,
            PropertyValuesHolder.ofFloat("scaleX", 1.0f),
            PropertyValuesHolder.ofFloat("scaleY", 1.0f))

        showUp.duration = duration * 4

        val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
            heartIV,
            PropertyValuesHolder.ofFloat("scaleX", 0.75f),
            PropertyValuesHolder.ofFloat("scaleY", 0.75f))

        scaleDown.duration = duration * 5
        scaleDown.repeatCount = ObjectAnimator.INFINITE
        scaleDown.repeatMode = ObjectAnimator.REVERSE

        val mAnimationSet = AnimatorSet()

        val animators = ArrayList<Animator>()

        animators.add(showUp)
        animators.add(scaleDown)

        mAnimationSet.playSequentially(animators)
        mAnimationSet.start()

        Utils.delay(context, 2500) {
            finish()
            overridePendingTransition(0, 0)
        }

    }

    override fun finish() {
        super.finish()

        var intent = Intent()
        setResult(Activity.RESULT_OK, intent)
    }
}
