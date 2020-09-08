package com.example.videoteacher.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.example.videoteacher.model.Course
import com.example.videoteacher.repository.MainRepoImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val repo: MainRepoImpl) : ViewModel() {
    private val courseSearch = MutableLiveData<String>()
    private val uiScope = CoroutineScope(Dispatchers.Default)
    var courseList:LiveData<List<Course>>
    init {
        repo.setScope(viewModelScope)
        courseSearch.postValue(MainActivity.INIT_QUERY)
        courseList = Transformations.switchMap(courseSearch) { input: String? ->
            return@switchMap input?.let { repo.getMainCourseList() }
        }


    }
    fun setCourseQuery(query: String) {
        courseSearch.postValue(query)
    }

    fun observeList(): LiveData<List<Course>> = courseList

}