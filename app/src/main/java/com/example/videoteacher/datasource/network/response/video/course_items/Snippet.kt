package com.olm.magtapp.data.data_source.network.response.video.course_items

import com.example.videoteacher.datasource.network.response.video.Thumbnails
import java.util.*

data class Snippet(
	val playlistId: String? = null,
	val resourceId: ResourceId? = null,
	val publishedAt: Date? = null,
	val description: String? = null,
	val position: Int? = null,
	val title: String? = null,
	val thumbnails: Thumbnails? = null,
	val channelId: String? = null,
	val channelTitle: String? = null
){
	fun isDataValid() = resourceId?.videoId!=null && position!=null && title!=null && description!=null
}
data class ResourceId(
	val kind: String? = null,
	val videoId: String? = null
)