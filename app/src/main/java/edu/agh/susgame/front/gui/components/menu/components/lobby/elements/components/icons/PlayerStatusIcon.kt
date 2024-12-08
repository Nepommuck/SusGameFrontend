package edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.icons


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import edu.agh.susgame.R
import edu.agh.susgame.front.gui.components.common.util.player.PlayerStatus

@Composable
internal fun PlayerStatusIcon(
    playerStatus: MutableState<PlayerStatus>
) {
    val status by playerStatus

    val iconId: Int = when (status) {
        PlayerStatus.READY -> R.drawable.ready
        PlayerStatus.NOT_READY -> R.drawable.unready
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}