package com.devstories.starball_android.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.starball_android.R
import com.devstories.starball_android.activities.ChattingMatchFragment
import com.devstories.starball_android.activities.DlgPostOptionActivity
import com.devstories.starball_android.base.Config
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.Utils
import com.nostra13.universalimageloader.core.ImageLoader
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


open class MatchAdapter(fragment: ChattingMatchFragment, data: ArrayList<JSONObject>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data = data
    private lateinit var context: Context
    private var fragment = fragment
    private lateinit var matchChattingAdapter: MatchChattingAdapter
    private var adapterdata = ArrayList<JSONObject>()

    class Match(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        var op_timeTV = itemView.findViewById<View>(R.id.op_timeTV) as TextView
        var chattingLV = itemView.findViewById<View>(R.id.chattingLV) as ListView
        var menuLL = itemView.findViewById<View>(R.id.menuLL) as LinearLayout
        var micLL = itemView.findViewById<View>(R.id.micLL) as LinearLayout
        var msgET = itemView.findViewById<View>(R.id.msgET) as EditText
        var edit_chatLL = itemView.findViewById<View>(R.id.edit_chatLL) as LinearLayout
        var chatLL = itemView.findViewById<View>(R.id.chatLL) as LinearLayout
        var sendLL = itemView.findViewById<View>(R.id.sendLL) as LinearLayout
        var menuIV = itemView.findViewById<View>(R.id.menuIV) as ImageView

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(com.devstories.starball_android.R.layout.item_chatting_match, parent, false)
        context = parent.context
        return Match(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val member_id = PrefUtils.getIntPreference(context, "member_id")

        val json = data[position]
        val type = Utils.getString(json, "type")
        val holder = holder as Match
        val Profile = json.getJSONObject("Profile")
        val image_uri = Utils.getString(Profile, "image_uri")

        val send_chatting = json.getString("send_chatting")
        val room_id = json.getInt("room_id")
        val make_room_yn = json.getString("make_room_yn")

        val Member = json.getJSONObject("Member")
        val like_member_id = Utils.getInt(Member, "id")
        val name = Utils.getString(Member, "name")
        val birth = Utils.getString(Member, "birth")
        val gender = Utils.getString(Member, "gender")

        val Like = json.getJSONObject("Like")
        val like_id = Utils.getInt(Like, "id")
        val created_at = Utils.getString(Like, "created_at")



        val births = birth.split("-")
        var age = 0

        if (births.count() == 3) {

            var now = System.currentTimeMillis()
            var date = Date(now)
            val sdfNow = SimpleDateFormat("yyyy")
            val year = sdfNow.format(date)

            age = year.toInt() - births[0].toInt()
        }


        if (room_id>0){

        }else{

        }




        holder.chattingLV.visibility  = View.VISIBLE
        matchChattingAdapter = MatchChattingAdapter(context, R.layout.item_match, adapterdata)
        holder.chattingLV.adapter = matchChattingAdapter
        adapterdata.clear()
        if (send_chatting =="Y"){
            val LastChatting = json.getJSONObject("LastChatting")
            adapterdata.add(LastChatting)
            matchChattingAdapter.notifyDataSetChanged()
            holder.menuLL.visibility = View.GONE
        }else{
            holder.menuLL.visibility = View.VISIBLE
        }


        holder.timeTV.visibility = View.GONE
        holder.op_timeTV.visibility = View.VISIBLE
        holder.op_timeTV.text = created_at.substring(0, 10).replace("-", ".")

        holder.sendtitleLL.visibility = View.GONE
        holder.nameTV.text = name + " " + age

        if (make_room_yn =="Y"){
            holder.sendIV.visibility = View.VISIBLE
            holder.sendIV.setImageResource(R.mipmap.send_btn)
            holder.sendIV.setOnClickListener {

            }
        }else{
            holder.sendIV.visibility = View.GONE
        }

        holder.chatLL.setOnClickListener {
            holder.chatLL.visibility = View.GONE
            holder.edit_chatLL.visibility = View.VISIBLE
        }
        holder.menuIV.setOnClickListener {
            val intent = Intent(context, DlgPostOptionActivity::class.java)
            intent.putExtra("like_member_id",like_member_id)
            intent.putExtra("like_id",like_id)
            fragment.startActivity(intent)
        }



        holder.sendLL.setOnClickListener {
            var content = Utils.getString(holder.msgET)
            fragment.sendChatting(1,content,like_member_id)

        }

        ImageLoader.getInstance().displayImage(Config.url + image_uri, holder.likeIV, Utils.UILoptions)


    }


    override fun getItemCount() = data.count()

    override fun getItemViewType(position: Int): Int {
        return position
    }


}