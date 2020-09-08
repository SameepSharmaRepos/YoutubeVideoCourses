package com.olm.magtapp.data.data_source.network.response.video.search_course

import com.example.videoteacher.datasource.network.response.video.Thumbnails
import java.util.*

data class Snippet(
	val publishedAt: Date? = null,
	val description: String? = null,
	val title: String? = null,
	val thumbnails: Thumbnails? = null,
	val channelId: String? = null,
	val channelTitle: String? = null
){
	fun isDataValid() = title!=null && description!=null && publishedAt!=null && thumbnails?.high?.url!=null && channelId!=null && channelTitle!=null
}