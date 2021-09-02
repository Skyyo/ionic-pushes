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
        //this part is view related, doesn't matter
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //we subscribe to the topic here, for testing purposes. You won't need this also
        FirebaseMessaging.getInstance().subscribeToTopic("testTopic")

        lifecycleScope.launch {
            for (notification in NotificationEventDispatcher.emitter) {
                Log.d("vovk", "event in activity")
                //TODO invoke ionic callback
                val activeChatId: Int? = 99
                if (notification.chatId == activeChatId) {
                    //TODO display notification
                }
            }
        }
    }
}





