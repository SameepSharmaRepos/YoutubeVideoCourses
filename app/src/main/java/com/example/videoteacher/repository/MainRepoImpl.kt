package com.example.videoteacher.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.videoteacher.datasource.CourseOfflineDataSource
import com.example.videoteacher.datasource.CourseOnlineDataSource
import com.example.videoteacher.model.Course
import com.example.videoteacher.util.isNetworkAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainRepoImpl(
    private val onlineDataSource: CourseOnlineDataSource,
    private val offlineDataSource: CourseOfflineDataSource,
    private val context: Application
) {

    private lateinit var scope: CoroutineScope


    suspend fun insertMainList(list: List<Course>) {
        offlineDataSource.insertMainList(list)
    }

    fun getSearchedCourseList(query: String): LiveData<PagedList<Course>>? {

        return if(context.isNetworkAvailable){
            Log.e("NetWorkAvailable>>", "Yes <<<")
            onlineDataSource.searchCourseNew(query)
        }else{

            Log.e("NetWorkAvailable>>", "Yes <<<")
            offlineDataSource.getSavedCourses()
        }
    }

    fun setScope(coroutineScope: CoroutineScope) {
        this.scope = coroutineScope
    }

    fun getPlaylist(id: String): LiveData<PagedList<Course>> {

                return onlineDataSource.getPlayList(id)
       }
}