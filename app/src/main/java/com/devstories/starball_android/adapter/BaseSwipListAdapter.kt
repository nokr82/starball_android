package com.devstories.starball_android.adapter

import android.widget.*

abstract class BaseSwipeListAdapter() : BaseAdapter() {

    var enablePosition = -1

    fun getSwipeEnableByPosition(position: Int): Boolean {

        if(position == enablePosition) {
            return false
        }

        return true
    }

    fun setSwipeEnablePosition(position: Int) {
        this.enablePosition = position
    }
}