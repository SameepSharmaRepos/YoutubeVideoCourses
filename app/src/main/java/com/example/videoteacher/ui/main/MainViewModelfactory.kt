package com.example.videoteacher.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.videoteacher.repository.MainRepo
import com.example.videoteacher.repository.MainRepoImpl

class MainViewModelfactory(val mainRepo: MainRepoImpl) : ViewModelProvider.NewInstanceFactory() { @Suppress("UNCHECKED_CAST")
override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return MainViewModel(mainRepo) as T
}
}