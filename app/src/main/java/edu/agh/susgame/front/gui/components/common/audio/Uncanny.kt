package edu.agh.susgame.front.gui.components.common.audio

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import edu.agh.susgame.R

@Composable
fun AudioPlayer(audioId: MutableState<Int?>) {
    val context = LocalContext.current
    val resourceId = when (audioId.value) {
        2 -> R.raw.uncanny_2
        3 -> R.raw.uncanny_3
        4 -> R.raw.uncanny_4
        5 -> R.raw.uncanny_5
        6 -> R.raw.uncanny_6
        else -> null
    }

    // Trzymaj referencję do MediaPlayera
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    LaunchedEffect(audioId.value) {
        // Jeśli jest aktywny MediaPlayer, zatrzymaj i zwolnij zasoby
        mediaPlayer?.let {
            it.stop()
            it.release()
        }

        // Jeśli audioId jest nie null, odtwórz nowy dźwięk
        resourceId?.let { newAudioId ->
            mediaPlayer = MediaPlayer.create(context, newAudioId).apply {
                setOnCompletionListener {
                    release() // Zwolnij MediaPlayer po zakończeniu odtwarzania
                    mediaPlayer = null
                }
                start()
            }
        }
    }
}