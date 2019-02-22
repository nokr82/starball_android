package com.devstories.starball_android.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.devstories.starball_android.R
import org.json.JSONArray


class SwipeStackItemAdapter(private val data: JSONArray) :
    RecyclerView.Adapter<SwipeStackItemAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTV: TextView

        init {
            titleTV = itemView.findViewById<View>(R.id.titleTV) as TextView
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeStackItemAdapter.MyViewHolder {
        // create a new view
        val itemView = LayoutInflater.from(parent.context).inflate(com.devstories.starball_android.R.layout.activity_main_search, parent, false) as View
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.titleTV.text = position.toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = data.length()
}

