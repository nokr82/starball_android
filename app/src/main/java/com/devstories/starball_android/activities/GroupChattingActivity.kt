package com.devstories.starball_android.activities

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.AbsListView
import android.widget.BaseAdapter
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.ChattingAction
import com.devstories.starball_android.adapter.AdverbAdapter
import com.devstories.starball_android.adapter.GroupChattingAdapter
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_group_chatting.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class GroupChattingActivity : RootActivity(), AbsListView.OnScrollListener {
    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    val DELETE_ADBERB = 100

    private var userScrolled: Boolean = false
    private var lastItemVisibleFlag: Boolean = false

    var member_id = -1
    var other_member_id = -1
    var room_id = -1

    var first_id = -1
    var last_id = -1

    var translation_yn = ""

    lateinit var adapter: GroupChattingAdapter
    var adapterData = ArrayList<JSONObject>()

//    lateinit var adverbAdapter: AdverbAdapter
    var adverbAdapterData = ArrayList<JSONObject>()

    internal var loadDataHandler: Handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            group_chatting()
        }
    }

    private var timer: Timer? = null

    private val FROM_ALBUM = 101
    private val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 2
    private var selectedImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chatting)
        this.context = this
        progressDialog = ProgressDialog(context)

        member_id = PrefUtils.getIntPreference(context, "member_id")
//        member_list =  intent.getStringExtra("member_list")
        room_id = intent.getIntExtra("room_id",-1)

        adapter = GroupChattingAdapter(context, R.layout.item_chatting, adapterData, this)
        groupLV.adapter = adapter
        groupLV.setOnScrollListener(this)
        groupLV.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, DlgProposeActivity::class.java)
            startActivity(intent)
        }

        starballIV.setOnClickListener {
            val intent = Intent(context, DlgSendProposeActivity::class.java)
            startActivity(intent)
        }

        reportIV.setOnClickListener {
            val intent = Intent(context, ReportActivity::class.java)
            intent.putExtra("report_member_id", other_member_id)
            startActivity(intent)
        }

        globalIV.setOnClickListener {
            translation_yn = if (translation_yn == "Y") "N" else "Y"

//            editRoom()
        }

        plusLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                emoLL.visibility = View.VISIBLE
            } else {
                emoLL.visibility = View.GONE
            }
        }

        languageIV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                languageIV.setImageResource(R.mipmap.bubble_on)
                languageLL.visibility = View.VISIBLE
            } else {
                languageIV.setImageResource(R.mipmap.bubble)
                languageLL.visibility = View.GONE
            }
        }

        backIV.setOnClickListener {
            finish()
        }

        sendLL.setOnClickListener {

            val contents = Utils.getString(contentsET)

            if ("" == contents) {
                return@setOnClickListener
            }

//            sendChatting(1)

        }

        sendImageIV.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                loadPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE)
            } else {
                imageFromGallery()
            }
        }

        addAdverbLL.setOnClickListener {
            val adverb = Utils.getString(adverbET)

            if (adverb == "") {
                return@setOnClickListener
            }

            addAdverb()

        }

//        detail()
        timerStart()
        adverb()

        backIV.setOnClickListener {
            finish()
        }


    }

    private fun loadPermissions(perm: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, perm) !== PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(perm), requestCode)
        } else {
            imageFromGallery()
        }
    }

    private fun imageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, FROM_ALBUM)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_PERMISSION_READ_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imageFromGallery()
                } else {
                    // no granted
                }
                return
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                FROM_ALBUM -> {
                    if (data != null && data.data != null) {
                        val selectedImageUri = data.data

                        val filePathColumn = arrayOf(MediaStore.MediaColumns.DATA)

                        val cursor =
                            context!!.contentResolver.query(selectedImageUri!!, filePathColumn, null, null, null)
                        if (cursor!!.moveToFirst()) {
                            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                            val picturePath = cursor.getString(columnIndex)

                            cursor.close()

                            selectedImage = Utils.getImage(context!!.contentResolver, picturePath)

//                            sendChatting(2)

                        }
                    }
                }

                DELETE_ADBERB -> {

                    if (data != null) {
                        var adverb_id = data.getIntExtra("adverb_id", -1)

                        for (j in 0 until adverbAdapterData.size) {

                            println("j::::::::::::::::::::::::::::::::j")

                            var adverb = adverbAdapterData[j]
                            val id = Utils.getInt(adverb, "id")

                            if (adverb_id == id) {
                                adverbAdapterData.removeAt(j)
                                break
                            }

                        }

//                        adverbAdapter.notifyDataSetChanged()
                    }

                }
            }
        }
    }

    fun timerStart() {
        val task = object : TimerTask() {
            override fun run() {
                loadDataHandler.sendEmptyMessage(0)
            }
        }

        timer = Timer()
        timer!!.schedule(task, 0, 2000)

    }

    fun adverb() {

        val params = RequestParams()
        params.put("member_id", member_id)

        ChattingAction.adverb(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    adverbAdapterData.clear()

                    if ("ok" == result) {

                        val adverbs = response.getJSONArray("adverb")

                        for (i in 0 until adverbs.length()) {
                            adverbAdapterData.add(adverbs[i] as JSONObject)
                        }

                    } else {

                    }

//                    adverbAdapter.notifyDataSetChanged()

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

    fun addAdverb() {

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("content", Utils.getString(adverbET))

        ChattingAction.add_adverb(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        adverbET.setText("")

                        val adverb = response.getJSONObject("adverb")
                        adverbAdapterData.add(0, adverb)
//                        adverbAdapter.notifyDataSetChanged()

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

 /*   fun chattingLike(chatting_id: Int, like_yn: String) {

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("chatting_id", chatting_id)
        params.put("like_yn", like_yn)

        ChattingAction.like(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        if (like_yn == "Y") {
                            val intent = Intent(context, LikedNotiActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(0, 0)
                        }

                        for (i in 0 until adapterData.size) {
                            val data = adapterData[i]
                            val chatting = data.getJSONObject("Chatting")
                            val id = Utils.getInt(chatting, "id")

                            if (id == chatting_id) {
                                chatting.put("like_yn", like_yn)
                                break
                            }
                        }

                        adapter.notifyDataSetChanged()

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
    }*/

  /*  fun detail() {

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("room_id", room_id)

        ChattingAction.detail(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        val room = response.getJSONObject("Room")

                        val founderMemberObj = response.getJSONObject("founderMember")
                        val founderMember = founderMemberObj.getJSONObject("Member")
                        val founderProfile = founderMemberObj.getJSONObject("Profile")

                        val attendMemberObj = response.getJSONObject("attendMember")
                        val attendMember = attendMemberObj.getJSONObject("Member")
                        val attendProfile = attendMemberObj.getJSONObject("Profile")

                        val founder_member_id = Utils.getInt(room, "founder_member_id")
                        val attend_member_id = Utils.getInt(room, "attend_member_id")

                        var name = Utils.getString(founderMember, "name")
                        var birth = Utils.getString(founderMember, "birth")

                        if (member_id == founder_member_id) {
                            name = Utils.getString(attendMember, "name")
                            birth = Utils.getString(attendMember, "birth")

                            translation_yn = Utils.getString(room, "founder_translation_yn")
                            other_member_id = attend_member_id
                        } else {
                            translation_yn = Utils.getString(room, "attend_translation_yn")
                            other_member_id = founder_member_id
                        }

                        val births = birth.split("-")
                        var age = 0

                        if (births.count() == 3) {

                            var now = System.currentTimeMillis()
                            var date = Date(now)
                            val sdfNow = SimpleDateFormat("yyyy")
                            val year = sdfNow.format(date)

                            age = year.toInt() - births[0].toInt()
                        }

                        titleTV.text = name + " " + age

                        if (translation_yn == "Y") {
                            globalIV.setImageResource(R.mipmap.global_on)
                        } else {
                            globalIV.setImageResource(R.mipmap.global)
                        }

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
    }*/

   /* fun editRoom() {

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("room_id", room_id)
        params.put("translation_yn", translation_yn)

        ChattingAction.edit_room(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        if (translation_yn == "Y") {
                            globalIV.setImageResource(R.mipmap.global_on)
                        } else {
                            globalIV.setImageResource(R.mipmap.global)
                        }

                        adapter.notifyDataSetChanged()

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
    }*/

    fun group_chatting() {

        val params = RequestParams()
        params.put("group_id",room_id)


        ChattingAction.group_chatting(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        val list = response.getJSONArray("chattings")

                        if (first_id > 0) {
                            for (i in 0 until list.length()) {
                                val data = list.get(i) as JSONObject
                                adapterData.add(0, data)
                            }

                        } else {
                            for (i in 0 until list.length()) {

                                val data = list.get(i) as JSONObject
                                adapterData.add(data)

                                groupLV.setSelection(adapter.count - 1)
                            }
                        }

                        if (adapterData.size > 0) {
                            val data = adapterData[adapterData.size - 1]
                            val chatting = data.getJSONObject("Chatting")
                            last_id = Utils.getInt(chatting, "id")
                        }

                        if (list.length() > 0) {
                            (adapter as BaseAdapter).notifyDataSetChanged()
                        }

                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

//                val listViewHeight = Utils.getListViewHeightBasedOnItems(chatLV)
//
//                if (chatLV.height < listViewHeight) {
//                    chatLV.transcriptMode = AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL
//                } else {
//                    chatLV.transcriptMode = AbsListView.TRANSCRIPT_MODE_NORMAL
//                }
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

    /*fun sendChatting(type: Int) {

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("room_id", room_id)
        params.put("type", type)
        params.put("contents", Utils.getString(contentsET))

        if (type == 2) {
            if (selectedImage != null) {
                val selectedImg = ByteArrayInputStream(Utils.getByteArray(selectedImage))
                params.put("upload", selectedImg)
            }
        }

        ChattingAction.send_chatting(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    selectedImage = null
                    contentsET.setText("")

                    if ("ok" == result) {

                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

//                val listViewHeight = Utils.getListViewHeightBasedOnItems(chatLV)
//
//                if (chatLV.height < listViewHeight) {
//                    chatLV.transcriptMode = AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL
//                } else {
//                    chatLV.transcriptMode = AbsListView.TRANSCRIPT_MODE_NORMAL
//                }
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
    }*/

    override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        lastItemVisibleFlag = totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount

        if (firstVisibleItem == 0 && firstVisibleItem + visibleItemCount < totalItemCount) {
            if (adapterData.size > 0) {
                try {
                    val firstMSG = adapterData[0]
                    val chatting = firstMSG.getJSONObject("Chatting")
                    first_id = Utils.getInt(chatting, "id")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                last_id = -1
            } else {
                first_id = -1
                if (adapterData.size > 0) {
                    try {
                        val lastMSG = adapterData[adapterData.size - 1]
                        val chatting = lastMSG.getJSONObject("Chatting")
                        last_id = Utils.getInt(chatting, "id")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                } else {
                    last_id = -1
                }

            }
        } else {
            first_id = -1
            if (adapterData.size > 0) {
                try {
                    val lastMSG = adapterData[adapterData.size - 1]
                    val chatting = lastMSG.getJSONObject("Chatting")
                    last_id = Utils.getInt(chatting, "id")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            } else {
                last_id = -1
            }
        }

    }

    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            userScrolled = true
            if (timer != null) {
                timer!!.cancel()
            }
        } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
            if (timer != null) {
                timer!!.cancel()
            }
        } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (lastItemVisibleFlag) {
                if (adapterData.size > 0) {
                    val lastMSG = adapterData[adapterData.size - 1]
                    first_id = -1
                    try {
                        val chatting = lastMSG.getJSONObject("Chatting")
                        last_id = Utils.getInt(chatting, "id")
                    } catch (e: NumberFormatException) {
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            } else {
                /*
                if (first_id > 0) {
                    loadData();
                }
                */
            }

            if (adapterData.size > 0) {
                if (timer != null) {
                    timer!!.cancel()
                }

                val task = object : TimerTask() {
                    override fun run() {
                        loadDataHandler.sendEmptyMessage(0)
                    }
                }

                timer = Timer()
                timer!!.schedule(task, 1000, 2000)
            }

        } else {
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        if (timer != null) {
            timer!!.cancel()
        }
    }
}
