package com.olm.magtapp.data.data_source.network.response.video.course_items

import com.google.api.services.youtube.model.PageInfo

data class CourseItemResponse(
	val nextPageToken: String? = null,
	val pageInfo: com.example.videoteacher.datasource.network.response.video.PageInfo? = null,
	val items: List<ItemsItem?>? = null
){
	fun isItemListValid(): Boolean{
		var isValid = true
		items?.forEach {
			if (it?.snippet == null) isValid = false
		}
		return isValid
	}
	fun hasMoreVideo() = nextPageToken != null
}

data class ItemsItem(
	val snippet: Snippet? = null
)