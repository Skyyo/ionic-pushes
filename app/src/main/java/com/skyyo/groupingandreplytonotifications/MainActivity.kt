package com.skyyo.groupingandreplytonotifications

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessaging
import com.skyyo.groupingandreplytonotifications.databinding.ActivityMainBinding
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    //this part is view related, doesn't matter
    private lateinit var binding: ActivityMainBinding

    override fun onResume() {
        super.onResume()
        isAppPaused = false
    }

    override fun onPause() {
        isAppPaused = true
        super.onPause()
    }

    override fun onStop() {
        isAppPaused = true
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            for (notification in NotificationEventDispatcher.emitter) {
                val activeChatId: Int? = 99   //TODO invoke ionic callback
                if (notification.chatId == activeChatId) {
                    //TODO display notification
                }
            }
        }
    }
}





