package com.olm.magtapp.data.data_source.network.response.video.video_info

import com.example.videoteacher.datasource.network.response.video.Thumbnails
import java.util.*

data class Snippet(
	val publishedAt: Date? = null,
	val description: String? = null,
	val title: String? = null,
	val thumbnails: Thumbnails? = null,
	val channelId: String? = null,
	val channelTitle: String? = null
)
