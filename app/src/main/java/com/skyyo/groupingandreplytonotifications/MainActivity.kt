package com.skyyo.groupingandreplytonotifications

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.firebase.messaging.FirebaseMessaging
import com.skyyo.groupingandreplytonotifications.databinding.ActivityMainBinding
import com.skyyo.persistance.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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


        val databaseHelper = DatabaseHelper(this)
        GlobalScope.launch(Dispatchers.IO) {
            binding.tvHistory.text = databaseHelper.getDatabaseEntries()
        }

        binding.btnClearData.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                databaseHelper.clearDatabase()
                val entries = databaseHelper.getDatabaseEntries()
                withContext(Dispatchers.Main) {
                    binding.tvHistory.text = entries
                }
            }
        }
    }


}





