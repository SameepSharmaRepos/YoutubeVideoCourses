package com.example.videoteacher.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.videoteacher.dao.MainActDao
import com.example.videoteacher.model.Course
import kotlinx.coroutines.CoroutineScope

class CourseOfflineDataSource(private val mainActDao: MainActDao){

    private lateinit var scope:CoroutineScope

    fun getSavedCourses(): LiveData<PagedList<Course>>? {
        val factory: DataSource.Factory<Int, Course> = mainActDao.getSavedVideosMain()
        val pagedListBuilder: LivePagedListBuilder<Int, Course> = LivePagedListBuilder<Int, Course>(factory,10 )
        return pagedListBuilder.build()

    }

    suspend fun insertMainList(list: List<Course>){
        Log.e("OfflineDataSrc >>>", "${list.size} <<<")
        mainActDao.insertVideoList(list)
    }

    fun setScope(coroutineScope: CoroutineScope){
        this.scope=coroutineScope
    }

    fun getSavedPlaylist(id: String): LiveData<List<Course>> {

        return mainActDao.getSavedPlaylist(id)

    }

}