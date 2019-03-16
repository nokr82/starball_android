package com.devstories.starball_android.base

import android.content.Context
import com.devstories.starball_android.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds

class AdmobUtils() {

    companion object {

        private lateinit var mInterstitialAd: InterstitialAd

        fun loadAd(context:Context, onAdClosed: () -> Unit) {

            MobileAds.initialize(context, context.getString(R.string.ADMOB_APP_ID))

            mInterstitialAd = InterstitialAd(context)
            mInterstitialAd.adUnitId = context.getString(R.string.ADMOB_UNIT_ID)
            mInterstitialAd.loadAd(AdRequest.Builder().build())
            mInterstitialAd.adListener = object: AdListener() {
                override fun onAdLoaded() {
                    mInterstitialAd.show()
                }

                override fun onAdFailedToLoad(errorCode: Int) {
                    // Code to be executed when an ad request fails.
                }

                override fun onAdOpened() {
                    // Code to be executed when the ad is displayed.
                }

                override fun onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                override fun onAdClosed() {
                    onAdClosed()
                }
            }
        }

    }

}