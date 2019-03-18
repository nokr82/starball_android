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
//            selectedImage = intent.getParcelableExtra("selectedImage")
            cancelTV.text = "취소"
            doneTV.setOnClickListener {
                var intent = Intent()
                intent.putExtra("result","ok")
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            cancelTV.setOnClickListener {
                var intent = Intent()
                intent.putExtra("result","fail")
                setResult(Activity.RESULT_OK, intent)
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

}
