package edu.agh.susgame.front.ui.components.menu.components.lobby.elements.icons


import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import edu.agh.susgame.R
import edu.agh.susgame.front.ui.components.common.util.player.PlayerStatus

@Composable
internal fun PlayerStatusIcon(
    playerStatus: MutableState<PlayerStatus>
) {
    val status by playerStatus
    val iconId: Int = when (status) {
        PlayerStatus.READY -> R.drawable.accept
        PlayerStatus.NOT_READY -> R.drawable.cross
        PlayerStatus.CONNECTING -> R.drawable.menu
    }

    Image(
        painter = painterResource(id = iconId),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(
                scaleX = 0.3f,
                scaleY = 0.3f,
            )
    )

}