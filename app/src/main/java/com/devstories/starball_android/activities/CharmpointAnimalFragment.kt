package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devstories.starball_android.R
import com.devstories.starball_android.base.Utils
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.fragment_charmpoint_animal.*


class CharmpointAnimalFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    var animal = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_charmpoint_animal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        animal1TV.setOnClickListener {
            animal = Utils.getString(animal1TV)
            animal1TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "ANIMAL_CHANGE"
            myContext.sendBroadcast(intent)
        }
        animal2TV.setOnClickListener {
            animal = Utils.getString(animal2TV)
            animal2TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "ANIMAL_CHANGE"
            myContext.sendBroadcast(intent)
        }
        animal3TV.setOnClickListener {
            animal = Utils.getString(animal3TV)
            animal3TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "ANIMAL_CHANGE"
            myContext.sendBroadcast(intent)
        }
        animal4TV.setOnClickListener {
            animal = Utils.getString(animal4TV)
            animal4TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "ANIMAL_CHANGE"
            myContext.sendBroadcast(intent)
        }
        animal5TV.setOnClickListener {
            animal = Utils.getString(animal5TV)
            animal5TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "ANIMAL_CHANGE"
            myContext.sendBroadcast(intent)
        }
        animal6TV.setOnClickListener {
            animal = Utils.getString(animal6TV)
            animal6TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "ANIMAL_CHANGE"
            myContext.sendBroadcast(intent)
        }
        animal7TV.setOnClickListener {
            animal = Utils.getString(animal7TV)
            animal7TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "ANIMAL_CHANGE"
            myContext.sendBroadcast(intent)
        }


        skipTV.setOnClickListener {
            var intent = Intent()
            intent.action = "ANIMAL_CHANGE"
            myContext.sendBroadcast(intent)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}
