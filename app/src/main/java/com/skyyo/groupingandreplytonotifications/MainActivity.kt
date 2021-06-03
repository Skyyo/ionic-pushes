package com.skyyo.groupingandreplytonotifications

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.skyyo.groupingandreplytonotifications.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnShowNotification.setOnClickListener { showNotificationGrouping() }
        FirebaseMessaging.getInstance().subscribeToTopic("testTopic")
    }

    private fun showNotificationGrouping() {
        //notification channel creation ( required since Android 8 #Oreo)
        val channelId = "your_app_name_channel_id" // doesn't matter much, but rename please
        val channelName = "your_app_name_channel_name" // doesn't matter much, but rename please
        val channel = NotificationChannelCompat.Builder(
            channelId, NotificationManagerCompat.IMPORTANCE_HIGH
        ).setName(channelName).build()
        NotificationManagerCompat.from(this).createNotificationChannel(channel)

        val notificationGroup =
            "notification_group" // eg: group chat, or messages from the same user/topic

        //these ids are important only if you want to update/remove particular notification
        val notificationId1 = 1
        val notificationId2 = 2
        val notificationId3 = 3
        val notificationSummaryId = 4

        //notification objects
        val notification0 = NotificationCompat.Builder(this, channelId)
            .setContentText("desc1")
            .setContentTitle("title1")
            .setColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright))
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setGroup(notificationGroup)
            .build()

//        val notification1 = NotificationCompat.Builder(this, channelId)
//            .setContentText("desc2")
//            .setContentTitle("title2")
//            .setColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright))
//            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
//            .setGroup(notificationGroup)
//            .build()
//
//        val notification2 = NotificationCompat.Builder(this, channelId)
//            .setContentText("desc3")
//            .setContentTitle("title3")
//            .setColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright))
//            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
//            .setGroup(notificationGroup)
//            .build()

        //notification summary. This code is what creates the notification summary/grouping ( think of X + summary for each group_id)
        val notificationSummary = NotificationCompat.Builder(this, channelId)
            .setStyle(NotificationCompat.InboxStyle())
            .setColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright))
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setGroup(notificationGroup)
            .setGroupSummary(true)
            .build()

        //this part creates the notifications by providing the notification object + notification id to the android notification manager
        NotificationManagerCompat.from(this).notify(notificationId1, notification0)
//        NotificationManagerCompat.from(this).notify(notificationId2, notification1)
//        NotificationManagerCompat.from(this).notify(notificationId3, notification2)
        NotificationManagerCompat.from(this).notify(notificationSummaryId, notificationSummary)

    }


}





