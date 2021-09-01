package com.skyyo.groupingandreplytonotifications

import android.annotation.SuppressLint
import android.app.Notification
import android.app.Notification.Builder.recoverBuilder
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.skyyo.groupingandreplytonotifications.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    //this part is view related, doesn't matter
    private lateinit var binding: ActivityMainBinding
    private var notificationId = 0
    private val CHANNEL_ID = "testChannel"
    private val GROUP = "summary"

    private val activeNotifications = mutableMapOf<Int, NotificationCompat.MessagingStyle>()
    private val activeNotificationsNewApi = mutableMapOf<Int, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this part is view related, doesn't matter
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //we subscribe to the topic here, for testing purposes. You won't need this also
        FirebaseMessaging.getInstance().subscribeToTopic("testTopic")
        createNotificationChannel()

        binding.btn.setOnClickListener { createNotificationWithReplyFeature() }

    }


    private fun createNotificationWithReplyFeature() {
        //approach with storing style -> userId+style
//        val userId = 911
//        val userName = "Name:$notificationId"
//        val person: Person = Person.Builder().apply {
//            setKey("$userId")
//            setName(userName)
//            setIcon(
//                IconCompat.createWithResource(
//                    this@MainActivity,
//                    android.R.drawable.ic_lock_power_off
//                )
//            )
//        }.build()
//        val messagingStyle: NotificationCompat.MessagingStyle
//
//        if (activeNotifications.containsKey(userId)) {
//            //style object exists, we just need to update it
//            messagingStyle = activeNotifications.getValue(userId)
//        } else {
//            // we need to create new style object store & display it
//            messagingStyle = NotificationCompat.MessagingStyle(person).setGroupConversation(true)
//            activeNotifications[userId] = messagingStyle
//        }
//
//        val message = NotificationCompat.MessagingStyle.Message(
//            "$notificationId",
//            System.currentTimeMillis(),
//            person
//        )
//        messagingStyle.addMessage(message)
//        messagingStyle.build()
//
////        val deleteIntent = PendingIntent.DE
//        val notificationBuilder = NotificationCompat.Builder(this, "testChannel")
//            .setSmallIcon(android.R.drawable.ic_lock_idle_charging)
//            .setChannelId(CHANNEL_ID)
//            .setGroup(GROUP)
//        .setDeleteIntent(createOnDismissedIntent(notificationId))
//            .setStyle(messagingStyle)
//            .setAutoCancel(true)
//            .setContentTitle("Content tilte")
//            .setContentText("$notificationId")
//
//        NotificationManagerCompat.from(this).notify(0, notificationBuilder.build())


        //approach with revocering -> userId+notificationId
        if (notificationId == 0) {
            createInitialNotification()
        } else {
            updateNotification()
        }

        notificationId++
        if (notificationId > 10) {
            activeNotifications.clear()
            notificationId = 0
        }
    }

    private fun createInitialNotification() {
        val userId = 911
        val userName = "Name:$notificationId"
        val person: Person = Person.Builder().apply {
            setKey("$userId")
            setName(userName)
            setIcon(
                IconCompat.createWithResource(
                    this@MainActivity,
                    android.R.drawable.ic_lock_power_off
                )
            )
        }.build()
        val messagingStyle: NotificationCompat.MessagingStyle =
            NotificationCompat.MessagingStyle(person).setGroupConversation(true)
        val message = NotificationCompat.MessagingStyle.Message(
            "$notificationId",
            System.currentTimeMillis(),
            person
        )
        messagingStyle.addMessage(message)

        val notificationBuilder = NotificationCompat.Builder(this, "testChannel")
            .setSmallIcon(android.R.drawable.ic_lock_idle_charging)
            .setChannelId(CHANNEL_ID)
            .setGroup(GROUP)
            .setDeleteIntent(createOnDismissedIntent(notificationId))
            .setStyle(messagingStyle)
            .setAutoCancel(true)
            .setContentTitle("Content tilte")
            .setContentText("$notificationId")

        NotificationManagerCompat.from(this).notify(0, notificationBuilder.build())
    }

    @SuppressLint("NewApi")
    private fun updateNotification() {
        // Filter out notification by `notificationId`
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val statusBarNotification = notificationManager.activeNotifications.firstOrNull {
            it.id == 0
        } ?: return //TODO check!
        val notification = statusBarNotification.notification
        // Add new `Message` to original `Notification.Builder`
        val recoveredNotificationBuilder = recoverBuilder(this, notification)
        recoveredNotificationBuilder.also {
            val recoveredStyle = it.style as Notification.MessagingStyle
            recoveredStyle.addMessage(
                "$notificationId",
                System.currentTimeMillis(),
                recoveredStyle.user
            )
            it.style = recoveredStyle
        }

        NotificationManagerCompat.from(this).notify(0, recoveredNotificationBuilder.build())
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannelCompat.Builder(
            CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_HIGH
        ).setName(CHANNEL_ID).build()
        NotificationManagerCompat.from(this).createNotificationChannel(channel)
    }

    private fun createOnDismissedIntent(notificationId: Int): PendingIntent? {
        val intent = Intent(this, MessageDismissedReceiver::class.java)
        intent.putExtra("com.skyyo.groupingandreplytonotifications", notificationId)
        return PendingIntent.getBroadcast(this.applicationContext, notificationId, intent, PendingIntent.FLAG_IMMUTABLE)
    }

}





