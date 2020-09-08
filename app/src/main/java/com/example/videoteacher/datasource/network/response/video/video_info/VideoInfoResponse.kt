package com.olm.magtapp.data.data_source.network.response.video.video_info

import com.google.api.services.youtube.model.PageInfo

data class VideoInfoResponse(
	val pageInfo: PageInfo? = null,
	val items: List<ItemsItem?>? = null
){
	fun isItemListValid(): Boolean{
		var isValid = true
		items?.forEach {
			if (it==null && it?.contentDetails?.duration==null) isValid = false
		}
		return isValid
	}
}