package com.skyyo.groupingandreplytonotifications

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.google.firebase.messaging.FirebaseMessaging
import com.skyyo.groupingandreplytonotifications.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    //this part is view related, doesn't matter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this part is view related, doesn't matter
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //we subscribe to the topic here, for testing purposes. You won't need this also
        FirebaseMessaging.getInstance().subscribeToTopic("testTopic")

        // this is how we get the values from our notification, when app starts as a reaction
        // to user tapping on notification
        handleNotificationTap(intent.extras)

        binding.btn.setOnClickListener { createNotificationWithReplyFeature() }
    }

    //this will be called when app is already running, and user taps on notification
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleNotificationTap(intent?.extras)
    }

    private fun createNotificationWithReplyFeature() {
        // The direct reply action, introduced in Android 7.0 (API level 24)
        if (Build.VERSION.SDK_INT >= 24) {
            // Create an instance of remote input builder
            val remoteInput: RemoteInput = RemoteInput.Builder("KEY_TEXT_REPLY").apply {
                setLabel("Write your message here")
            }.build()

            // Create an intent
            val intent = Intent(this, DirectReplyReceiver::class.java).apply {
                action = "REPLY_ACTION"
                putExtra("KEY_NOTIFICATION_ID", 2)
                putExtra("KEY_CHANNEL_ID", "testChannel")
                putExtra("KEY_MESSAGE_ID", 23)
            }

            // Create a pending intent for the reply button
            val replyPendingIntent: PendingIntent = PendingIntent.getBroadcast(
                this,
                101,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            // Create reply action and add the remote input
            val action: NotificationCompat.Action = NotificationCompat.Action.Builder(
                android.R.drawable.ic_btn_speak_now,
                "Reply",
                replyPendingIntent
            ).addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build()


            // we always need to create the notification channel before showing notifications.
            // Inside the FcmService.kt we are doing it inside onCreate()
            createNotificationChannel()

            // Build a notification and add the action
            val builder = NotificationCompat.Builder(this, "testChannel")
                .setSmallIcon(android.R.drawable.ic_lock_idle_charging)
                .setContentTitle("Johny")
                .setContentText("you are a dead man amigo!")
                .addAction(action)

            // issue the notification
            NotificationManagerCompat.from(this).apply {
                notify(2, builder.build())
            }
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannelCompat.Builder(
            "testChannel",
            NotificationManagerCompat.IMPORTANCE_HIGH
        ).setName("testChannel").build()
        NotificationManagerCompat.from(this).createNotificationChannel(channel)
    }

    private fun handleNotificationTap(extras: Bundle?) {
        when (extras?.get("type")) {
            "simpleNotification" -> {
                //user tapped on a specific notification
                val notificationId = intent.extras?.get("notificationId")
            }
            "summaryNotification" -> {
                //user tapped on a summary(group) notification
                val notificationId = intent.extras?.get("notificationId")
                val groupId = intent.extras?.get("groupId")
            }
        }
    }

}





