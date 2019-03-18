package com.devstories.starball_android.adapter

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.starball_android.R
import com.devstories.starball_android.activities.DailyMomentListActivity
import com.devstories.starball_android.activities.DlgAlbumPayActivity
import com.devstories.starball_android.activities.DlgPostOptionActivity
import com.devstories.starball_android.base.Config
import com.devstories.starball_android.base.DateUtils
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.Utils
import com.nostra13.universalimageloader.core.ImageLoader
import com.yqritc.scalablevideoview.ScalableVideoView
import org.json.JSONObject


open class DaillyAdapter(context: Context, view:Int, data:ArrayList<JSONObject>,activity: DailyMomentListActivity) : ArrayAdapter<JSONObject>(context, view, data) {

    private lateinit var item: ViewHolder
    var view:Int = view
    var data:ArrayList<JSONObject> = data
    var activity:DailyMomentListActivity =activity

    override fun getView(position: Int, convertView: View?, parent : ViewGroup?): View {

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
        if (split.get(0) == today){
            var timesplit = split.get(1).split(":")
            var noon = "오전"
            if (timesplit.get(0).toInt() >= 12){
                noon = "오후"
            }
            var time = noon + " " + timesplit.get(0) + ":" + timesplit.get(1)


            item.timeTV.setText(time)
        } else {
            var since = Utils.since(created_at)

            item.timeTV.setText(since)
        }


        Log.d("컨텐츠",image_uri.toString())
        Log.d("프로필",profile_image_uri.toString())

        item.nameTV.text = name+" "+age
        if (type==1){
            item.videoRL.visibility = View.GONE
            item.videoVV.visibility = View.GONE
            item.contentIV.visibility = View.VISIBLE
            ImageLoader.getInstance().displayImage(Config.url + image_uri, item.contentIV, Utils.UILoptionsProfile)
        }else{
            item.contentIV.visibility = View.GONE
            item.videoRL.visibility = View.VISIBLE
            item.videoVV.visibility = View.VISIBLE
            item.videoVV.setDataSource(Config.url + video_uri)
            Log.d("동영상",Config.url + video_uri.toString())
//            item.videoVV.prepare(MediaPlayer.OnPreparedListener {  item.videoVV.seekTo(1)})
//            item.videoVV.prepareAsync()
        }
        item.playIV.setOnClickListener {
            item.playIV.visibility = View.GONE
            item.videoVV.start()
        }
        ImageLoader.getInstance().displayImage(Config.url + profile_image_uri, item.profileIV, Utils.UILoptionsProfile)
        item.contentIV.setOnClickListener {
            val intent = Intent(context, DlgAlbumPayActivity::class.java)
            intent.putExtra("like_member_id",like_member_id)
           context.startActivity(intent)
        }
        if (like_yn=="N"){
            item.likeIV.setImageResource(R.mipmap.lounge_heart_like)
        }else{
            item.likeIV.setImageResource(R.mipmap.profile_pre_super_like)
        }


        item.likeIV.setOnClickListener {
            if (like_yn=="N"){
                likecnt + 1
                activity.like(content_id)
                item.likeIV.setImageResource(R.mipmap.profile_pre_super_like)
                activity.daillyAdapter.notifyDataSetChanged()
            }else{
                likecnt - 1
                activity.like(content_id)
                item.likeIV.setImageResource(R.mipmap.lounge_heart_like)
                activity.daillyAdapter.notifyDataSetChanged()
            }
        }
        item.likecntTV.text = likecnt.toString()

        item.subIV.setOnClickListener {

        }
        item.menuIV.setOnClickListener {
            if (like_member_id == member_id){
                val intent = Intent(context, DlgPostOptionActivity::class.java)
                intent.putExtra("like_member_id",like_member_id)
                intent.putExtra("content_id",content_id)
                context.startActivity(intent)
            }else{
                return@setOnClickListener
            }

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
        var nameTV : TextView
        var timeTV: TextView
        var profileIV: ImageView
        var subIV: ImageView
        var menuIV: ImageView
        var contentIV: ImageView
        var likeIV: ImageView
        var likecntTV: TextView
        var videoVV: ScalableVideoView
        var videoRL: RelativeLayout
        var playIV: ImageView


        init {
            playIV= v.findViewById(R.id.playIV)
            videoRL= v.findViewById(R.id.videoRL)
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
}
