package com.devstories.starball_android.base

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.devstories.starball_android.R
import java.text.SimpleDateFormat
import kotlin.concurrent.schedule
import android.os.Looper
import android.os.Handler
import java.util.*


class ElapsedTimeTextView(context: Context?, attrs: AttributeSet?) : TextView(context, attrs) {

    init {

        println("attrs : $attrs")

        var input:String? = null

        val ta = context?.obtainStyledAttributes(attrs, R.styleable.app, 0, 0)
        try {
            input = ta?.getString(R.styleable.app_dest_date_time)
        } finally {
            ta?.recycle()
        }

        Timer().schedule(0, 1000 * 60) {

            println("RRR : $text")

            if(input != null) {

                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN)
                val destDateTime = formatter.parse(input)

                val currentTime = Date()

                val result = convertFromDuration(destDateTime.time - currentTime.time)

                val handler = Handler(Looper.getMainLooper())
                handler.post(Runnable {
                    text = String.format(context!!.resources.getString(R.string.remaining_time), result.hours, result.minutes)
                })

            }
        }

    }


    fun convertFromDuration(timeInSeconds: Long): TimeInHours {
        var time = timeInSeconds / 1000
        val hours = time / 3600
        time %= 3600
        val minutes = time / 60
        time %= 60
        val seconds = time
        return TimeInHours(hours.toInt(), minutes.toInt(), seconds.toInt())
    }

    class TimeInHours(val hours: Int, val minutes: Int, val seconds: Int) {
    }
}