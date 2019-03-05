package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.SwipeStackItemAdapter
import com.devstories.starball_android.base.NoScrollLinearLayoutManager
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_join_step12_preview.*
import org.json.JSONArray
import org.json.JSONObject



class JoinStep12PreviewActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var activity:Activity

    private var pages = JSONArray()

    private var overallXScroll = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_step12_preview)
        this.context = this
        progressDialog = ProgressDialog(context)

        this.activity = this

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

        // 회원정보
        val email = PrefUtils.getStringPreference(context, "join_email")
        val name = PrefUtils.getStringPreference(context, "join_name")
        val gender = PrefUtils.getStringPreference(context, "join_gender")
        val height = PrefUtils.getStringPreference(context, "join_height")
        val birth = PrefUtils.getStringPreference(context, "join_birth")
        val language = PrefUtils.getStringPreference(context, "join_language")
        val job = PrefUtils.getStringPreference(context, "join_job")
        val school = PrefUtils.getStringPreference(context, "join_school")
        val intro = PrefUtils.getStringPreference(context, "join_intro")


        val memberInfo = JSONObject()
        memberInfo.put("email", email)
        memberInfo.put("name", name)
        memberInfo.put("gender", gender)
        memberInfo.put("height", height)
        memberInfo.put("birth", birth)
        memberInfo.put("language", language)
        memberInfo.put("job", job)
        memberInfo.put("school", school)
        memberInfo.put("intro", intro)

        val recyclerView = findViewById<android.support.v7.widget.RecyclerView>(R.id.my_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            val noScrollLinearLayoutManager = NoScrollLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = noScrollLinearLayoutManager

            // specify an viewAdapter (ee also next example)
            adapter = SwipeStackItemAdapter(context, activity, memberInfo, pages, true)

            PagerSnapHelper().attachToRecyclerView(this)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    overallXScroll += dx;


                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    println("newState : $newState")

                    if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val screenWidth = Utils.getScreenWidth(context)

                        println("$overallXScroll / $screenWidth = ${overallXScroll / screenWidth}")

                        if(overallXScroll % screenWidth == 0) {
                            val position = (overallXScroll / screenWidth)
                            if(position >= pages.length()) {
                                return
                            }

                            val item = pages.get(position) as JSONObject

                            println(item)


                            val id = Utils.getInt(item!!, "id")
                            val path = Utils.getString(item!!, "path")
                            val mediaType = Utils.getInt(item!!, "mediaType")

                            if(mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {

                                layoutManager!!.findViewByPosition(position)

                                var dataSource = path

                                println("dd $dataSource")

                                val holder = recyclerView.findViewHolderForAdapterPosition(position)
                                if (holder is SwipeStackItemAdapter.MainSearchType1) {
                                    holder.videoVV.player.playWhenReady = true
                                    holder.videoVV.player.seekTo(0)
                                } else if (holder is SwipeStackItemAdapter.MainSearchType2) {
                                    holder.videoVV.player.playWhenReady = true
                                    holder.videoVV.player.seekTo(0)
                                } else if (holder is SwipeStackItemAdapter.MainSearchType3) {
                                    holder.videoVV.player.playWhenReady = true
                                    holder.videoVV.player.seekTo(0)
                                }
                            }
                        }
                    }
                }
            })

        }

        backIV.setOnClickListener {
            finish()
        }

        nextIV.setOnClickListener {
            val intent = Intent(context, JoinResultActivity::class.java)
            startActivity(intent)
        }

    }
}
