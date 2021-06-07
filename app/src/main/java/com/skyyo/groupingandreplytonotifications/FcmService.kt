package com.skyyo.groupingandreplytonotifications

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.time.Instant
import java.util.*

class FcmService : FirebaseMessagingService() {

    //notification channel creation ( required since Android 8 #Oreo)
    val channelId = "your_app_name_channel_id" // doesn't matter much, but rename please
    val channelName = "your_app_name_channel_name" // doesn't matter much, but rename please
    val activityResultCode = 2313 // doesn't matter much

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
        val notificationType = remoteMessage.data["type"] ?: return
        when (notificationType) {
            "simpleNotification" -> showSimpleNotification(alertId, title, description)
            "summaryNotification" -> showSummaryNotification(
                alertId,
                title,
                description,
                groupName,
                groupId
            )
        }
    }

    private fun showReplyNotification() {
//        var message1 = NotificationCompat.MessagingStyle.Message(
//            "text",
//            Instant.now(),
//            "sender"
//        )
//        var message2 = NotificationCompat.MessagingStyle.Message(
//            messages[1].getText(),
//            messages[1].getTime(),
//            messages[1].getSender()
//        )
//        var notification = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setSmallIcon(R.drawable.new_message)
//            .setStyle(
//                NotificationCompat.MessagingStyle(resources.getString(R.string.reply_name))
//                    .addMessage(message1)
//                    .addMessage(message2)
//            )
//            .build()
    }

    private fun showSimpleNotification(
        notificationId: Int,
        title: String,
        description: String,
    ) {
        //code that allows us to open activity on notification tap
        val intentSimpleNotification = Intent(this, MainActivity::class.java).apply {

            // now this is related to the architecture of the app. you can find more info here,
            // https://developer.android.com/guide/components/activities/tasks-and-back-stack#IntentFlagsForTasks
            // and choose one that fits you best if needed. Also imporant part here is that activity is declared as singleInstance
            // inside manifest.xml
            flags = FLAG_ACTIVITY_SINGLE_TOP

            // you can put key/value pairs here. For example you tap on notification, it takes you
            // to Product Details Screen, and you want to fetch the info of that product.
            // That's a situation, when you want productId to be passed from backend into FCM payload, so
            // it can be used here.
            putExtra("type", "simpleNotification")
            putExtra("notificationId", notificationId)
        }
        val pendingIntentSimpleNotification = PendingIntent.getActivity(
            this,
            activityResultCode,
            intentSimpleNotification,
            FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
        )

        //notification object
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentText(description)
            .setContentTitle(title)
            .setColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright))
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setAutoCancel(true)
            .setContentIntent(pendingIntentSimpleNotification)
            .build()

        NotificationManagerCompat.from(this).notify(notificationId, notification)
        // this is how we cancel the notification
        // NotificationManagerCompat.from(this).cancel(notificationId)

    }

    private fun showSummaryNotification(
        notificationId: Int,
        title: String,
        description: String,
        groupName: String,
        groupId: Int
    ) {

        //code that allows us to open activity on notification tap
        val intentSimpleNotification = Intent(this, MainActivity::class.java).apply {

            // now this is related to the architecture of the app. you can find more info here,
            // https://developer.android.com/guide/components/activities/tasks-and-back-stack#IntentFlagsForTasks
            // and choose one that fits you best if needed. Also imporant part here is that activity is declared as singleInstance
            // inside manifest.xml
            flags = FLAG_ACTIVITY_SINGLE_TOP

            // you can put key/value pairs here. For example you tap on notification, it takes you
            // to Product Details Screen, and you want to fetch the info of that product.
            // That's a situation, when you want productId to be passed from backend into FCM payload, so
            // it can be used here.
            putExtra("type", "simpleNotification")
            putExtra("notificationId", notificationId)
        }
        val pendingIntentSimpleNotification = PendingIntent.getActivity(
            this,
            activityResultCode,
            intentSimpleNotification,
            FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
        )

        //code that allows us to open activity on summary notification tap
        val intentSummaryNotification = Intent(this, MainActivity::class.java).apply {

            // now this is related to the architecture of the app. you can find more info here,
            // https://developer.android.com/guide/components/activities/tasks-and-back-stack#IntentFlagsForTasks
            // and choose one that fits you best if needed. Also imporant part here is that activity is declared as singleInstance
            // inside manifest.xml
            flags = FLAG_ACTIVITY_SINGLE_TOP

            // you can put key/value pairs here. For example you tap on notification, it takes you
            // to Product Details Screen, and you want to fetch the info of that product.
            // That's a situation, when you want productId to be passed from backend into FCM payload, so
            // it can be used here.
            putExtra("type", "summaryNotification")
            putExtra("groupId", groupId)
        }
        val pendingIntentSummaryNotification = PendingIntent.getActivity(
            this,
            activityResultCode,
            intentSummaryNotification,
            FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
        )

        //notification object
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentText(description)
            .setContentTitle(title)
            .setColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright))
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentIntent(pendingIntentSimpleNotification)
            .setAutoCancel(true)
            .setGroup(groupName)
            .build()

        //notification summary object. This code is what creates the notification summary/grouping ( think of X + summary for each group_id)
        val notificationSummary = NotificationCompat.Builder(this, channelId)
            .setStyle(NotificationCompat.InboxStyle())
            .setColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright))
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentIntent(pendingIntentSummaryNotification)
            .setGroup(groupName)
            .setAutoCancel(true)
            .setGroupSummary(true)
            .build()

        NotificationManagerCompat.from(this).apply {
            notify(notificationId, notification)
            notify(groupId, notificationSummary)
        }
        // this is how we cancel notification summary ( group ) notification
        // NotificationManagerCompat.from(this).cancel(groupId)
    }
}
