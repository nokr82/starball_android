package com.devstories.starball_android.activities

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.support.v4.content.CursorLoader
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Toast
import com.devstories.adapter.ImageAdapter
import com.devstories.starball_android.R
import com.devstories.starball_android.base.ImageLoader
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_find_picture_grid.*
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class FindVideoGridActivity() : RootActivity(), AdapterView.OnItemClickListener {
    private lateinit var context: Context

    private var photoList: ArrayList<ImageAdapter.PhotoData> = ArrayList<ImageAdapter.PhotoData>()

    private val selected = LinkedList<String>()

    private var imageUri: Uri? = null

    private var FROM_CAMERA: Int = 100

    private val SELECT_PICTURE: Int = 101

    private var imagePath: String? = ""

    private var displayName: String? = ""

    // private var count: Int = 0

    private lateinit var mAuth: FirebaseAuth

    private var selectedImage: Bitmap? = null
    private var type = -1
    constructor(parcel: Parcel) : this() {
        imageUri = parcel.readParcelable(Uri::class.java.classLoader)
        FROM_CAMERA = parcel.readInt()
        imagePath = parcel.readString()
        // count = parcel.readInt()
    }

    private var pictureCnt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_picture_grid)

        context = this

        pictureCnt = intent.getIntExtra("pictureCnt", 0)
        type = intent.getIntExtra("type", -1)

        if (type==3){
            titleTV.text = "동영상 올리기"
        }


        mAuth = FirebaseAuth.getInstance();
        var cursor: Cursor? = null
        val resolver = contentResolver

        try {
            val proj = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.MINI_THUMB_MAGIC,
                MediaStore.Files.FileColumns.MEDIA_TYPE
            )
            val idx = IntArray(proj.size)

            val selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO

            /*
            cursor = MediaStore.Images.Media.query(
                    resolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null,
                    null,
                    MediaStore.Images.Media.DATE_ADDED + " DESC"
            )
            */

            val cursorLoader = CursorLoader(
                    this,
                    MediaStore.Files.getContentUri("external"),
                    null,
                    selection,
                    null, // Selection args (none).
                    MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
            );

            val cursor = cursorLoader.loadInBackground()

            if (cursor != null && cursor.moveToFirst()) {
                idx[0] = cursor.getColumnIndex(proj[0])
                idx[1] = cursor.getColumnIndex(proj[1])
                idx[2] = cursor.getColumnIndex(proj[2])
                idx[3] = cursor.getColumnIndex(proj[3])
                idx[4] = cursor.getColumnIndex(proj[4])
                idx[5] = cursor.getColumnIndex(proj[5])
                idx[6] = cursor.getColumnIndex(proj[6])
                idx[7] = cursor.getColumnIndex(proj[7])
                idx[8] = cursor.getColumnIndex(proj[8])
                idx[9] = cursor.getColumnIndex(proj[9])
                idx[10] = cursor.getColumnIndex(proj[10])

                var photo = ImageAdapter.PhotoData()

                do {
                    val photoID = cursor.getInt(idx[0])
                    val photoPath = cursor.getString(idx[1])
                    val displayName = cursor.getString(idx[2])
                    val orientation = cursor.getInt(idx[3])
                    val bucketDisplayName = cursor.getString(idx[4])

                    val videoID = cursor.getInt(idx[5])
                    val videoPath = cursor.getString(idx[6])
                    val videoDisplayName = cursor.getString(idx[7])
                    val videoBucketDisplayName = cursor.getString(idx[8])
                    val miniThumbMagic = cursor.getString(idx[9])

                    val mediaType = cursor.getInt(idx[10])

                    if (displayName != null) {
                        photo = ImageAdapter.PhotoData()


                        photo.videoID = videoID
                        photo.videoPath = videoPath
                        photo.videoDisplayName = videoBucketDisplayName
                        photo.videoBucketDisplayName = videoBucketDisplayName

                        photo.mediaType = mediaType

                        photoList!!.add(photo)
                    }

                } while (cursor.moveToNext())

                cursor.close()
            }
        } catch (ex: Exception) {
        } finally {
            try {
                if (cursor != null && !cursor.isClosed) {
                    cursor.close()
                }
            } catch (ex: Exception) {
            }

        }

        selectGV.setOnItemClickListener(this)

        val imageLoader: ImageLoader = ImageLoader(resolver)

        val adapter = ImageAdapter(this, photoList, imageLoader, selected)

        selectGV.adapter = adapter

        imageLoader.setListener(adapter)

        adapter.notifyDataSetChanged()

        backIV.setOnClickListener {
            try {
                if (cursor != null && !cursor.isClosed) {
                    cursor.close()
                }
            } catch (ex: Exception) {
            }
            finish()
        }

        doneLL.setOnClickListener {

            if (selected != null) {
                val ids = ArrayList<Int>(selected.size)
                val result = arrayOfNulls<String>(selected.size)
                val name = arrayOfNulls<String>(selected.size)
                val mediaTypes = ArrayList<Int>(selected.size)

                var idx = 0

                var items = ArrayList<String>()
                for (strPo in selected) {

                    val photo = photoList[Integer.parseInt(strPo)]

                    val item = JSONObject()

                    if(photo.mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                        item.put("id", photo.photoID)
                        item.put("path", photo.photoPath)
                        item.put("displayName", photo.displayName)
                    } else {
                        item.put("id", photo.videoID)
                        item.put("path", photo.videoPath)
                        item.put("displayName", photo.videoDisplayName)

                    }
                    item.put("mediaType", photo.mediaType)

                    items.add(item.toString())
                }

                try {
                    if (cursor != null && !cursor.isClosed) {
                        cursor.close()
                    }
                } catch (ex: Exception) {

                }

                val returnIntent = Intent()
                returnIntent.putExtra("items", items)
                setResult(RESULT_OK, returnIntent)

                finish()
            }

        }


    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val strPo = position.toString()

        val photo_id = photoList[position].photoID

        if (photo_id == -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {

            } else {
                takePhoto()
            }
        } else {
            if (selected.contains(strPo)) {
                selected.remove(strPo)

                countTV.text = selected.size.toString()

                val adapter = selectGV.getAdapter()
                if (adapter != null) {
                    val f = adapter as ImageAdapter
                    (f as BaseAdapter).notifyDataSetChanged()
                }

            } else {
                if (pictureCnt + selected.size >= 9) {
                    Toast.makeText(context, "사진은 9개까지 등록가능합니다.", Toast.LENGTH_SHORT).show()
                    return
                }

                selected.add(strPo)

                countTV.text = selected.size.toString()

                val adapter = selectGV.getAdapter()
                if (adapter != null) {
                    val f = adapter as ImageAdapter
                    (f as BaseAdapter).notifyDataSetChanged()
                }
            }
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {

            // File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

            // File photo = new File(dir, System.currentTimeMillis() + ".jpg");

            try {
                val photo = File.createTempFile(
                        System.currentTimeMillis().toString(), /* prefix */
                        ".jpg", /* suffix */
                        storageDir      /* directory */
                )

                //                imageUri = Uri.fromFile(photo);
                imageUri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", photo)
                Log.d("yjs", "Uri : " + imageUri)
                imagePath = photo.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, FROM_CAMERA)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    companion object CREATOR : Parcelable.Creator<FindVideoGridActivity> {
        override fun createFromParcel(parcel: Parcel): FindVideoGridActivity {
            return FindVideoGridActivity(parcel)
        }

        override fun newArray(size: Int): Array<FindVideoGridActivity?> {
            return arrayOfNulls(size)
        }
    }

    override fun onBackPressed() {
            finish()
            Utils.hideKeyboard(context)
    }

}
