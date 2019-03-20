package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.utils.Coomon
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import kotlinx.android.synthetic.main.dlg_recommend.*

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


        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        this.context = this
        progressDialog = ProgressDialog(context)

        callbackManager = CallbackManager.Factory.create()

        facebookTV.setOnClickListener {
            shareFacebook()
        }

        instagramTV.setOnClickListener {
            shareInstagram()
        }

    }

    private fun shareFacebook() {
        if (!ShareDialog.canShow(SharePhotoContent::class.java)) {
            Toast.makeText(context, getString(R.string.facebook_required), Toast.LENGTH_SHORT).show()
            return
        }

        val image = BitmapFactory.decodeResource(resources, R.drawable.share)

        val photo = SharePhoto.Builder()
            .setBitmap(image)
            .build()

        val content = SharePhotoContent.Builder()
            .addPhoto(photo)
            .build()

        val shareDialog = ShareDialog(this)
        shareDialog.registerCallback(callbackManager, object: FacebookCallback<Sharer.Result> {
            override fun onSuccess(result: Sharer.Result?) {
                Coomon.freeStraball(context, 3)
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(context, getString(R.string.facebook_required), Toast.LENGTH_SHORT).show()
            }

            override fun onCancel() {
                Toast.makeText(context, getString(R.string.facebook_required), Toast.LENGTH_SHORT).show()
            }
        })

        if (ShareDialog.canShow(SharePhotoContent::class.java)) {
            shareDialog.show(content)
        } else {
            Toast.makeText(context, getString(R.string.facebook_required), Toast.LENGTH_SHORT).show()
        }

    }

    private fun shareInstagram() {

        val instaShareImageUri = Uri.parse("android.resource://$packageName/drawable/share");

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, instaShareImageUri)
        shareIntent.setPackage("com.instagram.android")

        try {
            startActivityForResult(shareIntent, INSTAGRAM_REQUEST_CODE)
        } catch (e:Exception) {
            Toast.makeText(context, getString(R.string.instagram_required), Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                INSTAGRAM_REQUEST_CODE -> {
                    Coomon.freeStraball(context, 8)
                }
            }
        }
    }

}
