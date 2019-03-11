package com.devstories.starball_android.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.starball_android.R
import com.devstories.starball_android.base.Utils
import org.json.JSONObject

open class StarballHistoryAdapter (context: Context, view:Int, data:ArrayList<JSONObject>) : ArrayAdapter<JSONObject>(context, view, data) {

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

        if (position == 0) {
            item.headerLL.visibility = View.VISIBLE
        } else {
            item.headerLL.visibility = View.GONE
        }

        val json = data[position]

        val starball = Utils.getInt(json, "starball")
        val type = Utils.getInt(json, "type")
        val cate = Utils.getInt(json, "cate")
        val balance = Utils.getInt(json, "balance")

        if (type == 1) {

            if (cate == 1) {
                item.typeTV.text = "구매"
            } else if (cate == 2) {
                item.typeTV.text = "광고"
            } else {
                item.typeTV.text = "초대"
            }

            item.usingTV.text = "+" + starball

        } else {
            item.typeTV.text = "사용"

            item.usingTV.text = "-" + starball
        }

        item.balanceTV.text = balance.toString()

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

        var headerLL: LinearLayout
        var dataLL: LinearLayout

        var dateTV: TextView
        var typeTV: TextView
        var usingTV: TextView
        var balanceTV: TextView

        init {
            headerLL = v.findViewById(R.id.headerLL)
            dataLL = v.findViewById(R.id.dataLL)

            dateTV = v.findViewById(R.id.dateTV)
            typeTV = v.findViewById(R.id.typeTV)
            usingTV = v.findViewById(R.id.usingTV)
            balanceTV = v.findViewById(R.id.balanceTV)
        }
    }
}
