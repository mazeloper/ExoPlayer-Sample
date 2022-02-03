package com.stacon.exoplaysample

import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.metadata.Metadata
import com.google.android.exoplayer2.text.Cue
import com.google.android.exoplayer2.video.VideoSize
import com.stacon.exoplaysample.util.JsLog

object ExoListener {
    fun playerEventListener() = object : Listener {

        /** 타임라인이 새로 고쳐지면 호출 */
        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            super.onTimelineChanged(timeline, reason)
            JsLog.information("타임라인이 새로 고쳐지면 호출 >>>> reason : ${if (reason == 0) "재생 목록 항목 또는 항목 순서 변경의 결과" else "재생된 미디어에 의한 동적 업데이트 결과"}")
        }

        /** 재생이 미디어 항목으로 전환되거나 현재에 따라 미디어 항목 반복을 시작할 때 호출 */
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
            mediaItem?.let { JsLog.error("mediaItem : ${it.mediaId}") }
            JsLog.information("재생이 미디어 항목으로 전환되거나 현재에 따라 미디어 항목 반복을 시작할 때 호출")
        }

        /** 사용 가능하거나 선택한 트랙이 변경될 때 호출 */
        override fun onTracksInfoChanged(tracksInfo: TracksInfo) {
            super.onTracksInfoChanged(tracksInfo)
            JsLog.information("사용 가능하거나 선택한 트랙이 변경될 때 호출")
        }

        /** MediaMetadata 결합된 변경사항이 있을 때 호출 */
        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            super.onMediaMetadataChanged(mediaMetadata)
            JsLog.information("MediaMetadata 결합된 변경사항이 있을 때 호출")
        }

        /** MediaMetadata 재생목록이 변경될 때 호출 */
        override fun onPlaylistMetadataChanged(mediaMetadata: MediaMetadata) {
            super.onPlaylistMetadataChanged(mediaMetadata)
            JsLog.information("MediaMetadata 재생목록이 변경될 때 호출")
        }

        /** 플레이어가 소스 로드를 시작하거나 중지할 때 호출 */
        override fun onIsLoadingChanged(isLoading: Boolean) {
            super.onIsLoadingChanged(isLoading)
            // JsLog.information("플레이어가 소스 로드를 시작하거나 중지할 때 호출 :  $isLoading")
        }

        /** Player.isCommandAvailable(int) 적어도 하나의 변경사항에서 반환된 값이 호출될때 호출 */
        override fun onAvailableCommandsChanged(availableCommands: Player.Commands) {
            super.onAvailableCommandsChanged(availableCommands)
            JsLog.information("Player.isCommandAvailable(int) 적어도 하나의 변경사항에서 반환된 값이 호출될때 호출")
        }

        /** Player.getPlaybackState() 값이 변경되어 반환할 때 호출 */
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            // JsLog.information("Player.getPlaybackState() 값이 변경되어 반환할 때 호출")
        }

        /** Player.getPlayWhenReady() 값이 변경되어 반환할 때 호출 */
        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            super.onPlayWhenReadyChanged(playWhenReady, reason)
            JsLog.information("Player.getPlayWhenReady() 값이 변경되어 반환할 때 호출")
        }

        /** Player.getPlaybackSuppressionReason() 값이 변경되어 반환할 때 호출 */
        override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {
            super.onPlaybackSuppressionReasonChanged(playbackSuppressionReason)
            JsLog.information("Player.getPlaybackSuppressionReason() 값이 변경되어 반환할 때 호출")
        }

        /** Player.isPlaying() 값이 변경될 때 호출 */
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            JsLog.information("Player.isPlaying() 값이 변경될 때 호출")
        }

        /** Player.getRepeatMode() 값이 변경될 때 호출 */
        override fun onRepeatModeChanged(repeatMode: Int) {
            super.onRepeatModeChanged(repeatMode)
            JsLog.information("Player.getRepeatMode() 값이 변경될 때 호출")
        }

        /** Player.getShuffleModeEnabled() 값이 변경될 때 호출 */
        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            super.onShuffleModeEnabledChanged(shuffleModeEnabled)
            JsLog.information("Player.getShuffleModeEnabled() 값이 변경될 때 호출")
        }

        /** 오류발생 시 호출 */
        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            JsLog.information("오류발생 시 호출")
        }

        /** 에러 변경사항에 의해 반환될 때 호출 */
        override fun onPlayerErrorChanged(error: PlaybackException?) {
            super.onPlayerErrorChanged(error)
            JsLog.information("에러 변경사항에 의해 반환될 때 호출")
        }

        /** 위치 불연속성이 발생될 때 호출 */
        override fun onPositionDiscontinuity(oldPosition: PositionInfo, newPosition: PositionInfo, reason: Int) {
            super.onPositionDiscontinuity(oldPosition, newPosition, reason)
            val old = (oldPosition.positionMs / 1000).let {
                String.format("%02d:%02d", it / 60, it % 60)
            }
            val new = (newPosition.positionMs / 1000).let {
                String.format("%02d:%02d", it / 60, it % 60)
            }
            val r: String = when (reason) {
                DISCONTINUITY_REASON_AUTO_TRANSITION -> "DISCONTINUITY_REASON_AUTO_TRANSITION"
                DISCONTINUITY_REASON_SEEK -> "DISCONTINUITY_REASON_SEEK"
                DISCONTINUITY_REASON_SEEK_ADJUSTMENT -> "DISCONTINUITY_REASON_SEEK_ADJUSTMENT"
                DISCONTINUITY_REASON_SKIP -> "DISCONTINUITY_REASON_SKIP"
                DISCONTINUITY_REASON_REMOVE -> "DISCONTINUITY_REASON_REMOVE"
                DISCONTINUITY_REASON_INTERNAL -> "DISCONTINUITY_REASON_INTERNAL"
                else -> "do nothing..."
            }
            JsLog.information("위치 불연속성이 발생될 때 호출 >>>> oldPosition : $old  ,  newPosition : $new , reason: $r")
            // TODO: 2022/02/03 유저액션
        }

        /** 현재 재생 매개변수가 변경될 때 호출 */
        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
            super.onPlaybackParametersChanged(playbackParameters)
            JsLog.information("현재 재생 매개변수가 변경될 때 호출")
        }

        /** Player.getSeekBackIncrement() 값이 변경될 때 호출 */
        override fun onSeekBackIncrementChanged(seekBackIncrementMs: Long) {
            super.onSeekBackIncrementChanged(seekBackIncrementMs)
            JsLog.information("Player.getSeekBackIncrement() 값이 변경될 때 호출")
        }

        /** Player.getSeekForwardIncrement() 값이 변경될 때 호출 */
        override fun onSeekForwardIncrementChanged(seekForwardIncrementMs: Long) {
            super.onSeekForwardIncrementChanged(seekForwardIncrementMs)
            JsLog.information("Player.getSeekForwardIncrement() 값이 변경될 때 호출")
        }

        /** 하나 이상의 플레이어 상태가 변경될 때 호출 */
        override fun onEvents(player: Player, events: Events) {
            super.onEvents(player, events)
            eventHandling(events)
        }

        /** 오디오 세션 ID가 변경될 때 호출 */
        override fun onAudioSessionIdChanged(audioSessionId: Int) {
            super.onAudioSessionIdChanged(audioSessionId)
            JsLog.information("오디오 세션 ID가 변경될 때 호출")
        }

        /** 오디오 속성이 변경될 때 호출 */
        override fun onAudioAttributesChanged(audioAttributes: AudioAttributes) {
            super.onAudioAttributesChanged(audioAttributes)
            JsLog.information("오디오 속성이 변경될 때 호출")
        }

        /** 볼륨이 변경될 때 호출 */
        override fun onVolumeChanged(volume: Float) {
            super.onVolumeChanged(volume)
            JsLog.information("볼륨이 변경될 때 호출")
        }

        /** 오디오 스트림에서 묵음 건너뛰기가 활성화 or 비활성화될 때 호출 */
        override fun onSkipSilenceEnabledChanged(skipSilenceEnabled: Boolean) {
            super.onSkipSilenceEnabledChanged(skipSilenceEnabled)
            JsLog.information("오디오 스트림에서 묵음 건너뛰기가 활성화 or 비활성화될 때 호출")
        }

        /** 디바이스 정보가 변경될 때 호출 */
        override fun onDeviceInfoChanged(deviceInfo: DeviceInfo) {
            super.onDeviceInfoChanged(deviceInfo)
            JsLog.information("디바이스 정보가 변경될 때 호출")
        }

        /** 디바이스 볼륨 또는 음소거 상태가 변경될 때 호출 */
        override fun onDeviceVolumeChanged(volume: Int, muted: Boolean) {
            super.onDeviceVolumeChanged(volume, muted)
            JsLog.information("디바이스 볼륨 또는 음소거 상태가 변경될 때 호출")
        }

        /** 렌더링되는 비디오의 크기가 변경될 때마다 호출*/
        override fun onVideoSizeChanged(videoSize: VideoSize) {
            super.onVideoSizeChanged(videoSize)
            JsLog.information("렌더링되는 비디오의 크기가 변경될 때마다 호출")
        }

        /** 비디오가 렌더링되는 표면의 크기가 변경될 때마다 호출 */
        override fun onSurfaceSizeChanged(width: Int, height: Int) {
            super.onSurfaceSizeChanged(width, height)
            JsLog.information("비디오가 렌더링되는 표면의 크기가 변경될 때마다 호출")
        }

        /** 표면을 설정한 이후, 렌더러가 재설정된 이후, 또는 렌더링되는 스트림이 변경된 이후로 프레임이 처음으로 렌더링될 때 호출 */
        override fun onRenderedFirstFrame() {
            super.onRenderedFirstFrame()
            JsLog.information("표면을 설정한 이후, 렌더러가 재설정된 이후, 또는 렌더링되는 스트림이 변경된 이후로 프레임이 처음으로 렌더링될 때 호출")
        }

        /** Cues 에 변경 사항이 있을 때 호출 */
        override fun onCues(cues: MutableList<Cue>) {
            super.onCues(cues)
            JsLog.information("Cues 에 변경 사항이 있을 때 호출")
        }

        /** 현재 재생 시간과 관련된 메타데이터가 있을 때 호출 */
        override fun onMetadata(metadata: Metadata) {
            super.onMetadata(metadata)
            JsLog.information("현재 재생 시간과 관련된 메타데이터가 있을 때 호출")
        }
    }

    private fun eventHandling(events: Events) {
        val size = if (events.size() > 0) events.size() else return
        for (i in 0 until size) {
            when (events[i]) {
                EVENT_TIMELINE_CHANGED -> eventLog("Player.EVENT_TIMELINE_CHANGED")
                EVENT_MEDIA_ITEM_TRANSITION -> eventLog("Player.EVENT_MEDIA_ITEM_TRANSITION")
                EVENT_TRACKS_CHANGED -> eventLog("Player.EVENT_TRACKS_CHANGED")
                EVENT_PLAYBACK_STATE_CHANGED -> eventLog("Player.EVENT_PLAYBACK_STATE_CHANGED")
                EVENT_PLAY_WHEN_READY_CHANGED -> eventLog("Player.EVENT_PLAY_WHEN_READY_CHANGED")
                EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED -> eventLog("Player.EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED")
                EVENT_IS_PLAYING_CHANGED -> eventLog("Player.EVENT_IS_PLAYING_CHANGED")
                EVENT_REPEAT_MODE_CHANGED -> eventLog("Player.EVENT_REPEAT_MODE_CHANGED")
                EVENT_SHUFFLE_MODE_ENABLED_CHANGED -> eventLog("Player.EVENT_SHUFFLE_MODE_ENABLED_CHANGED")
                EVENT_PLAYER_ERROR -> eventLog("Player.EVENT_PLAYER_ERROR")
                EVENT_POSITION_DISCONTINUITY -> eventLog("Player.EVENT_POSITION_DISCONTINUITY")
                EVENT_PLAYBACK_PARAMETERS_CHANGED -> eventLog("Player.EVENT_PLAYBACK_PARAMETERS_CHANGED")
                EVENT_AVAILABLE_COMMANDS_CHANGED -> eventLog("Player.EVENT_AVAILABLE_COMMANDS_CHANGED")
                EVENT_MEDIA_METADATA_CHANGED -> eventLog("Player.EVENT_MEDIA_METADATA_CHANGED")
                EVENT_PLAYLIST_METADATA_CHANGED -> eventLog("Player.EVENT_PLAYLIST_METADATA_CHANGED")
                EVENT_SEEK_BACK_INCREMENT_CHANGED -> eventLog("Player.EVENT_SEEK_BACK_INCREMENT_CHANGED")
                EVENT_SEEK_FORWARD_INCREMENT_CHANGED -> eventLog("Player.EVENT_SEEK_FORWARD_INCREMENT_CHANGED")
                EVENT_MAX_SEEK_TO_PREVIOUS_POSITION_CHANGED -> eventLog("Player.EVENT_MAX_SEEK_TO_PREVIOUS_POSITION_CHANGED")
                EVENT_TRACK_SELECTION_PARAMETERS_CHANGED -> eventLog("Player.EVENT_TRACK_SELECTION_PARAMETERS_CHANGED")
            }
        }
    }

    private fun eventLog(str: String) {
        // JsLog.debug("PlayerListener >> onEvents Flag = $str")
    }
}