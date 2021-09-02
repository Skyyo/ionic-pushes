package com.skyyo.groupingandreplytonotifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class MessageDismissedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent?.extras != null) {
            val userId = intent.getIntExtra("userId", -99)
            ActiveNotificationsHandler.activeNotifications.remove(userId)
        }

    }
}