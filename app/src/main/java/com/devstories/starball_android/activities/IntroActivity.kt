package com.devstories.starball_android.activities

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_intro.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class IntroActivity : RootActivity() {

    protected var _splashTime = 2000 // time to display the splash screen in ms
    private val _active = true
    private var splashThread: Thread? = null

    private var progressDialog: ProgressDialog? = null

    private var context: Context? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.devstories.starball_android.R.layout.activity_intro)

        this.context = this
        progressDialog = ProgressDialog(context)

        // clear all notification
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancelAll()

        startAnimation()

        getHash()

        val buldle = intent.extras
        if (buldle != null) {
            try {

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        /*
        splashThread = object : Thread() {
            override fun run() {
                try {
                    var waited = 0
                    while (waited < _splashTime && _active) {
                        Thread.sleep(100)
                        waited += 100
                    }
                } catch (e: InterruptedException) {
//                     do nothing
                } finally {
                     // stopIntro()
                }
            }
        }
        (splashThread as Thread).start()
        */
    }

    private fun startAnimation() {

        val duration = 500L

        val sFadeIn = ObjectAnimator.ofFloat(sIV, "alpha", 0f, 1f)
        sFadeIn.duration = duration

        val tFadeIn = ObjectAnimator.ofFloat(tIV, "alpha", 0f, 1f)
        tFadeIn.duration = duration

        val aFadeIn = ObjectAnimator.ofFloat(aIV, "alpha", 0f, 1f)
        aFadeIn.duration = duration

        val rFadeIn = ObjectAnimator.ofFloat(rIV, "alpha", 0f, 1f)
        rFadeIn.duration = duration

        val bFadeIn = ObjectAnimator.ofFloat(bIV, "alpha", 0f, 1f)
        bFadeIn.duration = duration

        val a2FadeIn = ObjectAnimator.ofFloat(a2IV, "alpha", 0f, 1f)
        a2FadeIn.duration = duration

        val lFadeIn = ObjectAnimator.ofFloat(lIV, "alpha", 0f, 1f)
        lFadeIn.duration = duration

        val l2FadeIn = ObjectAnimator.ofFloat(l2IV, "alpha", 0f, 1f)
        l2FadeIn.duration = duration

        val mAnimationSet = AnimatorSet()

        val animators = ArrayList<Animator>()
        animators.add(sFadeIn)
        animators.add(tFadeIn)
        animators.add(aFadeIn)
        animators.add(rFadeIn)
        animators.add(bFadeIn)
        animators.add(a2FadeIn)
        animators.add(lFadeIn)
        animators.add(l2FadeIn)

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
                stopIntro()
            }

            override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                stopIntro()
            }
        })


        val rotate = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate.duration = duration * 3
        rotate.interpolator = LinearInterpolator()
        rotate.repeatCount = Animation.INFINITE
        rotate.repeatMode = Animation.INFINITE
        logoIV.startAnimation(rotate)

    }

    private fun stopIntro() {

        val memberId = PrefUtils.getIntPreference(context, "member_id")
        if(memberId > 0) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {
            val intent = Intent(context, JoinActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

    private fun getHash() {


        try {
            val info = packageManager.getPackageInfo("com.devstories.starball_android", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }

        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }


    }

}
