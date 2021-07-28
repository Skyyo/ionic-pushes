package com.skyyo.persistance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyyo.groupingandreplytonotifications.PushNotificationContent

@Dao
interface PushNotificationsDao {


//    @Query("DELETE FROM pushes_table")
//    suspend fun deleteScanModel()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPushNotificationContent(model: PushNotificationContent)

    @Query("SELECT * from pushes_table")
    suspend fun getPushNotificationContents(): List<PushNotificationContent>

}
