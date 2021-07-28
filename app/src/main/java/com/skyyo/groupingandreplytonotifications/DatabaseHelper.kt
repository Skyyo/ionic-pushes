package com.skyyo.groupingandreplytonotifications

import android.content.Context
import androidx.room.Room
import com.skyyo.persistance.AppDatabase

class DatabaseHelper(context: Context) {

    private val dataBase = Room.databaseBuilder(context, AppDatabase::class.java, "pushes_database")
        .fallbackToDestructiveMigration()
        .build()
    private val pushNotificationsDao = dataBase.pushNotificationsDao()

    fun clearDatabase() = dataBase.clearAllTables()

    suspend fun getDatabaseEntries(): String {
        val listOfPushNotificationContents = pushNotificationsDao.getPushNotificationContents()
        return buildString {
            listOfPushNotificationContents.forEach { content ->
                append("${content.id}:${content.title}")
                appendLine()
            }
        }
    }
}