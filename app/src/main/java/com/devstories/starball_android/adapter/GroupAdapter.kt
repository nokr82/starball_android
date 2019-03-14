package com.devstories.starball_android.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.devstories.starball_android.R
import com.devstories.starball_android.activities.FriendChattingActivity
import com.devstories.starball_android.base.Config
import com.devstories.starball_android.base.Utils
import com.nostra13.universalimageloader.core.ImageLoader
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


open class GroupAdapter (context: Context, view:Int, data:ArrayList<JSONObject>) : ArrayAdapter<JSONObject>(context, view, data) {

    private lateinit var item: ViewHolder
    var view:Int = view
    var data: ArrayList<JSONObject> = data


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

        val Member =  json.getJSONObject( "Member")
        val Profile =  json.getJSONObject( "Profile")
        val member_id = Utils.getString(Member, "id")
        val name = Utils.getString(Member, "name")
        val birth = Utils.getString(Member, "birth")
        val image_uri = Utils.getString(Profile, "image_uri")
        val births = birth.split("-")
        var age = 0

        if (births.count() == 3) {

            var now = System.currentTimeMillis()
            var date = Date(now)
            val sdfNow = SimpleDateFormat("yyyy")
            val year = sdfNow.format(date)

            age = year.toInt() - births[0].toInt()
        }

        item.nameTV.text = name + " " + age

        item.ckIV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected){
                item.ckIV.setImageResource(R.mipmap.radio_on)
            }else{
                item.ckIV.setImageResource(R.mipmap.radio_off)
            }
        }
        ImageLoader.getInstance().displayImage(Config.url + image_uri, item.profileIV, Utils.UILoptionsPosting)


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

        var nameTV: TextView
        var profileIV: ImageView
        var ckIV: ImageView

        init {
            ckIV = v.findViewById(R.id.ckIV)
            profileIV = v.findViewById(R.id.profileIV)
            nameTV = v.findViewById(R.id.nameTV)

        }
    }
}
