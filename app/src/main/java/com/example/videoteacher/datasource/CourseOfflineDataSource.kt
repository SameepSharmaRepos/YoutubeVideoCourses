package com.example.videoteacher.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.videoteacher.dao.MainActDao
import com.example.videoteacher.model.Course
import kotlinx.coroutines.CoroutineScope

class CourseOfflineDataSource(private val mainActDao: MainActDao){

    private lateinit var scope:CoroutineScope

    fun getSavedCourses(): LiveData<List<Course>>? {

        Log.e("Fetching FromDb>> ", "Yes <<<")
        return mainActDao.getSavedVideosMain()

    }

    suspend fun insertMainList(list: List<Course>){
        mainActDao.insertVideoList(list)
    }

    fun setScope(coroutineScope: CoroutineScope){
        this.scope=coroutineScope
    }

}