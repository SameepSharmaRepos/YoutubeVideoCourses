package com.example.videoteacher.database

import android.content.Context
import androidx.room.*
import com.example.videoteacher.dao.MainActDao
import com.example.videoteacher.model.Course
import com.example.videoteacher.model.VideoItem

@Database(entities = arrayOf(VideoItem::class, Course::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class VideoDb:RoomDatabase() {

    abstract fun mainActDao():MainActDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: VideoDb? = null

        fun getDatabase(context: Context): VideoDb {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VideoDb::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}