package edu.agh.susgame.front.ui.components.game.components.map.components

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
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.front.Config
import edu.agh.susgame.front.Translation
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.ui.components.common.theme.PaddingS
import edu.agh.susgame.front.ui.components.common.util.Calculate
import edu.agh.susgame.front.ui.components.common.util.ZoomState
import edu.agh.susgame.front.ui.components.game.components.computer.ComputerComponent
import edu.agh.susgame.front.ui.components.game.components.map.components.drawers.EdgeDrawer
import edu.agh.susgame.front.ui.components.game.components.map.components.drawers.NodeDrawer
import edu.agh.susgame.front.ui.components.game.components.map.components.elements.NodeInfoComp
import edu.agh.susgame.front.ui.components.game.components.map.components.elements.ProgressBarComp
import edu.agh.susgame.front.ui.components.game.components.map.components.elements.bottombar.NavIcons
import edu.agh.susgame.front.ui.graph.GameManager
import edu.agh.susgame.front.ui.graph.Path
import edu.agh.susgame.front.ui.graph.PathBuilder
import edu.agh.susgame.front.ui.graph.node.NodeId
import edu.agh.susgame.front.utils.ProviderType

private val SIZE_DP = 50.dp

@Composable
internal fun GameGraphComponent(
    gameManager: GameManager,
    gameService: GameService
) {
    when (Config.providers) {
        ProviderType.MockLocal -> {

        }

        ProviderType.Web ->
            LaunchedEffect(Unit) {
                gameService.initGameFront(gameManager)
                gameService.sendStartGame()
            }

    }

    var inspectedNodeId by remember { mutableStateOf<NodeId?>(null) }
    var playerIdChangingPath by remember { mutableStateOf<PlayerId?>(null) }
    var pathBuilderState by remember { mutableStateOf(PathBuilder()) }
    var isComputerViewVisible by remember { mutableStateOf(false) }


    val zoomState = remember {
        ZoomState(
            maxZoomIn = 2f,
            maxZoomOut = 0.5f,
            totalSize = gameManager.mapSize,
        )
    }

    fun setComputerViewVisibility(visible: Boolean) {
        isComputerViewVisible = visible
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
                playerIdChangingPath = playerIdChangingPath,
                pathBuilderState = pathBuilderState,
                onInspectedNodeChange = { newId -> inspectedNodeId = newId },
            )
        }

        inspectedNodeId?.let { nodeId ->
            gameManager.nodes[nodeId]?.takeIf { playerIdChangingPath == null }?.let { node ->
                NodeInfoComp(
                    node = node,
                    onExit = { inspectedNodeId = null },
                    playerIdChangingPath = { newId -> playerIdChangingPath = newId },
                    pathBuilderState = pathBuilderState,
                    gameManager = gameManager
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
        ) {
            playerIdChangingPath?.let {
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
                                gameManager.edges.forEach { (_, edge) ->
                                    edge.removePlayer(playerIdChangingPath!!)
                                }
                                playerIdChangingPath = null
                                pathBuilderState = PathBuilder()
                            }
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(SIZE_DP)
                            .padding(PaddingS)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.accept), // Drawable for accept
                            contentDescription = Translation.Game.ACCEPT_PATH,
                            modifier = Modifier
                                .alpha(Calculate.getAlpha(pathBuilderState.isPathValid(serverId = gameManager.serverId)))
                                .clickable(
                                    enabled = pathBuilderState.isPathValid(
                                        serverId = gameManager.serverId
                                    )
                                ) {
                                    if (pathBuilderState.isPathValid(serverId = gameManager.serverId)) {
                                        val path = Path(pathBuilderState.path)
                                        gameService.sendHostUpdate(
                                            NodeId(3), path.path, 2
                                        )

//                                        gameInfo.value.getHostID(it)?.let { hostId ->
//                                            gameService.sendHostUpdate(
//                                                NodeId(2), pathBuilderState.path, 1 )
//                                        }
//                                        gameGraphProvider.changePlayerPath( // THERE SHOULD BE REQUEST TO THE SERVER
//                                            playerId = it, pathBuilder = pathBuilderState
//                                        )
                                        playerIdChangingPath = null
                                        inspectedNodeId = null
                                        pathBuilderState = PathBuilder()
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