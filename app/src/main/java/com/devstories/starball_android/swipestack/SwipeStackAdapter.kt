package com.devstories.starball_android.swipestack

import android.animation.ObjectAnimator
import android.content.Context
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.devstories.starball_android.R
import com.devstories.starball_android.activities.MainActivity
import com.devstories.starball_android.adapter.SwipeStackItemAdapter
import com.devstories.starball_android.base.Config
import com.devstories.starball_android.base.NoScrollLinearLayoutManager
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.item_card.view.*
import org.json.JSONObject

class SwipeStackAdapter(private val context: Context, private val activity: MainActivity, private val data: ArrayList<JSONObject>, swipeHelper: SwipeHelper) : BaseAdapter() {

    private var mSwipeHelper = swipeHelper
    private var dxs = 0

    private var overallXScroll = 0

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

        val pages = item.getJSONArray("pages")

        val recyclerView = convertView.findViewById<android.support.v7.widget.RecyclerView>(R.id.my_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            val noScrollLinearLayoutManager = NoScrollLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = noScrollLinearLayoutManager

            // specify an viewAdapter (ee also next example)
            adapter = SwipeStackItemAdapter(context, activity, item, item.getJSONArray("pages"), false,activity.starball,2,
                context as MainActivity
            )

            PagerSnapHelper().attachToRecyclerView(this)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    dxs += dx

                    overallXScroll += dx;

                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    // println("newState : $newState")

                    if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val screenWidth = Utils.getScreenWidth(context)

                        println("$overallXScroll / $screenWidth = ${overallXScroll / screenWidth}")

                        if(overallXScroll % screenWidth == 0) {
                            val position = (overallXScroll / screenWidth)

                            // println("pos : $position, pages.length() : ${pages.length()}")

                            if(position >= pages.length()) {
                                return
                            }

                            val item = pages.get(position) as JSONObject

                            println(item)

                            val id = Utils.getInt(item!!, "id")
                            val member_id = Utils.getInt(item!!, "member")
                            val path = Utils.getString(item!!, "image_uri")
                            val mediaType = Utils.getInt(item!!, "type")
                            if(mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {

                                layoutManager!!.findViewByPosition(position)

                                var dataSource = Config.url + path

                                println("dd $dataSource")

                                val holder = recyclerView.findViewHolderForAdapterPosition(position)
                                if (holder is SwipeStackItemAdapter.MainSearchType1) {
                                    // holder.videoVV.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM)
                                    holder.videoVV.player.seekTo(0)
                                    holder.videoVV.player.playWhenReady = true
                                } else if (holder is SwipeStackItemAdapter.MainSearchType2) {
                                    // holder.videoVV.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM)
                                    holder.videoVV.player.seekTo(0)
                                    holder.videoVV.player.playWhenReady = true
                                } else if (holder is SwipeStackItemAdapter.MainSearchType3) {
                                    // holder.videoVV.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM)
                                    holder.videoVV.player.seekTo(0)
                                    holder.videoVV.player.playWhenReady = true
                                }
                            }

                            activity.rightBottomAngle = 0f
                        }
                    }
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

    fun showLikePassByProgress(yView:View, progress:Float) {

        val recyclerView = yView.my_recycler_view

        val screenWidth = Utils.getScreenWidth(context)
        val position = (overallXScroll / screenWidth)

        val view = recyclerView.findViewHolderForAdapterPosition(position) ?:return

        when (view.itemViewType) {
            0 -> {
                val holder = view as SwipeStackItemAdapter.MainSearchType1

                when {
                    progress < 0 -> {
                        holder.likeIV.alpha = Math.abs(progress) * 3
                        holder.passIV.alpha = 0.0f
                    }
                    progress > 0 -> {
                        holder.likeIV.alpha = 0.0f
                        holder.passIV.alpha = Math.abs(progress) * 3
                    }
                    else -> {
                        holder.likeIV.alpha = 0.0f
                        holder.passIV.alpha = 0.0f
                    }
                }

            }
            1 -> {
                val holder = view as SwipeStackItemAdapter.MainSearchType10

                when {
                    progress < 0 -> {
                        holder.likeIV.alpha = Math.abs(progress) * 3
                        holder.passIV.alpha = 0.0f
                    }
                    progress > 0 -> {
                        holder.likeIV.alpha = 0.0f
                        holder.passIV.alpha = Math.abs(progress) * 3
                    }
                    else -> {
                        holder.likeIV.alpha = 0.0f
                        holder.passIV.alpha = 0.0f
                    }
                }

            }
            2 -> {
                val holder = view as SwipeStackItemAdapter.MainSearchType2

                when {
                    progress < 0 -> {
                        holder.likeIV.alpha = Math.abs(progress) * 3
                        holder.passIV.alpha = 0.0f
                    }
                    progress > 0 -> {
                        holder.likeIV.alpha = 0.0f
                        holder.passIV.alpha = Math.abs(progress) * 3
                    }
                    else -> {
                        holder.likeIV.alpha = 0.0f
                        holder.passIV.alpha = 0.0f
                    }
                }

            }
            else -> {
                val holder = view as SwipeStackItemAdapter.MainSearchType3

                when {
                    progress < 0 -> {
                        holder.likeIV.alpha = Math.abs(progress) * 3
                        holder.passIV.alpha = 0.0f
                    }
                    progress > 0 -> {
                        holder.likeIV.alpha = 0.0f
                        holder.passIV.alpha = Math.abs(progress) * 3
                    }
                    else -> {
                        holder.likeIV.alpha = 0.0f
                        holder.passIV.alpha = 0.0f
                    }
                }

            }
        }

    }

    fun rotateSuperLike(yView: View, rightBottomAngle: Float) {

        val recyclerView = yView.my_recycler_view

        val screenWidth = Utils.getScreenWidth(context)
        val position = (overallXScroll / screenWidth)

        val view = recyclerView.findViewHolderForAdapterPosition(position) ?:return

        when (view.itemViewType) {
            0 -> {
                val holder = view as SwipeStackItemAdapter.MainSearchType1
                val wingRotateAnimator = ObjectAnimator.ofFloat(
                    holder.charmIV,
                    "rotation", rightBottomAngle, rightBottomAngle + (360 / 5)
                )

                wingRotateAnimator.duration = 500
                wingRotateAnimator.start()
            }
            1 -> {
                val holder = view as SwipeStackItemAdapter.MainSearchType10
                val wingRotateAnimator = ObjectAnimator.ofFloat(
                    holder.charmIV,
                    "rotation", rightBottomAngle, rightBottomAngle + (360 / 5)
                )

                wingRotateAnimator.duration = 500
                wingRotateAnimator.start()

            }
            2 -> {
                val holder = view as SwipeStackItemAdapter.MainSearchType2
                val wingRotateAnimator = ObjectAnimator.ofFloat(
                    holder.charmIV,
                    "rotation", rightBottomAngle, rightBottomAngle + (360 / 5)
                )

                wingRotateAnimator.duration = 500
                wingRotateAnimator.start()

            }
            else -> {
                val holder = view as SwipeStackItemAdapter.MainSearchType3
                val wingRotateAnimator = ObjectAnimator.ofFloat(
                    holder.charmIV,
                    "rotation", rightBottomAngle, rightBottomAngle + (360 / 5)
                )

                wingRotateAnimator.duration = 500
                wingRotateAnimator.start()

            }
        }

    }
}