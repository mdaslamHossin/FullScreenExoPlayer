package com.aslamconsole.fullscreenexoplayer

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.aslamconsole.fullscreenexoplayer.databinding.ActivityVideoPlayerBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding
    private var simpleExoPlayer: SimpleExoPlayer? = null

    companion object {
        const val VIDEO_URL_TEST =
            "https://5b44cf20b0388.streamlock.net:8443/vod/smil:bbb.smil/playlist.m3u8"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVideoPlayer()
        bindPlayerControlEvents()
    }

    private fun initVideoPlayer() {
        simpleExoPlayer = SimpleExoPlayer.Builder(this).build()
        binding.playerView.player = simpleExoPlayer
        simpleExoPlayer?.addMediaItem(MediaItem.fromUri(VIDEO_URL_TEST))
        simpleExoPlayer?.prepare()
        simpleExoPlayer?.play()
    }

    private fun bindPlayerControlEvents() = with(binding.playerView) {
        findViewById<ImageView>(R.id.fwd).setOnClickListener {
            simpleExoPlayer?.currentPosition?.plus(10000)?.let { position ->
                simpleExoPlayer?.seekTo(position)
            }
        }
        findViewById<ImageView>(R.id.rew).setOnClickListener {
            val num = simpleExoPlayer?.currentPosition?.minus(10000)
            if (num != null) {
                if (num < 0) {
                    simpleExoPlayer?.seekTo(0)
                } else {
                    simpleExoPlayer?.currentPosition?.minus(10000)?.let { position ->
                        simpleExoPlayer?.seekTo(position)
                    }
                }
            }
        }
        findViewById<LinearLayout>(R.id.view_full_screen_controls).setOnClickListener {
            val orientation = resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                enterFullScreenPlayer()
            } else {
                exitFullScreenPlayer()
            }
        }
        findViewById<ImageView>(R.id.exo_play).setOnClickListener { simpleExoPlayer?.play() }
        findViewById<ImageView>(R.id.exo_pause).setOnClickListener { simpleExoPlayer?.pause() }
        (binding.playerView).setControllerVisibilityListener { }
    }

    private fun exitFullScreenPlayer() {
        supportActionBar?.show()
        showSystemUI((binding.playerView))
        bindExitFullScreenIcon()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    private fun enterFullScreenPlayer() {
        supportActionBar?.hide()
        hideSystemUI((binding.playerView))
        bindEnterFullScreenIcon()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    }

    private fun bindEnterFullScreenIcon() = with(binding.playerView) {
        findViewById<ImageView>(R.id.exo_enter_full_screen).visibility = View.GONE
        findViewById<ImageView>(R.id.exo_exit_full_screen).visibility = View.VISIBLE
    }

    private fun bindExitFullScreenIcon() = with(binding.playerView) {
        findViewById<ImageView>(R.id.exo_enter_full_screen).visibility = View.VISIBLE
        findViewById<ImageView>(R.id.exo_exit_full_screen).visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer?.release()
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        simpleExoPlayer?.playWhenReady
        simpleExoPlayer?.playbackState
    }
}