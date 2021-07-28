package com.skyyo.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.skyyo.groupingandreplytonotifications.PushNotificationContent

@Database(
    version = 1,
    exportSchema = true,
    entities = [PushNotificationContent::class]
)

@TypeConverters(MoshiTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pushNotificationsDao(): PushNotificationsDao

}
