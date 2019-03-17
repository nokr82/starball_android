package com.devstories.starball_android.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.starball_android.R
import com.devstories.starball_android.activities.DlgAdverbDeleteConfirmActivity
import com.devstories.starball_android.activities.GroupChattingActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_group_chatting.*
import org.json.JSONObject

class GroupAdverbAdapter(data: ArrayList<JSONObject>, activity: GroupChattingActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data: ArrayList<JSONObject> = data
    private lateinit var context: Context
    private var activity :GroupChattingActivity = activity
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(com.devstories.starball_android.R.layout.item_adverb, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val holder = holder as AdverbAdapter.ViewHolder

        val adverb = data[position]

        holder.contentTV.text = Utils.getString(adverb, "content")
        holder.delLL.setOnClickListener {
            var intent = Intent(context, DlgAdverbDeleteConfirmActivity::class.java)
            intent.putExtra("adverb_id", Utils.getInt(adverb, "id"))
            (context as GroupChattingActivity).startActivityForResult(intent, 100)
        }

        holder.LL.setOnClickListener {
            activity.contentsET.setText(Utils.getString(adverb, "content"))
        }

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var contentTV:TextView
        var delLL:LinearLayout
        var LL:LinearLayout

        init {
            contentTV = v.findViewById(R.id.contentTV)
            delLL = v.findViewById(R.id.delLL)
            LL = v.findViewById(R.id.LL)
        }
    }

}