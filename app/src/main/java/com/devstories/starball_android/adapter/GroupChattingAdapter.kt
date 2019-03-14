package com.devstories.starball_android.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.starball_android.R
import com.devstories.starball_android.activities.FriendChattingActivity
import com.devstories.starball_android.activities.GroupChattingActivity
import com.devstories.starball_android.base.Config
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.Utils
import com.nostra13.universalimageloader.core.ImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

open class GroupChattingAdapter (context: Context, view:Int, data:ArrayList<JSONObject>, activity: GroupChattingActivity) : ArrayAdapter<JSONObject>(context, view, data) {

    private lateinit var item: ViewHolder
    var view:Int = view
    var data:ArrayList<JSONObject> = data
    var activity: GroupChattingActivity = activity

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

        val json = data[position]
        val chatting = json.getJSONObject("Chatting")

        val chatting_member_id = Utils.getInt(chatting, "member_id")
        val member_id = PrefUtils.getIntPreference(context, "member_id")

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val dateFormat2 = SimpleDateFormat("MM/dd hh:mm a", Locale.US)

        val type = Utils.getInt(chatting, "type")
        val contents = Utils.getString(chatting, "contents")
        val created_at = Utils.getString(chatting, "created_at")

        val created_dt = dateFormat.parse(created_at)
        val created = dateFormat2.format(created_dt)

        if (chatting_member_id != member_id) {
            item.otherLL.visibility = View.VISIBLE
            item.myLL.visibility = View.GONE

            item.otherContentsTV.text = contents
            item.otherCreatedTV.text = created



            if (type == 2) {
                item.otherImageIV.visibility = View.VISIBLE
                item.otherContentsLL.visibility = View.GONE
            } else {
                item.otherImageIV.visibility = View.GONE
                item.otherContentsLL.visibility = View.VISIBLE
            }

        } else {
            item.otherLL.visibility = View.GONE
            item.myLL.visibility = View.VISIBLE

            item.myContentsTV.text = contents
            item.myCreatedTV.text = created

            if (type == 2) {

                ImageLoader.getInstance().displayImage(Config.url + Utils.getString(chatting, "image_uri"), item.myImageIV, Utils.UILoptionsPosting)

                item.myImageIV.visibility = View.VISIBLE
                item.myContentsLL.visibility = View.GONE
            } else {
                item.myImageIV.visibility = View.GONE
                item.myContentsLL.visibility = View.VISIBLE
            }
        }

        var like_yn = Utils.getString(chatting, "like_yn")
        if (like_yn == "Y") {
            item.likeIV.setImageResource(R.mipmap.lounge_heart_like)
            item.likeIV.setBackgroundColor(Color.parseColor("#000000"))
        } else {
            item.likeIV.setImageResource(R.mipmap.lounge_heart_like)
            item.likeIV.setBackgroundColor(Color.parseColor("#00000000"))
        }



        return retView
    }

    override fun getItem(position: Int): JSONObject {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }
    class ViewHolder(v: View) {

        var otherLL: LinearLayout
        var otherContentsLL: LinearLayout
        var otherContentsTV: TextView
        var otherCreatedTV: TextView
        var translationTV: TextView
        var otherImageIV: ImageView

        var myLL: LinearLayout
        var myContentsLL: LinearLayout
        var myContentsTV: TextView
        var myCreatedTV: TextView
        var myImageIV: ImageView

        var profileIV: CircleImageView

        var likeIV: ImageView
        var likeLL: LinearLayout

        init {

            otherLL = v.findViewById(R.id.otherLL)
            otherContentsLL = v.findViewById(R.id.otherContentsLL)
            otherContentsTV = v.findViewById(R.id.otherContentsTV)
            otherCreatedTV = v.findViewById(R.id.otherCreatedTV)
            translationTV = v.findViewById(R.id.translationTV)
            otherImageIV = v.findViewById(R.id.otherImageIV)

            myLL = v.findViewById(R.id.myLL)
            myContentsLL = v.findViewById(R.id.myContentsLL)
            myContentsTV = v.findViewById(R.id.myContentsTV)
            myCreatedTV = v.findViewById(R.id.myCreatedTV)
            myImageIV = v.findViewById(R.id.myImageIV)

            profileIV = v.findViewById(R.id.profileIV)
            likeIV = v.findViewById(R.id.likeIV)
            likeLL = v.findViewById(R.id.likeLL)

        }
    }
}
