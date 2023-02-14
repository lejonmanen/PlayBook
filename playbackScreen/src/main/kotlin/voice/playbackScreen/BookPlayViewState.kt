package voice.playbackScreen

import androidx.compose.runtime.Immutable
import voice.common.compose.ImmutableFile
import voice.data.ChapterMark
import voice.playback.misc.Decibel
import kotlin.time.Duration

@Immutable
data class BookPlayViewState(
  val chapterName: String?,
  val showPreviousNextButtons: Boolean,
  val title: String,
  val sleepTime: Duration,
  val sleepEoc: Boolean,
  val playedTime: Duration,
  val duration: Duration,
  val playing: Boolean,
  val cover: ImmutableFile?,
  val skipSilence: Boolean,
  // PlayBooks
  val showChapterNumbers: Boolean,
  val useChapterCover: Boolean,
  val playedTimeInPer: Long,
  val remainingTimeInMs: Long,
  val seekTime: Int,
  val seekTimeRewind: Int,
  val currentVolume: Int,
  val showSliderVolume: Boolean,
  val playbackSpeed: Float,
  val skipButtonStyle: Int,
  val playButtonStyle: Int,
  val paddings: String,
  val chapterMarks: List<ChapterMark>,
  val selectedIndex: Int?,
  val author: String?,
  val playerBackground: Int,
  val repeatModeBook: Int,
)

internal sealed interface BookPlayDialogViewState {
  data class SpeedDialog(
    val speed: Float,
  ) : BookPlayDialogViewState {

    val maxSpeed: Float get() = if (speed < 2F) 2F else 4F
  }

  data class VolumeGainDialog(
    val gain: Decibel,
    val valueFormatted: String,
    val maxGain: Decibel,
  ) : BookPlayDialogViewState

  data class SelectChapterDialog(
    val chapters: List<ChapterMark>,
    val selectedIndex: Int?,
  ) : BookPlayDialogViewState
}

data class PrefViewState(
  val repeatMode: Int,
)
