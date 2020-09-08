package com.olm.magtapp.data.data_source.network.response.video.search_course

import com.example.videoteacher.datasource.network.response.video.PageInfo


data class SearchCoursesResponse(
	val nextPageToken: String? = null,
	val pageInfo: PageInfo? = null,
	val items: List<ItemsItem?>? = null
){
	fun isItemListValid(): Boolean{
		var isValid = true
		items?.forEach {
			if (it?.snippet == null || it.id==null) isValid = false
		}
		return isValid
	}

	fun isCourseInfoCorrect(): Boolean{
		var isValid = false
		if (items!=null && items.isNotEmpty()){
			val snippet = items[0]?.snippet
			if (snippet!=null){
				isValid = true
			}
		}
		return isValid
	}
}
