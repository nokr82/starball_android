package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.base.Config
import com.devstories.starball_android.base.RootActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.share.Sharer
import com.facebook.share.model.ShareMediaContent
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import kotlinx.android.synthetic.main.dlg_recommend.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class DlgRecommendActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var chatting_id = -1
    var chatting_contents = ""
    var delete = false

    var instaShareImageUri:Uri? = null
    var facebookShareImageUri:Uri? = null

    val INSTAGRAM_REQUEST_CODE = 1001

    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_recommend)

        this.context = this
        progressDialog = ProgressDialog(context)

        callbackManager = CallbackManager.Factory.create();

        facebookTV.setOnClickListener {
            shareFacebook()
        }

        instagramTV.setOnClickListener {
            shareInstagram()
        }

    }


    private fun shareInstagram() {

        //외부저장 권한 요청(안드로이드 6.0 이후 필수)
        onRequestPermission(1)

    }

    private fun shareFacebook() {
        //외부저장 권한 요청(안드로이드 6.0 이후 필수)
        onRequestPermission(2)

    }


    private var permissionCheck = false
    var REQUEST_EXTERNAL_STORAGE_CODE = 1

    private var requestPermission: Int = 1

    fun onRequestPermission(type: Int) {

        requestPermission = type

        val permissionReadStorage = ContextCompat.checkSelfPermission(FacebookSdk.getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionWriteStorage = ContextCompat.checkSelfPermission(FacebookSdk.getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionReadStorage == PackageManager.PERMISSION_DENIED || permissionWriteStorage == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_EXTERNAL_STORAGE_CODE)
        } else {
            permissionCheck = true //이미 허용되어 있으므로 PASS

            if(requestPermission == 1) {
                doShareInstagram();
            } else if(requestPermission == 2) {
                doShareFacebook();
            }
        }
    }


    private fun doShareFacebook() {

        if (!ShareDialog.canShow(SharePhotoContent::class.java)) {
            Toast.makeText(context, getString(R.string.facebook_required), Toast.LENGTH_SHORT).show()
            return
        }

//        facebookShareImageUri = Uri.parse("android.resource://" + packageName + "/" + R.mipmap.main_starball);

        var bm = BitmapFactory.decodeResource(getResources(), R.drawable.share);

        var extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        var file = File(extStorageDirectory, "starball.png");
        var outStream: FileOutputStream? = null;
            try {
                outStream = FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.flush();
                outStream.close();

            } catch (e: FileNotFoundException) {
                e.printStackTrace();
            } catch (e: IOException) {
                e.printStackTrace();
            }

        facebookShareImageUri = Uri.fromFile(file);

        val photo = SharePhoto.Builder()
            .setImageUrl(facebookShareImageUri)
            .build();

        val content = SharePhotoContent.Builder()
            .addPhoto(photo)
            .build();

        val shareDialog = ShareDialog(this)

        shareDialog.registerCallback(callbackManager, object: FacebookCallback<Sharer.Result> {
            override fun onSuccess(result: Sharer.Result?) {
                contentResolver.delete(facebookShareImageUri, null, null)
            }

            override fun onError(error: FacebookException?) {
                contentResolver.delete(facebookShareImageUri, null, null)
            }

            override fun onCancel() {
                contentResolver.delete(facebookShareImageUri, null, null)
            }
        })

        if (ShareDialog.canShow(SharePhotoContent::class.java)) {
            shareDialog.show(content);
        } else{

            contentResolver.delete(facebookShareImageUri, null, null)

            Toast.makeText(context, getString(R.string.facebook_required), Toast.LENGTH_SHORT).show()
        }

        Handler().postDelayed({
            contentResolver.delete(facebookShareImageUri, null, null)
        }, 1000 * 30)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE_CODE -> for (i in permissions.indices) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission == android.Manifest.permission.READ_EXTERNAL_STORAGE) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        permissionCheck = true

                        if(requestPermission == 1) {
                            doShareInstagram();
                        } else if(requestPermission == 2) {
                            doShareFacebook();
                        }

                    } else {
                        Toast.makeText(this.context, getString(R.string.allow_permission), Toast.LENGTH_LONG).show()
                        permissionCheck = false
                    }
                } else if (permission == android.Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        permissionCheck = true

                        if(requestPermission == 1) {
                            doShareInstagram();
                        } else if(requestPermission == 2) {
                            doShareFacebook();
                        }

                    } else {
                        Toast.makeText(this.context, getString(R.string.allow_permission), Toast.LENGTH_LONG).show()
                        permissionCheck = false
                    }
                }
            }
        }
    }

    private fun doShareInstagram() {

        var bm = BitmapFactory.decodeResource(getResources(), R.drawable.share);

        var extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        var file = File(extStorageDirectory, "starball.png");
        var outStream: FileOutputStream? = null;

        try {

            outStream = FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (e: FileNotFoundException) {
            e.printStackTrace();
        } catch (e: IOException) {
            e.printStackTrace();
        }

        instaShareImageUri = Uri.fromFile(file);

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareInstagramReally(shareIntent)

    }

    private fun shareInstagramReally(shareIntent: Intent) {
        try {

            // shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + path));

            shareIntent.putExtra(Intent.EXTRA_STREAM, instaShareImageUri);
            shareIntent.setPackage("com.instagram.android")
            startActivityForResult(shareIntent, INSTAGRAM_REQUEST_CODE)

            Handler().postDelayed({
                contentResolver.delete(instaShareImageUri, null, null)
            }, 1000 * 30)

        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()

            Toast.makeText(context, getString(R.string.instagram_not_installed), Toast.LENGTH_SHORT).show()

            contentResolver.delete(instaShareImageUri, null, null)

        } catch (e: Exception) {
            e.printStackTrace()

            Toast.makeText(context, getString(R.string.instagram_not_installed), Toast.LENGTH_SHORT).show()

            contentResolver.delete(instaShareImageUri, null, null)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                INSTAGRAM_REQUEST_CODE -> {
                    if(instaShareImageUri != null) {
                        contentResolver.delete(instaShareImageUri, null, null)
                    }
                }
            }
        }
    }

}
