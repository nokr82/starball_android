package com.devstories.starball_android.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.starball_android.R
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject
import java.util.*

open class EmoticonAdapter (context: Context, val view:Int, val data:ArrayList<JSONObject>) : ArrayAdapter<JSONObject>(context, view, data) {

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
            }
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

    }

}
