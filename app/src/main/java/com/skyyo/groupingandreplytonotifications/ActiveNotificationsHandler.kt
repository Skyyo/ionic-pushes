package com.skyyo.groupingandreplytonotifications

import androidx.core.app.NotificationCompat

object ActiveNotificationsHandler {
    val activeNotifications = mutableMapOf<Int, NotificationCompat.MessagingStyle>()
}
