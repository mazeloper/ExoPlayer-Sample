package com.stacon.exoplaysample

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.util.Util
import com.stacon.exoplaysample.databinding.ActivityCustomBinding

class CustomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomBinding

    private var videoPlayer: ExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
        } else {
            // TODO
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            actionBar?.hide()
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT < 24 || videoPlayer == null)) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        videoPlayer = ExoPlayer
            .Builder(this)
            .setSeekForwardIncrementMs(5000) // 빨리감기 시간정의
            .setSeekBackIncrementMs(5000) // 뒤로감기 시간정의
            .build()
            .also { exoPlayer ->
                binding.playerView.player = exoPlayer
                binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT

                exoPlayer.addMediaItem(MediaItem.fromUri(getString(R.string.media_url_mp4_vertical_1)))
                exoPlayer.addMediaItem(MediaItem.fromUri(getString(R.string.media_url_mp4_vertical_2)))
                exoPlayer.addMediaItem(MediaItem.fromUri(getString(R.string.media_url_mp4_vertical_3)))
                exoPlayer.addMediaItem(MediaItem.fromUri(getString(R.string.media_url_mp4_1)))
                exoPlayer.addMediaItem(MediaItem.fromUri(getString(R.string.media_url_mp4_2)))
                exoPlayer.addMediaItem(MediaItem.fromUri(getString(R.string.media_url_mp4_3)))

                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.addListener(playerEventListener())
                exoPlayer.prepare()
            }
    }

    private fun releasePlayer() {
        videoPlayer?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentMediaItemIndex
            playWhenReady = this.playWhenReady
            release()
        }
        videoPlayer = null

    }

    private fun playerEventListener() = object : Player.Listener {
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)

            // 테스트를 위해 playList 를 섞었지만 (shuffle) 해당 리스너는 list 에 담겼던 인덱스 그대로 내려주고있다.
            // 그래서 이럴 때 각 아이템에 tagId 등으로 시청중인 영상에 인덱스를 찾아야한다.
            binding.playerView.player?.videoSize?.let { video ->
                binding.playerView.resizeMode = if (video.width >= video.height) {
                    AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                } else {
                    AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
                }
            }
        }
    }
}