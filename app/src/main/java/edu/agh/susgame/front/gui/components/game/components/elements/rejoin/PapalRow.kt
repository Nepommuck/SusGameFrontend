package edu.agh.susgame.front.gui.components.game.components.elements.rejoin

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.agh.susgame.R


@Composable
fun PapalRow() {
    val mediaPlayer = MediaPlayer.create(
        LocalContext.current,
        R.raw.disconnect,
    )

    mediaPlayer.start()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 100.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Image(
            painter = painterResource(R.drawable.jp1),
            contentDescription = "Jan Paweł I",
            contentScale = ContentScale.Fit,
        )
        Image(
            painter = painterResource(R.drawable.jp2),
            contentDescription = "Jan Paweł II",
            contentScale = ContentScale.Fit,
        )
    }
}
