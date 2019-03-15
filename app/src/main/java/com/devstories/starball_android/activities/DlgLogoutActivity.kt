package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.ReportAction
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.dlg_alert_common.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream

class DlgLogoutActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    private var selectedImage: Bitmap? = null

    var type = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_logout)

        this.context = this
        progressDialog = ProgressDialog(context)

        type = intent.getIntExtra("type", -1)
        if (type ==1){
            titleTV.text = "사진등록"
            contentTV.text = "등록하시겠습니까?"
            selectedImage = intent.getParcelableExtra("selectedImage")
            cancelTV.text = "취소"
            doneTV.setOnClickListener {
                update()
            }
            cancelTV.setOnClickListener {
                var intent = Intent()
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }
        }else{
            doneTV.setOnClickListener {
                var intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

            cancelTV.setOnClickListener {
                var intent = Intent()
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }
        }
    }
    fun update() {
        var member_id = PrefUtils.getIntPreference(context, "member_id")
        val params = RequestParams()
        params.put("member_id", member_id)
        val selectedImg = ByteArrayInputStream(Utils.getByteArray(selectedImage))
        params.put("upload", selectedImg)

        ReportAction.report(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        val intent = Intent()
                        intent.putExtra("result", "result")
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
