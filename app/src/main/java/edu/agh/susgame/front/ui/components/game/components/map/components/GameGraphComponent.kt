package edu.agh.susgame.front.ui.components.game.components.map.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import edu.agh.susgame.R
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.front.Translation
import edu.agh.susgame.front.model.graph.GameGraph
import edu.agh.susgame.front.model.graph.NodeId
import edu.agh.susgame.front.model.graph.PathBuilder
import edu.agh.susgame.front.service.interfaces.ServerMapProvider
import edu.agh.susgame.front.ui.components.common.util.ZoomState
import edu.agh.susgame.front.ui.components.game.components.map.components.drawers.EdgeDrawer
import edu.agh.susgame.front.ui.components.game.components.map.components.drawers.NodeDrawer
import edu.agh.susgame.front.ui.components.game.components.map.components.elements.NodeInfoComp

private const val buttonWidth = 70
private const val buttonHeight = 30

@Composable
internal fun GameGraphComponent(
    gameGraph: GameGraph,
    gameGraphProvider: ServerMapProvider,
) {
    var inspectedNodeId by remember { mutableStateOf<NodeId?>(null) }
    var playerIdChangingPath by remember { mutableStateOf<PlayerId?>(null) }
    var pathBuilderState by remember { mutableStateOf(PathBuilder()) }


    val zoomState = remember {
        ZoomState(
            maxZoomIn = 2f,
            maxZoomOut = 0.5f,
            totalSize = gameGraph.mapSize,
        )
    }
    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(
                scaleX = 2f,
                scaleY = 3f,
                rotationZ = 90f
            )
    )

    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .clipToBounds()
        .pointerInput(Unit) {
            detectTransformGestures { _, pan, zoom, _ ->
                zoomState.scale(zoom)
                zoomState.move(pan)
            }
        })
    {

        Box(
            modifier = Modifier.graphicsLayer(
                scaleX = zoomState.scaleValue(),
                scaleY = zoomState.scaleValue(),
                translationX = zoomState.translationX(),
                translationY = zoomState.translationY(),
                clip = false
            )
        ) {
            EdgeDrawer(gameGraph = gameGraph)

            NodeDrawer(
                gameGraph = gameGraph,
                playerIdChangingPath = playerIdChangingPath,
                pathBuilderState = pathBuilderState,
                onInspectedNodeChange = { newId -> inspectedNodeId = newId },
            )

            Text(zoomState.scaleValue().toString(), color = Color.Black)

        }

        inspectedNodeId?.let { nodeId ->
            gameGraph.nodes[nodeId]?.takeIf { playerIdChangingPath == null }?.let { node ->
                NodeInfoComp(
                    node = node,
                    onExit = { inspectedNodeId = null },
                    playerIdChangingPath = { newId -> playerIdChangingPath = newId },
                    pathBuilderState = pathBuilderState,
                    mapState = gameGraph
                )
            }
        }


        playerIdChangingPath?.let {
            Column {
                Button(onClick = {
                    gameGraph.edges.forEach { (_, edge) -> edge.removePlayer(playerIdChangingPath!!) }
                    playerIdChangingPath = null
                    pathBuilderState = PathBuilder()

                }) {
                    Text(Translation.Game.ABORT_PATH)
                }
                Button(
                    onClick = {
                        if (pathBuilderState.isPathValid(serverId = gameGraph.serverId)) {
                            gameGraphProvider.changePlayerPath(
                                playerId = it, pathBuilder = pathBuilderState
                            )
                            playerIdChangingPath = null
                            inspectedNodeId = null
                            pathBuilderState = PathBuilder()
                        }

                    }, enabled = pathBuilderState.isPathValid(serverId = gameGraph.serverId)


                ) {
                    Text(Translation.Game.ACCEPT_PATH)
                }
            }
        }

    }
}





