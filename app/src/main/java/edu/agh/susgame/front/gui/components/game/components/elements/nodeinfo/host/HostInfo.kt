package edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.host

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import edu.agh.susgame.R
import edu.agh.susgame.front.gui.components.common.graph.node.Host
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.managers.GameManager

@Composable
fun HostInfo(
    host: Host,
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
                    text = "${Translation.Game.PACKETS_TO_SEND}:",
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
                    text = host.getFlow(),
                    style = TextStyler.TerminalS,
                    textAlign = TextAlign.Center,
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                Image(
                    painter = painterResource(id = R.drawable.token0),
                    contentDescription = "Fix router",
                    modifier = Modifier.fillMaxSize(0.7f)
                )
            }
            Box(modifier = Modifier.weight(4f)) {
                Text(
                    text = gameManager.getPlayerTokens(host.playerId).intValue.toString(),
                    style = TextStyler.TerminalS,
                    textAlign = TextAlign.Left,
                )
            }
        }
    }
}

