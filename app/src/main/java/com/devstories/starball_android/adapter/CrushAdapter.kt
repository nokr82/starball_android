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
import com.devstories.starball_android.base.Utils
import com.google.android.exoplayer2.ui.PlayerView
import org.json.JSONObject
import java.util.*


class CrushAdapter(view: Int, data: ArrayList<JSONObject>) :
    RecyclerView.Adapter<ViewHolder>() {

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
        if (type == "superLikeTitle") {
            holder.sendtitleLL.visibility = View.VISIBLE
            holder.titleTV.text = "보낸 Starball"
            holder.titleIV.setImageResource(R.mipmap.recive_star)
            holder.itemLL.visibility = View.GONE
        } else if (type == "LikeTitle") {
            holder.sendtitleLL.visibility = View.VISIBLE
            holder.titleTV.text = "보낸 Like"
            holder.titleIV.setImageResource(R.mipmap.lounge_heart_like)
            holder.itemLL.visibility = View.GONE
        }else{
            holder.sendtitleLL.visibility = View.GONE
            holder.itemLL.visibility = View.VISIBLE
        }


    }


    override fun getItemCount() = data.count()

    override fun getItemViewType(position: Int): Int {
        return position
    }



}
