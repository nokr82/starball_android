package com.devstories.starball_android.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.devstories.starball_android.R
import com.devstories.starball_android.base.Utils
import com.nostra13.universalimageloader.core.ImageLoader

open class EmoticonAdapter (context: Context, val view:Int, val data: Array<Int>) : ArrayAdapter<Int>(context, view, data) {

    private lateinit var item: ViewHolder

    override fun getView(position: Int, convertView: View?, parent : ViewGroup?): View {

        lateinit var retView: View

        if (convertView == null) {
            retView = View.inflate(context, view, null)
            item = ViewHolder(retView)
            retView.tag = item
        } else {
            retView = convertView
            if (convertView.tag == null) {
                retView = View.inflate(context, view, null)
                item = ViewHolder(retView)
                retView.tag = item
            } else {
                item = convertView.tag as ViewHolder
            }
        }

        val value = data[position]

        ImageLoader.getInstance().displayImage("drawable://$value", item.imageIV, Utils.UILoptionsPosting)

        return retView
    }

    override fun getItem(position: Int): Int {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }

    class ViewHolder(v: View) {
        val imageIV: ImageView = v.findViewById(R.id.imageIV)
    }

}
