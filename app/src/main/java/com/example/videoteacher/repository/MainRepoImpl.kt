package com.example.videoteacher.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.videoteacher.datasource.CourseOfflineDataSource
import com.example.videoteacher.datasource.CourseOnlineDataSource
import com.example.videoteacher.model.Course
import com.example.videoteacher.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainRepoImpl(
    private val onlineDataSource: CourseOnlineDataSource,
    private val offlineDataSource: CourseOfflineDataSource
) {

    private lateinit var scope:CoroutineScope


    suspend fun insertMainList(list: List<Course>) {
        offlineDataSource.insertMainList(list)
    }

    fun getSearchedCourseList(query:String): LiveData<List<Course>>? {
        scope.launch {
            //Log.e("IsNullData>>", "${offlineDataSource.getSavedCourses()?.value?.size} <<<")
            if (null==offlineDataSource.getSavedCourses()?.value){
                Log.e("Is NullLocal>>", "Yes ${query} <<<")
                onlineDataSource.searchCourse(query)
            }
        }
        return offlineDataSource.getSavedCourses()
    }

    fun setScope(coroutineScope: CoroutineScope){
        this.scope=coroutineScope
    }

    fun getPlaylist(id: String): LiveData<List<Course>> {
        val playList = offlineDataSource.getSavedPlaylist(id)
        Log.e("PlayListSizeOffline>>", "${playList.value?.size} <<<")
        scope.launch {
            if (null==playList.value)
            onlineDataSource.getPlayList(id)
        }

        return offlineDataSource.getSavedPlaylist(id)
    }
}