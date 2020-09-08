package com.olm.magtapp.data.data_source.network.response.video.search_course

data class PlaylistDurationResponse(
    val items: List<DurationItem?>? = null
){
    fun isItemListValid(): Boolean{
        var isValid = true
        items?.forEach {
            if (it?.contentDetails?.itemCount==null || it.id==null) isValid = false
        }
        return isValid
    }
}

data class DurationItem(
    val contentDetails: ContentDetails?,
    val id: String?
)

data class ContentDetails(
    val itemCount: Int
)