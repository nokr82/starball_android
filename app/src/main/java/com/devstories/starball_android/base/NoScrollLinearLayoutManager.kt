package com.devstories.starball_android.base

import android.content.Context
import android.support.v7.widget.LinearLayoutManager

class NoScrollLinearLayoutManager(context: Context, orientation:Int, reverseLayout:Boolean) : LinearLayoutManager(context, orientation, reverseLayout) {
    private var scrollable = true

    fun enableScrolling() {
        scrollable = true
    }

    fun disableScrolling() {
        scrollable = false
    }

    override fun canScrollVertically() = super.canScrollVertically() && scrollable


    override fun canScrollHorizontally() = super.canScrollHorizontally() && scrollable
}