package com.olm.magtapp.data.data_source.network.response.video.video_info

data class ItemsItem(
	val snippet: Snippet? = null,
	val id: String? = null,
	val contentDetails: ContentDetails? = null
){
	fun isValid() = id!=null && contentDetails?.duration!=null
}

data class ContentDetails(
	val duration: String? = null
)