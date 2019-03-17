package com.devstories.starball_android.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.starball_android.R
import org.json.JSONObject


open class DaillyAdapter(context: Context, view:Int, data:ArrayList<JSONObject>) : ArrayAdapter<JSONObject>(context, view, data) {

    private lateinit var item: ViewHolder
    var view:Int = view
    var data:ArrayList<JSONObject> = data

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



        item.delLL.setOnClickListener {
            data.removeAt(position)
            notifyDataSetChanged()
        }

        return retView
    }

    override fun getItem(position: Int): JSONObject {
        return data[position]
    }

    override fun getCount(): Int {
        return data.size
    }

    class ViewHolder(v: View) {

        var optionTV: TextView
        var delLL: LinearLayout

        init {
            optionTV = v.findViewById(R.id.optionTV)
            delLL = v.findViewById(R.id.delLL)
        }
    }
}
