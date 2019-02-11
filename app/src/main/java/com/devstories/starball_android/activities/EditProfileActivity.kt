package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.ProfileAdapter
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var ProfileAdapter: ProfileAdapter

    var step = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        this.context = this
        progressDialog = ProgressDialog(context)


        ProfileAdapter = ProfileAdapter(context,R.layout.item_profile_img, 20)
        profileGV.adapter = ProfileAdapter





        heightLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            startActivity(intent)
        }
        regionLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 2
            intent.putExtra("step",step)
            startActivity(intent)

        }
        policyLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 3
            intent.putExtra("step",step)
            startActivity(intent)
        }
        babyLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 4
            intent.putExtra("step",step)
            startActivity(intent)
        }
        animalLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 5
            intent.putExtra("step",step)
            startActivity(intent)
        }
        smokeLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 6
            intent.putExtra("step",step)
            startActivity(intent)
        }
        drinkLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 7
            intent.putExtra("step",step)
            startActivity(intent)
        }
        healthLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 8
            intent.putExtra("step",step)
            startActivity(intent)
        }
        sportLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 9
            intent.putExtra("step",step)
            startActivity(intent)
        }
        workLL.setOnClickListener {
            val intent = Intent(context, CharmpointSettingAcitivity::class.java)
            step = 10
            intent.putExtra("step",step)
            startActivity(intent)
        }




        backIV.setOnClickListener {
            finish()
        }

        charmRL.setOnClickListener {
            val intent = Intent(context, CharmPointActivity::class.java)
            startActivity(intent)
        }


        phoneLL.setOnClickListener {
            val intent = Intent(context, PhoneCertiActivity::class.java)
            startActivity(intent)
        }
        saveLL.setOnClickListener {
            val intent = Intent(context, SaveJoinActivity::class.java)
            startActivity(intent)
        }
        emailLL.setOnClickListener {
            val intent = Intent(context, EmailConnectActivity::class.java)
            startActivity(intent)
        }





    }



}
