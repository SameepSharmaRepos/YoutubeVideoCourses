package com.example.videoteacher.datasource.network.response.video

data class Thumbnails(
    val default: Thumbnail? = null,
    val high: Thumbnail? = null,
    val medium: Thumbnail? = null,
    val standard: Thumbnail? = null,
    val maxres: Thumbnail? = null
)
