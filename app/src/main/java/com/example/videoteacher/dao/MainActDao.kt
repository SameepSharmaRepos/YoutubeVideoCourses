package com.example.videoteacher.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.videoteacher.model.Course
import com.example.videoteacher.model.VideoItem

@Dao
interface MainActDao {

    @Query("SELECT * FROM courses")
    fun getSavedVideosMain() : LiveData<List<Course>>?

    @Insert
    fun insertVideoList(videoList:List<Course>)


}