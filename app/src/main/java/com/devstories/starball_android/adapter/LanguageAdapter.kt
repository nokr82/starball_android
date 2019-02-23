package com.devstories.starball_android.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.starball_android.R

open class LanguageAdapter(context: Context, view:Int, data:ArrayList<String>) : ArrayAdapter<String>(context, view, data) {

    private lateinit var item: ViewHolder
    var view:Int = view
    var data:ArrayList<String> = data

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

        val language = data[position]


        item.optionTV.text = language
        item.delLL.setOnClickListener {
            data.removeAt(position)
            notifyDataSetChanged()
        }

        return retView
    }

    override fun getItem(position: Int): String {
        return data[position]
    }

    override fun getCount(): Int {
        return data.size
    }

    class ViewHolder(v: View) {

        var optionTV:TextView
        var delLL:LinearLayout

        init {
            optionTV = v.findViewById(R.id.optionTV)
            delLL = v.findViewById(R.id.delLL)
        }
    }
}
