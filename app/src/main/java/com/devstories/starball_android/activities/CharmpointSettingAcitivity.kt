package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.devstories.starball_android.R
import kotlinx.android.synthetic.main.activity_charmpoint_setting.*

class CharmpointSettingAcitivity : FragmentActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var step = -1
    var DLGMSG = 101

    val CharmpointHeightFragment: CharmpointHeightFragment = CharmpointHeightFragment()
    val CharmpointRegionFragment: CharmpointRegionFragment = CharmpointRegionFragment()
    val CharmpointPolicyFragment: CharmpointPolicyFragment = CharmpointPolicyFragment()
    val CharmpointBabyFragment: CharmpointBabyFragment = CharmpointBabyFragment()
    val CharmpointAnimalFragment: CharmpointAnimalFragment = CharmpointAnimalFragment()
    val CharmpointSmokeFragment: CharmpointSmokeFragment = CharmpointSmokeFragment()
    val CharmpointDrinkFragment: CharmpointDrinkFragment = CharmpointDrinkFragment()
    val CharmpointHealthFragment: CharmpointHealthFragment = CharmpointHealthFragment()
    val CharmpointSportsFragment: CharmpointSportsFragment = CharmpointSportsFragment()
    val CharmpointWorkFragment: CharmpointWorkFragment = CharmpointWorkFragment()
    internal var heightReciver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                setmenu()
                heightV.setBackgroundColor(Color.parseColor("#a862b2"))
                regionV.setBackgroundColor(Color.parseColor("#a862b2"))
                supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointRegionFragment).commit()
            }
        }
    }
    internal var regionReciver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                setmenu()
                heightV.setBackgroundColor(Color.parseColor("#a862b2"))
                regionV.setBackgroundColor(Color.parseColor("#a862b2"))
                policyV.setBackgroundColor(Color.parseColor("#a862b2"))
                supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointPolicyFragment).commit()
            }
        }
    }
    internal var policyReciver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                setmenu()
                heightV.setBackgroundColor(Color.parseColor("#a862b2"))
                regionV.setBackgroundColor(Color.parseColor("#a862b2"))
                policyV.setBackgroundColor(Color.parseColor("#a862b2"))
                babyV.setBackgroundColor(Color.parseColor("#a862b2"))
                supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointBabyFragment).commit()
            }
        }
    }
    internal var babyReciver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                setmenu()
                heightV.setBackgroundColor(Color.parseColor("#a862b2"))
                regionV.setBackgroundColor(Color.parseColor("#a862b2"))
                policyV.setBackgroundColor(Color.parseColor("#a862b2"))
                babyV.setBackgroundColor(Color.parseColor("#a862b2"))
                animalV.setBackgroundColor(Color.parseColor("#a862b2"))
                supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointAnimalFragment).commit()
            }
        }
    }
    internal var animalReciver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                setmenu()
                heightV.setBackgroundColor(Color.parseColor("#a862b2"))
                regionV.setBackgroundColor(Color.parseColor("#a862b2"))
                policyV.setBackgroundColor(Color.parseColor("#a862b2"))
                babyV.setBackgroundColor(Color.parseColor("#a862b2"))
                animalV.setBackgroundColor(Color.parseColor("#a862b2"))
                smokeV.setBackgroundColor(Color.parseColor("#a862b2"))
                supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointSmokeFragment).commit()
            }
        }
    }
    internal var smokeReciver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                setmenu()
                heightV.setBackgroundColor(Color.parseColor("#a862b2"))
                regionV.setBackgroundColor(Color.parseColor("#a862b2"))
                policyV.setBackgroundColor(Color.parseColor("#a862b2"))
                babyV.setBackgroundColor(Color.parseColor("#a862b2"))
                animalV.setBackgroundColor(Color.parseColor("#a862b2"))
                smokeV.setBackgroundColor(Color.parseColor("#a862b2"))
                drinkV.setBackgroundColor(Color.parseColor("#a862b2"))
                supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointDrinkFragment).commit()
            }
        }
    }
    internal var drinkReciver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                setmenu()
                heightV.setBackgroundColor(Color.parseColor("#a862b2"))
                regionV.setBackgroundColor(Color.parseColor("#a862b2"))
                policyV.setBackgroundColor(Color.parseColor("#a862b2"))
                babyV.setBackgroundColor(Color.parseColor("#a862b2"))
                animalV.setBackgroundColor(Color.parseColor("#a862b2"))
                smokeV.setBackgroundColor(Color.parseColor("#a862b2"))
                drinkV.setBackgroundColor(Color.parseColor("#a862b2"))
                healthV.setBackgroundColor(Color.parseColor("#a862b2"))
                supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointHealthFragment).commit()
            }
        }
    }
    internal var healthReciver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                setmenu()
                heightV.setBackgroundColor(Color.parseColor("#a862b2"))
                regionV.setBackgroundColor(Color.parseColor("#a862b2"))
                policyV.setBackgroundColor(Color.parseColor("#a862b2"))
                babyV.setBackgroundColor(Color.parseColor("#a862b2"))
                animalV.setBackgroundColor(Color.parseColor("#a862b2"))
                smokeV.setBackgroundColor(Color.parseColor("#a862b2"))
                drinkV.setBackgroundColor(Color.parseColor("#a862b2"))
                healthV.setBackgroundColor(Color.parseColor("#a862b2"))
                sportV.setBackgroundColor(Color.parseColor("#a862b2"))
                supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointSportsFragment).commit()
            }
        }
    }
    internal var sportsReciver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                setmenu()
                heightV.setBackgroundColor(Color.parseColor("#a862b2"))
                regionV.setBackgroundColor(Color.parseColor("#a862b2"))
                policyV.setBackgroundColor(Color.parseColor("#a862b2"))
                babyV.setBackgroundColor(Color.parseColor("#a862b2"))
                animalV.setBackgroundColor(Color.parseColor("#a862b2"))
                smokeV.setBackgroundColor(Color.parseColor("#a862b2"))
                drinkV.setBackgroundColor(Color.parseColor("#a862b2"))
                healthV.setBackgroundColor(Color.parseColor("#a862b2"))
                sportV.setBackgroundColor(Color.parseColor("#a862b2"))
                workV.setBackgroundColor(Color.parseColor("#a862b2"))
                supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointWorkFragment).commit()
            }
        }
    }
    internal var workReciver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                intent.putExtra("result", "result")
                finish()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charmpoint_setting)

        this.context = this
        progressDialog = ProgressDialog(context)

        step = intent.getIntExtra("step", -1)

        Log.d("step", step.toString())


        var filter1 = IntentFilter("HEIGHT_CHANGE")
        registerReceiver(heightReciver, filter1)
        var filter2 = IntentFilter("REGION_CHANGE")
        registerReceiver(regionReciver, filter2)
        var filter3 = IntentFilter("POLICY_CHANGE")
        registerReceiver(policyReciver, filter3)
        var filter4 = IntentFilter("BABY_CHANGE")
        registerReceiver(babyReciver, filter4)
        var filter5 = IntentFilter("SMOKE_CHANGE")
        registerReceiver(smokeReciver, filter5)
        var filter6 = IntentFilter("DRINK_CHANGE")
        registerReceiver(drinkReciver, filter6)
        var filter7 = IntentFilter("HEALTH_CHANGE")
        registerReceiver(healthReciver, filter7)
        var filter8 = IntentFilter("SPORTS_CHANGE")
        registerReceiver(sportsReciver, filter8)
        var filter9 = IntentFilter("WORK_CHANGE")
        registerReceiver(workReciver, filter9)
        var filter10 = IntentFilter("ANIMAL_CHANGE")
        registerReceiver(animalReciver, filter10)

        supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointHeightFragment).commit()
        setstart()


        closeIV.setOnClickListener {
            val intent = Intent(context, DlgCharmpointActivity::class.java)
            startActivityForResult(intent, DLGMSG)

        }
        profileIV.setOnClickListener {
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            DLGMSG -> {
                if (resultCode == Activity.RESULT_OK) {
                    finish()
                }
            }
        }
    }

    fun setstart() {
        if (step == 2) {
            setmenu()
            heightV.setBackgroundColor(Color.parseColor("#a862b2"))
            regionV.setBackgroundColor(Color.parseColor("#a862b2"))
            supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointRegionFragment).commit()
        } else if (step == 3) {
            setmenu()
            heightV.setBackgroundColor(Color.parseColor("#a862b2"))
            regionV.setBackgroundColor(Color.parseColor("#a862b2"))
            policyV.setBackgroundColor(Color.parseColor("#a862b2"))
            supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointPolicyFragment).commit()
        } else if (step == 4) {
            setmenu()
            heightV.setBackgroundColor(Color.parseColor("#a862b2"))
            regionV.setBackgroundColor(Color.parseColor("#a862b2"))
            policyV.setBackgroundColor(Color.parseColor("#a862b2"))
            babyV.setBackgroundColor(Color.parseColor("#a862b2"))
            supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointBabyFragment).commit()
        } else if (step == 5) {
            setmenu()
            heightV.setBackgroundColor(Color.parseColor("#a862b2"))
            regionV.setBackgroundColor(Color.parseColor("#a862b2"))
            policyV.setBackgroundColor(Color.parseColor("#a862b2"))
            babyV.setBackgroundColor(Color.parseColor("#a862b2"))
            animalV.setBackgroundColor(Color.parseColor("#a862b2"))
            supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointAnimalFragment).commit()
        } else if (step == 6) {
            setmenu()
            heightV.setBackgroundColor(Color.parseColor("#a862b2"))
            regionV.setBackgroundColor(Color.parseColor("#a862b2"))
            policyV.setBackgroundColor(Color.parseColor("#a862b2"))
            babyV.setBackgroundColor(Color.parseColor("#a862b2"))
            animalV.setBackgroundColor(Color.parseColor("#a862b2"))
            smokeV.setBackgroundColor(Color.parseColor("#a862b2"))
            supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointSmokeFragment).commit()
        } else if (step == 7) {
            setmenu()
            heightV.setBackgroundColor(Color.parseColor("#a862b2"))
            regionV.setBackgroundColor(Color.parseColor("#a862b2"))
            policyV.setBackgroundColor(Color.parseColor("#a862b2"))
            babyV.setBackgroundColor(Color.parseColor("#a862b2"))
            animalV.setBackgroundColor(Color.parseColor("#a862b2"))
            smokeV.setBackgroundColor(Color.parseColor("#a862b2"))
            drinkV.setBackgroundColor(Color.parseColor("#a862b2"))
            supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointDrinkFragment).commit()
        } else if (step == 8) {
            setmenu()
            heightV.setBackgroundColor(Color.parseColor("#a862b2"))
            regionV.setBackgroundColor(Color.parseColor("#a862b2"))
            policyV.setBackgroundColor(Color.parseColor("#a862b2"))
            babyV.setBackgroundColor(Color.parseColor("#a862b2"))
            animalV.setBackgroundColor(Color.parseColor("#a862b2"))
            smokeV.setBackgroundColor(Color.parseColor("#a862b2"))
            drinkV.setBackgroundColor(Color.parseColor("#a862b2"))
            healthV.setBackgroundColor(Color.parseColor("#a862b2"))
            supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointHealthFragment).commit()
        } else if (step == 9) {
            setmenu()
            heightV.setBackgroundColor(Color.parseColor("#a862b2"))
            regionV.setBackgroundColor(Color.parseColor("#a862b2"))
            policyV.setBackgroundColor(Color.parseColor("#a862b2"))
            babyV.setBackgroundColor(Color.parseColor("#a862b2"))
            animalV.setBackgroundColor(Color.parseColor("#a862b2"))
            smokeV.setBackgroundColor(Color.parseColor("#a862b2"))
            drinkV.setBackgroundColor(Color.parseColor("#a862b2"))
            healthV.setBackgroundColor(Color.parseColor("#a862b2"))
            sportV.setBackgroundColor(Color.parseColor("#a862b2"))
            supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointSportsFragment).commit()
        } else if (step == 10) {
            setmenu()
            heightV.setBackgroundColor(Color.parseColor("#a862b2"))
            regionV.setBackgroundColor(Color.parseColor("#a862b2"))
            policyV.setBackgroundColor(Color.parseColor("#a862b2"))
            babyV.setBackgroundColor(Color.parseColor("#a862b2"))
            animalV.setBackgroundColor(Color.parseColor("#a862b2"))
            smokeV.setBackgroundColor(Color.parseColor("#a862b2"))
            drinkV.setBackgroundColor(Color.parseColor("#a862b2"))
            healthV.setBackgroundColor(Color.parseColor("#a862b2"))
            sportV.setBackgroundColor(Color.parseColor("#a862b2"))
            workV.setBackgroundColor(Color.parseColor("#a862b2"))
            supportFragmentManager.beginTransaction().replace(R.id.charmFL, CharmpointWorkFragment).commit()
        }
    }


    fun setmenu() {
        heightV.setBackgroundColor(Color.parseColor("#c9c9c9"))
        regionV.setBackgroundColor(Color.parseColor("#c9c9c9"))
        policyV.setBackgroundColor(Color.parseColor("#c9c9c9"))
        babyV.setBackgroundColor(Color.parseColor("#c9c9c9"))
        animalV.setBackgroundColor(Color.parseColor("#c9c9c9"))
        smokeV.setBackgroundColor(Color.parseColor("#c9c9c9"))
        drinkV.setBackgroundColor(Color.parseColor("#c9c9c9"))
        healthV.setBackgroundColor(Color.parseColor("#c9c9c9"))
        sportV.setBackgroundColor(Color.parseColor("#c9c9c9"))
        workV.setBackgroundColor(Color.parseColor("#c9c9c9"))
    }


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
        if (heightReciver != null) {
            unregisterReceiver(heightReciver)
        }
        if (regionReciver != null) {
            unregisterReceiver(regionReciver)
        }
        if (policyReciver != null) {
            unregisterReceiver(policyReciver)
        }
        if (babyReciver != null) {
            unregisterReceiver(babyReciver)
        }
        if (smokeReciver != null) {
            unregisterReceiver(smokeReciver)
        }
        if (drinkReciver != null) {
            unregisterReceiver(drinkReciver)
        }
        if (healthReciver != null) {
            unregisterReceiver(healthReciver)
        }
        if (sportsReciver != null) {
            unregisterReceiver(sportsReciver)
        }

        if (workReciver != null) {
            unregisterReceiver(workReciver)
        }
        if (animalReciver != null) {
            unregisterReceiver(animalReciver)
        }


    }


}


