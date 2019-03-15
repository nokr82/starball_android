package com.devstories.starball_android.activities

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.devstories.starball_android.R
import com.devstories.starball_android.adapter.DaillyAdapter
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_daily_mement_list.*
import kotlinx.android.synthetic.main.item_daily_momenthead.*

class DailyMomentListActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var DaillyAdapter: DaillyAdapter

    private val FROM_ALBUM = 101
    private val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 2
    private var selectedImage: Bitmap? = null

    lateinit var header: View
    lateinit var backIV: ImageView
    lateinit var videoLL: LinearLayout
    lateinit var photoLL: LinearLayout
    lateinit var headRL: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_mement_list)
        this.context = this
        progressDialog = ProgressDialog(context)


        DaillyAdapter = DaillyAdapter(context, R.layout.item_daily_list, 6)
        dailyLV.adapter = DaillyAdapter



        dailyLV.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, DlgAlbumPayActivity::class.java)
            startActivity(intent)
        }




        header = View.inflate(this, R.layout.item_daily_momenthead, null)
        backIV = header.findViewById(R.id.backIV)
        headRL = header.findViewById(R.id.headRL)
        videoLL = header.findViewById(R.id.videoLL)
        photoLL = header.findViewById(R.id.photoLL)
        dailyLV.addHeaderView(header)
        headRL.setOnClickListener {

        }
        videoLL.setOnClickListener {

        }
        photoLL.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                loadPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE)
            } else {
                imageFromGallery()
            }
        }



        backIV.setOnClickListener {
            finish()
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

                        val cursor =
                            context!!.contentResolver.query(selectedImageUri!!, filePathColumn, null, null, null)
                        if (cursor!!.moveToFirst()) {
                            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                            val picturePath = cursor.getString(columnIndex)
                            cursor.close()

                            selectedImage = Utils.getImage(context!!.contentResolver, picturePath)

                        }
                    }
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }

    }
}
