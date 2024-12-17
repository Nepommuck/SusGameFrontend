package edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.host

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.gui.components.common.graph.node.Host
import edu.agh.susgame.front.managers.GameManager

@Composable
fun HostBottom(
    host: Host,
    gameManager: GameManager
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (host.id == gameManager.hostIdByPlayerId[gameManager.localPlayerId]) {
            Slider(
                value = host.packetsToSend.intValue.toFloat(),
                onValueChange = { newValue ->
                    gameManager.handleHostFlowChange(
                        hostId = host.id,
                        flow = newValue.toInt()
                    )
                },
                valueRange = 0f..host.maxPacketsToSend.intValue.toFloat(),
                steps = host.maxPacketsToSend.intValue + 1,
            )
        }
    }

}