package com.example.videoteacher.datasource.network

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.olm.magtapp.data.data_source.network.response.video.course_items.CourseItemResponse
import com.olm.magtapp.data.data_source.network.response.video.search_course.PlaylistDurationResponse
import com.olm.magtapp.data.data_source.network.response.video.search_course.SearchCoursesResponse
import com.olm.magtapp.data.data_source.network.response.video.video_info.VideoInfoResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CourseService {

    @GET("playlistItems")
    suspend fun getCourseItems(
        @Query("playlistId") playlistId: String,
        @Query("pageToken") pageToken: String? = null,
        @Query("maxResults") maxResults: Int = 20,
        @Query("part") part: String = "snippet"
    ): Response<CourseItemResponse>

    @GET("playlists")
    suspend fun getPlaylistDurations(
        @Query("id") id: String,
        @Query("part") part: String = "contentDetails"
    ): Response<PlaylistDurationResponse>

    @GET("search")
    suspend fun searchCourses(
        @Query("q") query: String,
        @Query("pageToken") pageToken: String? = null,
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "playlist",
        @Query("maxResults") maxResults: Int = 25
    ): Response<SearchCoursesResponse>

    @GET("search")
    suspend fun courseInfo(
        @Query("q") query: String,
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "playlist",
        @Query("pageToken") pageToken: String? = null,
        @Query("maxResults") maxResults: Int = 1
    ): Response<SearchCoursesResponse>

    // part = snippet,contentDetails -> gives duration and video info.
    @GET("videos")
    suspend fun getVideoInfo(
        @Query("id") id: String,
        @Query("part") part: String = "contentDetails"
    ): Response<VideoInfoResponse>

    companion object {

        operator fun invoke(): CourseService {

            val requestInterceptor = Interceptor { chain ->
                val request = chain.request()

                val originalUrl = request.url()
                val url = originalUrl.newBuilder()
                    .addQueryParameter("key", "AIzaSyDI39BoS1zJ9YB511Brb8iTIgtrEr11K2M")
                    .build()

                val updatedRequest = request.newBuilder()
                    .url(url)
                    .build()

                Log.e("NewRequestUrl>>", "${updatedRequest.url()} <<<")

                return@Interceptor chain.proceed(updatedRequest)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .build()
                .create(CourseService::class.java)
        }

    }
}
