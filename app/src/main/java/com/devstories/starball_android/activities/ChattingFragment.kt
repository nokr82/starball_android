package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.ChattingAction
import com.devstories.starball_android.adapter.ChattingRoomAdapter
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_chatting.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.facebook.FacebookSdk.getApplicationContext
import com.baoyz.swipemenulistview.SwipeMenuItem
import com.baoyz.swipemenulistview.SwipeMenuCreator
import android.graphics.Color
import android.widget.AbsListView
import com.baoyz.swipemenulistview.SwipeMenuListView
import com.devstories.starball_android.adapter.GroupChattingRoomAdapter


//채팅화면

class ChattingFragment : Fragment() {

    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var header: View
    lateinit var footer: View

    lateinit var plusIV: ImageView
    lateinit var groupLV: ListView
    lateinit var storyLV: ListView

    var member_id = -1

    var page = 1
    var totalPage = 1
    lateinit var GrouproomAdapter: GroupChattingRoomAdapter
    lateinit var roomAdapter: ChattingRoomAdapter
    var roomAdapterData = ArrayList<JSONObject>()
    var GrouproomAdapterData = ArrayList<JSONObject>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)

        return inflater.inflate(R.layout.fragment_chatting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        header = View.inflate(myContext, R.layout.item_chatting_head, null)
//        footer = View.inflate(myContext, R.layout.item_chatting_foot, null)

        plusIV = header.findViewById(R.id.plusIV)
        groupLV = header.findViewById(R.id.groupLV)
//        storyLV = footer.findViewById(R.id.storyLV)

        GrouproomAdapter = GroupChattingRoomAdapter(myContext, R.layout.item_chat_profile, GrouproomAdapterData)
        groupLV.adapter = GrouproomAdapter

        chattingLV.addHeaderView(header)
//        chattingLV.addHeaderView(footer)

        plusIV.setOnClickListener {
            val intent = Intent(context, GrouptMakeActivity::class.java)
            startActivity(intent)
        }


        roomAdapter = ChattingRoomAdapter(myContext, R.layout.item_chat_profile, roomAdapterData)
        chattingLV.adapter = roomAdapter

        val creator = SwipeMenuCreator { menu ->
            // create "open" item
            val deleteItem = SwipeMenuItem(
                getApplicationContext()
            )
            // set item background
            deleteItem.setBackground(R.drawable.background_e6e6e6)
            // set item width
            deleteItem.width = Utils.dpToPx(70f).toInt()
            // set item title
            deleteItem.title = getString(R.string.delete)
            // set item title fontsize
            deleteItem.titleSize = 9
            // set item title font color
            deleteItem.titleColor = Color.parseColor("#22313f")
            deleteItem.setIcon(R.mipmap.lounge_del)
            // add to menu
            menu.addMenuItem(deleteItem)

            // create "delete" item
            val pinItem = SwipeMenuItem(
                getApplicationContext()
            )
            pinItem.title = getString(R.string.pin)
            pinItem.titleSize = 9
            pinItem.titleColor = Color.parseColor("#FFFFFF")
            // set item background
            pinItem.setBackground(R.drawable.background_gradient_7140b3_923b9f)
            // set item width
            pinItem.width = Utils.dpToPx(70f).toInt()
            // set a icon
            pinItem.setIcon(R.mipmap.lounge_pin)
            // add to menu
            menu.addMenuItem(pinItem)
        }

        // set creator
        chattingLV.setMenuCreator(creator)
        chattingLV.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        member_id = PrefUtils.getIntPreference(myContext, "member_id")

        groupLV.setOnItemClickListener { parent, view, position, id ->

            if (position < 1) {
                return@setOnItemClickListener
            }

            val json = GrouproomAdapterData[position - 1]

            val Group = json.getJSONObject("Group")

            var intent = Intent(context, GroupChattingActivity::class.java)
            intent.putExtra("room_id", Utils.getInt(Group, "id"))
            startActivity(intent)

            /*  val lastChatting = json.getJSONObject("LastChatting")
             lastChatting.put("read_yn", "Y")*/

            roomAdapter.notifyDataSetChanged()

        }


        chattingLV.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(p0: AbsListView?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onScrollStateChanged(absListView: AbsListView, newState: Int) {
                if (!chattingLV.canScrollVertically(-1)) {

                } else if (!chattingLV.canScrollVertically(1)) {
                    if (totalPage > page) {
                        page++
                        loadData()
                    }
                } else {
                }
            }
        })
        chattingLV.setOnItemClickListener { parent, view, position, id ->

            if (position < 1) {
                return@setOnItemClickListener
            }

            val json = roomAdapterData[position - 1]

            val room = json.getJSONObject("Room")

            var intent = Intent(context, FriendChattingActivity::class.java)
            intent.putExtra("room_id", Utils.getInt(room, "id"))
            startActivity(intent)

            val lastChatting = json.getJSONObject("LastChatting")
            lastChatting.put("read_yn", "Y")

            roomAdapter.notifyDataSetChanged()

        }
        loadGroupData()
        loadData()

    }


    fun loadGroupData() {

        val params = RequestParams()
        params.put("member_id", member_id)

        ChattingAction.group(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        val list = response.getJSONArray("list")

                        for (i in 0 until list.length()) {
                            GrouproomAdapterData.add(list[i] as JSONObject)
                        }

                        GrouproomAdapter.notifyDataSetChanged()

                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(myContext, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseString: String?,
                throwable: Throwable
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

//                 System.out.println(responseString);

                throwable.printStackTrace()
                error()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                throwable: Throwable,
                errorResponse: JSONObject?
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                throwable.printStackTrace()
                error()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                throwable: Throwable,
                errorResponse: JSONArray?
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                throwable.printStackTrace()
                error()
            }

            override fun onStart() {
                // show dialog
                if (progressDialog != null) {
                    progressDialog!!.show()
                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }

    fun loadData() {

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("page", page)

        ChattingAction.index(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        page = Utils.getInt(response, "page")
                        totalPage = Utils.getInt(response, "totalPage")

                        if (page == 1) {
                            roomAdapterData.clear()
                            roomAdapter.notifyDataSetChanged()
                        }

                        val chat = response.getJSONArray("chat")

                        for (i in 0 until chat.length()) {
                            roomAdapterData.add(chat[i] as JSONObject)
                        }

                        roomAdapter.notifyDataSetChanged()

                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(myContext, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseString: String?,
                throwable: Throwable
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

//                 System.out.println(responseString);

                throwable.printStackTrace()
                error()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                throwable: Throwable,
                errorResponse: JSONObject?
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                throwable.printStackTrace()
                error()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                throwable: Throwable,
                errorResponse: JSONArray?
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                throwable.printStackTrace()
                error()
            }

            override fun onStart() {
                // show dialog
                if (progressDialog != null) {
                    progressDialog!!.show()
                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}
