package com.example.videoteacher.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.videoteacher.model.Course
import com.example.videoteacher.model.VideoItem

@Dao
interface MainActDao {

    @Query("SELECT * FROM courses")
    fun getSavedVideosMain() : LiveData<List<Course>>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVideoList(videoList:List<Course>)

    @Query("SELECT * FROM courses WHERE id=:id")
    fun getSavedPlaylist(id: String): LiveData<List<Course>>


}