package com.devstories.starball_android.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.devstories.starball_android.R
import com.devstories.starball_android.activities.DlgAlbumPayActivity
import com.devstories.starball_android.base.Config
import com.devstories.starball_android.base.DateUtils
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.Utils
import com.nostra13.universalimageloader.core.ImageLoader
import org.json.JSONObject


open class DaillyAdapter(context: Context, view:Int, data:ArrayList<JSONObject>) : ArrayAdapter<JSONObject>(context, view, data) {

    private lateinit var item: ViewHolder
    var view:Int = view
    var data:ArrayList<JSONObject> = data

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
        val created_at = Utils.getInt(content, "created_at")
        val content_id = Utils.getInt(content, "id")

        Log.d("컨텐츠",image_uri.toString())
        Log.d("프로필",profile_image_uri.toString())

        item.nameTV.text = name+" "+age
        ImageLoader.getInstance().displayImage(Config.url + profile_image_uri, item.profileIV, Utils.UILoptionsProfile)
        ImageLoader.getInstance().displayImage(Config.url + image_uri, item.contentIV, Utils.UILoptionsProfile)
        item.likecntTV.text = likecnt.toString()
        item.contentIV.setOnClickListener {
            val intent = Intent(context, DlgAlbumPayActivity::class.java)
            intent.putExtra("like_member_id",like_member_id)
           context.startActivity(intent)
        }
        item.likeIV.setOnClickListener {

        }
        item.subIV.setOnClickListener {

        }
        item.menuIV.setOnClickListener {

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


        init {
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
