package com.devstories.starball_android.fcm

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import com.devstories.starball_android.activities.IntroActivity
import com.devstories.starball_android.base.PrefUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationChannelGroup



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

        val title = remoteMessage.notification!!.title
        val body = remoteMessage.notification!!.body
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
//        intent.putExtra("FROM_PUSH", true)

        val push_type = data["type"]

        if (isAppRunning(this)) {

            if (push_type == "chatting") {
                val broadcastIntent = Intent()
                broadcastIntent.putExtra("room_id", data["room_id"]!!.toInt())
                broadcastIntent.putExtra("contents", data["contents"])
                broadcastIntent.putExtra("created", data["created_at"])
                broadcastIntent.action = "PUSH_CHATTING"
                this.sendBroadcast(broadcastIntent)

                if (data["chatting_animation"]!!.toBoolean()) {
                    val broadcastIntent = Intent()
                    broadcastIntent.action = "CHATTING_ANIMATION"
                    this.sendBroadcast(broadcastIntent)
                }

            } else if (push_type == "propose") {
                val broadcastIntent = Intent()
                broadcastIntent.putExtra("propose_id", data["propose_id"]!!.toInt())
                broadcastIntent.action = "PROPOSE"
                this.sendBroadcast(broadcastIntent)
            } else if (push_type == "like") {
                val broadcastIntent = Intent()
                broadcastIntent.action = "LIKE_ANIMATION"
                this.sendBroadcast(broadcastIntent)
            } else if (push_type == "matching") {
                val broadcastIntent = Intent()
                broadcastIntent.putExtra("man_url", data["man_url"])
                broadcastIntent.putExtra("woman_url", data["woman_url"])
                broadcastIntent.action = "STARBALL_USE"
                this.sendBroadcast(broadcastIntent)
            }

        }

        if (push_type == "chatting") {
            intent.putExtra("room_id", data["room_id"]!!.toInt())
            intent.putExtra("chatting_member_id", data["chatting_member_id"]!!.toInt())
            intent.putExtra("chatting_animation", data["chatting_animation"]!!.toBoolean())
        } else if (push_type == "propose") {
            intent.putExtra("propose_id", data["propose_id"]!!.toInt())
        } else if (push_type == "like") {
            intent.putExtra("like_animation", true)
        } else if (push_type == "matching") {
            intent.putExtra("man_url", data["man_url"])
            intent.putExtra("woman_url", data["woman_url"])
        }

        intent.putExtra("FROM_PUSH", true)
        intent.putExtra("PUSH_TYPE", data["type"])

        val pendingIntent = PendingIntent.getActivity(this, System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(com.devstories.starball_android.R.mipmap.ic_launcher)
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
                    .setSmallIcon(com.devstories.starball_android.R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, com.devstories.starball_android.R.mipmap.ic_launcher))
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
    private fun createChannel(notificationManager: NotificationManager, channelId: String, title: String?, body: String?) {

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

    private fun isAppRunning(context: Context): Boolean {
        val PackageName = packageName
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val componentInfo: ComponentName?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val tasks = manager.appTasks
            componentInfo = tasks[0].taskInfo.topActivity
        } else {
            val tasks = manager.getRunningTasks(1)
            componentInfo = tasks[0].topActivity
        }

        if (null != componentInfo) {
            if (componentInfo.packageName == PackageName) {
                return true
            }
        }
        return false
    }

}
