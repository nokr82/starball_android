package com.devstories.starball_android.adapter

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.ChattingAction
import com.devstories.starball_android.actions.MemberAction
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
import kotlinx.android.synthetic.main.fragment_charmpoint_work.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

open class ChattingAdapter (context: Context, view:Int, data:ArrayList<JSONObject>, activity: FriendChattingActivity) : ArrayAdapter<JSONObject>(context, view, data) {

    private lateinit var item: ViewHolder
    var view:Int = view
    var data:ArrayList<JSONObject> = data
    var activity: FriendChattingActivity = activity

    override fun getView(position: Int, convertView: View?, parent : ViewGroup?): View {

        lateinit var retView: View

        if (convertView == null) {
            retView = View.inflate(context, view, null)
            item = ViewHolder(retView)
            retView.tag = item
        } else {
            retView = convertView
            item = convertView.tag as ViewHolder
            if (item == null) {
                retView = View.inflate(context, view, null)
                item = ViewHolder(retView)
                retView.tag = item
            }
        }

        val json = data[position]
        val chatting = json.getJSONObject("Chatting")

        val chatting_member_id = Utils.getInt(chatting, "member_id")
        val member_id = PrefUtils.getIntPreference(context, "member_id")

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val dateFormat2 = SimpleDateFormat("MM/dd hh:mm a", Locale.US)

        val type = Utils.getInt(chatting, "type")
        val contents = Utils.getString(chatting, "contents")
        var translate_in_my = Utils.getString(chatting, "translate_in_my")
        if(translate_in_my.isEmpty()) {
            translate_in_my = context.getString(R.string.in_translate)
        }
        val created_at = Utils.getString(chatting, "created_at")

        val created_dt = dateFormat.parse(created_at)
        val created = dateFormat2.format(created_dt)

        if (chatting_member_id == member_id) {
            item.otherLL.visibility = View.VISIBLE
            item.myLL.visibility = View.GONE

            item.otherContentsTV.text = contents
            item.otherCreatedTV.text = created

            if (activity.translation_yn == "Y") {
                item.translationTV.visibility = View.VISIBLE
                item.translationTV.text = translate_in_my

                item.translationTV.tag = chatting
                translate(chatting, item.translationTV)

            } else {
                item.translationTV.visibility = View.GONE
                item.translationTV.text = contents
            }

            if (type == 2) {
                item.otherImageIV.visibility = View.VISIBLE
                item.otherContentsLL.visibility = View.GONE
                item.otherVoiceLL.visibility = View.GONE
            } else if (type == 3) {
                item.otherImageIV.visibility = View.GONE
                item.otherContentsLL.visibility = View.GONE
                item.otherVoiceLL.visibility = View.VISIBLE

                item.otherVoiceIV.setOnClickListener {
                    activity.playing(Utils.getString(chatting, "voice_uri"), Utils.getInt(chatting, "id"))
                }

            } else {
                item.otherImageIV.visibility = View.GONE
                item.otherContentsLL.visibility = View.VISIBLE
                item.otherVoiceLL.visibility = View.GONE
            }

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
                    activity.playing(Utils.getString(chatting, "voice_uri"), Utils.getInt(chatting, "id"))
                }

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

        var otherLL: LinearLayout
        var otherContentsLL: LinearLayout
        var otherContentsTV: TextView
        var otherCreatedTV: TextView
        var translationTV: TextView
        var otherImageIV: ImageView
        var otherVoiceLL: LinearLayout
        var otherVoiceIV: ImageView

        var myLL: LinearLayout
        var myContentsLL: LinearLayout
        var myContentsTV: TextView
        var myCreatedTV: TextView
        var myImageIV: ImageView
        var myVoiceLL: LinearLayout
        var myVoiceIV: ImageView

        var profileIV: CircleImageView

        var likeIV: ImageView
        var likeLL: LinearLayout

        init {

            otherLL = v.findViewById(R.id.otherLL)
            otherContentsLL = v.findViewById(R.id.otherContentsLL)
            otherContentsTV = v.findViewById(R.id.otherContentsTV)
            otherCreatedTV = v.findViewById(R.id.otherCreatedTV)
            translationTV = v.findViewById(R.id.translationTV)
            otherImageIV = v.findViewById(R.id.otherImageIV)
            otherVoiceLL = v.findViewById(R.id.otherVoiceLL)
            otherVoiceIV = v.findViewById(R.id.otherVoiceIV)

            myLL = v.findViewById(R.id.myLL)
            myContentsLL = v.findViewById(R.id.myContentsLL)
            myContentsTV = v.findViewById(R.id.myContentsTV)
            myCreatedTV = v.findViewById(R.id.myCreatedTV)
            myImageIV = v.findViewById(R.id.myImageIV)
            myVoiceLL = v.findViewById(R.id.myVoiceLL)
            myVoiceIV = v.findViewById(R.id.myVoiceIV)

            profileIV = v.findViewById(R.id.profileIV)
            likeIV = v.findViewById(R.id.likeIV)
            likeLL = v.findViewById(R.id.likeLL)

        }
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
                val translate = TranslateOptions.newBuilder().setApiKey("AIzaSyCLwg17uTRmdqs-fwD6paGGgji32cIFVi4").build().service

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
