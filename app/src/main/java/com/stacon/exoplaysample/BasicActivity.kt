package com.stacon.exoplaysample

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.stacon.exoplaysample.databinding.ActivityBasicBinding
import com.stacon.exoplaysample.util.JsLog


class BasicActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBasicBinding

    private var videoPlayer: ExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L


    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        // hideSystemUi()
        if ((Util.SDK_INT < 24 || videoPlayer == null)) {
            initializePlayer()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasicBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        // Basic
        // videoPlayer = ExoPlayer.Builder(this).build().also { exoPlayer ->
        videoPlayer = initTrackSelector().also { exoPlayer ->
            binding.playerView.player = exoPlayer
            /*
            // 재생목록 만들기_1
            exoPlayer.setMediaItem(MediaItem.fromUri(getString(R.string.media_url_mp4_1)))
             */

            /*
            // 재생목록 만들기_2
            val mediaList: List<MediaItem> = listOf(
                MediaItem.fromUri(getString(R.string.media_url_mp4_1)),
                MediaItem.fromUri(getString(R.string.media_url_mp4_2)),
                MediaItem.fromUri(getString(R.string.media_url_mp4_3))
            )
            exoPlayer.setMediaItems(mediaList)
             */

            /*
            // 재생목록 만들기_3
            exoPlayer.addMediaItem(MediaItem.fromUri(getString(R.string.media_url_mp4_1)))
            exoPlayer.addMediaItem(MediaItem.fromUri(getString(R.string.media_url_mp4_2)))
            exoPlayer.addMediaItem(MediaItem.fromUri(getString(R.string.media_url_mp4_3)))
            */

            // * 메타데이터를 사용하면 UI 업데이트 시 유용할 수 있다.
            val metaDataMediaItem = MediaItem.Builder()
                .setUri(getString(R.string.media_url_mp4_1))
                .setMediaId("mediaId")
                .setTag("0")
                .build()

            // * 비표준 파일 확장자 처리 (표준확장자 X || 확장자명시 X)
            val mimeTypeMediaItem = MediaItem.Builder()
                .setUri(getString(R.string.media_url_dash))
                .setMimeType(MimeTypes.APPLICATION_MPD)
                .setTag("1")
                .build()
            // + 자막 트랙 사이드 로딩, 미디어 스트림 클리핑, DRM 속성, 광고 삽입

            exoPlayer.addMediaItem(metaDataMediaItem)
            exoPlayer.addMediaItem(mimeTypeMediaItem)

            exoPlayer.playWhenReady = playWhenReady
            exoPlayer.seekTo(currentWindow, playbackPosition)
            exoPlayer.addListener(playerEventListener())
            exoPlayer.prepare()
        }
    }

    /**
     * Adaptive streaming
     *
     * 적응형 스트리밍 은 사용 가능한 네트워크 대역폭에 따라 스트림 품질을 변경하여 미디어를 스트리밍하는 기술입니다. 이를 통해 사용자는 대역폭이 허용하는 최고 품질의 미디어를 경험할 수 있습니다
     */
    private fun initTrackSelector(): ExoPlayer {
        // trackSelector 에 표준 화질 이하의 트랙만 선택하도록 지시하십시오.
        // 이는 품질을 희생하면서 사용자 데이터를 저장하는 좋은 방법입니다.
        val trackSelector = DefaultTrackSelector(this).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }
        return ExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        binding.playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
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
        override fun onPlaybackStateChanged(playbackState: Int) {
            val stateString: String = when (playbackState) {
                ExoPlayer.STATE_IDLE -> "STATE_IDLE"
                ExoPlayer.STATE_BUFFERING -> "STATE_BUFFERING"
                ExoPlayer.STATE_READY -> "STATE_READY"
                ExoPlayer.STATE_ENDED -> "STATE_ENDED"
                else -> "UNKNOWN_STATE"
            }
            JsLog.information("ExoPlayer State Changed :    $stateString")
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
            val tag = mediaItem?.localConfiguration?.tag
            binding.tvIndex.text = "Index : $tag"
        }
    }
}