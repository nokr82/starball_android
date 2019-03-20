package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import android.util.Log
import android.view.MenuItem
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.Toast
import com.baoyz.swipemenulistview.SwipeMenu
import com.baoyz.swipemenulistview.SwipeMenuListView
import com.devstories.starball_android.utils.Coomon


//채팅화면

class ChattingFragment : Fragment() {

    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null


    var member_id = -1
    lateinit var header: View
    var page = 1
    var totalPage = 1

    var pin_yn = "N"
    var del_yn = "N"
    var room_id = -1

    lateinit var roomAdapter: ChattingRoomAdapter
    var roomAdapterData = ArrayList<JSONObject>()
    var GrouproomAdapterData = ArrayList<JSONObject>()
    lateinit var plusIV: ImageView

    internal var chattingReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {

                var room_id = intent.getIntExtra("room_id", -1)

                if (room_id > 0) {

                    var has = false

                    var contents = intent.getStringExtra("contents")
                    var member_id = intent.getIntExtra("member_id", -1)
                    val created_at = intent.getStringExtra("created_at")

                    var type2_position = -1

                    for (i in 0 until roomAdapterData.size) {
                        val data = roomAdapterData[i]

                        val type = Utils.getInt(data, "type")

                        if (type == 2) {

                            var pin_yn = Utils.getString(data, "pin_yn")

                            val room = data.getJSONObject("Room")
                            val Chatting = data.getJSONObject("LastChatting")

                            val id = Utils.getInt(room, "id")

                            if (type2_position < 1 && pin_yn == "N") {
                                type2_position = i
                            }

                            if (id == room_id) {
                                has = true

                                Chatting.put("contents", contents)
                                Chatting.put("created", created_at)

                                if (member_id > 0) {
                                    Chatting.put("member_id", member_id)
                                }

                                if (type2_position > 0) {
                                    roomAdapterData.removeAt(i)
                                    roomAdapterData.add(type2_position, data)
                                }

                                break
                            }

                        }

                    }

                    roomAdapter.notifyDataSetChanged()

                    if (!has) {
                        loadAddNewRoom(room_id)
                    }

                }

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)

        return inflater.inflate(R.layout.fragment_chatting, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        header = View.inflate(myContext, R.layout.item_chatting_head, null)
        plusIV = header.findViewById(R.id.plusIV)

        var filter = IntentFilter("PUSH_CHATTING")
        myContext.registerReceiver(chattingReceiver, filter)

        roomAdapter = ChattingRoomAdapter(myContext, R.layout.item_chat_profile, roomAdapterData, 1)
        chattingLV.adapter = roomAdapter
        chattingLV.addHeaderView(header)

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
        chattingLV.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        member_id = PrefUtils.getIntPreference(myContext, "member_id")

        plusIV.setOnClickListener {
            val intent = Intent(context, GrouptMakeActivity::class.java)
            startActivity(intent)
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

        chattingLV.setOnMenuItemClickListener(object : SwipeMenuListView.OnMenuItemClickListener {

            override fun onMenuItemClick(position: Int, menu: SwipeMenu, index: Int): Boolean {

                var json = roomAdapterData[position]
                val type = Utils.getInt(json, "type")
                when (index) {
                    0 -> {
                        if (type == 1) {
                            val Group = json.getJSONObject("Group")
                            val group_id = Utils.getInt(Group, "id")
                            del_yn="Y"
                            editGroupRoom(group_id)
                        } else if (type == 2) {
                            val Room = json.getJSONObject("Room")
                            val room_id = Utils.getInt(Room, "id")
                            del_yn="Y"
                            editRoom(room_id)
                        }
                        roomAdapterData.removeAt(position)
                        roomAdapter.notifyDataSetChanged()

                    }
                    1 -> {
                        if (type == 1) {
                            val Group = json.getJSONObject("Group")
                            val group_id = Utils.getInt(Group, "id")
                            pin_yn = Utils.getString(Group, "pin_yn")
                            if (pin_yn.equals("Y")){
                                pin_yn="N"
                                Group.put("pin_yn", pin_yn)
                            }else{
                                pin_yn="Y"
                                Group.put("pin_yn", pin_yn)
                            }
                            editGroupRoom(group_id)

                        } else if (type == 2) {
                             pin_yn = Utils.getString(json, "pin_yn")
                            val Room = json.getJSONObject("Room")
                            val room_id = Utils.getInt(Room, "id")
                            if (pin_yn.equals("Y")){
                                pin_yn="N"
                                json.put("pin_yn", pin_yn)
                            }else{
                                pin_yn="Y"
                                json.put("pin_yn", pin_yn)
                            }
                            editRoom(room_id)
                        }



                    }
                }
                return false
            }
        })

        chattingLV.setOnItemClickListener { parent, view, position, id ->

            if (position < 1) {
                return@setOnItemClickListener
            }

            var json = roomAdapterData[position - 1]
            val type = Utils.getInt(json, "type")

            if (type == 1) {
                val Group = json.getJSONObject("Group")

                var intent = Intent(context, GroupChattingActivity::class.java)
                intent.putExtra("room_id", Utils.getInt(Group, "id"))
                startActivity(intent)

                roomAdapter.notifyDataSetChanged()

            } else if (type == 2) {
                val room = json.getJSONObject("Room")

                var intent = Intent(context, FriendChattingActivity::class.java)
                intent.putExtra("room_id", Utils.getInt(room, "id"))
                startActivity(intent)

                val lastChatting = json.getJSONObject("LastChatting")
                lastChatting.put("read_yn", "Y")
                roomAdapter.notifyDataSetChanged()

            }

        }

        loadGroupData()

    }

    fun loadAddNewRoom(room_id: Int) {

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("room_id", room_id)

        ChattingAction.load_add_new_chatting(params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                val result = response!!.getString("result")

                if (result == "ok") {
                    val room = response.getJSONObject("room")

                    var type2_position = -1

                    for (i in 0 until roomAdapterData.size) {
                        val data = roomAdapterData[i]
                        val type = Utils.getInt(data,"type")

                        val pin_yn = Utils.getString(data, "pin_yn")

                        if (type == 2 && pin_yn == "N") {
                            type2_position = i
                            break
                        }
                    }

                    roomAdapterData.add(type2_position, room)
                    roomAdapter.notifyDataSetChanged()

                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                println(responseString)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                println(errorResponse)
            }
        })
    }

    fun editGroupRoom(room_id: Int) {

        if (room_id < 1) {
            return
        }
        val params = RequestParams()
        params.put("group_id", room_id)
        params.put("pin_yn", pin_yn)
        params.put("del_yn", del_yn)

        ChattingAction.edit_group(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        del_yn = "N"
                        roomAdapter.notifyDataSetChanged()
                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                // System.out.println(responseString);
            }

            private fun error() {
                // Utils.alert(context, "조회중 장애가 발생하였습니다.")
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

                // System.out.println(responseString);

                throwable.printStackTrace()
                error()
            }


            override fun onStart() {
                // show dialog
//                if (progressDialog != null) {
//                    progressDialog!!.show()
//                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }

    fun editRoom(room_id: Int) {

        if (room_id < 1) {
            return
        }
        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("room_id", room_id)
        params.put("pin_yn", pin_yn)
        params.put("del_yn", del_yn)

        ChattingAction.edit_room(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        del_yn = "N"
                        roomAdapter.notifyDataSetChanged()
                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                // System.out.println(responseString);
            }

            private fun error() {
                // Utils.alert(context, "조회중 장애가 발생하였습니다.")
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

                // System.out.println(responseString);

                throwable.printStackTrace()
                error()
            }


            override fun onStart() {
                // show dialog
//                if (progressDialog != null) {
//                    progressDialog!!.show()
//                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
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
                        if (page == 1) {
                            roomAdapterData.clear()
                            roomAdapter.notifyDataSetChanged()
                        }
                        val list = response.getJSONArray("list")

                        for (i in 0 until list.length()) {
//                            GrouproomAdapterData.add(list[i] as JSONObject)

                            val json = list[i] as JSONObject
                            json.put("type", 1)

                            roomAdapterData.add(json)
                        }


                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                loadData()

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

                        val chat = response.getJSONArray("chat")
                        val data = chat[0] as JSONObject
                        data.put("title", "chatting_title")

                        val json = JSONObject()
                        json.put("type", 3)
                        roomAdapterData.add(json)

                        for (i in 0 until chat.length()) {
                            val data = chat[i] as JSONObject
                            data.put("type", 2)

                            roomAdapterData.add(data)
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

        if (chattingReceiver != null) {
            myContext.unregisterReceiver(chattingReceiver)
        }

    }
}


