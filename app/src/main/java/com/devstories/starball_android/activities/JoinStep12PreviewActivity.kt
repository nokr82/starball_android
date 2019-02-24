package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.SwipeStackItemAdapter
import com.devstories.starball_android.base.NoScrollLinearLayoutManager
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import kotlinx.android.synthetic.main.activity_join12_preview.*
import org.json.JSONArray
import org.json.JSONObject

class JoinStep12PreviewActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    private var pages = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join12_preview)
        this.context = this
        progressDialog = ProgressDialog(context)

        val joinPics = PrefUtils.getStringPreference(context, "join_pics", "")
        if(joinPics.isNotEmpty()) {
            val splited = joinPics.split("`devstories`")
            for (sp in  splited) {
                try {
                    val item = JSONObject(sp)
                    pages.put(item)
                } catch (e:Exception) {

                }
            }
        }

        val recyclerView = findViewById<android.support.v7.widget.RecyclerView>(R.id.my_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            val noScrollLinearLayoutManager = NoScrollLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = noScrollLinearLayoutManager

            // specify an viewAdapter (ee also next example)
            adapter = SwipeStackItemAdapter(context, pages)

            PagerSnapHelper().attachToRecyclerView(this)

        }










        nextIV.setOnClickListener {
            val intent = Intent(context, JoinResultActivity::class.java)
            startActivity(intent)
        }




    }



}
