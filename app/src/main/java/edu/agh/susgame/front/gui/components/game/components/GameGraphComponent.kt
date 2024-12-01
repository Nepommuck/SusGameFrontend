package edu.agh.susgame.front.gui.components.game.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.agh.susgame.R
import edu.agh.susgame.front.gui.components.common.graph.edge.Path
import edu.agh.susgame.front.gui.components.common.graph.node.NodeId
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
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.service.interfaces.GameService

private val SIZE_DP = 50.dp

@Composable
internal fun GameGraphComponent(
    gameManager: GameManager,
    gameService: GameService
) {
    LaunchedEffect(Unit) {
        gameService.initGameManager(gameManager)
        gameManager.addGameService(gameService)
        gameService.sendStartGame()
    }

    var inspectedNodeId by remember { mutableStateOf<NodeId?>(null) }
    var changingPath by gameManager.isPathBeingChanged
    val pathBuilder by gameManager.pathBuilder
    val isPathValid by gameManager.pathBuilder.value.isPathValid
    var isComputerViewVisible by remember { mutableStateOf(false) }

    fun setComputerViewVisibility(visible: Boolean) {
        isComputerViewVisible = visible
    }

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

            EdgeDrawer(gameManager = gameManager)

            NodeDrawer(
                gameManager = gameManager,
                changingPath = changingPath,
                onInspectedNodeChange = { newId -> inspectedNodeId = newId },
            )
        }

        inspectedNodeId?.let { nodeId ->
            gameManager.nodesById[nodeId]?.takeIf { !changingPath }?.let { node ->
                NodeInfoComp(
                    node = node,
                    onExit = { inspectedNodeId = null },
                    changingPath = { state -> changingPath = state },
                    gameManager = gameManager
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
        ) {
            if (changingPath) {
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
                                gameManager.clearEdgesLocal(gameManager.localPlayerId)
                                changingPath = false
                                pathBuilder.reset()

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
                                        gameManager.updatePathFromLocal(Path(pathBuilder.path))
                                        changingPath = false
                                        inspectedNodeId = null
                                        pathBuilder.reset()
                                    }
                                }
                        )
                    }
                }
            }
        }
        ProgressBarComp(gameManager = gameManager)

        if (isComputerViewVisible) {
            ComputerComponent(gameService = gameService, gameManager = gameManager)
        }

        NavIcons(
            isComputerVisible = isComputerViewVisible,
            setComputerViewVisibility = { visible -> setComputerViewVisibility(visible) }
        )
    }
}
