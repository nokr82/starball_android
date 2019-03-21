package com.devstories.starball_android.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.ChattingAction
import com.devstories.starball_android.activities.ChattingMatchFragment
import com.devstories.starball_android.activities.DailyMomentViewListActivity
import com.devstories.starball_android.base.Config
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.Utils
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.nostra13.universalimageloader.core.ImageLoader
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.util.*

open class MatchChattingAdapter (context: Context, view:Int, data:ArrayList<JSONObject>,fragment: ChattingMatchFragment) : ArrayAdapter<JSONObject>(context, view, data) {

    private lateinit var item: ViewHolder
    var view:Int = view
    var data:ArrayList<JSONObject> = data
    var fragment:ChattingMatchFragment = fragment

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

        val member_id = PrefUtils.getIntPreference(context,"member_id")

        val json = data[position]

        Log.d("매치채팅0",json.toString())
        val type = json.getInt("type")

        val contents = json.getString("contents")
        val voice_uri = json.getString("voice_uri")

        val translate_in_english = json.getString("translate_in_english")
        val like_member_id = json.getInt("member_id")

        val Profile = json.getJSONObject("Profile")
        val image_uri = Profile.getString("image_uri")
        if (type == 3)  {
            item.myVoiceLL.visibility = View.VISIBLE
            item.contentTV.visibility = View.GONE
            item.transTV.visibility = View.GONE
            item.myVoiceIV.setOnClickListener {
                fragment.playing(Config.url + voice_uri,json.getInt("id"))
            }

            val voiceProgress = Utils.getInt(json,"voice_progress")

            item.myVoicePB.max = Utils.getInt(json, "voice_duration")
            item.myVoicePB.progress = voiceProgress

            val minutes = ( voiceProgress % (1000*60*60) ) / (1000*60)
            val seconds = ( ( voiceProgress % (1000*60*60) ) % (1000*60) ) / 1000

            item.myProgressTV.text = "${minutes}:${seconds}"
            item.myVoiceTimeTV.text = json.getString("voice_time")

            if (Utils.getBoolen( json,"isPlaying")) {
                item.myVoiceIV.setImageResource(R.mipmap.player_pause)
            } else {
                item.myVoiceIV.setImageResource(R.mipmap.player_play)
            }

        }else{
            item.contentTV.visibility = View.VISIBLE
            item.transTV.visibility = View.VISIBLE
        }
        ImageLoader.getInstance().displayImage(Config.url + image_uri, item.profileIV, Utils.UILoptionsProfile)

        item.profileIV.setOnClickListener {
            if (like_member_id != member_id){
                var intent = Intent(context, DailyMomentViewListActivity::class.java)
                intent.putExtra("daily_member_id",like_member_id)
                context.startActivity(intent)
            }
        }

        item.contentTV.text = contents
        item.transTV.text = translate_in_english
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
        var contentTV : TextView
        var transTV: TextView
        var profileIV: ImageView
        var myVoiceLL: LinearLayout = v.findViewById(R.id.myVoiceLL)
        var myVoiceIV: ImageView = v.findViewById(R.id.myVoiceIV)
        var myVoicePB: ProgressBar = v.findViewById(R.id.myVoicePB)
        var myProgressTV: TextView = v.findViewById(R.id.myProgressTV)
        var myVoiceTimeTV: TextView = v.findViewById(R.id.myVoiceTimeTV)
        init {
            contentTV= v.findViewById(R.id.contentTV)
            transTV= v.findViewById(R.id.transTV)
            profileIV = v.findViewById(R.id.profileIV)
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
                var englishLanguage = Locale.getDefault().language
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
