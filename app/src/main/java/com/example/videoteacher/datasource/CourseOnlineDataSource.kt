package com.example.videoteacher.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.videoteacher.dao.MainActDao
import com.example.videoteacher.datasource.network.CourseService
import com.example.videoteacher.model.Course
import com.olm.magtapp.data.factory.course.CourseDataSourceFactory
import com.olm.magtapp.data.factory.course.PlaylistDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.IOException
import java.util.*

class CourseOnlineDataSource(
    private val courseOfflineDataSource: CourseOfflineDataSource,
    private val mainActDao: MainActDao,
    private val courseService: CourseService
) {

    companion object{
        const val EVENT_COURSE_SEARCH_NO_INTERNET=101
        const val EVENT_COURSE_SEARCH_SOMETHING_WENT_WRONG=102
    }

    private var observer=MutableLiveData<Int>()
    private var nextPageToken: String? = null

suspend fun searchCourse(query: String): LiveData<List<Course>> {
        Log.e("SearchCourse>>", "$query <<<")
        val searchRes = MutableLiveData<List<Course>>()
            try {
                //val course = mainActDao.getSavedVideosMain()
            //if (course?.value == null) {
                val onlineSearch = courseService.searchCourses(query, nextPageToken)
                if (onlineSearch.isSuccessful) {
                    val course = onlineSearch.body()?:throw Exception(onlineSearch.message())
                    if (course.isItemListValid()){
                        nextPageToken = course.nextPageToken
                        val courseListRaw = course.items
                        val courseList = mutableListOf<Course>()

                        courseListRaw?.forEach{
                            val snippet = it?.snippet ?: return@forEach
                            val courseId = it.id?.playlistId ?: return@forEach
                            if (snippet.isDataValid()){
                                courseList.add(Course(courseId, snippet.title!!,snippet.channelId!!,
                                    snippet.channelTitle!!,snippet.description!!,snippet.thumbnails?.medium?.url!!,
                                    "","youtube",false,false,0,snippet.publishedAt!!,null, Date()
                                ))
                            }
                        }
                        Log.e("SizeFromOL>>", "${courseList.size} <<<")
                        mainActDao.insertVideoList(courseList)
                        searchRes.postValue(courseList)
                    } else throw Exception("Error while searching course.")
                    }else {
                    throw Exception("Error while searching course.")
                //}
                }
            }catch (e: IOException){
                e.printStackTrace()
                // Internet not connected.
                observer.postValue(EVENT_COURSE_SEARCH_NO_INTERNET)
                Log.e("Nointernet>>", "${e.localizedMessage} <<<")
            }catch (e:Exception){
                e.printStackTrace()
                // Something went wrong
                Log.e("OtherError>>", "${e.message} <<<")
                observer.postValue(EVENT_COURSE_SEARCH_SOMETHING_WENT_WRONG)
            }

        return searchRes

        }

    fun getPlayList(id: String): LiveData<PagedList<Course>> {
        Log.e("GetPlayList>>", "$id <<<")
        var pagedList: LiveData<PagedList<Course>>? = null

// Call the New Data Source for Getting the List of News
        val dataSourceFactory = PlaylistDataSourceFactory(id, CourseService.invoke(),
            CoroutineScope(Dispatchers.IO),courseOfflineDataSource)
        val config = (PagedList.Config.Builder())
            .setPageSize(25)
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(25)
            .build()
        pagedList = (LivePagedListBuilder(dataSourceFactory,config)).build()
        return pagedList!!
    }

    fun searchCourseNew(query: String): LiveData<PagedList<Course>>? {
        Log.e("SearchCourseNew>>", "$query <<<")
        var pagedList: LiveData<PagedList<Course>>? = null

// Call the New Data Source for Getting the List of News
        val dataSourceFactory = CourseDataSourceFactory(query, CourseService.invoke(),
            CoroutineScope(Dispatchers.IO),courseOfflineDataSource)
        val config = (PagedList.Config.Builder())
            .setPageSize(25)
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(25)
            .build()
        pagedList = (LivePagedListBuilder(dataSourceFactory,config)).build()
        return pagedList!!
    }

}