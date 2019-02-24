package com.devstories.starball_android.adapter

import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.devstories.starball_android.R
import com.devstories.starball_android.base.Utils
import org.json.JSONArray
import org.json.JSONObject


class SwipeStackItemAdapter(private val context:Context, private val data: JSONArray) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MainSearchType1(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imgIV = itemView.findViewById<View>(R.id.imgIV) as ImageView

    }

    class MainSearchType2(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imgIV = itemView.findViewById<View>(R.id.imgIV) as ImageView

    }

    class MainSearchType3(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imgIV = itemView.findViewById<View>(R.id.imgIV) as ImageView

    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when(viewType) {
            0 -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_main_search1, parent, false) as View
                return MainSearchType1(itemView)
            }

            1 -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_main_search2, parent, false) as View
                return MainSearchType2(itemView)
            }
        }

        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_main_search3, parent, false) as View

        return MainSearchType3(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = data.get(position) as JSONObject

        val id = Utils.getInt(item!!, "id")
        val path = Utils.getString(item!!, "path")
        val mediaType = Utils.getInt(item!!, "mediaType")

        var bitmap:Bitmap?
        if(mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
            bitmap = Utils.getImage(context.contentResolver, path)
        } else {
            bitmap = MediaStore.Video.Thumbnails.getThumbnail(context.contentResolver, id.toLong(), MediaStore.Video.Thumbnails.MINI_KIND, null)
        }

        when (holder.itemViewType) {
            0 -> {
                val holder = holder as MainSearchType1
                holder.imgIV.setImageBitmap(bitmap)
            }

            1 -> {
                val holder = holder as MainSearchType2
                holder.imgIV.setImageBitmap(bitmap)
            }

            else -> {
                val holder = holder as MainSearchType3
                holder.imgIV.setImageBitmap(bitmap)
            }
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = data.length()

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

