package com.devstories.starball_android.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.starball_android.R
import com.devstories.starball_android.activities.GroupChattingActivity
import com.devstories.starball_android.base.Utils
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
        val chatting = json.getJSONObject("GroupChatting")
        val read_count = Utils.getInt(json, "read_count")
        val time = Utils.getInt(json, "time")

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val dateFormat2 = SimpleDateFormat("MM/dd hh:mm a", Locale.US)

        val type = Utils.getInt(chatting, "type")
        val contents = Utils.getString(chatting, "contents")
        val created_at = Utils.getString(chatting, "created_at")

        val created_dt = dateFormat.parse(created_at)
        val created = dateFormat2.format(created_dt)


        item.contentsTV.text = contents
        item.createTV.text = created

        var read_cnt = activity.member_count - read_count
        item.readcountTV.text = activity.member_count.toString()+" / "+ read_cnt.toString()
        if (time!=0){
            item.timerLL.visibility = View.VISIBLE
        }else{
            item.timerLL.visibility = View.GONE
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
        var timerLL: LinearLayout
        var translationLL: LinearLayout
        var translationTV: TextView
        var cancelTV: TextView
        var createTV: TextView
        var timerTV: TextView
        var readcountTV: TextView
        var contentsTV: TextView



        init {

            timerLL= v.findViewById(R.id.timeLL)
            timerTV= v.findViewById(R.id.timerTV)
            cancelTV= v.findViewById(R.id.cancelTV)
            createTV= v.findViewById(R.id.createTV)
            translationTV= v.findViewById(R.id.translationTV)
            readcountTV= v.findViewById(R.id.readcountTV)
            translationLL= v.findViewById(R.id.translationLL)
            contentsTV= v.findViewById(R.id.contentsTV)




        }
    }
}
