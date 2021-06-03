package com.skyyo.groupingandreplytonotifications

import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {

    //notification channel creation ( required since Android 8 #Oreo)
    val channelId = "your_app_name_channel_id" // doesn't matter much, but rename please
    val channelName = "your_app_name_channel_name" // doesn't matter much, but rename please

    override fun onCreate() {
        val channel = NotificationChannelCompat.Builder(channelId, IMPORTANCE_HIGH)
            .setName(channelName).build()
        NotificationManagerCompat.from(this).createNotificationChannel(channel)

    }

    override fun onNewToken(token: String) {
        // here you want to send a reliable request to the backend with new token
        // this is rarely called so prioritize accordingly
    }

    // this will be triggered always, if the payload contains "data" object without "notification" object
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val alertId = remoteMessage.data["id"]?.toInt() ?: return
        val title = remoteMessage.data["title"] ?: return
        val description = remoteMessage.data["description"] ?: return
        val groupName = remoteMessage.data["groupName"] ?: return
        val groupId = remoteMessage.data["groupId"]?.toInt() ?: return
        showNotification(alertId, title, description, groupName, groupId)
    }

    private fun showNotification(
        notificationId: Int,
        title: String,
        description: String,
        groupName: String,
        groupId: Int
    ) {
        //notification object
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentText(description)
            .setContentTitle(title)
            .setColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright))
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setGroup(groupName)
            .build()

        //notification summary object. This code is what creates the notification summary/grouping ( think of X + summary for each group_id)
        val notificationSummary = NotificationCompat.Builder(this, channelId)
            .setStyle(NotificationCompat.InboxStyle())
            .setColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright))
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setGroup(groupName)
            .setGroupSummary(true)
            .build()

        NotificationManagerCompat.from(this).apply {
            notify(notificationId, notification)
            notify(groupId, notificationSummary)
        }
        // this is how we cancel the notification, or notification summary ( group )
//        NotificationManagerCompat.from(this).cancel(notificationId)
        // or
//        NotificationManagerCompat.from(this).cancel(groupId)
    }

}
