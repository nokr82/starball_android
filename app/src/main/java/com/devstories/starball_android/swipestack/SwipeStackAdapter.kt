package com.devstories.starball_android.swipestack

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.SwipeStackItemAdapter
import com.devstories.starball_android.base.NoScrollLinearLayoutManager
import org.json.JSONObject

class SwipeStackAdapter(private val context: Context, private val data: ArrayList<JSONObject>, swipeHelper: SwipeHelper) : BaseAdapter() {

    private var mSwipeHelper = swipeHelper
    private var dxs = 0

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): JSONObject {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = View.inflate(context, com.devstories.starball_android.R.layout.item_card, null)

        val item = data[position]

        convertView.findViewById<TextView>(R.id.idxTV).apply {
            text = position.toString()
        }

        val recyclerView = convertView.findViewById<android.support.v7.widget.RecyclerView>(R.id.my_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            val noScrollLinearLayoutManager = NoScrollLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = noScrollLinearLayoutManager

            // specify an viewAdapter (ee also next example)
            adapter = SwipeStackItemAdapter(context, item.getJSONArray("pages"))

            PagerSnapHelper().attachToRecyclerView(this)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    dxs += dx
                }
            })

            val mOnItemTouchListener = object : RecyclerView.OnItemTouchListener {

                var xDistance: Float = 0.toFloat()
                var yDistance: Float = 0.toFloat()
                var lastX: Float = 0.toFloat()
                var lastY: Float = 0.toFloat()

                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

                    // println("onTouchEvent at Adapter 2 : $e")

                    when (e.action) {
                        MotionEvent.ACTION_DOWN -> {
                            dxs = 0
                            yDistance = 0f
                            xDistance = yDistance
                            lastX = e.x
                            lastY = e.y
                            mSwipeHelper.onTouch(rv, e)
                        }
                        MotionEvent.ACTION_MOVE -> {
                            val curX = e.x
                            val curY = e.y
                            xDistance = Math.abs(curX - lastX)
                            yDistance = Math.abs(curY - lastY)
                            // lastX = curX
                            // lastY = curY

                            println("POS : $position, diff : ${Math.abs(xDistance - yDistance)}")

                            println("mSwipeHelper.isFloating : ${mSwipeHelper.isFloating}")

                            if(mSwipeHelper.isFloating) {
                                mSwipeHelper.onTouch(rv, e)
                                noScrollLinearLayoutManager.disableScrolling()
                            } else {
                                if(Math.abs(xDistance - yDistance) > 10) {
                                    if (dxs == 0 && xDistance <= yDistance) {
                                        mSwipeHelper.onTouch(rv, e)
                                    } else {
                                        noScrollLinearLayoutManager.enableScrolling()
                                    }
                                } else {
                                    noScrollLinearLayoutManager.disableScrolling()
                                }
                            }
                        }
                        MotionEvent.ACTION_UP -> {
                            dxs = 0
                            noScrollLinearLayoutManager.enableScrolling()
                            if (mSwipeHelper.isFloating) {
                                mSwipeHelper.onTouch(rv, e)
                                return true
                            }
                        }
                        MotionEvent.ACTION_CANCEL -> {
                            dxs = 0
                            noScrollLinearLayoutManager.enableScrolling()
                            mSwipeHelper.onTouch(rv, e)
                        }
                    }

                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

                }
            }

            addOnItemTouchListener(mOnItemTouchListener)

        }

        return convertView
    }
}