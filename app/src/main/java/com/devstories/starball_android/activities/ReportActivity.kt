package com.devstories.starball_android.activities

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.ArrayAdapter
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.ReportAction
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_report.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream

class ReportActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var reportType = ArrayList<String>()

    var member_id = -1
    var report_member_id = -1

    private val FROM_ALBUM = 101
    private val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 2
    private var selectedImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        this.context = this
        progressDialog = ProgressDialog(context)

        member_id =  PrefUtils.getIntPreference(context, "member_id")

        report_member_id = intent.getIntExtra("report_member_id", -1)

        reportType.add(getString(R.string.select_hint))
        reportType.add(getString(R.string.report_type1))
        reportType.add(getString(R.string.report_type2))
        reportType.add(getString(R.string.report_type3))
        reportType.add(getString(R.string.report_type4))
        reportType.add(getString(R.string.report_type5))
        reportType.add(getString(R.string.report_type6))
        reportType.add(getString(R.string.report_type7))
        reportType.add(getString(R.string.report_type8))

        reportTypeSP.adapter = ArrayAdapter<String>(context, R.layout.spinner_item, reportType)

        backIV.setOnClickListener {
            finish()
        }

        addImageTV.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                loadPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE)
            } else {
                imageFromGallery()
            }
        }

        reportTV.setOnClickListener {

            if (selectedImage == null) {
                Toast.makeText(context, getString(R.string.report_image_empty), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (reportTypeSP.selectedItemPosition == 0) {
                Toast.makeText(context, getString(R.string.report_type_empty), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            report()
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

                        val cursor = context!!.contentResolver.query(selectedImageUri!!, filePathColumn, null, null, null)
                        if (cursor!!.moveToFirst()) {
                            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                            val picturePath = cursor.getString(columnIndex)

                            cursor.close()

                            selectedImage = Utils.getImage(context!!.contentResolver, picturePath)

                            reportTV.setBackgroundColor(Color.parseColor("#333333"))
                        }
                    }
                }

            }
        }
    }

    fun report() {

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("report_member_id", report_member_id)
        params.put("type", reportTypeSP.selectedItemPosition - 1)

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
                        Toast.makeText(context, getString(R.string.report_done), Toast.LENGTH_LONG).show()
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
