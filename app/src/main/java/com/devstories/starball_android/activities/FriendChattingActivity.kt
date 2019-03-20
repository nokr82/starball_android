package com.devstories.starball_android.activities

import android.Manifest
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
import com.devstories.starball_android.adapter.AdverbAdapter
import com.devstories.starball_android.adapter.ChattingAdapter
import com.devstories.starball_android.adapter.EmoticonAdapter
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_friend_chatting.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class FriendChattingActivity : RootActivity()
    , AbsListView.OnScrollListener {
    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    val DELETE_ADBERB = 100

    private var userScrolled: Boolean = false
    private var lastItemVisibleFlag: Boolean = false

    var member_id = -1
    var other_member_id = -1
    var other_name = ""
    var other_profile = ""
    var room_id = -1

    var first_id = -1
    var last_id = -1

    var translation_yn = ""


    lateinit var adapter: ChattingAdapter
    var adapterData = ArrayList<JSONObject>()


    lateinit var adverbAdapter: AdverbAdapter
    var adverbAdapterData = ArrayList<JSONObject>()

    var emoticonData2 = arrayOf(
        R.drawable.stic_001_15, R.drawable.stic_001_16, R.drawable.stic_001_17, R.drawable.stic_001_18, R.drawable.stic_001_19,
        R.drawable.stic_001_20, R.drawable.stic_001_21, R.drawable.stic_001_22, R.drawable.stic_001_23,
        R.drawable.stic_002_07, R.drawable.stic_002_08, R.drawable.stic_002_09,
        R.drawable.stic_002_10, R.drawable.stic_002_11, R.drawable.stic_002_12,
        R.drawable.stic_003_07, R.drawable.stic_003_08, R.drawable.stic_003_09,
        R.drawable.stic_003_10, R.drawable.stic_003_11, R.drawable.stic_003_12, R.drawable.stic_003_13, R.drawable.stic_003_14,
        R.drawable.stic_004_08, R.drawable.stic_004_09,
        R.drawable.stic_004_10, R.drawable.stic_004_11, R.drawable.stic_004_12, R.drawable.stic_004_13, R.drawable.stic_004_14, R.drawable.stic_004_15, R.drawable.stic_004_16, R.drawable.stic_004_17
    )

    lateinit var emoticonAdapter: EmoticonAdapter

    internal var loadDataHandler: Handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            chatting()
        }
    }

    internal var playerHandler: Handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {

            for (i in 0 until adapterData.size) {

                val data = adapterData[i]
                val chatting = data.getJSONObject("Chatting")

                if (chatting_id == Utils.getInt(chatting, "id")) {
                    val voice_progress = Utils.getInt(chatting, "voice_progress")
                    val voice_duration = Utils.getInt(chatting, "voice_duration")

                    if (voice_progress <= voice_duration) {
                        chatting.put("isPlaying", true)
                        chatting.put("voice_progress",  voice_progress + 1000)
                    } else {
                        chatting.put("isPlaying", false)
                        this.removeMessages(0)
                    }

                } else {
                    chatting.put("isPlaying", false)
                    chatting.put("voice_progress", 0)
                }

            }

            adapter.notifyDataSetChanged()

            this.sendEmptyMessageDelayed(0, 1000)
        }
    }

    private var timer: Timer? = null

    private val EDIT_CHATTING = 300
    private val FROM_ALBUM = 101
    private val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 2

    private var selectedImage: Bitmap? = null
    private var selectedEmoticon: Bitmap? = null

    private var recorder: MediaRecorder = MediaRecorder()

    private var record = false
    var record_path = ""
    var isPlaying = false
    var length = -1
    var chatting_id = -1

    private var player: MediaPlayer? = null

    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100

    var blockdata = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_chatting)

        this.context = this
        progressDialog = ProgressDialog(context)

        blockdata.add("Wechat")
        blockdata.add("kakaotalk")
        blockdata.add("line")
        blockdata.add("whatsapp")
        blockdata.add("zalo")
        blockdata.add("viber")

        member_id = PrefUtils.getIntPreference(context, "member_id")

        room_id = intent.getIntExtra("room_id", -1)

        emoticonAdapter = EmoticonAdapter(context, R.layout.item_emoticon, emoticonData2)
        emoticonGV.adapter = emoticonAdapter
        emoticonGV.setOnItemClickListener { parent, view, position, id ->

            val emoticon = emoticonData2[position]
//            val lid = context.resources.getIdentifier("@drawable/" + emoticon, "drawable", context.packageName)

            selectedEmoticon = BitmapFactory.decodeResource(resources, emoticon)

            sendChatting(4)

        }

        adverbRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adverbAdapter = AdverbAdapter(adverbAdapterData, this)
        adverbRV.adapter = adverbAdapter

        adapter = ChattingAdapter(context, R.layout.item_chatting, adapterData, this)
        listLV.adapter = adapter
        listLV.setOnScrollListener(this)
        listLV.setOnItemLongClickListener { parent, view, position, id ->

            val data = adapterData[position]
            val chatting = data.getJSONObject("Chatting")
            val chatting_member_id = Utils.getInt(chatting, "member_id")

            if (Utils.getInt(chatting, "type") != 1) {
                return@setOnItemLongClickListener true
            }

            var delete = false
            if (chatting_member_id == member_id) {
                delete = true
            }

            recordStop()

            var intent = Intent(context, DlgChattingActivity::class.java)
            intent.putExtra("chatting_id", Utils.getInt(chatting, "id"))
            intent.putExtra("chatting_contents", Utils.getString(chatting, "contents"))
            intent.putExtra("delete", delete)
            startActivityForResult(intent, EDIT_CHATTING)
            return@setOnItemLongClickListener true
        }

        listLV.setOnItemClickListener { parent, view, position, id ->
//            val intent = Intent(context, DlgProposeActivity::class.java)
//            startActivity(intent)
        }

        starballIV.setOnClickListener {

            recordStop()

            val intent = Intent(context, DlgSendProposeActivity::class.java)
            intent.putExtra("propose_member_id", other_member_id)
            intent.putExtra("name", other_name)
            intent.putExtra("profile", other_profile)
            startActivity(intent)
        }

        reportIV.setOnClickListener {

            recordStop()

            val intent = Intent(context, ReportActivity::class.java)
            intent.putExtra("report_member_id", other_member_id)
            startActivity(intent)
        }

        globalIV.setOnClickListener {
            translation_yn = if (translation_yn == "Y") "N" else "Y"

            editRoom()
        }

        plusLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                emoLL.visibility = View.VISIBLE
            } else {
                emoLL.visibility = View.GONE
            }
        }

        adverbBtnLL.setOnClickListener {

            emoticonIV.setImageResource(R.mipmap.emoticon)
            emoticonLL.visibility = View.GONE

            if (adverbLL.visibility == View.GONE) {
                adverbIV.setImageResource(R.mipmap.bubble_on)
                adverbLL.visibility = View.VISIBLE
            } else {
                adverbIV.setImageResource(R.mipmap.bubble)
                adverbLL.visibility = View.GONE
            }
        }

        emoticonBtnLL.setOnClickListener {

            adverbIV.setImageResource(R.mipmap.bubble)
            adverbLL.visibility = View.GONE

            if (emoticonLL.visibility == View.GONE) {
                emoticonIV.setImageResource(R.mipmap.emoticon_on)
                emoticonLL.visibility = View.VISIBLE
            } else {
                emoticonIV.setImageResource(R.mipmap.emoticon)
                emoticonLL.visibility = View.GONE
            }

        }

        backIV.setOnClickListener {
            finish()
        }

        sendLL.setOnClickListener {

            val contents = Utils.getString(contentsET)

            for (i in 0 until blockdata.size) {
                Log.d("사이코",blockdata[i])
                if (contents.contains(blockdata[i])){
                    Toast.makeText(context,"주의:상대방이 다른 메신져를 이용해서 사기 또는 금전을 요구할 가능성이 있으니 신중하십시오.\n" +
                            "타른 수단에서 발생한 피해는 책임지지 않습니다.\n",Toast.LENGTH_SHORT).show()
                }
            }

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

        voiceLL.setOnClickListener {
            if (record) {
                // 녹음 끝
                record = false

                recordStop()

//                voiceLL.setBackgroundColor(Color.parseColor("#00000000"))
                recordIV.setImageResource(R.mipmap.chatting_mic)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                    loadPermissions(perms, MY_PERMISSIONS_REQUEST_READ_CONTACTS)
                } else {
                    // 녹음 시작
                    record_start()
                }
            }

        }

        detail()
        timerStart()
        adverb()

    }

    fun playing(recordPath: String, chatting_id: Int) {

        if (this.chatting_id != chatting_id) {

            if (player != null) {
                player!!.release()
            }

            isPlaying = false
            length = -1

            playerHandler.removeMessages(0)
        }

        if (isPlaying == false) {

            try {

                if (player != null) {
                    player!!.release()
                }
                player = MediaPlayer()

                this.chatting_id = chatting_id

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

            for (i in 0 until adapterData.size) {
                val data = adapterData[i]
                val chatting = data.getJSONObject("Chatting")

                if (Utils.getInt(chatting, "id") == chatting_id) {
                    chatting.put("isPlaying", false)
                    chatting.put("voice_progress", 0)
                    break
                }
            }

            adapter.notifyDataSetChanged()

            playerHandler.sendEmptyMessage(0)

        } else if (length > 0) {

            if (length > player!!.duration) {
                length = 0
            }

            player!!.seekTo(length)
            player!!.start()
            length = -1

            playerHandler.sendEmptyMessage(0)

        } else {

            playingPause()

            for (i in 0 until adapterData.size) {
                val data = adapterData[i]
                val chatting = data.getJSONObject("Chatting")

                if (Utils.getInt(chatting, "id") == chatting_id) {
                    chatting.put("isPlaying", false)
                    break
                }
            }

            adapter.notifyDataSetChanged()

            playerHandler.removeMessages(0)
        }
    }

    private fun playingPause() {
        if (isPlaying) {
            player!!.pause()
            length = player!!.currentPosition
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

            recorder = MediaRecorder()

            val date = Date()
            val time = date.time

            record_path = Environment.getExternalStorageDirectory().absolutePath + "/starball/"

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
            record_path = Environment.getExternalStorageDirectory().absolutePath + "/starball/" + time + "_record.aac"

            recorder.setOutputFile(record_path)

            //저장될 파일을 저장한뒤

            recorder.prepare()

            recorder.start()

            //시작하면된다.
            Toast.makeText(context, "녹음을 시작합니다.", Toast.LENGTH_LONG).show()

//            voiceLL.setBackgroundColor(Color.parseColor("#333333"))
            recordIV.setImageResource(R.mipmap.chatting_pause)
            record = true

//            try {
//                recorder.prepare()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }

        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
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

        recordStop()

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

                        val cursor = context.contentResolver.query(selectedImageUri!!, filePathColumn, null, null, null)
                        if (cursor!!.moveToFirst()) {
                            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                            val picturePath = cursor.getString(columnIndex)

                            cursor.close()

                            selectedImage = Utils.getImage(context.contentResolver, picturePath)

                            sendChatting(2)

                        }
                    }
                }

                DELETE_ADBERB-> {

                    if (data != null) {
                        var adverb_id = data.getIntExtra("adverb_id", -1)

                        for (j in 0 until adverbAdapterData.size) {

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

                EDIT_CHATTING -> {
                    if (data != null) {

                        val type = data.getIntExtra("type", 1)
                        val chatting_id = data.getIntExtra("chatting_id", -1)
                        val chatting_contents= data.getStringExtra("chatting_contents")

                        if (type == 1) {
                            val clipboardManager: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            var clipData: ClipData = ClipData.newPlainText("label", chatting_contents)
                            clipboardManager.primaryClip = clipData

                            Toast.makeText(context, context.getString(R.string.clipboard), Toast.LENGTH_SHORT).show()
                        } else if (type == 2) {
                            deleteChatting(chatting_id)
                        } else if (type == 3) {
                            contentsET.setText(chatting_contents)
                        } else {
                            addAdverb(chatting_contents)
                        }

                    }
                }
            }
        }
    }

    fun timerStart(){
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
                            val chatting = data.getJSONObject("Chatting")

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
                        adverbAdapter.notifyDataSetChanged()

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

    fun chattingLike(chatting_id: Int, like_yn: String) {

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

//                        if(like_yn == "Y") {
//                            val intent = Intent(context, ChatNotiActivity::class.java)
//                            startActivity(intent)
//                            overridePendingTransition(0, 0)
//                        }

                        for (i in 0 until adapterData.size) {
                            val data = adapterData[i]
                            val chatting = data.getJSONObject("Chatting")
                            val id = Utils.getInt(chatting,"id")

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
    }

    fun detail() {

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

                        other_name = Utils.getString(founderMember, "name")
                        other_profile = Utils.getString(founderProfile, "image_uri")
                        var birth = Utils.getString(founderMember, "birth")

                        if (member_id == founder_member_id) {
                            other_name = Utils.getString(attendMember, "name")
                            other_profile = Utils.getString(attendProfile, "image_uri")
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

                        titleTV.text = other_name + " " + age

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

    fun chatting() {

        if (first_id < 1) {
            if (adapterData.size > 0) {
                try {
                    try {
                        val lastMSG = adapterData.get(adapterData.size - 1)
                        val chatting = lastMSG.getJSONObject("Chatting")
                        last_id = Utils.getInt(chatting, "id")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                } catch (e: NumberFormatException) {

                }

            }
        }

        val params = RequestParams()
        params.put("member_id", PrefUtils.getIntPreference(context, "member_id"))
        params.put("room_id", room_id)
        params.put("first_id", first_id)
        params.put("last_id", last_id)

        ChattingAction.chatting(params, object : JsonHttpResponseHandler() {

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

                                listLV.setSelection(adapter.count - 1)
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

    fun addChatting(chatting: JSONObject) : Boolean {
        for (i in 0 until adapterData.size) {
            val data = adapterData[i]
            val chat = data.getJSONObject("Chatting")
            if (Utils.getInt(chat, "id") == Utils.getInt(chatting, "id")) {
                return false
            }
        }
        return true
    }

    fun sendChatting(type: Int) {

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

        if (type == 3) {
            if (record_path != "" && record_path != null) {
                val file = File(record_path)
                val player = MediaPlayer()

                val fis = FileInputStream(file)
                val filinput = ByteArrayInputStream(Utils.getByteArray(fis))
                params.put("voice", filinput)

                player.setDataSource(record_path)
                player.prepare()

                var millis = player.duration

                var minutes = ( millis % (1000*60*60) ) / (1000*60)
                var seconds = ( ( millis % (1000*60*60) ) % (1000*60) ) / 1000

                params.put("voice_time", minutes.toString() + ":" + seconds.toString())
                params.put("voice_duration", player.duration)

            }
        }

        if (type == 4) {
            if (selectedEmoticon != null) {
                val selectedImg = ByteArrayInputStream(Utils.getByteArray(selectedEmoticon))
                params.put("emoticon", selectedImg)
            }
        }

        ChattingAction.send_chatting(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        val contents = Utils.getString(response, "contents")
                        val created_at = Utils.getString(response, "created_at")

                        selectedImage = null
                        selectedEmoticon = null
                        record_path = ""
                        contentsET.setText("")

                        var intent = Intent()
                        intent.putExtra("room_id", room_id)
                        intent.putExtra("contents", contents)
                        intent.putExtra("created_at", created_at)
                        intent.putExtra("member_id", member_id)
                        intent.setAction("PUSH_CHATTING")
                        sendBroadcast(intent)

                    } else {
                        Toast.makeText(context, getString(R.string.api_error), Toast.LENGTH_LONG).show()
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

    override fun finish() {
        super.finish()

        recordStop()

    }

}
