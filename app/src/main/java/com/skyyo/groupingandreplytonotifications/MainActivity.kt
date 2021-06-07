package com.skyyo.groupingandreplytonotifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
    }

    //this will be called when app is already running, and user taps on notification
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleNotificationTap(intent?.extras)
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





