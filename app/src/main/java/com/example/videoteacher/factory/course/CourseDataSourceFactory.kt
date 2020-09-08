package com.olm.magtapp.data.factory.course

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.videoteacher.datasource.network.CourseService
import com.example.videoteacher.model.Course
import kotlinx.coroutines.CoroutineScope

class CourseDataSourceFactory(
    val search: String?,
    val service: CourseService,
    val scope: CoroutineScope,
    val observer : MutableLiveData<Int>
): DataSource.Factory<Int, Course>() {

    override fun create() = CourseDataSource(search, service, scope, observer)
}
