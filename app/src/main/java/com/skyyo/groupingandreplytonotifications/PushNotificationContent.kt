package com.skyyo.groupingandreplytonotifications

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pushes_table")
class PushNotificationContent(
    @PrimaryKey(autoGenerate = true)
    val id: Int, // use notificationId if you have those
    val title: String?,
    val description: String?
)