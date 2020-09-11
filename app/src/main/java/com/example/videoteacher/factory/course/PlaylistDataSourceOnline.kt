package com.olm.magtapp.data.factory.course

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.videoteacher.datasource.CourseOfflineDataSource
import com.example.videoteacher.datasource.network.CourseService
import com.example.videoteacher.model.Course
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*


class PlaylistDataSourceOnline(
    val playId: String?,
    val service: CourseService,
    val scope: CoroutineScope,
    val offlineDataSource: CourseOfflineDataSource
): PageKeyedDataSource<Int, Course>() {

    companion object{
        const val EVENT_COURSE_SEARCH_NO_INTERNET=125
        const val EVENT_COURSE_SEARCH_SOMETHING_WENT_WRONG=126
    }


    private var nextPageToken: String? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Course>) {
        if (playId==null) return
        Log.e("Course loading Loading","Load initial data")
        scope.launch {
            try {
                val courseSearchResponse = service.getCourseItems(playId, nextPageToken)
                if (courseSearchResponse.isSuccessful) {
                    val courseData = courseSearchResponse.body() ?: throw Exception(courseSearchResponse.message())
                    if (courseData.isItemListValid()) {
                        val courseRawData = courseData.items
                        nextPageToken = courseData.nextPageToken
                        val courseList = mutableListOf<Course>()
                        courseRawData?.forEach {
                            val snippet = it?.snippet ?: return@forEach
                            val courseId = it.snippet.playlistId ?: return@forEach
                            if (snippet.isDataValid()){
                                courseList.add(Course(courseId, snippet.title!!,snippet.channelId!!,
                                    snippet.channelTitle!!,snippet.description!!,snippet.thumbnails?.medium?.url!!,
                                    "","youtube",false,false,0,snippet.publishedAt!!,null,Date()
                                ))
                            }
                        }
                        //loadVideoDurations(courseList)
                        Log.e("onlineDaTa>>", "${courseList.size} <<<")
                        offlineDataSource.insertMainList(courseList)
                        // send news callback.
                        callback.onResult( courseList , null, 1 )
                    } else throw Exception("Error while searching course.")
                } else {
                    throw Exception("Error while searching course.")
                }
            }catch (e: IOException){
                e.printStackTrace()
                // Internet not connected.
               // observer.postValue(EVENT_COURSE_SEARCH_NO_INTERNET)
            }catch (e:Exception){
                e.printStackTrace()
                // Something went wrong
               // observer.postValue(EVENT_COURSE_SEARCH_SOMETHING_WENT_WRONG)
            }
        }
    }

    private suspend fun loadVideoDurations(courseList: MutableList<Course>) {
        val listOfCourseId = courseList.map { return@map it.id }.joinToString(",")
        try {
            val courseItemsRequest = service.getPlaylistDurations(listOfCourseId)
            if (courseItemsRequest.isSuccessful && courseItemsRequest.body()!=null && courseItemsRequest.body()!!.isItemListValid()) {
                courseItemsRequest.body()!!.items!!.forEach {durationItem ->
                    val totalItems = durationItem?.contentDetails?.itemCount ?: return@forEach
                    val indexOfFirst = courseList.indexOfFirst { it.id == durationItem.id }
                    courseList[indexOfFirst].totalVideos = totalItems
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Course>) {
        if (playId==null) return
        scope.launch {
            try {
                val courseSearchResponse = service.searchCourses(playId, nextPageToken)
                if (courseSearchResponse.isSuccessful) {
                    val courseData = courseSearchResponse.body() ?: throw Exception(courseSearchResponse.message())
                    if (courseData.isItemListValid()) {
                        val courseRawData = courseData.items
                        nextPageToken = courseData.nextPageToken
                        val courseList = mutableListOf<Course>()
                        courseRawData?.forEach {
                            val snippet = it?.snippet ?: return@forEach
                            val courseId = it.id?.playlistId ?: return@forEach
                            if (snippet.isDataValid()){
                                courseList.add(Course(courseId, snippet.title!!,snippet.channelId!!,
                                    snippet.channelTitle!!,snippet.description!!,snippet.thumbnails?.medium?.url!!,
                                    "","youtube",false,false,0,snippet.publishedAt!!,null,Date()
                                ))
                            }
                        }
                        loadVideoDurations(courseList)
                        // send news callback.
                        callback.onResult( courseList , 1 )
                    } else throw Exception("Error while searching course.")
                } else {
                    throw Exception("Error while searching course.")
                }
            }catch (e: IOException){
                e.printStackTrace()
                // Internet not connected.
                //observer.postValue(EVENT_COURSE_SEARCH_NO_INTERNET)
            }catch (e:Exception){
                e.printStackTrace()
                // Something went wrong
                //observer.postValue(EVENT_COURSE_SEARCH_SOMETHING_WENT_WRONG)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Course>) = Unit


}