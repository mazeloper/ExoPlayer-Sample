package com.stacon.exoplaysample

import android.graphics.Color
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.github.rubensousa.previewseekbar.PreviewBar
import com.github.rubensousa.previewseekbar.PreviewLoader
import com.github.rubensousa.previewseekbar.exoplayer.PreviewTimeBar
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.stacon.exoplaysample.databinding.ActivityExtensionBinding
import com.stacon.exoplaysample.manager.GlideThumbnailTransformation

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

            val metaDataMediaItem = MediaItem.Builder()
                .setUri(getString(R.string.media_url_mp4_1))
                .setMediaId("테스트트트트트트아이이이이이이디디디디")
                .setTag("0")
                .build()


            exoPlayer.addMediaItem(MediaItem.fromUri(getString(R.string.media_url_mp4_vertical_2)))
            exoPlayer.addMediaItem(metaDataMediaItem)
            exoPlayer.addMediaItem(MediaItem.fromUri(getString(R.string.media_url_mp4_vertical_3)))
            exoPlayer.addMediaItem(MediaItem.fromUri(getString(R.string.media_url_mp4_1)))

            exoPlayer.playWhenReady = playWhenReady
            exoPlayer.seekTo(currentWindow, playbackPosition)
            exoPlayer.addListener(ExoListener.playerEventListener())
            exoPlayer.prepare()
        }

        previewImage = binding.playerView.findViewById(R.id.iv_preview)
        previewTimeBar = binding.playerView.findViewById(R.id.exo_progress)
        previewTimeBar.setPreviewLoader(previewListener())
        previewTimeBar.addOnScrubListener(onScrubListener())

        // Color
        previewTimeBar.setPreviewThumbTint(Color.parseColor("#843B63"))
        previewTimeBar.setBufferedColor(Color.parseColor("#84AA63"))
        //previewTimeBar.setBackgroundColor(Color.parseColor("#2DAADE"))
        previewTimeBar.setPlayedColor(Color.parseColor("#FEAADE"))
        previewTimeBar.setUnplayedColor(Color.parseColor("#FE0204"))

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

    /**
     * Preview Loader Listener
     *
     */
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

    /**
     * PreviewBar 동작 리스너
     *
     */
    private fun onScrubListener() = object : PreviewBar.OnScrubListener {
        override fun onScrubStart(previewBar: PreviewBar?) {
            binding.playerView.player?.playWhenReady = false
        }

        override fun onScrubMove(previewBar: PreviewBar?, progress: Int, fromUser: Boolean) {
            //JsLog.debug("MOVE to " + progress / 1000 + " FROM USER: " + fromUser)
        }

        override fun onScrubStop(previewBar: PreviewBar?) {
            val time = previewBar!!.progress / 1000
            //JsLog.warning(">>>> ${String.format("%02d:%02d", time / 60, time % 60)}")
            // TODO 추가작업 필요: Js - 플레이중에 프리뷰로 인하여 멈췄을 경우 다시 재실행
            /*if (resumeVideoOnPreviewStop) {*/
            binding.playerView.player?.playWhenReady = true
        }
    }
}
