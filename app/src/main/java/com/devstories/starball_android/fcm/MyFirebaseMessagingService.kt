package com.devstories.starball_android.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import com.devstories.starball_android.R
import com.devstories.starball_android.activities.IntroActivity
import com.devstories.starball_android.base.PrefUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by dev1 on 2017-12-15.
 */

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val mHandler: Handler

    init {

        mHandler = Handler()
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        if (remoteMessage == null) {
            return
        }

        val data = remoteMessage.data ?: return

        println("data:::::::::::::::::::::::::::::::${data}")

        val title = data["title"]
        val body = data["body"]
//        val channelId = getString(R.string.default_notification_channel_id)
        val channelId = "Starball"
        val group = channelId

        val intent = Intent(this, IntroActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("last_id", data["last_id"])
//        intent.putExtra("created", data["created"])
//        intent.putExtra("chatting_member_id", data["chatting_member_id"])
//        intent.putExtra("content_id", data["content_id"])
//        intent.putExtra("friend_id", data["friend_id"])

        intent.putExtra("FROM_PUSH", true)

        if (data["type"] == "chatting") {
            PrefUtils.setPreference(this, "room_id", data["room_id"]!!.toInt())
        }

        PrefUtils.setPreference(this, "PUSH_TYPE", data["type"])
        PrefUtils.setPreference(this, "FROM_PUSH", true)

        val pendingIntent = PendingIntent.getActivity(this, System.currentTimeMillis().toInt(), intent, 0)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setGroup(group)
                .setVibrate(longArrayOf(1000, 1000))
                .setContentIntent(pendingIntent)

        notificationBuilder.setSound(defaultSoundUri)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationManager, channelId, title, body)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val gnotificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                    .setGroup(group)
                    .setGroupSummary(true)
                    .setAutoCancel(true)

            notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
            notificationManager.notify(group, 0, gnotificationBuilder.build())
        } else {
            notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createChannel(
            notificationManager: NotificationManager,
            channelId: String,
            title: String?,
            body: String?
    ) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val mChannel = NotificationChannel(channelId, title, importance)
        mChannel.description = body
        mChannel.enableLights(true)
        mChannel.lightColor = Color.BLUE
        notificationManager.createNotificationChannel(mChannel)

    }

    companion object {
        private val TAG = "MyFirebaseMsgService"
    }

}
