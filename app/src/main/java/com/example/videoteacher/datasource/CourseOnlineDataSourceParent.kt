package com.example.videoteacher.datasource

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.videoteacher.dao.MainActDao
import com.example.videoteacher.datasource.network.CourseService
import com.example.videoteacher.model.Course
import com.olm.magtapp.data.factory.course.CourseDataSourceFactory
import kotlinx.coroutines.CoroutineScope


class CourseOnlineDataSourceParent (
    courseApi: CourseService,
    private val mainDao: MainActDao,
    application: Application
){

        private val context = application
        private val service = courseApi
        var pagedList: LiveData<PagedList<Course>>? = null

        fun getVideoList(scope: CoroutineScope, category:String, offline: CourseOfflineDataSource): LiveData<PagedList<Course>> {
            // Call the New Data Source for Getting the List of News
            val dataSourceFactory = CourseDataSourceFactory(category,service,scope,offline)
            val config = (PagedList.Config.Builder())
                .setPageSize(25)
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .build()
            pagedList = (LivePagedListBuilder(dataSourceFactory,config)).build()
            return pagedList!!
        }

    }