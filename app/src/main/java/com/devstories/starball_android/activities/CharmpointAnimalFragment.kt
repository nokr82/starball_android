package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devstories.starball_android.actions.JoinAction
import com.devstories.starball_android.actions.MemberAction
import com.devstories.starball_android.R
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_charmpoint_animal.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class CharmpointAnimalFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    var animal = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_charmpoint_animal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        animal1TV.setOnClickListener {
            setmenu()
            animal = Utils.getString(animal1TV)
            animal1TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "ANIMAL_CHANGE"
            myContext.sendBroadcast(intent)
            edit_info()
        }
        animal2TV.setOnClickListener {
            setmenu()
            animal = Utils.getString(animal2TV)
            animal2TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "ANIMAL_CHANGE"
            myContext.sendBroadcast(intent)
            edit_info()
        }
        animal3TV.setOnClickListener {
            setmenu()
            animal = Utils.getString(animal3TV)
            animal3TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "ANIMAL_CHANGE"
            myContext.sendBroadcast(intent)
            edit_info()
        }
        animal4TV.setOnClickListener {
            setmenu()
            animal = Utils.getString(animal4TV)
            animal4TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "ANIMAL_CHANGE"
            myContext.sendBroadcast(intent)
            edit_info()
        }
        animal5TV.setOnClickListener {
            setmenu()
            animal = Utils.getString(animal5TV)
            animal5TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "ANIMAL_CHANGE"
            myContext.sendBroadcast(intent)
            edit_info()
        }
        animal6TV.setOnClickListener {
            setmenu()
            animal = Utils.getString(animal6TV)
            animal6TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "ANIMAL_CHANGE"
            myContext.sendBroadcast(intent)
            edit_info()
        }
        animal7TV.setOnClickListener {
            setmenu()
            animal = Utils.getString(animal7TV)
            animal7TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
            var intent = Intent()
            intent.action = "ANIMAL_CHANGE"
            myContext.sendBroadcast(intent)
            edit_info()
        }
        get_info()

        skipTV.setOnClickListener {
            var intent = Intent()
            intent.action = "ANIMAL_CHANGE"
            myContext.sendBroadcast(intent)
        }

    }
    fun edit_info() {
        var member_id = PrefUtils.getIntPreference(context, "member_id")
        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("animal", animal)

        JoinAction.final_join(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    Log.d("결과",result.toString())
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
    fun get_info() {

        var member_id = PrefUtils.getIntPreference(context, "member_id")

        val params = RequestParams()
        params.put("member_id", member_id)

        MemberAction.get_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    Log.d("결과", result.toString())
                    if ("ok" == result) {

                        val member = response.getJSONObject("member")

                        var animal = Utils.getString(member,"animal")

                        if (animal == Utils.getString(animal1TV)){
                            animal1TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (animal == Utils.getString(animal2TV)){
                            animal2TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (animal ==Utils.getString(animal3TV)){
                            animal3TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (animal == Utils.getString(animal4TV)){
                            animal4TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (animal == Utils.getString(animal5TV)){
                            animal5TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (animal == Utils.getString(animal6TV)){
                            animal6TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else if (animal == Utils.getString(animal7TV)){
                            animal7TV.setBackgroundResource(R.drawable.background_border_strock_a862b2)
                        }else{

                        }

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
    fun setmenu(){
        animal1TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        animal2TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        animal3TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        animal4TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        animal5TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        animal6TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
        animal7TV.setBackgroundResource(R.drawable.background_border_strock_c9c9c9)
    }
    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}
