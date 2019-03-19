package com.devstories.starball_android.adapter

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.starball_android.R
import com.devstories.starball_android.activities.DailyMomentListActivity
import com.devstories.starball_android.activities.DailyMomentViewListActivity
import com.devstories.starball_android.activities.DlgAlbumPayActivity
import com.devstories.starball_android.activities.DlgPostOptionActivity
import com.devstories.starball_android.base.Config
import com.devstories.starball_android.base.DateUtils
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.Utils
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.TransferListener
import com.google.android.exoplayer2.util.Util
import com.nostra13.universalimageloader.core.ImageLoader
import com.yqritc.scalablevideoview.ScalableVideoView
import org.json.JSONObject


open class DaillyAdapter(
    context: Context,
    view: Int,
    data: ArrayList<JSONObject>,
    activity: DailyMomentListActivity,
    activity2: DailyMomentViewListActivity,
    v_type: Int
) : ArrayAdapter<JSONObject>(context, view, data) {

    private lateinit var item: ViewHolder
    var view: Int = view
    var data: ArrayList<JSONObject> = data
    var activity: DailyMomentListActivity = activity
    var activity2: DailyMomentViewListActivity = activity2
    var v_type: Int = v_type
    private var playedPlayIV: ImageView? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        lateinit var retView: View

        if (convertView == null) {
            retView = View.inflate(context, view, null)
            item = ViewHolder(retView)
            retView.tag = item
        } else {
            retView = convertView
            item = convertView.tag as ViewHolder
            if (item == null) {
                retView = View.inflate(context, view, null)
                item = ViewHolder(retView)
                retView.tag = item
            }
        }
        val member_id = PrefUtils.getIntPreference(context, "member_id")

        val json = data[position]

        val likecnt = json.getInt("ContentLikeCount")
        val like_yn = json.getString("LikeYn")

        val member = json.getJSONObject("Member")
        val name = Utils.getString(member, "name")
        val birth = Utils.getString(member, "birth")
        val age = DateUtils.getYearDiffCount(birth, DateUtils.getToday("yyyyMMdd"), "yyyyMMdd")

        val profile = json.getJSONObject("Profile")
        val profile_image_uri = Utils.getString(profile, "image_uri")

        val content = json.getJSONObject("Content")
        val type = Utils.getInt(content, "type")
        val like_member_id = Utils.getInt(content, "member_id")
        val image_uri = Utils.getString(content, "image_uri")
        val video_uri = Utils.getString(content, "video_uri")
        val created_at = Utils.getString(content, "created_at")
        val content_id = Utils.getInt(content, "id")

        val today = Utils.todayStr()
        var split = created_at.split("T")
        if (split.get(0) == today) {
            var timesplit = split.get(1).split(":")
            var noon = "오전"
            if (timesplit.get(0).toInt() >= 12) {
                noon = "오후"
            }
            var time = noon + " " + timesplit.get(0) + ":" + timesplit.get(1)


            item.timeTV.text = time
        } else {
            // var since = Utils.since(created_at)

            // item.timeTV.text = since
        }


        Log.d("컨텐츠", image_uri.toString())
        Log.d("프로필", profile_image_uri.toString())

        item.nameTV.text = name + " " + age
        if (type == 1) {
            item.videoRL.visibility = View.GONE
            // item.videoVV.visibility = View.GONE
            item.contentIV.visibility = View.VISIBLE
            ImageLoader.getInstance().displayImage(Config.url + image_uri, item.contentIV, Utils.UILoptionsProfile)
        } else {
            item.contentIV.visibility = View.GONE
            item.videoRL.visibility = View.VISIBLE
            // item.videoVV.visibility = View.VISIBLE

            val dataSource = com.devstories.starball_android.base.Config.url + video_uri.toString()

            Log.d("동영상11", Config.url + video_uri.toString())

            // item.videoVV.release()
            // item.videoVV.reset()

            /*
            item.videoVV.setDataSource(Config.url + video_uri)
            // item.videoVV.setDataSource(context, Uri.parse(Config.url + video_uri))

            item.videoVV.prepare {
                item.videoVV.seekTo(1)
            }
            */

            val (mediaSource, player) = createExoPlayer(dataSource)

            // holder.videoVV.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM)
            item.videoVV.requestFocus()

            item.videoVV.player = player
            player.playWhenReady = false
            player.prepare(mediaSource)
            player.addListener(object : Player.EventListener {
                override fun onLoadingChanged(isLoading: Boolean) {

                }

                override fun onPlayerError(error: ExoPlaybackException?) {

                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if(playbackState == Player.STATE_ENDED) {
                        playedPlayIV?.visibility = View.VISIBLE
                    }
                }

                override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {

                }

                override fun onSeekProcessed() {

                }

                override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {

                }

                override fun onPositionDiscontinuity(reason: Int) {

                }

                override fun onRepeatModeChanged(repeatMode: Int) {

                }

                override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {

                }

                override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {

                }
            })

            item.playIV.setOnClickListener {

                it.visibility = View.GONE

                playedPlayIV = it as ImageView

                val videoVV = (it.parent as RelativeLayout).findViewById<PlayerView>(R.id.videoVV)

                videoVV.player.seekTo(0);

                videoVV.player.playWhenReady = true
            }


        }


        ImageLoader.getInstance().displayImage(Config.url + profile_image_uri, item.profileIV, Utils.UILoptionsProfile)
        item.contentIV.setOnClickListener {
            val intent = Intent(context, DlgAlbumPayActivity::class.java)
            intent.putExtra("like_member_id", like_member_id)
            context.startActivity(intent)
        }

        if (like_yn == "N") {
            item.likeIV.setImageResource(R.mipmap.lounge_heart_like)
        } else {
            item.likeIV.setImageResource(R.mipmap.profile_pre_super_like)
        }



        item.likeIV.setOnClickListener {
            if (like_yn == "N") {
                if (v_type == 1) {
                    activity.like(content_id)
                } else {
                    activity2.like(content_id)
                }

                item.likeIV.setImageResource(R.mipmap.profile_pre_super_like)
            } else {
                if (v_type == 1) {
                    activity.like(content_id)
                } else {
                    activity2.like(content_id)
                }
                item.likeIV.setImageResource(R.mipmap.lounge_heart_like)
            }
        }
        item.likecntTV.text = likecnt.toString()

        item.subIV.setOnClickListener {

        }
        item.menuIV.setOnClickListener {
            val intent = Intent(context, DlgPostOptionActivity::class.java)
            intent.putExtra("like_member_id", like_member_id)
            intent.putExtra("content_id", content_id)
            context.startActivity(intent)
        }
        if (v_type == 1) {
            item.subIV.visibility = View.VISIBLE
            item.menuIV.visibility = View.VISIBLE
            item.timeTV.visibility = View.VISIBLE
            item.profileIV.visibility = View.VISIBLE
            item.profileIV.setOnClickListener {
                if (member_id!=like_member_id){
                    var intent = Intent(context, DailyMomentViewListActivity::class.java)
                    intent.putExtra("daily_member_id",like_member_id)
                    context.startActivity(intent)
                }else{
                    return@setOnClickListener
                }

            }

        } else {
            item.subIV.visibility = View.GONE
            item.menuIV.visibility = View.GONE
            item.timeTV.visibility = View.GONE
            item.profileIV.visibility = View.GONE
            item.nameTV.text = created_at.substring(0, 10).replace("-", ".")
            ImageLoader.getInstance().displayImage(Config.url + profile_image_uri,activity2.profileIV, Utils.UILoptionsProfile)
            activity2.nameTV.text = name + " " + age
        }


        return retView
    }

    override fun getItem(position: Int): JSONObject {
        return data[position]
    }

    override fun getCount(): Int {
        return data.size
    }

    class ViewHolder(v: View) {
        var nameTV: TextView
        var timeTV: TextView
        var profileIV: ImageView
        var subIV: ImageView
        var menuIV: ImageView
        var contentIV: ImageView
        var likeIV: ImageView
        var likecntTV: TextView
        var videoVV: PlayerView
        var videoRL: RelativeLayout
        var playIV: ImageView


        init {
            playIV = v.findViewById(R.id.playIV)
            videoRL = v.findViewById(R.id.videoRL)
            videoVV = v.findViewById(R.id.videoVV)
            nameTV = v.findViewById(R.id.nameTV)
            timeTV = v.findViewById(R.id.timeTV)
            profileIV = v.findViewById(R.id.profileIV)
            subIV = v.findViewById(R.id.subIV)
            menuIV = v.findViewById(R.id.menuIV)
            subIV = v.findViewById(R.id.subIV)
            contentIV = v.findViewById(R.id.contentIV)
            likeIV = v.findViewById(R.id.likeIV)
            likecntTV = v.findViewById(R.id.likecntTV)

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

}
