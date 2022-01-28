package com.stacon.exoplaysample.manager

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.github.rubensousa.previewseekbar.PreviewBar
import com.github.rubensousa.previewseekbar.PreviewLoader
import com.github.rubensousa.previewseekbar.exoplayer.PreviewTimeBar
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util

import com.bumptech.glide.request.target.Target
import com.google.android.exoplayer2.ExoPlayer
import java.nio.ByteBuffer
import java.security.MessageDigest

class ExoPlayerManager(
    private val playerView: PlayerView,
    private val previewTimeBar: PreviewTimeBar, private val imageView: ImageView,
    thumbnailsUrl: String,
) : PreviewLoader, PreviewBar.OnScrubListener {
    private var player: ExoPlayer? = null
    private val thumbnailsUrl: String
    private var resumeVideoOnPreviewStop: Boolean
    private val eventListener: Player.EventListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_READY && playWhenReady) {
                previewTimeBar.hidePreview()
            }
        }
    }

    fun play(uri: Uri?) {
        // mediaSourceBuilder.setUri(uri)
    }

    fun onStart() {
        if (Util.SDK_INT > 23) {
            createPlayers()
        }
    }

    fun onResume() {
        if (Util.SDK_INT <= 23) {
            createPlayers()
        }
    }

    fun onPause() {
        if (Util.SDK_INT <= 23) {
            releasePlayers()
        }
    }

    fun onStop() {
        if (Util.SDK_INT > 23) {
            releasePlayers()
        }
    }

    fun setResumeVideoOnPreviewStop(resume: Boolean) {
        resumeVideoOnPreviewStop = resume
    }

    private fun releasePlayers() {
        if (player != null) {
            player!!.release()
            player = null
        }
    }

    private fun createPlayers() {
        if (player != null) {
            player!!.release()
        }
        player = createPlayer()
        playerView.player = player
        playerView.controllerShowTimeoutMs = 15000
    }

    private fun createPlayer(): SimpleExoPlayer {
        val player = SimpleExoPlayer.Builder(playerView.context).build()
        player.playWhenReady = true
//        player.prepare(mediaSourceBuilder.getMediaSource(false))
        player.addListener(eventListener)
        return player
    }

    override fun loadPreview(currentPosition: Long, max: Long) {
        if (player!!.isPlaying) {
            player!!.playWhenReady = false
        }
        Glide.with(imageView)
            .load(thumbnailsUrl)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .transform(GlideThumbnailTransformation(currentPosition))
            .into(imageView)
    }

    override fun onScrubStart(previewBar: PreviewBar) {
        player!!.playWhenReady = false
    }

    override fun onScrubMove(previewBar: PreviewBar, progress: Int, fromUser: Boolean) {}
    override fun onScrubStop(previewBar: PreviewBar) {
        if (resumeVideoOnPreviewStop) {
            player!!.playWhenReady = true
        }
    }

    init {
        this.thumbnailsUrl = thumbnailsUrl
        previewTimeBar.addOnScrubListener(this)
        previewTimeBar.setPreviewLoader(this)
        resumeVideoOnPreviewStop = true
    }
}


class GlideThumbnailTransformation(position: Long) : BitmapTransformation() {
    private val x: Int
    private val y: Int

    override fun transform(
        pool: BitmapPool, toTransform: Bitmap,
        outWidth: Int, outHeight: Int,
    ): Bitmap {
        val width = toTransform.width / MAX_COLUMNS
        val height = toTransform.height / MAX_LINES
        return Bitmap.createBitmap(toTransform, x * width, y * height, width, height)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        val data = ByteBuffer.allocate(8).putInt(x).putInt(y).array()
        messageDigest.update(data)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as GlideThumbnailTransformation
        return if (x != that.x) false else y == that.y
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    companion object {
        private const val MAX_LINES = 7
        private const val MAX_COLUMNS = 7
        private const val THUMBNAILS_EACH = 5000 // milliseconds
    }

    init {
        val square = position.toInt() / THUMBNAILS_EACH
        y = square / MAX_LINES
        x = square % MAX_COLUMNS
    }
}
