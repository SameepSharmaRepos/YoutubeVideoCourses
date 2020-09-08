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

    fun getMainCourseList(): LiveData<List<Course>>? {
        scope.launch {
            //Log.e("IsNullData>>", "${offlineDataSource.getSavedCourses()?.value?.size} <<<")
            if (null==offlineDataSource.getSavedCourses()?.value){
                Log.e("Is NullLocal>>", "Yes ${MainActivity.INIT_QUERY} <<<")
                onlineDataSource.searchCourse(MainActivity.INIT_QUERY)
            }
        }
        return offlineDataSource.getSavedCourses()
    }

    fun setScope(coroutineScope: CoroutineScope){
        this.scope=coroutineScope
    }


}