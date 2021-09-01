package com.skyyo.groupingandreplytonotifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class MessageDismissedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val notificationId = intent.extras!!.getInt("com.skyyo.groupingandreplytonotifications")
            Log.d("vovk", "notificationId $notificationId")
        }

    }
}