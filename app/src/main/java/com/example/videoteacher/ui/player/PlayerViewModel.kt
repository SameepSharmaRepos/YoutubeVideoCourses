package com.example.videoteacher.ui.player

import android.util.Log
import androidx.lifecycle.*
import com.example.videoteacher.model.Course
import com.example.videoteacher.repository.MainRepoImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerViewModel(private val repo: MainRepoImpl) : ViewModel() {

    private lateinit var playlist:LiveData<Course>

    init {
        //playlist=repo.getPlaylist()
    }

}