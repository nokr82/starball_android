package com.devstories.starball_android.adapter

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.ChattingAction
import com.devstories.starball_android.activities.FriendChattingActivity
import com.devstories.starball_android.base.Config
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.Utils
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.nostra13.universalimageloader.core.ImageLoader
import cz.msebera.android.httpclient.Header
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

open class ChattingAdapter (context: Context, val view:Int, val data:ArrayList<JSONObject>, val activity: FriendChattingActivity) : ArrayAdapter<JSONObject>(context, view, data) {

    private lateinit var item: ViewHolder

    override fun getView(position: Int, convertView: View?, parent : ViewGroup?): View {

        lateinit var retView: View

        if (convertView == null) {
            retView = View.inflate(context, view, null)
            item = ViewHolder(retView)
            retView.tag = item
        } else {
            retView = convertView
            if (convertView.tag == null) {
                retView = View.inflate(context, view, null)
                item = ViewHolder(retView)
                retView.tag = item
            }
        }

        val json = data[position]
        val chatting = json.getJSONObject("Chatting")

        val profile = json.getJSONObject("Profile")

        val chattingMemberId = Utils.getInt(chatting, "member_id")
        val memberId = PrefUtils.getIntPreference(context, "member_id")

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val dateFormat2 = SimpleDateFormat("MM/dd hh:mm a", Locale.US)

        val type = Utils.getInt(chatting, "type")
        val contents = Utils.getString(chatting, "contents")
        var translateInMy = Utils.getString(chatting, "translate_in_my")
        if(translateInMy.isEmpty()) {
            translateInMy = context.getString(R.string.in_translate)
        }
        val createdAt = Utils.getString(chatting, "created_at")

        val createdDt = dateFormat.parse(createdAt)
        val created = dateFormat2.format(createdDt)

        if (chattingMemberId == memberId) {
            item.otherLL.visibility = View.VISIBLE
            item.myLL.visibility = View.GONE

            item.otherContentsTV.text = contents
            item.otherCreatedTV.text = created

            if (activity.translation_yn == "Y") {
                item.translationTV.visibility = View.VISIBLE
                item.translationTV.text = translateInMy

                if(translateInMy == context.getString(R.string.in_translate)) {
                    translate(chatting, item.translationTV)
                }

            } else {
                item.translationTV.visibility = View.GONE
                item.translationTV.text = contents
            }

            when (type) {
                2 -> {
                    item.otherImageIV.visibility = View.VISIBLE
                    item.otherContentsLL.visibility = View.GONE
                    item.otherVoiceLL.visibility = View.GONE
                }
                3 -> {
                    item.otherImageIV.visibility = View.GONE
                    item.otherContentsLL.visibility = View.GONE
                    item.otherVoiceLL.visibility = View.VISIBLE

                    item.otherVoiceIV.setOnClickListener {
                        activity.playing(Config.url + Utils.getString(chatting, "voice_uri"), Utils.getInt(chatting, "id"))
                    }

                    val voice_progress = Utils.getInt(chatting, "voice_progress")

                    item.otherVoicePB.max = Utils.getInt(chatting, "voice_duration")
                    item.otherVoicePB.progress = voice_progress

                    var minutes = ( voice_progress % (1000*60*60) ) / (1000*60)
                    var seconds = ( ( voice_progress % (1000*60*60) ) % (1000*60) ) / 1000

                    item.otherProgressTV.text = "${minutes}:${seconds}"
                    item.otherVoiceTimeTV.text = Utils.getString(chatting, "voice_time")

                }
                else -> {
                    item.otherImageIV.visibility = View.GONE
                    item.otherContentsLL.visibility = View.VISIBLE
                    item.otherVoiceLL.visibility = View.GONE
                }
            }

            ImageLoader.getInstance().displayImage(Config.url + Utils.getString(profile, "image_uri"), item.profileIV, Utils.UILoptionsPosting)

        } else {
            item.otherLL.visibility = View.GONE
            item.myLL.visibility = View.VISIBLE

            item.myContentsTV.text = contents
            item.myCreatedTV.text = created

            if (type == 2) {

                ImageLoader.getInstance().displayImage(Config.url + Utils.getString(chatting, "image_uri"), item.myImageIV, Utils.UILoptionsPosting)

                item.myImageIV.visibility = View.VISIBLE
                item.myContentsLL.visibility = View.GONE
                item.myVoiceLL.visibility = View.GONE

            } else if (type == 3) {
                item.myImageIV.visibility = View.GONE
                item.myContentsLL.visibility = View.GONE
                item.myVoiceLL.visibility = View.VISIBLE

                item.myVoiceIV.setOnClickListener {
                    activity.playing(Config.url + Utils.getString(chatting, "voice_uri"), Utils.getInt(chatting, "id"))
                }

                val voice_progress = Utils.getInt(chatting, "voice_progress")

                item.myVoicePB.max = Utils.getInt(chatting, "voice_duration")
                item.myVoicePB.progress = voice_progress

                var minutes = ( voice_progress % (1000*60*60) ) / (1000*60)
                var seconds = ( ( voice_progress % (1000*60*60) ) % (1000*60) ) / 1000

                item.myProgressTV.text = "${minutes}:${seconds}"
                item.myVoiceTimeTV.text = Utils.getString(chatting, "voice_time")

            } else {

                item.myImageIV.visibility = View.GONE
                item.myContentsLL.visibility = View.VISIBLE
                item.myVoiceLL.visibility = View.GONE

            }
        }

        var like_yn = Utils.getString(chatting, "like_yn")
        if (like_yn == "Y") {
            item.likeIV.setImageResource(R.mipmap.lounge_heart_like)
            item.likeIV.setBackgroundColor(Color.parseColor("#000000"))
        } else {
            item.likeIV.setImageResource(R.mipmap.lounge_heart_like)
            item.likeIV.setBackgroundColor(Color.parseColor("#00000000"))
        }

        item.likeLL.setOnClickListener {
            var edit_like_yn = if (like_yn == "Y") "N" else "Y"
            activity.chattingLike(Utils.getInt(chatting, "id"), edit_like_yn)
        }

        return retView
    }

    override fun getItem(position: Int): JSONObject {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }

    class ViewHolder(v: View) {

        var otherLL: LinearLayout = v.findViewById(R.id.otherLL)
        var otherContentsLL: LinearLayout = v.findViewById(R.id.otherContentsLL)
        var otherContentsTV: TextView = v.findViewById(R.id.otherContentsTV)
        var otherCreatedTV: TextView = v.findViewById(R.id.otherCreatedTV)
        var translationTV: TextView = v.findViewById(R.id.translationTV)
        var otherImageIV: ImageView = v.findViewById(R.id.otherImageIV)
        var otherVoiceLL: LinearLayout = v.findViewById(R.id.otherVoiceLL)
        var otherVoiceIV: ImageView = v.findViewById(R.id.otherVoiceIV)
        var otherVoicePB: ProgressBar = v.findViewById(R.id.otherVoicePB)
        var otherProgressTV: TextView = v.findViewById(R.id.otherProgressTV)
        var otherVoiceTimeTV: TextView = v.findViewById(R.id.otherVoiceTimeTV)

        var myLL: LinearLayout = v.findViewById(R.id.myLL)
        var myContentsLL: LinearLayout = v.findViewById(R.id.myContentsLL)
        var myContentsTV: TextView = v.findViewById(R.id.myContentsTV)
        var myCreatedTV: TextView = v.findViewById(R.id.myCreatedTV)
        var myImageIV: ImageView = v.findViewById(R.id.myImageIV)
        var myVoiceLL: LinearLayout = v.findViewById(R.id.myVoiceLL)
        var myVoiceIV: ImageView = v.findViewById(R.id.myVoiceIV)
        var myVoicePB: ProgressBar = v.findViewById(R.id.myVoicePB)
        var myProgressTV: TextView = v.findViewById(R.id.myProgressTV)
        var myVoiceTimeTV: TextView = v.findViewById(R.id.myVoiceTimeTV)

        var profileIV: CircleImageView = v.findViewById(R.id.profileIV)

        var likeIV: ImageView = v.findViewById(R.id.likeIV)
        var likeLL: LinearLayout = v.findViewById(R.id.likeLL)

    }

    private fun translate(data:JSONObject, translatedTV:TextView) {
        val task = TranslateAsyncTask(context, data, translatedTV)
        task.execute()
    }


    companion object {
        class TranslateAsyncTask internal constructor(context: Context, json: JSONObject, translatedTV: TextView) : AsyncTask<Void, String, Pair<String, String>>() {

            private val contextReference: WeakReference<Context> = WeakReference(context)
            private val jsonReference: WeakReference<JSONObject> = WeakReference(json)
            private val translatedTVReference: WeakReference<TextView> = WeakReference(translatedTV)

            override fun onPreExecute() {
                println("onPreExecute onPreExecute")
            }

            override fun doInBackground(vararg params: Void?): Pair<String, String> {
                val translate = TranslateOptions.newBuilder().setApiKey("AIzaSyAHMbqyG5pv-GEDv8K3ceD1xZBohrzO6aU").build().service

                var contents = Utils.getString(jsonReference.get(),"contents")

                println("contents : $contents")

                // my
                var myLanguage = Locale.getDefault().language
                val myTranslation = translate.translate(
                    contents,
                    Translate.TranslateOption.targetLanguage(myLanguage))

                // english
                var englishLanguage = Locale.ENGLISH.language
                val englishTranslation = translate.translate(
                    contents,
                    Translate.TranslateOption.targetLanguage(englishLanguage))

                return Pair(myTranslation.translatedText, englishTranslation.translatedText)
            }

            override fun onPostExecute(result: Pair<String, String>) {
                jsonReference.get()!!.put("translate_in_my", result.first)
                jsonReference.get()!!.put("translate_in_english", result.second)

                translatedTVReference.get()!!.text = result.first

                saveTranslate(jsonReference.get()!!)
            }

        }

        private fun saveTranslate(json:JSONObject) {

            val chatting_id = Utils.getInt(json, "id")
            val translate_in_my = Utils.getString(json, "translate_in_my")
            val translate_in_english = Utils.getString(json, "translate_in_english")

            val params = RequestParams()
            params.put("chatting_id", chatting_id)
            params.put("translate_in_my", translate_in_my)
            params.put("translate_in_english", translate_in_english)

            ChattingAction.saveTranslate(params, object : JsonHttpResponseHandler() {

                override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {

                    try {
                        val result = response!!.getString("result")

                        Log.d("결과", result.toString())
                        if ("ok" == result) {

                        } else {

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

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>?,
                    responseString: String?,
                    throwable: Throwable
                ) {
                    // System.out.println(responseString);

                    throwable.printStackTrace()
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>?,
                    throwable: Throwable,
                    errorResponse: JSONObject?
                ) {
                    throwable.printStackTrace()
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>?,
                    throwable: Throwable,
                    errorResponse: JSONArray?
                ) {
                    throwable.printStackTrace()
                }

                override fun onStart() {

                }

                override fun onFinish() {

                }
            })
        }
    }


}
