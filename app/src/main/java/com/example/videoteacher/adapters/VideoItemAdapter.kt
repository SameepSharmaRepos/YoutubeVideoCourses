package com.example.videoteacher.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videoteacher.R

class VideoItemAdapter : RecyclerView.Adapter<VideoItemAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        )


    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    fun addToList() {
    }
}