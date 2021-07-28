package com.skyyo.persistance

import androidx.room.TypeConverter
import com.skyyo.groupingandreplytonotifications.PushNotificationContent
import com.squareup.moshi.*

object MoshiTypeConverters {
    private val moshi = Moshi.Builder().build()

//    private val listOfLongAdapter: JsonAdapter<List<Long>> =
//        moshi.adapter<List<Long>>(
//            Types.newParameterizedType(
//                List::class.java,
//                Long::class.javaObjectType
//            )
//        ).nonNull()
//
//    private val listOfStringAdapter: JsonAdapter<List<String>> =
//        moshi.adapter<List<String>>(
//            Types.newParameterizedType(
//                List::class.java,
//                String::class.javaObjectType
//            )
//        ).nonNull()

    private val listOfDiseasesAdapter: JsonAdapter<List<PushNotificationContent>> =
        moshi.adapter<List<PushNotificationContent>>(
            Types.newParameterizedType(
                List::class.java,
                PushNotificationContent::class.javaObjectType
            )
        ).nonNull()


//    @TypeConverter
//    @JvmStatic
//    @ToJson
//    fun toLongs(value: String): List<Long>? {
//        return listOfLongAdapter.fromJson(value) as List<Long>
//    }
//
//    @TypeConverter
//    @JvmStatic
//    @FromJson
//    fun fromLongs(value: List<Long>?): String? = listOfLongAdapter.toJson(value)
//
//    @TypeConverter
//    @JvmStatic
//    @ToJson
//    fun toStrings(value: String): List<String>? {
//        return listOfStringAdapter.fromJson(value) as List<String>
//    }
//
//    @TypeConverter
//    @JvmStatic
//    @FromJson
//    fun fromStrings(value: List<String>?): String? = listOfStringAdapter.toJson(value)

    @TypeConverter
    @JvmStatic
    @ToJson
    fun toPushNotificationContents(value: String): List<PushNotificationContent>? {
        return listOfDiseasesAdapter.fromJson(value) as List<PushNotificationContent>
    }

    @TypeConverter
    @JvmStatic
    @FromJson
    fun fromPushNotificationContents(value: List<PushNotificationContent>?): String? =
        listOfDiseasesAdapter.toJson(value)


    @TypeConverter
    @JvmStatic
    @ToJson
    fun fromPushNotificationContent(data: PushNotificationContent?): String? {
        if (data == null) {
            return null
        }
        return moshi.adapter(PushNotificationContent::class.java).toJson(data)
    }

    @TypeConverter
    @JvmStatic
    @ToJson
    fun toPushNotificationContent(json: String?): PushNotificationContent? {
        if (json == null) {
            return null
        }
        return moshi.adapter(PushNotificationContent::class.java).fromJson(json)
    }

}
