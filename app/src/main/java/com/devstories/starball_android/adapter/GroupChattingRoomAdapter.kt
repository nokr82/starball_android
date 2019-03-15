package com.devstories.starball_android.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject
import com.devstories.starball_android.R
import com.devstories.starball_android.base.Config
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.Utils
import com.nostra13.universalimageloader.core.ImageLoader
import java.text.SimpleDateFormat

open class GroupChattingRoomAdapter(context: Context, view: Int, data: ArrayList<JSONObject>) :
    ArrayAdapter<JSONObject>(context, view, data) {

    private lateinit var item: ViewHolder
    var view: Int = view
    var data: ArrayList<JSONObject> = data

    var group_members = ArrayList<JSONObject>()

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

        val json = data[position]

        val Group = json.getJSONObject("Group")
        var title = Utils.getString(Group, "title")
        var created_at = Utils.getString(Group, "created_at")
        val GroupMembers = json.getJSONArray("GroupMembers")


//        val member_id = PrefUtils.getIntPreference(context, "member_id")


        for (i in 0 until GroupMembers.length()) {
            group_members.add(GroupMembers[i] as JSONObject)

        }
        val MemberProfile = group_members[0].getJSONObject("MemberProfile")
        val profile = Utils.getString(MemberProfile, "image_uri")

        /* if (Utils.getInt(lastChatting, "member_id") != member_id) {
             item.chattingIV.setImageResource(R.mipmap.lounge_message_recie)
         } else {
             item.chattingIV.setImageResource(R.mipmap.lounge_message_send)
         }*/

        /*if (Utils.getInt(room, "founder_member_id") == member_id) {
            profile = Utils.getString(attendProfile, "image_uri")
            name = Utils.getString(attendMember, "name")
        }*/

        ImageLoader.getInstance().displayImage(Config.url + profile, item.profileIV, Utils.UILoptionsProfile)
        item.nameTV.text = title + " " + GroupMembers.length().toString()


        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val dateFormat2 = SimpleDateFormat("yy.MM.dd    HH:mm")
        val created = dateFormat.parse(created_at)

        item.createdTV.text = dateFormat2.format(created)


        /*if ("Y" == read_yn) {
            item.newIV.visibility = View.GONE
        } else {
            item.newIV.visibility = View.VISIBLE
        }*/

        return retView
    }

    override fun getItem(position: Int): JSONObject {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.count()
    }

    class ViewHolder(v: View) {

        var profileIV: CircleImageView

        var nameTV: TextView
        var contentsTV: TextView
        var createdTV: TextView

        var chattingIV: ImageView
        var pinIV: ImageView
        var newIV: ImageView

        init {

            profileIV = v.findViewById(R.id.profileIV)

            nameTV = v.findViewById(R.id.nameTV)
            contentsTV = v.findViewById(R.id.contentsTV)
            createdTV = v.findViewById(R.id.createdTV)

            chattingIV = v.findViewById(R.id.chattingIV)
            pinIV = v.findViewById(R.id.pinIV)
            newIV = v.findViewById(R.id.newIV)

        }
    }
}
