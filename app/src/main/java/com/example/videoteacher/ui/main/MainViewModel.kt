package com.example.videoteacher.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.example.videoteacher.model.Course
import com.example.videoteacher.repository.MainRepoImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val repo: MainRepoImpl) : ViewModel() {

    val SUGGESTION_INIT = "Android Tutorials"

    private val courseSearch = MutableLiveData<String>()
    private val initSuggested = MutableLiveData<String>()
    private val uiScope = CoroutineScope(Dispatchers.IO)
    var courseList:LiveData<List<Course>>
    lateinit var suggestedAndroidList : LiveData<List<Course>>



    init {
        repo.setScope(uiScope)
        courseSearch.postValue(MainActivity.INIT_QUERY)
        courseList = Transformations.switchMap(courseSearch) { input: String? ->
            return@switchMap input?.let { repo.getSearchedCourseList(input) }
        }

        initSuggested.postValue(SUGGESTION_INIT)
        suggestedAndroidList=Transformations.switchMap(initSuggested){
            q:String->
            return@switchMap repo.getSearchedCourseList(q)
        }

    }



    fun setCourseQuery(query: String) {
        Log.e("OnQueyChange>>", "$query <<<")
        courseSearch.postValue(query)
    }

    fun getPlaylistItems(id: String): LiveData<List<Course>> {

        return repo.getPlaylist(id)

    }

}