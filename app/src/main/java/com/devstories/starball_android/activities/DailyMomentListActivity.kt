package com.devstories.starball_android.activities

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.*
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import com.devstories.starball_android.R
import com.devstories.starball_android.actions.DailyAction
import com.devstories.starball_android.adapter.DaillyAdapter
import com.devstories.starball_android.base.PrefUtils
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_daily_mement_list.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.File

class DailyMomentListActivity : RootActivity() {

    private val SELECT_PICTURE_REQUEST = 1001

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var daillyAdapter: DaillyAdapter
    var adapterdata = ArrayList<JSONObject>()

    private var pictures = arrayListOf<JSONObject>()

    var page = 1
    var totalPage = 1
    var type = 1
    private val UPDATE_TIME_LINE = 995
    private val FROM_ALBUM = 101
    private val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 2
    private var selectedImage: Bitmap? = null
    lateinit var header: View
    lateinit var backIV: ImageView
    lateinit var videoLL: LinearLayout
    lateinit var mypostTV: TextView
    lateinit var nameTV: TextView
    lateinit var photoLL: LinearLayout
    lateinit var headRL: RelativeLayout
    lateinit var profileIV: ImageView

    var items= ArrayList<String>()

    internal var reloadReciver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                page = 1
                daily_list()
            }
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_mement_list)
        this.context = this
        progressDialog = ProgressDialog(context)


        var filter1 = IntentFilter("DEL_POST")
        registerReceiver(reloadReciver, filter1)

        daillyAdapter = DaillyAdapter(context, R.layout.item_daily_list, adapterdata,this,
            DailyMomentViewListActivity(),1
        )
        dailyLV.adapter = daillyAdapter

        dailyLV.setOnItemClickListener { parent, view, position, id ->
           /* val intent = Intent(context, DlgAlbumPayActivity::class.java)
            startActivity(intent)*/
        }
        dailyLV.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(p0: AbsListView?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onScrollStateChanged(listView: AbsListView, newState: Int) {
                if (!dailyLV.canScrollVertically(-1)) {

                } else if (!dailyLV.canScrollVertically(1)) {
                    if (totalPage > page) {
                        page++
                        daily_list()
                    }
                } else {
                }
            }
        })



        header = View.inflate(this, R.layout.item_daily_momenthead, null)
        backIV = header.findViewById(R.id.backIV)
        headRL = header.findViewById(R.id.headRL)
        videoLL = header.findViewById(R.id.videoLL)
        photoLL = header.findViewById(R.id.photoLL)
        profileIV = header.findViewById(R.id.profileIV)
        mypostTV = header.findViewById(R.id.mypostTV)
        nameTV = header.findViewById(R.id.nameTV)
        dailyLV.addHeaderView(header)

        mypostTV.setOnClickListener {
            var intent = Intent(context, DailyMomentViewListActivity::class.java)
            intent.putExtra("daily_member_id",PrefUtils.getIntPreference(context, "member_id"))
            startActivity(intent)

        }

        headRL.setOnClickListener {

        }
        videoLL.setOnClickListener {
            type=2
            var intent = Intent(context, FindVideoGridActivity::class.java)
            intent.putExtra("type", 3)
            intent.putExtra("pictureCnt", pictures.count())
            startActivityForResult(intent, SELECT_PICTURE_REQUEST)
        }
        photoLL.setOnClickListener {
            type=1
            var intent = Intent(context, FindPictureGridActivity::class.java)
            intent.putExtra("type", 2)
            intent.putExtra("pictureCnt", pictures.count())
            startActivityForResult(intent, SELECT_PICTURE_REQUEST)
        }




        backIV.setOnClickListener {
            finish()
        }


        daily_list()

    }

    private fun dlg_view(){
        val intent = Intent(context, DlgLogoutActivity::class.java)
        intent.putExtra("type",1)

        startActivityForResult(intent,UPDATE_TIME_LINE)
    }


    private fun permissionvideo() {

        val permissionlistener = object : PermissionListener {
            override fun onPermissionGranted() {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, FROM_ALBUM)
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(context, "권한설정을 해주셔야 합니다.", Toast.LENGTH_SHORT).show()
            }

        }

        TedPermission.with(this)
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .check();

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

                SELECT_PICTURE_REQUEST -> {

                    items = data?.getStringArrayListExtra("items")!!
                    for (i in 0..(items!!.size - 1)) {

                        val item = JSONObject(items[i])

                        pictures.add(item)





                        // reset(str, i, "picture", mediaType, id, -1, null)
                    }
                    dlg_view()

                }

                UPDATE_TIME_LINE -> {
                    if (data != null) {
                        val result = data.getStringExtra("result")
                        Log.d("결과",result)
                        if (result == "ok"){
                            update()
                        }else{
                            pictures.clear()
                        }

                    }
                }
            }
        }
    }


    fun daily_list() {
        var member_id = PrefUtils.getIntPreference(context, "member_id")
        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("page", page)

        DailyAction.list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                // Log.d("아우스0",response.toString())
                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        totalPage = Utils.getInt(response, "totalPage")
                        if (page == 1) {
                            adapterdata.clear()
                        }
                        val list = response.getJSONArray("list")

                        for (i in 0 until list.length()) {
                            var json = list[i] as JSONObject
                            // Log.d("제이슨", json.toString())
                            adapterdata.add(json)
                        }
                        daillyAdapter.notifyDataSetChanged()

                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                 System.out.println(responseString);
            }

            private fun error() {
                 Utils.alert(context, "조회중 장애가 발생하였습니다.")
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

                 System.out.println(responseString);

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

    fun like(content_id:Int) {
        var member_id = PrefUtils.getIntPreference(context, "member_id")
        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("content_id", content_id)

        DailyAction.like(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                Log.d("아우스0",response.toString())
                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        daily_list()
                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
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

                System.out.println(responseString);

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

    fun update() {
        var member_id = PrefUtils.getIntPreference(context, "member_id")
        val params = RequestParams()
        params.put("member_id", member_id)

        var picturesArr = ArrayList<String>()
        for (picture in pictures) {
            picturesArr.add(picture.toString())
        }
        if (picturesArr.isNotEmpty()) {
            for ((idx, sp) in picturesArr.withIndex()) {
                try {
                    val picture = JSONObject(sp)

                    val id = Utils.getInt(picture!!, "id")
                    val path = Utils.getString(picture!!, "path")
                    val mediaType = Utils.getInt(picture!!, "mediaType")

                    if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                        var bitmap = Utils.getImage(context.contentResolver, path)
                        Log.d("이미지", bitmap.toString())
                        params.put(
                            "uploads[$idx]",
                            ByteArrayInputStream(Utils.getByteArray(bitmap)),
                            "${System.currentTimeMillis()}.png"
                        )
                    } else {
                        val file = File(path)
                        var videoBytes = file.readBytes()
                        Log.d("동영상",videoBytes.toString())
                        params.put(
                            "uploads[$idx]",
                            ByteArrayInputStream(videoBytes),
                            "${System.currentTimeMillis()}.mp4"
                        )
                    }
//                    params.put("media_types[$idx]", mediaType)

                } catch (e: Exception) {

                }
            }

        }
        params.put("type", type)


        DailyAction.add_content(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                Log.d("아우스0",response.toString())
                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        daily_list()
                      Toast.makeText(context,"등록되었습니다.",Toast.LENGTH_SHORT).show()
                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                 System.out.println(responseString);
            }

            private fun error() {
                 Utils.alert(context, "조회중 장애가 발생하였습니다.")
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

                 System.out.println(responseString);

                throwable.printStackTrace()
                error()
            }


            override fun onStart() {
//                 show dialog
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


    override fun onDestroy() {
        super.onDestroy()
        if (reloadReciver!=null){
            unregisterReceiver(reloadReciver)
        }
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }

    }
}
