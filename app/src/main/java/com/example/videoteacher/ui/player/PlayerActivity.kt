package com.example.videoteacher.ui.player

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videoteacher.R
import com.example.videoteacher.adapters.CourseListAdapter
import com.example.videoteacher.adapters.NewsListAdapterInterface
import com.example.videoteacher.adapters.YoutubeAdapter
import com.example.videoteacher.dao.MainActDao
import com.example.videoteacher.database.VideoDb
import com.example.videoteacher.datasource.CourseOfflineDataSource
import com.example.videoteacher.datasource.CourseOnlineDataSource
import com.example.videoteacher.datasource.network.CourseService
import com.example.videoteacher.repository.MainRepoImpl
import com.example.videoteacher.ui.main.MainViewModel
import com.example.videoteacher.ui.main.MainViewModelfactory
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

//The activity which plays has YouTubePlayerView inside layout must extend YouTubeBaseActivity
//implement OnInitializedListener to get the state of the player whether it has succeed or failed
//to load
class PlayerActivity : AppCompatActivity(), YouTubePlayer.OnInitializedListener, NewsListAdapterInterface {
    private var playerView: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer? =
        null

    //ViewModel
    private lateinit var playerViewModel: MainViewModel

    //Dao
    private lateinit var mainDao: MainActDao
    private lateinit var offlineDataSource: CourseOfflineDataSource
    private lateinit var onlineDataSource: CourseOnlineDataSource

    //repo
    private lateinit var repo: MainRepoImpl
    private val tracker = YouTubePlayerTracker()

    //Overriding onCreate method(first method to be called) to create the activity
    //and initialise all the variable to their respective views in layout file and
    //adding listeners to required views
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        //method to fill the activity that is launched with  the activity_player.xml layout file
        setContentView(R.layout.activity_player)

        mainDao = VideoDb.getDatabase(applicationContext).mainActDao()
        offlineDataSource = CourseOfflineDataSource(mainDao)
        onlineDataSource = CourseOnlineDataSource(offlineDataSource, mainDao, CourseService())
        repo = MainRepoImpl(onlineDataSource, offlineDataSource, this.application)
        repo.setScope(coroutineScope = CoroutineScope(Dispatchers.IO))
        playerViewModel = ViewModelProviders.of(
            this,
            MainViewModelfactory(repo)
        ).get(MainViewModel::class.java)
        setUpRecycler()
        observeList()

        //getting youtube player view object

        player_rv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        //initialize method of YouTubePlayerView used to play videos and control video playback
        //arguments are a valid API key that is enabled to use the YouTube Data API v3 service
        //and YouTubePlayer.OnInitializedListener object or the callbacks that will be invoked
        //when the initialization succeeds or fails
        //as in this case the activity implements OnInitializedListener
        player_view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                intent.getStringExtra("VIDEO_ID")?.let {

                    youTubePlayer.loadVideo(it, 0f)

                }
                youTubePlayer.addListener(tracker)
            }

            override fun onStateChange(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                if (state == PlayerConstants.PlayerState.ENDED) {
                    //onVideoEnded()
                } else {
                    super.onStateChange(youTubePlayer, state)
                }
            }

            override fun onCurrentSecond(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                second: Float
            ) {
                if (second.toInt() % 10 == 0) {
                    //saveVideoPlayedDuration(second.toInt())
                }
            }
        })
        //initialising various descriptive data in the UI and player
        val video_title = findViewById<View>(R.id.player_title) as TextView
        val video_desc = findViewById<View>(R.id.player_description) as TextView
        val video_id = findViewById<View>(R.id.player_id) as TextView

        //setting text of each View form UI
        //setText method used to change the text shown in the view
        //getIntent method returns the object of current Intent
        //of which getStringExtra returns the string which was passed while calling the intent
        //by using the name that was associated during call
        video_title.text = intent.getStringExtra("VIDEO_TITLE")
        video_id.text = "Video ID : " + intent.getStringExtra("VIDEO_ID")
        video_desc.text = intent.getStringExtra("VIDEO_DESC")
    }

    private fun observeList() {

        playerViewModel.getPlaylistItems(intent.getStringExtra("VIDEO_ID")!!)

    }

    private fun setUpRecycler() {
        val list = playerViewModel.getPlaylistItems(intent.getStringExtra("VIDEO_ID")!!)
            .observe(this, Observer {
//                val adapter = YoutubeAdapter(this, list.value)
                val adapter = CourseListAdapter(this, this)
                adapter.submitList(it)
                player_rv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                player_rv.adapter = adapter
            })
    }

    //method called if the YouTubePlayerView fails to initialize
    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider,
        result: YouTubeInitializationResult
    ) {
        Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_LONG).show()
    }

    //method called if the YouTubePlayerView succeeds to load the video
    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider, player: YouTubePlayer,
        restored: Boolean
    ) {

        //initialise the video player only if it is not restored or is not yet set
        if (!restored) {

            //cueVideo takes video ID as argument and initialise the player with that video
            //this method just prepares the player to play the video
            //but does not download any of the video stream until play() is called
            player.cueVideo(intent.getStringExtra("VIDEO_ID"))
        }
    }
}