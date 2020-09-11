package com.example.videoteacher.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.videoteacher.R
import com.example.videoteacher.model.Course
import com.example.videoteacher.ui.player.PlayerActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_video.view.*

interface NewsListAdapterInterface

class CourseListAdapter(
    private val context: Context,
    private val listener: NewsListAdapterInterface
) : PagedListAdapter<Course, RecyclerView.ViewHolder>(
    DIFF_CALLBACK
) {

    private var isNewsLoaded = false
    private val isNightMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Course>() {
            override fun areContentsTheSame(p0: Course, p1: Course): Boolean {
                return p0.id == p1.id
            }

            override fun areItemsTheSame(p0: Course, p1: Course): Boolean {
                return p1.image == p0.image || p1.image == p0.image
            }
        }
        const val NEWS_ITEM = 1
        const val NEWS_ITEM_AD = 3
        const val NEWS_ITEM_C = 4
        const val LOADING = 2
        const val ADS_PER_VIEW = 8
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return CourseViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_video, p0, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return when  {
            (position % ADS_PER_VIEW == 0) -> NEWS_ITEM_AD
            position == itemCount - 1 -> LOADING
            else -> NEWS_ITEM
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val course = getItem(position) ?: return

        viewHolder.itemView.item_title.text=course.name
        Picasso.with(context).load(course.image).into(viewHolder.itemView.ivThumb)


        //setting on click listener for each video_item to launch clicked video in new activity
        viewHolder.itemView.llItemContainer.setOnClickListener(
            View.OnClickListener
//onClick method called when the view is clicked
            { //creating a intent for PlayerActivity class from this Activity
                //arguments needed are package context and the new Activity class
                val intent = Intent(context, PlayerActivity::class.java)

                //putExtra method helps to add extra/extended data to the intent
                //which can then be used by the new activity to get initial data from older activity
                //arguments is a name used to identify the data and other is the data itself
                intent.putExtra("VIDEO_ID", course.id)
                intent.putExtra("VIDEO_TITLE", course.pubName)
                intent.putExtra("VIDEO_DESC", course.description)

                //Flags define hot the activity should behave when launched
                //FLAG_ACTIVITY_NEW_TASK flag if set, the activity will become the start of a new task on this history stack.
                //adding flag as it is required for YoutubePlayerView Activity
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                //launching the activity by startActivity method
                //use mContext as this class is not the original context
                context.startActivity(intent)
            })

    }

    class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view)
}