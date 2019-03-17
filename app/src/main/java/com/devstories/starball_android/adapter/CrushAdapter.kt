package com.devstories.starball_android.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.starball_android.R
import com.devstories.starball_android.activities.ChattingCrushFragment
import com.devstories.starball_android.activities.ChattingSendCrushFragment
import com.devstories.starball_android.activities.DlgCrushActivity
import com.devstories.starball_android.activities.DlgStarballLackActivity
import com.devstories.starball_android.base.Config
import com.devstories.starball_android.base.Utils
import com.nostra13.universalimageloader.core.ImageLoader
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class CrushAdapter(fragment: ChattingSendCrushFragment,fragment2:ChattingCrushFragment,data: ArrayList<JSONObject>, a_type:Int) :
    RecyclerView.Adapter<ViewHolder>() {
    private var a_type = a_type
    private var data = data
    private lateinit var context: Context
    private var fragment = fragment
    private var fragment2 = fragment2



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
        var starballTV = itemView.findViewById<View>(R.id.starballTV) as TextView
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
//        val Member = json.getJSONObject( "Member" )
            val starball = Utils.getString(json, "starball")
            val created_at = Utils.getString(Like, "created_at")
            val name = Utils.getString(LikeMember, "name")
            val birth = Utils.getString(LikeMember, "birth")
            val like_member_id = Utils.getInt(LikeMember, "id")
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
                holder.starballTV.text ="+"+starball
                holder.sendIV.setImageResource(R.mipmap.bi)
                holder.sendIV.setOnClickListener {
                    if (fragment.starball>0){
                        val intent = Intent(context, DlgCrushActivity::class.java)
                        intent.putExtra("like_member_id",like_member_id)
                        context.startActivity(intent)
                    }else{
                        val intent = Intent(context, DlgStarballLackActivity::class.java)
                        context.startActivity(intent)
                    }

                }
            }else{
                holder.starballTV.text = "+"+starball
                holder.sendIV.setImageResource(R.mipmap.send_heart)
                holder.sendIV.setOnClickListener {
                    if (fragment2.starball>0){
                        fragment2.use_starball(like_member_id)
//                        fragment2.adapterdata.removeAt(position)
                    }else{
                        val intent = Intent(context, DlgStarballLackActivity::class.java)
                        context.startActivity(intent)
                    }

                }
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
