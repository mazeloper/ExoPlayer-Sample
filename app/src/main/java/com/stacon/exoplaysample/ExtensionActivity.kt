package com.stacon.exoplaysample

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.github.rubensousa.previewseekbar.PreviewBar
import com.github.rubensousa.previewseekbar.PreviewLoader
import com.github.rubensousa.previewseekbar.exoplayer.PreviewTimeBar
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.STATE_READY
import com.google.android.exoplayer2.Timeline
import com.stacon.exoplaysample.databinding.ActivityExtensionBinding
import com.stacon.exoplaysample.manager.GlideThumbnailTransformation
import com.stacon.exoplaysample.util.JsLog

class ExtensionActivity : BaseActivity<ActivityExtensionBinding>({ ActivityExtensionBinding.inflate(it) }) {

    private lateinit var previewTimeBar: PreviewTimeBar
    private lateinit var previewImage: ImageView
    private var videoPlayer: ExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    override fun initializePlayer() {
        videoPlayer = ExoPlayer.Builder(this).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer

            exoPlayer.addMediaItem(MediaItem.fromUri(getString(R.string.media_url_mp4_1)))

            exoPlayer.playWhenReady = playWhenReady
            exoPlayer.seekTo(currentWindow, playbackPosition)
            exoPlayer.addListener(playerEventListener())
            exoPlayer.prepare()
        }

        previewImage = binding.playerView.findViewById(R.id.iv_preview)
        previewTimeBar = binding.playerView.findViewById(R.id.exo_progress)
        previewTimeBar.setPreviewLoader(previewListener())
        previewTimeBar.addOnScrubListener(onScrubListener())
    }

    override fun releasePlayer() {
        videoPlayer?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentMediaItemIndex
            playWhenReady = this.playWhenReady
            release()
        }
        videoPlayer = null
    }

    private fun playerEventListener() = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            if (playbackState == STATE_READY) {
                JsLog.debug("currentMediaItem :: ${binding.playerView.player?.currentMediaItem}")
            }
        }

        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            super.onTimelineChanged(timeline, reason)
        }

        override fun onEvents(player: Player, events: Player.Events) {
            super.onEvents(player, events)
            val size = events.size()
            if (size > 0) {
                for (i in 0 until size) {
                    when (events[i]) {
                        Player.EVENT_TIMELINE_CHANGED -> "Player.EVENT_TIMELINE_CHANGED                     "
                        Player.EVENT_MEDIA_ITEM_TRANSITION -> "Player.EVENT_MEDIA_ITEM_TRANSITION                "
                        Player.EVENT_TRACKS_CHANGED -> "Player.EVENT_TRACKS_CHANGED                       "
                        Player.EVENT_PLAYBACK_STATE_CHANGED -> "Player.EVENT_PLAYBACK_STATE_CHANGED               "
                        Player.EVENT_PLAY_WHEN_READY_CHANGED -> "Player.EVENT_PLAY_WHEN_READY_CHANGED              "
                        Player.EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED -> "Player.EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED  "
                        Player.EVENT_IS_PLAYING_CHANGED -> "Player.EVENT_IS_PLAYING_CHANGED                   "
                        Player.EVENT_REPEAT_MODE_CHANGED -> "Player.EVENT_REPEAT_MODE_CHANGED                  "
                        Player.EVENT_SHUFFLE_MODE_ENABLED_CHANGED -> "Player.EVENT_SHUFFLE_MODE_ENABLED_CHANGED         "
                        Player.EVENT_PLAYER_ERROR -> "Player.EVENT_PLAYER_ERROR                         "
                        Player.EVENT_POSITION_DISCONTINUITY -> "Player.EVENT_POSITION_DISCONTINUITY               "
                        Player.EVENT_PLAYBACK_PARAMETERS_CHANGED -> "Player.EVENT_PLAYBACK_PARAMETERS_CHANGED          "
                        Player.EVENT_AVAILABLE_COMMANDS_CHANGED -> "Player.EVENT_AVAILABLE_COMMANDS_CHANGED           "
                        Player.EVENT_MEDIA_METADATA_CHANGED -> "Player.EVENT_MEDIA_METADATA_CHANGED               "
                        Player.EVENT_PLAYLIST_METADATA_CHANGED -> "Player.EVENT_PLAYLIST_METADATA_CHANGED            "
                        Player.EVENT_SEEK_BACK_INCREMENT_CHANGED -> "Player.EVENT_SEEK_BACK_INCREMENT_CHANGED          "
                        Player.EVENT_SEEK_FORWARD_INCREMENT_CHANGED -> "Player.EVENT_SEEK_FORWARD_INCREMENT_CHANGED       "
                        Player.EVENT_MAX_SEEK_TO_PREVIOUS_POSITION_CHANGED -> "Player.EVENT_MAX_SEEK_TO_PREVIOUS_POSITION_CHANGED"
                        Player.EVENT_TRACK_SELECTION_PARAMETERS_CHANGED -> "Player.EVENT_TRACK_SELECTION_PARAMETERS_CHANGED   "
                        else -> ""
                    }.run {
                    }
                }
            }
        }
    }

    private fun previewListener() = PreviewLoader { currentPosition, max ->
        binding.playerView.player?.let { player ->
            if (player.isPlaying) {
                player.playWhenReady = false
            }
            Glide.with(previewImage)
                .load("https://bitdash-a.akamaihd.net/content/MI201109210084_1/thumbnails/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.jpg")
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .transform(GlideThumbnailTransformation(currentPosition))
                .into(previewImage)
        }
    }

    private fun onScrubListener() = object : PreviewBar.OnScrubListener {
        override fun onScrubStart(previewBar: PreviewBar?) {
            binding.playerView.player?.playWhenReady = false
        }

        override fun onScrubMove(previewBar: PreviewBar?, progress: Int, fromUser: Boolean) {
            JsLog.debug("MOVE to " + progress / 1000 + " FROM USER: " + fromUser)
        }

        override fun onScrubStop(previewBar: PreviewBar?) {
            val time = previewBar!!.progress / 1000
            JsLog.warning(">>>> ${String.format("%02d:%02d", time / 60, time % 60)}")
            // TODO 추가작업 필요: Js - 플레이중에 프리뷰로 인하여 멈췄을 경우 다시 재실행
            /*if (resumeVideoOnPreviewStop) {*/
            binding.playerView.player?.playWhenReady = true
        }
    }


    // TODO 재생위치 확인

}
