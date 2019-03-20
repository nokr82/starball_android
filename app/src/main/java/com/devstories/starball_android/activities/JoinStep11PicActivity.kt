package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_join_picture.*
import org.json.JSONObject



class JoinStep11PicActivity : RootActivity() {

    private val SELECT_PICTURE_REQUEST = 1001


    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    private var pictures = arrayListOf<JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_picture)
        this.context = this
        progressDialog = ProgressDialog(context, com.devstories.starball_android.R.style.CustomProgressBar)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        nextTV.setBackgroundResource(R.drawable.background_border_strock2)
        val spanText = getString(R.string.picture_content)

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            descTV.text = Html.fromHtml(spanText);
        } else {
            descTV.text = Html.fromHtml(spanText, Html.FROM_HTML_MODE_LEGACY);
        }


        profile1RL.setOnClickListener {
            checkPermission()
        }

        profile2RL.setOnClickListener {
            checkPermission()
        }

        profile3RL.setOnClickListener {
            checkPermission()
        }

        profile4RL.setOnClickListener {
            checkPermission()
        }

        profile5RL.setOnClickListener {
            checkPermission()
        }

        profile6RL.setOnClickListener {
            checkPermission()
        }

        profile7RL.setOnClickListener {
            checkPermission()
        }

        profile8RL.setOnClickListener {
            checkPermission()
        }

        profile9RL.setOnClickListener {
            checkPermission()
        }


        val joinPics = PrefUtils.getStringPreference(context, "join_pics", "")
        if(joinPics.isNotEmpty()) {
            val splited = joinPics.split("`devstories`")
            for (sp in  splited) {
                try {
                    pictures.add(JSONObject(sp))
                    if (pictures.size<3){
                        nextTV.setBackgroundResource(R.drawable.background_border_strock2)
                    }else{
                        nextTV.setBackgroundColor(Color.BLACK)
                    }

                } catch (e:Exception) {

                }
            }

            updatePictures()
        }

        nextTV.setOnClickListener {
            if(pictures.size < 3) {
                Toast.makeText(context, getString(R.string.picture_min3), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var picturesArr = ArrayList<String>()
            for (picture in pictures) {
                picturesArr.add(picture.toString())
            }
            PrefUtils.setPreference(context, "join_pics", picturesArr.joinToString(separator = "`devstories`"))

            val intent = Intent(context, JoinStep12PreviewActivity::class.java)
            startActivity(intent)
        }

    }

    private fun checkPermission() {

        val permissionlistener = object : PermissionListener {
            override fun onPermissionGranted() {

                var intent = Intent(context, FindPictureGridActivity::class.java)
                intent.putExtra("pictureCnt", pictures.count())
                startActivityForResult(intent, SELECT_PICTURE_REQUEST)

            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {

            }

        }

        TedPermission.with(this)
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
            .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .check();
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                SELECT_PICTURE_REQUEST -> {

                    val items = data?.getStringArrayListExtra("items")
                    if (items!!.size<3){
                        nextTV.setBackgroundResource(R.drawable.background_border_strock2)
                    }else{
                        nextTV.setBackgroundColor(Color.BLACK)
                    }
                    for (i in 0..(items!!.size - 1)) {

                        val item = JSONObject(items[i])

                        pictures.add(item)

                        println(item)




                        // reset(str, i, "picture", mediaType, id, -1, null)
                    }

                    updatePictures()
                }

            }
        }


    }

    private fun updatePictures() {

        // clear
        for (idx in 0..8) {

            val imageIV = getIV(idx)
            val delIV = getDelIV(idx)

            imageIV.setImageBitmap(null)

            imageIV.visibility = View.GONE
            delIV.visibility = View.GONE
        }

        for ((idx, picture) in pictures.withIndex()) {
            val id = Utils.getInt(picture!!, "id")
            val path = Utils.getString(picture!!, "path")
            val mediaType = Utils.getInt(picture!!, "mediaType")

            val imageIV = getIV(idx)
            val delIV = getDelIV(idx)

            if(mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                var bitmap = Utils.getImage(context.contentResolver, path)
                imageIV.setImageBitmap(bitmap)
            } else {
                val curThumb = MediaStore.Video.Thumbnails.getThumbnail(context.contentResolver, id.toLong(), MediaStore.Video.Thumbnails.MINI_KIND, null)
                imageIV.setImageBitmap(curThumb)
            }

            delIV.tag = idx
            delIV.setOnClickListener(null)
            delIV.setOnClickListener {
                val tag = it.tag as Int
                if(pictures.size <= tag) {
                    return@setOnClickListener
                }
                pictures.removeAt(tag)

                updatePictures()
            }

            imageIV.visibility = View.VISIBLE
            delIV.visibility = View.VISIBLE

        }

        // change grades
        updateGrades()
    }

    private fun updateGrades() {
        // Basic – 최소 3장만 채웠을 경우
        // Plus – 3장 이상 채웠을 경우
        // Premium – 9장 이상을 채웠을 경우
        // VVIP – 9장에 사진과 동영상으로 구성했을 경우

        val pictureCnt = pictures.size
        if(pictureCnt == 3) {
            drawBasic()
            drawBasic2()
        } else if(pictureCnt in 4..8) {
            drawPlus()
            drawPlus2()
        } else if(pictureCnt > 8) {

            var videoContains = false
            for (picture in pictures) {
                val mediaType = Utils.getInt(picture!!, "mediaType")
                if(mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                    videoContains = true
                    break
                }
            }

            if(videoContains) {
                drawVVIP()
                drawVVIP2()
            } else {
                drawPremium()
                drawPremium2()
            }
        } else {
            initGrades()
        }

    }

    private fun drawBasic() {
        initGrades()
        basicV.setBackgroundColor(Color.parseColor("#903ba0"))
        plusV.setBackgroundColor(Color.parseColor("#903ba0"))
    }

    private fun drawPlus() {
        initGrades()
        plusV.setBackgroundColor(Color.parseColor("#903ba0"))
        premiumV.setBackgroundColor(Color.parseColor("#903ba0"))
    }

    private fun drawPremium() {
        initGrades()
        premiumV.setBackgroundColor(Color.parseColor("#903ba0"))
        vvipV.setBackgroundColor(Color.parseColor("#903ba0"))
    }

    private fun drawVVIP() {
        initGrades()
        premiumV.setBackgroundColor(Color.parseColor("#903ba0"))
        vvipV.setBackgroundColor(Color.parseColor("#903ba0"))
    }

    private fun initGrades() {
        basicV.setBackgroundColor(Color.parseColor("#d4d4d4"))
        plusV.setBackgroundColor(Color.parseColor("#d4d4d4"))
        premiumV.setBackgroundColor(Color.parseColor("#d4d4d4"))
        vvipV.setBackgroundColor(Color.parseColor("#d4d4d4"))
    }

    private fun drawBasic2() {
        initGrades2()
        basicV2.setBackgroundColor(Color.parseColor("#903ba0"))
        plusV2.setBackgroundColor(Color.parseColor("#903ba0"))
    }

    private fun drawPlus2() {
        initGrades2()
        plusV2.setBackgroundColor(Color.parseColor("#903ba0"))
        premiumV2.setBackgroundColor(Color.parseColor("#903ba0"))
    }

    private fun drawPremium2() {
        initGrades2()
        premiumV2.setBackgroundColor(Color.parseColor("#903ba0"))
        vvipV2.setBackgroundColor(Color.parseColor("#903ba0"))
    }

    private fun drawVVIP2() {
        initGrades2()
        premiumV2.setBackgroundColor(Color.parseColor("#903ba0"))
        vvipV2.setBackgroundColor(Color.parseColor("#903ba0"))
    }

    private fun initGrades2() {
        basicV2.setBackgroundColor(Color.parseColor("#d4d4d4"))
        plusV2.setBackgroundColor(Color.parseColor("#d4d4d4"))
        premiumV2.setBackgroundColor(Color.parseColor("#d4d4d4"))
        vvipV2.setBackgroundColor(Color.parseColor("#d4d4d4"))
    }

    private fun getIV(idx: Int): ImageView {
        when (idx) {
            0 -> return profile1IV
            1 -> return profile2IV
            2 -> return profile3IV
            3 -> return profile4IV
            4 -> return profile5IV
            5 -> return profile6IV
            6 -> return profile7IV
            7 -> return profile8IV
            8 -> return profile9IV
        }

        return profile1IV
    }

    private fun getDelIV(idx: Int): ImageView {
        when (idx) {
            0 -> return profile1DelIV
            1 -> return profile2DelIV
            2 -> return profile3DelIV
            3 -> return profile4DelIV
            4 -> return profile5DelIV
            5 -> return profile6DelIV
            6 -> return profile7DelIV
            7 -> return profile8DelIV
            8 -> return profile9DelIV
        }

        return profile1DelIV
    }
}
