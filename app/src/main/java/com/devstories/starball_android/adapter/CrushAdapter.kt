package com.devstories.starball_android.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.starball_android.R
import com.devstories.starball_android.base.Config
import com.devstories.starball_android.base.Utils
import com.nostra13.universalimageloader.core.ImageLoader
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class CrushAdapter(view: Int, data: ArrayList<JSONObject>,a_type:Int) :
    RecyclerView.Adapter<ViewHolder>() {
    private var a_type = a_type
    private var data = data
    private lateinit var context: Context


    class Crush(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemLL = itemView.findViewById<View>(R.id.itemLL) as LinearLayout
        var sendtitleLL = itemView.findViewById<View>(R.id.sendtitleLL) as LinearLayout
        var titleTV = itemView.findViewById<View>(R.id.titleTV) as TextView
        var timeTV = itemView.findViewById<View>(R.id.timeTV) as TextView
        var likeIV = itemView.findViewById<View>(R.id.likeIV) as ImageView
        var titleIV = itemView.findViewById<View>(R.id.titleIV) as ImageView
        var starballIV = itemView.findViewById<View>(R.id.starballIV) as ImageView
        var sendIV = itemView.findViewById<View>(R.id.sendIV) as ImageView
        var nameTV = itemView.findViewById<View>(R.id.nameTV) as TextView
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(com.devstories.starball_android.R.layout.item_chatting_match, parent, false)
        context = parent.context
        return Crush(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val json = data[position]
        val type = Utils.getString(json, "type")
        val holder = holder as Crush
        if (type ==null||type ==""){
            val Like = json.getJSONObject( "Like")
            val LikeMember = json.getJSONObject( "LikeMember")
            val LikeMemberProfile = json.getJSONObject( "LikeMemberProfile")
//        val Member = json.getJSONObject( "Member")
            val created_at = Utils.getString(Like, "created_at")
            val name = Utils.getString(LikeMember, "name")
            val birth = Utils.getString(LikeMember, "birth")
            val gender = Utils.getString(LikeMember, "gender")
            val image_uri = Utils.getString(LikeMemberProfile, "image_uri")
            var bitmap = Config.url + image_uri
            val births = birth.split("-")
            var age = 0

            if (births.count() == 3) {

                var now = System.currentTimeMillis()
                var date = Date(now)
                val sdfNow = SimpleDateFormat("yyyy")
                val year = sdfNow.format(date)

                age = year.toInt() - births[0].toInt()
            }

            holder.nameTV.text = name + " " + age

            if (a_type==2){
                holder.sendIV.setImageResource(R.mipmap.bi)
            }else{
                holder.sendIV.setImageResource(R.mipmap.send_btn)
            }
            holder.timeTV.text = created_at.substring(0,10).replace("-",".")
            ImageLoader.getInstance().displayImage(Config.url + image_uri, holder.likeIV, Utils.UILoptions)
        }else{
        }



        if (type == "superLikeTitle") {
            holder.sendtitleLL.visibility = View.VISIBLE
            if (a_type==2){
                holder.titleTV.text = "보낸 Starball"
            }else{
                holder.titleTV.text = "받은 Starball"
            }
            holder.titleIV.setImageResource(R.mipmap.recive_star)
            holder.itemLL.visibility = View.GONE
        }
        else if (type == "likeTitle") {
            holder.sendtitleLL.visibility = View.VISIBLE
            if (a_type==2){
                holder.titleTV.text = "보낸 Like"
            }else{
                holder.titleTV.text = "받은 Like"
            }
            holder.titleIV.setImageResource(R.mipmap.lounge_heart_like)
            holder.itemLL.visibility = View.GONE
        }
        else{
            holder.sendtitleLL.visibility = View.GONE
            holder.itemLL.visibility = View.VISIBLE
        }

    }


    override fun getItemCount() = data.count()

    override fun getItemViewType(position: Int): Int {
        return position
    }



}
