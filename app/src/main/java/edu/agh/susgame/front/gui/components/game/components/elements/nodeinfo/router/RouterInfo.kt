package edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.router

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import edu.agh.susgame.front.gui.components.common.graph.node.Router
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.PlayerIcon
import edu.agh.susgame.front.managers.GameManager


@Composable
fun RouterInfo(
    router: Router,
    gameManager: GameManager
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "${Translation.Game.STATE}:",
                    style = TextStyler.TerminalS,
                    textAlign = TextAlign.Left,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = router.getState(),
                    style = TextStyler.TerminalS,
                    textAlign = TextAlign.Center,
                    color = if (!router.isWorking.value) Color.Red.copy(alpha = 0.7f) else (Color.Green.copy(
                        alpha = 0.7f
                    ))
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "${Translation.Game.ROUTING}:",
                    style = TextStyler.TerminalS,
                    textAlign = TextAlign.Left,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .weight(1f),
                horizontalArrangement = Arrangement.Absolute.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                router.playersSet.forEach{
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center){
                        PlayerIcon(color = gameManager.getPlayerColor(playerId = it))
                    }
                }

            }
        }
    }
}