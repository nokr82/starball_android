package com.devstories.starball_android.activities

import android.Manifest
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.*
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.ChattingAction
import com.devstories.starball_android.adapter.GroupAdverbAdapter
import com.devstories.starball_android.adapter.GroupChattingAdapter
import com.devstories.starball_android.base.Config
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.nostra13.universalimageloader.core.ImageLoader
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_group_chatting.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
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

    var member_count = -1

    var translation_yn = ""

    lateinit var adapter: GroupChattingAdapter
    var adapterData = ArrayList<JSONObject>()

    lateinit var adverbAdapter: GroupAdverbAdapter
    var adverbAdapterData = ArrayList<JSONObject>()

    internal var loadDataHandler: Handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            group_chatting()
        }
    }

    private var timer: Timer? = null


    private val EDIT_CHATTING = 300

    private val FROM_ALBUM = 101
    private val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 2
    private var selectedImage: Bitmap? = null

    internal var timerHandler: Handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            for (i in adapterData.indices) {

                val json = adapterData.get(i)

                try {
                    json.put("time", json.getInt("time") - 1)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            this.sendEmptyMessageDelayed(0, 1000)

            adapter.notifyDataSetChanged()
        }
    }


    private var recorder: MediaRecorder = MediaRecorder()

    private var record = false
    var record_path = ""
    var isPlaying = false
    var length = -1
    var chatting_id = -1

    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chatting)
        this.context = this
        progressDialog = ProgressDialog(context)



        member_id = PrefUtils.getIntPreference(context, "member_id")
//        member_list =  intent.getStringExtra("member_list")
        room_id = intent.getIntExtra("room_id", -1)


        adverbRV.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        adverbAdapter = GroupAdverbAdapter(adverbAdapterData,this)
        adverbRV.adapter = adverbAdapter

        adapter = GroupChattingAdapter(context, R.layout.item_group_chatting, adapterData, this)
        groupLV.adapter = adapter
        groupLV.setOnScrollListener(this)


        groupLV.setOnItemLongClickListener { parent, view, position, id ->

            val data = adapterData[position]
            val chatting = data.getJSONObject("GroupChatting")
            val chatting_member_id = Utils.getInt(chatting, "member_id")

            if (Utils.getInt(chatting, "type") != 1) {
                return@setOnItemLongClickListener true
            }

            var delete = false
            if (chatting_member_id == member_id) {
                delete = true
            }

            var intent = Intent(context, DlgChattingActivity::class.java)
            intent.putExtra("chatting_id", Utils.getInt(chatting, "id"))
            intent.putExtra("chatting_contents", Utils.getString(chatting, "contents"))
            intent.putExtra("delete", delete)
            startActivityForResult(intent, EDIT_CHATTING)
            return@setOnItemLongClickListener true
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

        globalIV.setOnClickListener {
            translation_yn = if (translation_yn == "Y") "N" else "Y"

            editRoom()
        }

        backIV.setOnClickListener {
            finish()
        }

        sendLL.setOnClickListener {

            val contents = Utils.getString(contentsET)

            if ("" == contents) {
                return@setOnClickListener
            }

            sendChatting(1)

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

            addAdverb(adverb)

        }

        detail()
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

                            sendChatting(2)

                        }
                    }
                }
                EDIT_CHATTING -> {
                    if (data != null) {

                        val type = data.getIntExtra("type", 1)
                        val chatting_id = data.getIntExtra("chatting_id", -1)
                        val chatting_contents= data.getStringExtra("chatting_contents")

                        if (type == 1) {
                            val clipboardManager: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            var clipData: ClipData = ClipData.newPlainText("label", chatting_contents)
                            clipboardManager.setPrimaryClip(clipData);

                            Toast.makeText(context, context.getString(R.string.clipboard), Toast.LENGTH_SHORT).show();
                        } else if (type == 2) {
                            deleteChatting(chatting_id)
                        } else if (type == 3) {
                            contentsET.setText(chatting_contents)
                        } else {
                            addAdverb(chatting_contents)
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

                        adverbAdapter.notifyDataSetChanged()
                    }

                }
            }
        }
    }


    fun deleteChatting(chatting_id: Int) {

        val params = RequestParams()
        params.put("chatting_id", chatting_id)

        ChattingAction.del_chatting(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        for (i in 0 until adapterData.size) {
                            val data = adapterData[i]
                            val chatting = data.getJSONObject("GroupChatting")

                            val chat_id = Utils.getInt(chatting, "id")

                            if (chatting_id == chat_id) {
                                adapterData.removeAt(i)
                                break
                            }
                        }

                        adapter.notifyDataSetChanged()

                    } else {

                    }

                    adverbAdapter.notifyDataSetChanged()

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
    fun sendChatting(type: Int) {

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("group_id", room_id)
        params.put("type", type)
        params.put("chatting_type", 2)
        params.put("contents", Utils.getString(contentsET))

        if (type == 2) {
            if (selectedImage != null) {
                val selectedImg = ByteArrayInputStream(Utils.getByteArray(selectedImage))
                params.put("upload", selectedImg)
            }
        }


        if (type == 3) {
            if (record_path != "" && record_path != null) {
                val file = File(record_path)
                val player = MediaPlayer()

                val fis = FileInputStream(file)
                val `is` = ByteArrayInputStream(Utils.getByteArray(fis))
                params.put("voice", `is`)

                player.setDataSource(record_path)
                player.prepare()

                var millis = player.duration

                var minutes = ( millis % (1000*60*60) ) / (1000*60);
                var seconds = ( ( millis % (1000*60*60) ) % (1000*60) ) / 1000;

                params.put("voice_time", minutes.toString() + ":" + seconds.toString())

            }
        }

        ChattingAction.send_chatting(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                Log.d("채팅", response.toString())
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
    }

    fun timerStart() {
        val task = object : TimerTask() {
            override fun run() {
                loadDataHandler.sendEmptyMessage(0)
            }
        }

        timer = Timer()
        timer!!.schedule(task, 0, 2000)

        timerHandler.sendEmptyMessage(0)

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

    fun addAdverb(content: String) {

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("content", content)

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

    fun detail() {

        val params = RequestParams()
        params.put("group_id", room_id)

        ChattingAction.group_detail(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        val list = response.getJSONArray("GroupMembers")
                        val Group = response.getJSONObject("Group")
                        val title = Utils.getString(Group, "title")
                        titleTV.text = title
                        member_count = list.length()
                        countTV.text = member_count.toString()
                        groupmemberLL.removeAllViews()

                        for (i in 0 until list.length()) {
                            val data = list.get(i) as JSONObject
                            val GroupMember = data.getJSONObject("GroupMember")
                            val Member = data.getJSONObject("Member")
                            var Profile = data.getJSONObject("Profile")
                            val name = Utils.getString(Member, "name")
                            val group_member_id = Utils.getInt(GroupMember, "id")
                            val image_uri = Utils.getString(Profile, "image_uri")

                            val userView = View.inflate(context, R.layout.item_group_member, null)
                            var profileIV: ImageView = userView.findViewById(R.id.profileIV)
                            var nameTV: TextView = userView.findViewById(R.id.nameTV)
                            var delIV: ImageView = userView.findViewById(R.id.delIV)
                            delIV.setOnClickListener {
                                del_group_member(group_member_id)
                            }

                            ImageLoader.getInstance()
                                .displayImage(Config.url + image_uri, profileIV, Utils.UILoptionsProfile)
                            nameTV.text = name
                            groupmemberLL.addView(userView)

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
    }


    fun del_group_member(member_id: Int) {

        val params = RequestParams()
        params.put("group_member_id", member_id)

        ChattingAction.del_group_member(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        detail()
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

    fun editRoom() {

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
    }

    fun cancel_group_chatting(chatting_id: Int) {

        val params = RequestParams()
        params.put("group_chatting_id", chatting_id)

        ChattingAction.cancel_group_chatting(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
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
    }


    fun group_chatting() {

        if (first_id < 1) {
            if (adapterData.size > 0) {
                try {
                    try {
                        val lastMSG = adapterData.get(adapterData.size - 1)
                        val chatting = lastMSG.getJSONObject("GroupChatting")
                        last_id = Utils.getInt(chatting, "id")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                } catch (e: NumberFormatException) {

                }

            }
        }
        val params = RequestParams()
        params.put("group_id", room_id)
        params.put("first_id", first_id)
        params.put("last_id", last_id)

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
                            val chatting = data.getJSONObject("GroupChatting")
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

    override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        lastItemVisibleFlag = totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount

        if (firstVisibleItem == 0 && firstVisibleItem + visibleItemCount < totalItemCount) {
            if (adapterData.size > 0) {
                try {
                    val firstMSG = adapterData[0]
                    val chatting = firstMSG.getJSONObject("GroupChatting")
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
                        val chatting = lastMSG.getJSONObject("GroupChatting")
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
                    val chatting = lastMSG.getJSONObject("GroupChatting")
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
                        val chatting = lastMSG.getJSONObject("GroupChatting")
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


    fun playing(recordPath: String, chatting_id: Int) {

        if (this.chatting_id != chatting_id) {

            if (player != null) {
                player!!.release()
            }

            isPlaying = false
        }

        if (isPlaying == false) {

            try {

                if (player != null) {
                    player!!.release()
                }
                player = MediaPlayer()

                player!!.setDataSource(recordPath)
                player!!.prepare()
                player!!.start()

                player!!.setOnCompletionListener(MediaPlayer.OnCompletionListener {
                    isPlaying = false
                })

            } catch (e: IOException) {
                e.printStackTrace()
            }

            isPlaying = true
        } else if (length > 0) {

            if (length > player!!.getDuration()) {
                length = 0
            }
            player!!.seekTo(length)
            player!!.start()
            length = -1
        } else {
            playingPause()
        }
    }

    private fun playingPause() {
        if (isPlaying) {
            player!!.pause()
            length = player!!.getCurrentPosition()
        }
    }

    private fun playStop() {
        if (isPlaying) {
            player!!.stop()
        }
    }

    fun recordStop(){

        recorder.stop()

        sendChatting(3)

    }

    private fun loadPermissions(perms: Array<String>, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, perms[0]) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this,perms[1]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, perms, requestCode)
        } else {
            // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
            // 녹음 시작
            record_start()
        }
    }

    fun record_start() {

        try {

            val date = Date()
            val time = date.time

            record_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/starball/"

            val file = File(record_path)

            if (!file.exists()) {
                file.mkdirs()
            }

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)

            //첫번째로 어떤 것으로 녹음할것인가를 설정한다. 마이크로 녹음을 할것이기에 MIC로 설정한다.

            recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)

            //이것은 파일타입을 설정한다. 녹음파일의경우 3gp로해야 용량도 작고 효율적인 녹음기를 개발할 수있다.

            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

            //이것은 코덱을 설정하는 것이라고 생각하면된다.
            record_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/starball/" + time + "_record.aac"

            recorder.setOutputFile(record_path)

            //저장될 파일을 저장한뒤

            recorder.prepare()

            recorder.start()

            //시작하면된다.
            Toast.makeText(context, "녹음을 시작합니다.", Toast.LENGTH_LONG).show();

            voiceLL.setBackgroundColor(Color.parseColor("#333333"))

            record = true

            try {
                recorder.prepare()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }



    override fun onDestroy() {
        super.onDestroy()

        if (timer != null) {
            timer!!.cancel()
        }
    }
}
