package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.ChattingAction
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.dlg_alert_common.*
import org.json.JSONException
import org.json.JSONObject

class DlgAdverbDeleteConfirmActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var member_id = -1
    var adverb_id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_alert_common)

        this.context = this
        progressDialog = ProgressDialog(context)

        adverb_id = intent.getIntExtra("adverb_id", -1)

        contentTV.text = getString(R.string.confrim_adverb_del)

        titleTV.visibility = View.GONE
        cancelTV.visibility = View.VISIBLE

        doneTV.setOnClickListener {
            adverbDelete()
        }

        cancelTV.setOnClickListener {
            finish()
        }

    }

    fun adverbDelete() {

        val params = RequestParams()
        params.put("adverb_id", adverb_id)

        ChattingAction.del_adverb(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        var intent = Intent()
                        intent.putExtra("adverb_id", adverb_id)
                        setResult(Activity.RESULT_OK, intent)

                        finish()

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


}
