package com.example.videoteacher.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class Converters {
    companion object {

        @TypeConverter
        @JvmStatic
        fun interestStringToList(json: String?): List<String> {
            return if (json==null) emptyList()
            else {
                val listType = object : TypeToken<List<String>>() {}.type
                Gson().fromJson(json , listType )
            }
        }

        @TypeConverter
        @JvmStatic
        fun interestListToString(list: List<String>): String {
            val type = object : TypeToken<List<String>>() { }.type
            return Gson().toJson(list, type)
        }

        fun dateToTimestamp(date: Date?): Long? {
            return date?.time?.toLong()
        }
        fun fromTimestamp(value: Long?): Date? {
            return value?.let { Date(it) }
        }
    }
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

}