package com.example.videoteacher.repository

import com.example.videoteacher.model.VideoItem
import java.time.temporal.TemporalQuery

interface MainRepo {

    fun getVideosForMain(query: String):List<VideoItem>

}