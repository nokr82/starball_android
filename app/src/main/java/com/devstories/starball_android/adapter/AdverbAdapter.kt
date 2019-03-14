package com.devstories.starball_android.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.devstories.starball_android.R

class AdverbAdapter(imagePaths: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val _imagePaths: ArrayList<String> = imagePaths

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
//        return ViewHolder(p1)
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class ViewHolder(v: View) {

        var charmTV:TextView

        init {
            charmTV = v.findViewById(R.id.charmTV)
        }
    }

}