package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.ChattingAction
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.adapter.MatchAdapter
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_chatting_match.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

//채팅 매칭

class ChattingMatchFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    private var recorder: MediaRecorder = MediaRecorder()

    var member_id = -1
    lateinit var matchAdapter: MatchAdapter
    var adapterdata = ArrayList<JSONObject>()
    var starball = -1
    var page = 1
    var totalPage = 1

    var length = -1
    var isPlaying = false
    var player: MediaPlayer? = null

    val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100
    var record = false
    var record_path = ""

    var chatting_id = -1

    internal var playerHandler: Handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {

            for (i in 0 until adapterdata.size) {

                val data = adapterdata[i]
                val chatting = data.getJSONObject("LastChatting")

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

            matchAdapter.notifyDataSetChanged()

            this.sendEmptyMessageDelayed(0, 1000)
        }
    }

    internal var reloadReciver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                page = 1
                loadData()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.myContext = container!!.context
        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        return inflater.inflate(R.layout.fragment_chatting_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        matchLV.layoutManager = LinearLayoutManager(context)
        matchAdapter = MatchAdapter(this, adapterdata)
        matchLV.adapter = matchAdapter

        var filter1 = IntentFilter("DEL_MATCH")
        myContext.registerReceiver(reloadReciver, filter1)


        loadData()

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

            for (i in 0 until adapterdata.size) {
                val data = adapterdata[i]
                val chatting = data.getJSONObject("LastChatting")

                if (Utils.getInt(chatting, "id") == chatting_id) {
                    chatting.put("isPlaying", false)
                    chatting.put("voice_progress", 0)
                    break
                }
            }

            matchAdapter.notifyDataSetChanged()

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

            for (i in 0 until adapterdata.size) {
                val data = adapterdata[i]
                val chatting = data.getJSONObject("LastChatting")

                if (Utils.getInt(chatting, "id") == chatting_id) {
                    chatting.put("isPlaying", false)
                    break
                }
            }

            matchAdapter.notifyDataSetChanged()

            playerHandler.removeMessages(0)
        }
    }


    private fun playingPause() {
        if (isPlaying) {
            player!!.pause()
            length = player!!.currentPosition
        }
    }


    fun recordStop(type: Int,content:String,receiver_member_id:Int){
        recorder.stop()
        sendChatting(type,content,receiver_member_id)
    }

    fun loadPermissions(perms: Array<String>, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(myContext, perms[0]) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(myContext,perms[1]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(myContext as Activity, perms, requestCode)
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


    fun sendChatting(type: Int,content:String,receiver_member_id:Int) {
        member_id = PrefUtils.getIntPreference(context,"member_id")
        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("receiver_member_id", receiver_member_id )
        params.put("type", type)
        params.put("contents", content)

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

        ChattingAction.created_room(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                Log.d("결과",response.toString())
                try {
                    val result = response!!.getString("result")

                    record_path = ""
                    loadData()
                    if ("ok" == result) {

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

            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }


    private fun loadData() {

        val member_id = PrefUtils.getIntPreference(context,"member_id")

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("page", page)

        MemberAction.match_list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {

                    println(response)

                    val result =   Utils.getString(response,"result")
                    if ("ok" == result) {
                         totalPage = Utils.getInt(response, "totalPage")

                        if (page == 1) {
                            adapterdata.clear()
                            matchAdapter.notifyDataSetChanged()
                        }

                        val list = response!!.getJSONArray("list")

                        for (i in 0 until list.length()) {
                            adapterdata.add(list[i] as JSONObject)
                        }

                        matchAdapter.notifyDataSetChanged()
                    }

                    var intent = Intent()
                    intent.action = "LOUNGE_COUNT"
                    myContext.sendBroadcast(intent)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                super.onSuccess(statusCode, headers, response)
            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                // System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(context, getString(R.string.api_error))
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
                    // progressDialog!!.show()
                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })

    }


    fun confirm(room_id:Int,like_member_id:Int) {

        val member_id = PrefUtils.getIntPreference(context,"member_id")

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("room_id", room_id)
        params.put("like_member_id", like_member_id)

        ChattingAction.confirm(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                Log.d("매칭제발",response.toString())
                try {


                    val result =   Utils.getString(response,"result")
                    if ("ok" == result) {
                        var intent = Intent(context, FriendChattingActivity::class.java)
                        intent.putExtra("room_id", room_id)
                        myContext.startActivity(intent)
                    }


                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                super.onSuccess(statusCode, headers, response)
            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                // System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
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
                    // progressDialog!!.show()
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

        if (reloadReciver != null){
            myContext.unregisterReceiver(reloadReciver)
        }

        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}
