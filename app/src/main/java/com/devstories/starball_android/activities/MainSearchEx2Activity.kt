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
import kotlinx.android.synthetic.main.activity_main2_search.*



class MainSearchEx2Activity : Fragment() {

    private var progressDialog: ProgressDialog? = null
    lateinit var myContext: Context



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)

        return inflater.inflate(R.layout.activity_main2_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        safeIV.setOnClickListener {
            val intent = Intent(context, SaveJoinActivity::class.java)
            startActivity(intent)
        }


        charmIV.setOnClickListener {
            val intent = Intent(context, DlgStarballLackActivity::class.java)
            startActivity(intent)
        }
        infoIV.setOnClickListener {
            val intent = Intent(context, DlgCharInfoActivity::class.java)
            startActivity(intent)
        }

        }




}
