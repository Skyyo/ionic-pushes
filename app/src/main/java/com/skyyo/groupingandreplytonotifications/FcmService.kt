package com.skyyo.groupingandreplytonotifications

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import androidx.core.app.Person
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


var isAppPaused = true

class FcmService : FirebaseMessagingService() {

    //notification channel creation ( required since Android 8 #Oreo)
    private val channelId = "your_app_name_channel_id" // doesn't matter much, but rename please
    private val channelName = "your_app_name_channel_name" // doesn't matter much, but rename please
    private val activeNotifications = ActiveNotificationsHandler.activeNotifications

    override fun onCreate() {
        val channel =
            NotificationChannelCompat.Builder(channelId, IMPORTANCE_HIGH).setName(channelName)
                .build()
        NotificationManagerCompat.from(this).createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) {
        // here you want to send a reliable request to the backend with new token
        // this is rarely called so prioritize accordingly
    }

    // this will be triggered always, if the payload contains "data" object without "notification" object
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.data["title"] ?: return
        val userId = remoteMessage.data["userId"]?.toInt() ?: return
        val chatId = remoteMessage.data["chatId"]?.toInt() ?: return
        val userName = remoteMessage.data["userName"] ?: return
        val userMessage = remoteMessage.data["userMessage"] ?: return
        val groupName = remoteMessage.data["groupName"] ?: return


        //We are in background or app is terminated, so we want to show notification
        if (isAppPaused) {
            showNotification(title, chatId, userId, userName, userMessage, groupName)
        } else {
            //we are asking MainActivity to be responsible for dropping or showing notification
            val notification = NotificationModel(userId, userName, chatId)
            NotificationEventDispatcher.emit(notification)
        }
    }

    private fun showNotification(
        title: String,
        chatId: Int,
        userId: Int,
        userName: String,
        userMessage: String,
        group: String,
    ) {
        val person: Person = Person.Builder().apply {
            setKey("$userId")
            setName(userName)
        }.build()
        val messagingStyle: NotificationCompat.MessagingStyle

        if (activeNotifications.containsKey(chatId)) {
            //style object exists, we just need to update it
            messagingStyle = activeNotifications.getValue(chatId)
        } else {
            // we need to create new style object store & display it
            messagingStyle = NotificationCompat.MessagingStyle(person).setGroupConversation(true)
            activeNotifications[chatId] = messagingStyle
        }
        val message = NotificationCompat.MessagingStyle.Message(
            userMessage, // actual message
            System.currentTimeMillis(),
            person
        )
        messagingStyle.addMessage(message)
        messagingStyle.build()

        //code that allows us to open activity on notification tap
        val contentIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("type", "simpleNotification")
            putExtra("notificationId", "$chatId")
        }
        val pendingContentIntent = PendingIntent.getActivity(
            this,
            231,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_charging)
            .setGroup(group)
            .setContentIntent(pendingContentIntent)
            .setDeleteIntent(createOnDismissedIntent(chatId))
            .setStyle(messagingStyle)
            .setAutoCancel(true)
            .setContentTitle(title)

        NotificationManagerCompat.from(this).notify(chatId, notificationBuilder.build())
    }

    private fun createOnDismissedIntent(chatId: Int): PendingIntent? {
        val intent = Intent(this, MessageDismissedReceiver::class.java).apply {
            putExtra("chatId", chatId)
        }
        return PendingIntent.getBroadcast(
            applicationContext,
            chatId,
            intent,
            0
        )
    }
}
