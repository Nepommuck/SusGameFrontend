package edu.agh.susgame.front.gui.components.game.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.agh.susgame.R
import edu.agh.susgame.dto.socket.common.GameStatus
import edu.agh.susgame.front.gui.components.common.theme.PaddingS
import edu.agh.susgame.front.gui.components.common.util.Calculate
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.common.util.ZoomState
import edu.agh.susgame.front.gui.components.game.components.computer.ComputerComponent
import edu.agh.susgame.front.gui.components.game.components.drawers.EdgeDrawer
import edu.agh.susgame.front.gui.components.game.components.drawers.NodeDrawer
import edu.agh.susgame.front.gui.components.game.components.elements.NodeInfoComp
import edu.agh.susgame.front.gui.components.game.components.elements.ProgressBarComp
import edu.agh.susgame.front.gui.components.game.components.elements.bottombar.NavIcons
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.service.interfaces.GameService

private val SIZE_DP = 50.dp

@Composable
internal fun GameGraphComponent(
    gameManager: GameManager,
    gameService: GameService,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        gameService.initGameManager(gameManager)
        gameService.sendStartGame()
    }

    val gameState = gameManager.gameStateManager
    val isPathValid by gameManager.pathBuilder.isPathValid

    val zoomState = remember {
        ZoomState(
            maxZoomIn = 2f,
            maxZoomOut = 0.5f,
            totalSize = gameManager.mapSize,
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    zoomState.scale(zoom)
                    zoomState.move(pan)
                }
            }
            .graphicsLayer(
                scaleX = zoomState.scaleValue(),
                scaleY = zoomState.scaleValue(),
                translationX = zoomState.translationX(),
                translationY = zoomState.translationY(),
                clip = false
            )

        ) {

            EdgeDrawer(gameManager)

            NodeDrawer(gameManager)
        }

        gameState.currentlyInspectedNode.value
            ?.takeIf { !gameState.isPathBeingChanged.value }
            ?.let { node ->
                NodeInfoComp(
                    node = node,
                    onExit = { gameState.currentlyInspectedNode.value = null },
                    changingPath = { state -> gameState.isPathBeingChanged.value = state },
                    gameManager = gameManager,
                )
            }

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
        ) {
            if (gameState.isPathBeingChanged.value) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(SIZE_DP)
                            .padding(PaddingS)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.cross),
                            contentDescription = Translation.Game.ABORT_PATH,
                            modifier = Modifier.clickable {
                                gameManager.clearEdges(gameManager.localPlayerId)
                                gameState.isPathBeingChanged.value = false
                                gameManager.pathBuilder.reset()
                            }
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(SIZE_DP)
                            .padding(PaddingS)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.accept),
                            contentDescription = Translation.Game.ACCEPT_PATH,
                            modifier = Modifier
                                .alpha(
                                    Calculate.getAlpha(isPathValid)
                                )
                                .clickable(
                                    enabled = isPathValid
                                ) {
                                    if (isPathValid) {
                                        gameManager.handlePathChange()
                                        gameState.isPathBeingChanged.value = false
                                        gameState.currentlyInspectedNode.value = null
                                    }
                                }
                        )
                    }
                }
            }
        }
        ProgressBarComp(gameManager = gameManager)

        if (gameState.isComputerViewVisible.value) {
            ComputerComponent(gameService = gameService, gameManager = gameManager)
        }

        NavIcons(
            isComputerVisible = gameState.isComputerViewVisible,
        )
    }

    when (gameManager.gameStatus.value) {
        GameStatus.FINISHED_WON, GameStatus.FINISHED_LOST -> {
            val message =
                if (gameManager.gameStatus.value == GameStatus.FINISHED_WON) Translation.Game.YOU_WON else Translation.Game.YOU_LOST
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { navController.navigate(MenuRoute.SearchLobby.route) }) {
                    Text(message)
                }
            }
        }

        else -> { /* Do nothing for other states */
        }
    }
}
