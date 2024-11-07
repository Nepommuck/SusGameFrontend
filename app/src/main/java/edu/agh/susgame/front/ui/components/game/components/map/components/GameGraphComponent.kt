package edu.agh.susgame.front.ui.components.game.components.map.components

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
import edu.agh.susgame.front.ui.graph.GameMapFront
import edu.agh.susgame.front.ui.graph.Path
import edu.agh.susgame.front.ui.graph.PathBuilder
import edu.agh.susgame.front.ui.graph.node.NodeId
import edu.agh.susgame.front.ui.graph.node.Server

private val SIZE_DP = 50.dp

@Composable
internal fun GameGraphComponent(
    gameMapFront: GameMapFront,
    gameService: GameService
) {
    val gameInfo by remember {
        mutableStateOf(gameMapFront)
    }

    gameService.initGameFront(gameInfo)

    val server = gameInfo.nodes[gameInfo.serverId] as Server

    var inspectedNodeId by remember { mutableStateOf<NodeId?>(null) }
    var playerIdChangingPath by remember { mutableStateOf<PlayerId?>(null) }
    var pathBuilderState by remember { mutableStateOf(PathBuilder()) }
    val packetsReceived by remember { server.packetsReceived }
    var isComputerViewVisible by remember { mutableStateOf(false) }


    val zoomState = remember {
        ZoomState(
            maxZoomIn = 2f,
            maxZoomOut = 0.5f,
            totalSize = gameInfo.mapSize,
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

            EdgeDrawer(gameMapFront = gameInfo)

            NodeDrawer(
                gameMapFront = gameInfo,
                playerIdChangingPath = playerIdChangingPath,
                pathBuilderState = pathBuilderState,
                onInspectedNodeChange = { newId -> inspectedNodeId = newId },
            )
        }

        Button(
            onClick = { server.setReceived(10) },
            modifier = Modifier.align(Alignment.TopEnd)
        ) { Text("PACKETS") } // JUST FOR TESTING, WILL BE DELETED


        inspectedNodeId?.let { nodeId ->
            gameInfo.nodes[nodeId]?.takeIf { playerIdChangingPath == null }?.let { node ->
                NodeInfoComp(
                    node = node,
                    onExit = { inspectedNodeId = null },
                    playerIdChangingPath = { newId -> playerIdChangingPath = newId },
                    pathBuilderState = pathBuilderState,
                    mapState = gameInfo
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
                            painter = painterResource(id = R.drawable.cross), // Drawable for abort
                            contentDescription = Translation.Game.ABORT_PATH,
                            modifier = Modifier.clickable {
                                gameInfo.edges.forEach { (_, edge) ->
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
                                .alpha(Calculate.getAlpha(pathBuilderState.isPathValid(serverId = gameInfo.serverId)))
                                .clickable(
                                    enabled = pathBuilderState.isPathValid(
                                        serverId = gameInfo.serverId
                                    )
                                ) {
                                    if (pathBuilderState.isPathValid(serverId = gameInfo.serverId)) {
                                        val path = Path(pathBuilderState.path)
                                        gameService.sendHostUpdate(
                                            NodeId(3), path.path, 2 )

//                                        gameInfo.getHostID(it)?.let { hostId ->
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

        ProgressBarComp(packetsReceived = packetsReceived, packetsToWin = server.packetsToWin)

        if (isComputerViewVisible) {
            ComputerComponent(gameService = gameService, gameMapFront = gameInfo)
        }
        NavIcons(
            isComputerVisible = isComputerViewVisible,
            setComputerViewVisibility = { visible -> setComputerViewVisibility(visible) }
        )
    }
}





