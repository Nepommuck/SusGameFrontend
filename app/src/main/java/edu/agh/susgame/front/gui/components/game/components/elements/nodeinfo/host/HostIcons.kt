package edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.host

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.agh.susgame.R
import edu.agh.susgame.front.gui.components.common.graph.node.Host
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.Icons
import edu.agh.susgame.front.managers.GameManager

@Composable
fun HostIcons(
    host: Host,
    gameManager: GameManager,
    onExit: () -> Unit
) {
    val isPathValid by gameManager.pathBuilder.isPathValid
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {
        if (gameManager.localPlayerId == host.playerId) {
            if (!gameManager.gameState.isPathBeingChanged.value) {
                Icons(resourceId = R.drawable.shuffle,
                    cost = null,
                    modifier = Modifier
                        .fillMaxSize(0.6f)
                        .clickable {
                            gameManager.clearEdges(gameManager.localPlayerId)
                            gameManager.gameState.isPathBeingChanged.value = true
                            gameManager.addNodeToPathBuilder(nodeId = host.id)
                        }
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(0.8f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                if (isPathValid) {
                                    gameManager.handlePathChange()
                                    gameManager.gameState.isPathBeingChanged.value = false
                                    gameManager.gameState.currentlyInspectedNode.value = null
                                }
                            },
                            modifier = Modifier.requiredWidthIn(min = 120.dp),
                            enabled = isPathValid,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Green.copy(alpha = 0.3f),
                                disabledContainerColor = Color.LightGray.copy(alpha = 0.3f),
                            ),
                            shape = RoundedCornerShape(16.dp),

                            ) {
                            Text(
                                text = Translation.Game.ACCEPT_PATH,
                                style = TextStyler.TerminalL,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                gameManager.clearEdges(gameManager.localPlayerId)
                                gameManager.gameState.isPathBeingChanged.value = false
                                gameManager.pathBuilder.reset()
                            },
                            modifier = Modifier.requiredWidthIn(min = 120.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red.copy(alpha = 0.3f),
                                disabledContainerColor = Color.LightGray.copy(alpha = 0.3f),
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = Translation.Game.ABORT_PATH,
                                style = TextStyler.TerminalL,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .weight(1f)
//                            .clickable {
//                                gameManager.clearEdges(gameManager.localPlayerId)
//                                gameManager.gameState.isPathBeingChanged.value = false
//                                gameManager.pathBuilder.reset()
//                            }
//                    ) {
//                        Text(text = "ODRZUĆ")
//                    }


                }
            }
        }
    }
}
//    Column(
//        modifier = Modifier
//            .fillMaxSize(),
//
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.SpaceEvenly
//    )
//    {
//        Image(
//            painter = painterResource(id = R.drawable.accept),
//            contentDescription = Translation.Game.ACCEPT_PATH,
//            modifier = Modifier
//                .fillMaxSize(0.6f)
//                .weight(1f)
//                .alpha(
//                    Calculate.getAlpha(isPathValid)
//                )
//                .clickable(
//                    enabled = isPathValid
//                ) {
//                    if (isPathValid) {
//                        gameManager.handlePathChange()
//                        gameManager.gameState.isPathBeingChanged.value = false
//                        gameManager.gameState.currentlyInspectedNode.value = null
//                    }
//                }
//        )
//        Image(
//            painter = painterResource(id = R.drawable.cross),
//            contentDescription = Translation.Game.ABORT_PATH,
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxSize(0.6f)
//                .clickable {
//                    gameManager.clearEdges(gameManager.localPlayerId)
//                    gameManager.gameState.isPathBeingChanged.value = false
//                    gameManager.pathBuilder.reset()
//                }
//        )


