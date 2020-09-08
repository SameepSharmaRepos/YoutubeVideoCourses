package com.example.videoteacher.ui.main

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videoteacher.R
import com.example.videoteacher.adapters.VideoItemAdapter
import com.example.videoteacher.adapters.YoutubeAdapter
import com.example.videoteacher.dao.MainActDao
import com.example.videoteacher.database.VideoDb
import com.example.videoteacher.datasource.CourseOfflineDataSource
import com.example.videoteacher.datasource.CourseOnlineDataSource
import com.example.videoteacher.datasource.network.CourseService
import com.example.videoteacher.repository.MainRepoImpl
import com.example.videoteacher.ui.youtube.YoutubeConnector
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val INIT_QUERY = "Programming courses"
    }

    private lateinit var progress: ProgressDialog

    //yputubeAdapter
    lateinit var yAdapter: YoutubeAdapter
    lateinit var yConnector: YoutubeConnector

    //Dao
    private lateinit var mainDao: MainActDao
    private lateinit var offlineDataSource: CourseOfflineDataSource
    private lateinit var onlineDataSource: CourseOnlineDataSource

    //ViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainDao = VideoDb.getDatabase(applicationContext).mainActDao()
        offlineDataSource = CourseOfflineDataSource(mainDao)
        onlineDataSource = CourseOnlineDataSource(offlineDataSource, mainDao, CourseService())
        mainViewModel = ViewModelProviders.of(
            this,
            MainViewModelfactory(MainRepoImpl(onlineDataSource, offlineDataSource))
        ).get(MainViewModel::class.java)
        setupRecycler()
        setTextWatcher()

    }

    private fun setTextWatcher() {


        et_main.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mainViewModel.setCourseQuery(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

    }

    private fun getInitialVideos() {

        progress = ProgressDialog(this)
        progress.setMessage("Searching Videos")
        progress.show()
       // mainViewModel.setCourseQuery(INIT_QUERY)

        Log.e("CalledFuncInit>>", "Yes <<<")
        mainViewModel.observeList().observe(this, Observer {
            Log.e("MainSize>>", "${it.size} <<")
            yAdapter= YoutubeAdapter(this, it)
            rvRecentMain.adapter=yAdapter
            progress.dismiss()

            /*
            yAdapter.setResultList(it)
            yAdapter.notifyDataSetChanged()*/
        })

        mainViewModel.setCourseQuery(INIT_QUERY)



    }

    private fun setupRecycler() {

        val layoutMan = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rvRecentMain.layoutManager = layoutMan
        //rvRecentMain.adapter=adapterDemo

        val layoutManSug = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rvSuggestedMain.layoutManager = layoutManSug
        yAdapter= YoutubeAdapter(this, listOf())
        //rvSuggestedMain.adapter=adapterDemo
        getInitialVideos()

    }
}