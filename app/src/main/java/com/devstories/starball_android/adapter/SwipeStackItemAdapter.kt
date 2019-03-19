package com.devstories.starball_android.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.starball_android.R
import com.devstories.starball_android.activities.*
import com.devstories.starball_android.base.Config
import com.devstories.starball_android.base.DateUtils
import com.devstories.starball_android.base.Utils
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.TransferListener
import com.google.android.exoplayer2.util.Util
import com.nostra13.universalimageloader.core.ImageLoader
import org.json.JSONArray
import org.json.JSONObject

//type  = 1이면 가입화면


class SwipeStackItemAdapter(private val context:Context, private val activity:Activity, private val member:JSONObject, private val data: JSONArray, private val preview:Boolean,private val starball:Int,private val type:Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var langs = ArrayList<String>()
    var memberInfo = JSONObject()
    class MainSearchType1(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var infoLL = itemView.findViewById<View>(R.id.infoLL) as LinearLayout

        var imgIV = itemView.findViewById<View>(R.id.imgIV) as ImageView
        var videoVV = itemView.findViewById<View>(R.id.videoVV) as PlayerView
        var hereIV = itemView.findViewById<View>(R.id.hereIV) as ImageView
        var safeIV = itemView.findViewById<View>(R.id.safeIV) as ImageView
        var distanceTV = itemView.findViewById<View>(R.id.distanceTV) as TextView
        var infoIV = itemView.findViewById<View>(R.id.infoIV) as ImageView
        var nameTV = itemView.findViewById<View>(R.id.nameTV) as TextView
        var ageTV = itemView.findViewById<View>(R.id.ageTV) as TextView
        var fitRateTV = itemView.findViewById<View>(R.id.fitRateTV) as TextView
        var introTV = itemView.findViewById<View>(R.id.introTV) as TextView
        var charmIV = itemView.findViewById<View>(R.id.charmIV) as ImageView

    }

    class MainSearchType2(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var infoLL = itemView.findViewById<View>(R.id.infoLL) as LinearLayout
        var charmLL = itemView.findViewById<View>(R.id.charmLL) as LinearLayout
        var meetLL = itemView.findViewById<View>(R.id.meetLL) as LinearLayout
        var youcharmLL = itemView.findViewById<View>(R.id.youcharmLL) as LinearLayout
        var imgIV = itemView.findViewById<View>(R.id.imgIV) as ImageView
        var videoVV = itemView.findViewById<View>(R.id.videoVV) as PlayerView
        var hereIV = itemView.findViewById<View>(R.id.hereIV) as ImageView
        var safeIV = itemView.findViewById<View>(R.id.safeIV) as ImageView
        var distanceTV = itemView.findViewById<View>(R.id.distanceTV) as TextView
        var infoIV = itemView.findViewById<View>(R.id.infoIV) as ImageView
        var nameTV = itemView.findViewById<View>(R.id.nameTV) as TextView
        var ageTV = itemView.findViewById<View>(R.id.ageTV) as TextView
        var fitRateTV = itemView.findViewById<View>(R.id.fitRateTV) as TextView
        var charmIV = itemView.findViewById<View>(R.id.charmIV) as ImageView

    }

    class MainSearchType3(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var infoLL = itemView.findViewById<View>(R.id.infoLL) as LinearLayout
        var dailyLL = itemView.findViewById<View>(R.id.dailyLL) as LinearLayout
        var imgIV = itemView.findViewById<View>(R.id.imgIV) as ImageView
        var videoVV = itemView.findViewById<View>(R.id.videoVV) as PlayerView
        var hereIV = itemView.findViewById<View>(R.id.hereIV) as ImageView
        var safeIV = itemView.findViewById<View>(R.id.safeIV) as ImageView
        var distanceTV = itemView.findViewById<View>(R.id.distanceTV) as TextView
        var infoIV = itemView.findViewById<View>(R.id.infoIV) as ImageView
        var nameTV = itemView.findViewById<View>(R.id.nameTV) as TextView
        var ageTV = itemView.findViewById<View>(R.id.ageTV) as TextView
        var fitRateTV = itemView.findViewById<View>(R.id.fitRateTV) as TextView
        var charmIV = itemView.findViewById<View>(R.id.charmIV) as ImageView

    }

    class MainSearchType10(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var infoLL = itemView.findViewById<View>(R.id.infoLL) as LinearLayout

        var imgIV = itemView.findViewById<View>(R.id.imgIV) as ImageView
        var videoVV = itemView.findViewById<View>(R.id.videoVV) as PlayerView
        var hereIV = itemView.findViewById<View>(R.id.hereIV) as ImageView
        var safeIV = itemView.findViewById<View>(R.id.safeIV) as ImageView
        var distanceTV = itemView.findViewById<View>(R.id.distanceTV) as TextView
        var infoIV = itemView.findViewById<View>(R.id.infoIV) as ImageView
        var nameTV = itemView.findViewById<View>(R.id.nameTV) as TextView
        var ageTV = itemView.findViewById<View>(R.id.ageTV) as TextView
        var fitRateTV = itemView.findViewById<View>(R.id.fitRateTV) as TextView
        var charmIV = itemView.findViewById<View>(R.id.charmIV) as ImageView
        var heightTV = itemView.findViewById<View>(R.id.heightTV) as TextView
        var languageTV = itemView.findViewById<View>(R.id.languageTV) as TextView
        var jobTV = itemView.findViewById<View>(R.id.jobTV) as TextView
        var moneyTV = itemView.findViewById<View>(R.id.moneyTV) as TextView
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when(viewType) {
            0 -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_main_search1, parent, false) as View
                return MainSearchType1(itemView)
            }

            1 -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_main_search4, parent, false) as View
                return MainSearchType10(itemView)
            }

            2 -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_main_search2, parent, false) as View
                return MainSearchType2(itemView)
            }
        }

        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_main_search3, parent, false) as View
        return MainSearchType3(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = data.get(position) as JSONObject

        if (type ==1){
            memberInfo = member
        }else{
            memberInfo = member.getJSONObject("member")
        }

        val id = Utils.getInt(item, "id")
        var path = Utils.getString(item, "path")
        var mediaType = Utils.getInt(item, "mediaType")

        if(!preview) {
            path = Utils.getString(item, "image_uri")
            mediaType = Utils.getInt(item, "type")
        }

        var bitmap:Bitmap? = null
        if(mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
            if(preview) {
                bitmap = Utils.getImage(context.contentResolver, path)
            }
        } else {
            // bitmap = MediaStore.Video.Thumbnails.getThumbnail(context.contentResolver, id.toLong(), MediaStore.Video.Thumbnails.MINI_KIND, null)
            bitmap = null
        }

        when (holder.itemViewType) {
            0 -> {
                val holder = holder as MainSearchType1

                if(preview) {
                    holder.infoLL.visibility = View.VISIBLE
                } else {
                    holder.infoLL.visibility = View.VISIBLE
                }

                if(mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {

                    if(preview && bitmap != null) {
                        holder.imgIV.setImageBitmap(bitmap)
                    } else {
                        ImageLoader.getInstance().displayImage(Config.url + path, holder.imgIV, Utils.UILoptions)
                    }

                    holder.imgIV.visibility = View.VISIBLE
                    holder.videoVV.visibility = View.GONE

                } else {

                    var dataSource = path
                    if(!preview) {
                        dataSource = Config.url + path
                    }

                    println("dataSource 1 : $dataSource")

                    val (mediaSource, player) = createExoPlayer(dataSource)

                    // holder.videoVV.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM)
                    holder.videoVV.requestFocus()

                    holder.videoVV.player = player
                    player.playWhenReady = false
                    player.prepare(mediaSource)

                    holder.imgIV.visibility = View.GONE
                    holder.videoVV.visibility = View.VISIBLE
                }


                val like_member_id  = Utils.getInt(memberInfo,"id")
                val email = Utils.getString(memberInfo, "email")
                val name = Utils.getString(memberInfo, "name")
                val gender = Utils.getString(memberInfo, "gender")
                val height = Utils.getString(memberInfo, "height")
                val birth = Utils.getString(memberInfo, "birth")
                val savejoin_yn = Utils.getString(memberInfo,"savejoin_yn")

                val age = DateUtils.getYearDiffCount(birth, DateUtils.getToday("yyyyMMdd"), "yyyyMMdd")

                val language = Utils.getString(memberInfo, "language")
                val job = Utils.getString(memberInfo, "job")
                val school = Utils.getString(memberInfo, "school")
                val intro = Utils.getString(memberInfo, "intro")

                if (savejoin_yn=="N"){
                    holder.safeIV.visibility = View.GONE
                }else{
                    holder.safeIV.visibility = View.VISIBLE
                }


                holder.distanceTV.text = "17Km"
                holder.nameTV.text = name
                holder.ageTV.text = age.toString()
                holder.fitRateTV.text = "23%"
                holder.introTV.text = intro

                holder.infoIV.setOnClickListener {
                    val intent = Intent(context, ChatNotiActivity::class.java)
                    activity.startActivity(intent)
                    activity.overridePendingTransition(0, 0)
                }

                holder.charmIV.setOnClickListener {
                    if (starball>0){
                        val intent = Intent(context, DlgCrushActivity::class.java)
                        intent.putExtra("like_member_id",like_member_id)
                        context.startActivity(intent)
                    }else{
                        val intent = Intent(context, DlgStarballLackActivity::class.java)
                        context.startActivity(intent)
                    }
                }


            }
            1 -> {
                val holder = holder as MainSearchType10

                if(preview) {
                    holder.infoLL.visibility = View.VISIBLE
                } else {
                    holder.infoLL.visibility = View.VISIBLE
                }

                if(mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {

                    if(preview && bitmap != null) {
                        holder.imgIV.setImageBitmap(bitmap)
                    } else {
                        ImageLoader.getInstance().displayImage(Config.url + path, holder.imgIV, Utils.UILoptions)
                    }

                    holder.imgIV.visibility = View.VISIBLE
                    holder.videoVV.visibility = View.GONE

                } else {

                    var dataSource = path
                    if(!preview) {
                        dataSource = Config.url + path
                    }

                    println("dataSource 1 : $dataSource")

                    val (mediaSource, player) = createExoPlayer(dataSource)

                    // holder.videoVV.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM)
                    holder.videoVV.requestFocus()

                    holder.videoVV.player = player
                    player.playWhenReady = false
                    player.prepare(mediaSource)

                    holder.imgIV.visibility = View.GONE
                    holder.videoVV.visibility = View.VISIBLE
                }

                val savejoin_yn = Utils.getString(memberInfo,"savejoin_yn")
                if (savejoin_yn=="N"){
                    holder.safeIV.visibility = View.GONE
                }else{
                    holder.safeIV.visibility = View.VISIBLE
                }

                val like_member_id  = Utils.getInt(memberInfo,"id")
                val email = Utils.getString(memberInfo, "email")
                val name = Utils.getString(memberInfo, "name")
                val gender = Utils.getString(memberInfo, "gender")
                val height = Utils.getString(memberInfo, "height")
                val birth = Utils.getString(memberInfo, "birth")
                val work = Utils.getString(memberInfo, "work")

                val age = DateUtils.getYearDiffCount(birth, DateUtils.getToday("yyyyMMdd"), "yyyyMMdd")

                val job = Utils.getString(memberInfo, "job")
                val school = Utils.getString(memberInfo, "school")
                if (type !=1){
                    val languages = member.getJSONArray("languages")
                    langs.clear()
                    for (i in 0 until languages.length()){

                        val language = languages.get(i) as JSONObject

                        var lang= Utils.getString(language,"language")

                        langs.add(lang)
                    }
                    holder.languageTV.text = langs.toString().replace("[","").replace("]","")
                }


                holder.distanceTV.text = "17Km"
                holder.nameTV.text = name
                holder.ageTV.text = age.toString()
                holder.fitRateTV.text = "23%"

                holder.heightTV.text = height+"cm"

                holder.jobTV.text = "Job-"+job
                holder.moneyTV.text = "수입" + work

                holder.infoIV.setOnClickListener {
                    val intent = Intent(context, ChatNotiActivity::class.java)
                    activity.startActivity(intent)
                    activity.overridePendingTransition(0, 0)
                }

                holder.charmIV.setOnClickListener {
                    if (starball>0){
                        val intent = Intent(context, DlgCrushActivity::class.java)
                        intent.putExtra("like_member_id",like_member_id)
                        context.startActivity(intent)
                    }else{
                        val intent = Intent(context, DlgStarballLackActivity::class.java)
                        context.startActivity(intent)
                    }
                }


            }

            2 -> {
                val holder = holder as MainSearchType2

                if(preview) {
                    holder.infoLL.visibility = View.GONE
                } else {
                    holder.infoLL.visibility = View.VISIBLE
                }

                if(mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {

                    if(preview && bitmap != null) {
                        holder.imgIV.setImageBitmap(bitmap)
                    } else {
                        ImageLoader.getInstance().displayImage(Config.url + path, holder.imgIV, Utils.UILoptions)
                    }

                    holder.imgIV.visibility = View.VISIBLE
                    holder.videoVV.visibility = View.GONE

                } else {

                    var dataSource = path
                    if(!preview) {
                        dataSource = Config.url + path
                    }

                    println("dataSource 2 : $dataSource")

                    val (mediaSource, player) = createExoPlayer(dataSource)

                    // holder.videoVV.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM)
                    holder.videoVV.requestFocus()

                    holder.videoVV.player = player
                    player.playWhenReady = false
                    player.prepare(mediaSource)

                    holder.imgIV.visibility = View.GONE
                    holder.videoVV.visibility = View.VISIBLE
                }
                val savejoin_yn = Utils.getString(memberInfo,"savejoin_yn")
                if (savejoin_yn=="N"){
                    holder.safeIV.visibility = View.GONE
                }else{
                    holder.safeIV.visibility = View.VISIBLE
                }
                val like_member_id  = Utils.getInt(memberInfo,"id")
                val email = Utils.getString(memberInfo, "email")
                val name = Utils.getString(memberInfo, "name")
                val gender = Utils.getString(memberInfo, "gender")
                val height = Utils.getString(memberInfo, "height")
                val birth = Utils.getString(memberInfo, "birth")

                val age = DateUtils.getYearDiffCount(birth, DateUtils.getToday("yyyyMMdd"), "yyyyMMdd")

                val language = Utils.getString(memberInfo, "language")
                val job = Utils.getString(memberInfo, "job")
                val school = Utils.getString(memberInfo, "school")
                val intro = Utils.getString(memberInfo, "intro")

                Log.d("멤버정보",memberInfo.toString())


                if (type !=1){
                    val my_charms = member.getJSONArray("my_charms")
                    val your_charms = member.getJSONArray("your_charms")
                    val meets = member.getJSONArray("meets")
                    holder.charmLL.removeAllViews()
                    for (i in 0 until my_charms.length()){

                        val my_charm = my_charms.get(i) as JSONObject

                        val charmView = View.inflate(context, R.layout.item_main_charm_point, null)
                        var charmTV: TextView = charmView.findViewById(R.id.charmTV)
                        charmTV.text = Utils.getString(my_charm,"charm")

                        holder.charmLL.addView(charmView)
                    }

                    holder.youcharmLL.removeAllViews()
                    for (i in 0 until your_charms.length()){

                        val youcharm = your_charms.get(i) as JSONObject

                        val charmView = View.inflate(context, R.layout.item_main_charm_point, null)
                        var charmTV: TextView = charmView.findViewById(R.id.charmTV)
                        charmTV.text = Utils.getString(youcharm,"charm")

                        holder.youcharmLL.addView(charmView)
                    }

                    holder.meetLL.removeAllViews()
                    for (i in 0 until meets.length()){

                        val meet = meets.get(i) as JSONObject

                        val charmView = View.inflate(context, R.layout.item_main_charm_point, null)
                        var charmTV: TextView = charmView.findViewById(R.id.charmTV)
                        charmTV.text = Utils.getString(meet,"meet")

                        holder.meetLL.addView(charmView)
                    }
                }



                holder.distanceTV.text = "17Km"
                holder.nameTV.text = name
                holder.ageTV.text = age.toString()
                holder.fitRateTV.text = "23%"
                holder.charmIV.setOnClickListener {
                    Log.d("스타볼",starball.toString())
                    if (starball>0){
                        val intent = Intent(context, DlgCrushActivity::class.java)
                        intent.putExtra("like_member_id",like_member_id)
                        context.startActivity(intent)
                    }else{
                        val intent = Intent(context, DlgStarballLackActivity::class.java)
                        context.startActivity(intent)
                    }

                    /*  val intent = Intent(context, MatchedActivity::class.java)
                      context.startActivity(intent)*/
                }
                holder.infoIV.setOnClickListener {

                    val intent = Intent(context, LikedNotiActivity::class.java)
                    activity.startActivity(intent)
                    activity.overridePendingTransition(0, 0)


                    /*
                    val intent = Intent("LIKED_NOTI")
                    context.sendBroadcast(intent)
                    */
                }

            }

            else -> {
                val holder = holder as MainSearchType3

                if(preview) {
                    holder.infoLL.visibility = View.GONE
                } else {
                    holder.infoLL.visibility = View.VISIBLE
                }

                if(mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {

                    if(preview && bitmap != null) {
                        holder.imgIV.setImageBitmap(bitmap)
                    } else {
                        ImageLoader.getInstance().displayImage(Config.url + path, holder.imgIV, Utils.UILoptions)
                    }

                    holder.imgIV.visibility = View.VISIBLE
                    holder.videoVV.visibility = View.GONE

                } else {

                    var dataSource = path
                    if(!preview) {
                        dataSource = Config.url + path
                    }

                    println("dataSource 3 : $dataSource")

                    val (mediaSource, player) = createExoPlayer(dataSource)

                    // holder.videoVV.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM)
                    holder.videoVV.requestFocus()

                    holder.videoVV.player = player
                    player.playWhenReady = false
                    player.prepare(mediaSource)

                    holder.imgIV.visibility = View.GONE
                    holder.videoVV.visibility = View.VISIBLE
                }
                if (type != 1){
                    val dailys = member.getJSONArray("dailys")
                    if (dailys.length()<1){
                        holder.dailyLL.visibility = View.GONE
                    }
                    val daily_member_id  = Utils.getInt(memberInfo,"id")
                    holder.dailyLL.setOnClickListener {
                        var intent = Intent(context, DailyMomentViewListActivity::class.java)
                        intent.putExtra("daily_member_id",daily_member_id)
                        context.startActivity(intent)
                    }

                    holder.dailyLL.removeAllViews()
                    for (i in 0 until dailys.length()){
                        val daily = dailys.get(i) as JSONObject
                        val type = Utils.getInt(daily,"type")
                        if (type==1){
                            val image_uri = Utils.getString(daily,"image_uri")
                            val dailyView = View.inflate(context, R.layout.item_main_daily, null)
                            var dailyIV: ImageView = dailyView.findViewById(R.id.dailyIV)
                            ImageLoader.getInstance().displayImage(Config.url + image_uri,dailyIV, Utils.UILoptions)
                            holder.dailyLL.addView(dailyView)
                        }

                    }

                }

                val savejoin_yn = Utils.getString(memberInfo,"savejoin_yn")
                if (savejoin_yn=="N"){
                    holder.safeIV.visibility = View.GONE
                }else{
                    holder.safeIV.visibility = View.VISIBLE
                }


                Log.d("멤버정보",memberInfo.toString())
                val like_member_id  = Utils.getInt(memberInfo,"id")
                val email = Utils.getString(memberInfo, "email")
                val name = Utils.getString(memberInfo, "name")
                val gender = Utils.getString(memberInfo, "gender")
                val height = Utils.getString(memberInfo, "height")
                val birth = Utils.getString(memberInfo, "birth")

                val age = DateUtils.getYearDiffCount(birth, DateUtils.getToday("yyyyMMdd"), "yyyyMMdd")

                val language = Utils.getString(memberInfo, "language")
                val job = Utils.getString(memberInfo, "job")
                val school = Utils.getString(memberInfo, "school")
                val intro = Utils.getString(memberInfo, "intro")

                holder.distanceTV.text = "17Km"
                holder.nameTV.text = name
                holder.ageTV.text = age.toString()
                holder.fitRateTV.text = "23%"

                holder.charmIV.setOnClickListener {
                    Log.d("스타볼",starball.toString())
                    if (starball>0){
                        val intent = Intent(context, DlgCrushActivity::class.java)
                        intent.putExtra("like_member_id",like_member_id)
                        context.startActivity(intent)
                    }else{
                        val intent = Intent(context, DlgStarballLackActivity::class.java)
                        context.startActivity(intent)
                    }

                    /*  val intent = Intent(context, MatchedActivity::class.java)
                      context.startActivity(intent)*/
                }
            }
        }


    }

    private fun createExoPlayer(dataSource: String?): Pair<ExtractorMediaSource, SimpleExoPlayer> {
        val bandwidthMeter = DefaultBandwidthMeter()
        val extractorsFactory = DefaultExtractorsFactory()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val mediaDataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "mediaPlayerSample"),
            bandwidthMeter as TransferListener<in DataSource>
        )
        val mediaSource = ExtractorMediaSource(
            Uri.parse(dataSource),
            mediaDataSourceFactory, extractorsFactory, null, null
        )

        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        val player = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
        player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        return Pair(mediaSource, player)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = data.length()

    override fun getItemViewType(position: Int): Int {
        return position
    }

}

