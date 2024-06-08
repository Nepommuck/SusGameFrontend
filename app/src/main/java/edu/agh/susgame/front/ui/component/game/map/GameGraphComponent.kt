package edu.agh.susgame.front.ui.component.game.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.agh.susgame.front.model.PlayerId
import edu.agh.susgame.front.model.graph.GameGraph
import edu.agh.susgame.front.model.graph.Host
import edu.agh.susgame.front.model.graph.Node
import edu.agh.susgame.front.model.graph.NodeId
import edu.agh.susgame.front.model.graph.Path
import edu.agh.susgame.front.providers.interfaces.GameGraphProvider
import edu.agh.susgame.front.ui.theme.PaddingM
import edu.agh.susgame.front.ui.util.ZoomState

private const val buttonWidth = 70
private const val buttonHeight = 30

@Composable
internal fun GameGraphComponent(
    mapState: GameGraph,
    gameGraphProvider: GameGraphProvider,
) {
    var inspectedNodeId by remember { mutableStateOf<NodeId?>(null) }
    var playerIdChangingPath by remember { mutableStateOf<PlayerId?>(null) }
    var pathToShow by remember { mutableStateOf("") }
    var pathState by remember { mutableStateOf(Path()) }

    val zoomState = remember {
        ZoomState(
            maxZoomIn = 2f,
            maxZoomOut = 0.5f,
            totalSize = mapState.mapSize,
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clipToBounds()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    zoomState.scale(zoom)
                    zoomState.move(pan)
                }
            }
            .background(Color.Green)
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer(
                    scaleX = zoomState.scaleValue(),
                    scaleY = zoomState.scaleValue(),
                    translationX = zoomState.translationX(),
                    translationY = zoomState.translationY(),
                    clip = false
                )
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                mapState.edges.forEach { (_, edge) ->

                    val startXY = mapState.nodes[edge.firstNodeId]
                    val endXY = mapState.nodes[edge.secondNodeId]
                    if (startXY != null && endXY != null) {
                        drawLine(
                            color = edge.color,
                            start = Offset(
                                startXY.position.x.dp.toPx(),
                                startXY.position.y.dp.toPx(),
                            ),
                            end = Offset(
                                endXY.position.x.dp.toPx(),
                                endXY.position.y.dp.toPx(),
                            ),
                            strokeWidth = 3f
                        )
                        val baseStart =
                            Offset(startXY.position.x.dp.toPx(), startXY.position.y.dp.toPx())
                        val baseEnd = Offset(endXY.position.x.dp.toPx(), endXY.position.y.dp.toPx())

                        edge.playersIdsUsingEdge.forEachIndexed { index, playerId ->
                            val offset = index * 10
                            mapState.players[playerId]?.color?.let {
                                drawLine(
                                    color = it,
                                    start = Offset(baseStart.x + offset, baseStart.y + offset),
                                    end = Offset(baseEnd.x + offset, baseEnd.y + offset),
                                    strokeWidth = 3f
                                )
                            }
                        }
                    }
                }
            }

//            Text(zoomState.scaleValue().toString(), color = Color.Black)

            mapState.nodes.forEach { (key, node) ->
                Button(
                    modifier = Modifier
                        .offset(
                            x = (node.position.x - buttonWidth / 2).dp,
                            y = (node.position.y - buttonHeight / 2).dp,
                        )
                        .size(
                            width = buttonWidth.dp,
                            height = buttonHeight.dp
                        ),
                    onClick = {
                        playerIdChangingPath?.let {
                            addNodeToPath(
                                nodeId = node.id,
                                pathState = pathState,
                                pathToShow = { newState -> pathToShow = newState },
                                gameGraph = mapState
                            )
                        } ?: run {
                            inspectedNodeId = node.id
                        }
                    },

                    ) {
                    Text(
                        text = node.name + ":" + key.value,
                        fontSize = 7.sp
                    )
                }
            }
            Text(pathToShow)
        }

        inspectedNodeId?.let { nodeId ->
            mapState.nodes[nodeId]?.let {
                ShowInfo(
                    node = it,
                    onExit = {
                        inspectedNodeId = null
                    },
                    playerIdChangingPath = { newId -> playerIdChangingPath = newId },
                    pathToShow = { newState -> pathToShow = newState },
                    pathState = pathState
                )
            }
        }

        playerIdChangingPath?.let {
            Column() {
                Button(onClick = {
                    playerIdChangingPath = null
                    pathState = Path()
                    pathToShow = ""
                }) {
                    Text("Anuluj wybieranie trasy")
                }
                Button(onClick = {
                    playerIdChangingPath?.let {
                        if (pathState.path.lastOrNull() == mapState.serverId) {
                            gameGraphProvider.changePlayerPath(
                                playerId = it,
                                path = pathState
                            )
                            playerIdChangingPath = null
                            pathState = Path()
                            pathToShow = ""
                        }
                    }
                }) {
                    Text("Zaakceptuj trase")
                }
            }
        }

    }
}


@Composable
private fun ShowInfo(
    node: Node,
    onExit: () -> Unit,
    playerIdChangingPath: (PlayerId) -> Unit,
    pathToShow: (String) -> Unit,
    pathState: Path
) {
    Box(
        modifier = Modifier
            .background(Color.Cyan)
            .padding(PaddingM),
    ) {
        Row {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(node.getInfo())
            }
            Column() {
                Button(onClick = { onExit() }) {
                    Text("X")
                }

                val hostNode = node as? Host
                hostNode?.let { host ->
                    Button(onClick = {
                        playerIdChangingPath(host.playerId)
                        pathState.addNodeToPath(nodeId = node.id)
                        pathToShow(pathState.getPathString())
                        onExit()
                    }) {
                        Text("change!")
                    }
                }
            }
        }
    }
}

private fun addNodeToPath(
    nodeId: NodeId,
    pathState: Path,
    pathToShow: (String) -> Unit,
    gameGraph: GameGraph
) {
    val newEdgeId = pathState.path.lastOrNull()?.let { lastNode ->
        val key: Pair<NodeId, NodeId> = lastNode to nodeId
        gameGraph.nodesToEdges[key]

    }
    newEdgeId?.let {
        if (pathState.isNodeCorrect(nodeId)) {
            pathState.addNodeToPath(nodeId)
            pathToShow(pathState.getPathString())
            gameGraph.edges[newEdgeId]?.color = Color.Blue
        }
    }

}