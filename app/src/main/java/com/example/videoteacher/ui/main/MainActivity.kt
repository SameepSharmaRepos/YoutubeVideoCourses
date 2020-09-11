package com.example.videoteacher.ui.main

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videoteacher.R
import com.example.videoteacher.adapters.CourseListAdapter
import com.example.videoteacher.adapters.NewsListAdapterInterface
import com.example.videoteacher.adapters.YoutubeAdapter
import com.example.videoteacher.dao.MainActDao
import com.example.videoteacher.database.VideoDb
import com.example.videoteacher.datasource.CourseOfflineDataSource
import com.example.videoteacher.datasource.CourseOnlineDataSource
import com.example.videoteacher.datasource.network.CourseService
import com.example.videoteacher.model.Course
import com.example.videoteacher.repository.MainRepoImpl
import com.example.videoteacher.ui.youtube.YoutubeConnector
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewsListAdapterInterface {

    companion object {
        const val INIT_QUERY = "Programming courses"
    }

    private lateinit var progress: ProgressDialog

    //yputubeAdapter
    lateinit var yAdapter: YoutubeAdapter
    lateinit var yAdapterSuggested: YoutubeAdapter
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

        setUpToolbar()
        mainDao = VideoDb.getDatabase(applicationContext).mainActDao()
        offlineDataSource = CourseOfflineDataSource(mainDao)
        onlineDataSource = CourseOnlineDataSource(offlineDataSource, mainDao, CourseService())
        mainViewModel = ViewModelProviders.of(
            this,
            MainViewModelfactory(MainRepoImpl(onlineDataSource, offlineDataSource,this.application))
        ).get(MainViewModel::class.java)
        setupRecycler()
        setTextWatcher()

    }

    private fun setUpToolbar() {

        val tb = findViewById<Toolbar>(R.id.toolbar_main)
        setSupportActionBar(tb)

    }

    private fun setTextWatcher() {


        et_main.addTextChangedListener(object : TextWatcher {
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
        mainViewModel.setCourseQuery(INIT_QUERY)

        mainViewModel.courseList.observe(this, Observer {
            if (it.isNotEmpty())
            Log.e("PlainList>>", "${it[0]?.id} <<<")
            yAdapter = YoutubeAdapter(this, it)
            val pagedAdapter = CourseListAdapter(this, this)
            pagedAdapter.submitList(it)
            rvRecentMain.adapter = pagedAdapter
            progress.dismiss()

            /*
            yAdapter.setResultList(it)04
            yAdapter.notifyDataSetChanged()*/
        })

        mainViewModel.suggestedAndroidList?.observe(this, Observer {
            yAdapterSuggested= YoutubeAdapter(this, it)
            if (it.isNotEmpty())
            Log.e("Androidist>>", "${it[0]?.id} <<<")
            val paged = CourseListAdapter(this,this)
            paged.submitList(it)
            rvSuggestedMain.adapter=paged
            if (progress.isShowing)
                progress.dismiss()

        })



    }

    private lateinit var searchView: SearchView

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.app_bar_search)
        if (searchItem != null) {
            searchView = MenuItemCompat.getActionView(searchItem) as SearchView

            val searchPlate =
                searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText

            searchPlate.hint = "Search Courses"

            val searchPlateView: View =
                searchView.findViewById(androidx.appcompat.R.id.search_plate)

            searchPlateView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.transparent
                )
            )

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    mainViewModel.setCourseQuery(query!!)
                    Toast.makeText(this@MainActivity, "$query !!", Toast.LENGTH_LONG).show()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })

        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupRecycler() {

        val layoutMan = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rvRecentMain.layoutManager = layoutMan
        //rvRecentMain.adapter=adapterDemo

        val layoutManSug = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rvSuggestedMain.layoutManager = layoutManSug
        yAdapter = YoutubeAdapter(this, listOf())
        //rvSuggestedMain.adapter=adapterDemo
        getInitialVideos()

    }
}