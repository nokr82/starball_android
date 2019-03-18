package com.devstories.starball_android.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat.startActivity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.starball_android.R
import com.devstories.starball_android.activities.DlgAlbumPayActivity
import com.devstories.starball_android.activities.DlgCrushActivity


open class EventAdapter (context: Context, view:Int, data:Int,type:Int) : ArrayAdapter<Int>(context, view, data) {

    private lateinit var item: ViewHolder
    var view:Int = view
    var data: Int = data
    var type: Int = type
    lateinit var MatchAdapter: MatchAdapter

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

        if (type==1){
            item.sendIV.visibility = View.GONE
            item.menuLL.visibility = View.VISIBLE
        }else if (type == 2){
            item.heartIV.visibility = View.VISIBLE
            item.menuLL.visibility = View.GONE
        }else if (type == 3){
            item.starIV.visibility = View.VISIBLE
            item.menuLL.visibility = View.GONE
        }


//        MatchAdapter = MatchAdapter(context,R.layout.item_match,1)
//        item.chattingLV.adapter = MatchAdapter


        item.micLL.setOnClickListener {
//            item.chattingLV.visibility = View.VISIBLE
            item.menuLL.visibility = View.GONE
        }
        item.sendLL.setOnClickListener {
//            item.chattingLV.visibility = View.VISIBLE
            item.menuLL.visibility = View.GONE
            item.imgIV.visibility = View.VISIBLE

        }
        item.sendIV.setOnClickListener {


        }
        item.starballIV.setOnClickListener {
            val intent = Intent(context, DlgCrushActivity::class.java)
            context.startActivity(intent)
        }



        return retView
    }

    override fun getItem(position: Int): Int {
        return data
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data
    }
    class ViewHolder(v: View) {

        var micLL: LinearLayout
        var sendLL: LinearLayout
        var sendIV: ImageView
        var starballIV: ImageView
//        var chattingLV: ListView
        var menuLL: LinearLayout
        var starIV: ImageView
        var heartIV: ImageView
        var imgIV: ImageView

        init {
            imgIV=  v.findViewById(R.id.imgIV) as ImageView
            starIV=  v.findViewById(R.id.starIV) as ImageView
            heartIV=  v.findViewById(R.id.heartIV) as ImageView
//            chattingLV =  v.findViewById(R.id.chattingLV) as ListView
            micLL =  v.findViewById(R.id.micLL) as LinearLayout
            menuLL =  v.findViewById(R.id.menuLL) as LinearLayout
            sendLL =  v.findViewById(R.id.sendLL) as LinearLayout
            sendIV =  v.findViewById(R.id.sendIV) as ImageView
            starballIV =  v.findViewById(R.id.starballIV) as ImageView
        }
    }
}
