package edu.agh.susgame.front.gui.components.game.components.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.agh.susgame.R
import edu.agh.susgame.front.managers.state.GameStateManager

@Composable
fun LeftButtons(gameStateManager: GameStateManager) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth(0.06f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.router),
                contentDescription = "Fix router",
                modifier = Modifier
                    .clickable(
                        onClick = {
                            gameStateManager.areRouterBuffersShown.value =
                                !gameStateManager.areRouterBuffersShown.value
                        }
                    )
                    .alpha(
                        if (gameStateManager.areRouterBuffersShown.value) 1f else 0.3f
                    )
                    .padding(5.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.token0),
                contentDescription = "Fix router",
                modifier = Modifier
                    .clickable(
                        onClick = {
                            gameStateManager.areEdgesBandwidthShown.value =
                                !gameStateManager.areEdgesBandwidthShown.value
                        }
                    )
                    .alpha(
                        if (gameStateManager.areEdgesBandwidthShown.value) 1f else 0.3f
                    )
                    .padding(5.dp)
            )
        }
    }
}