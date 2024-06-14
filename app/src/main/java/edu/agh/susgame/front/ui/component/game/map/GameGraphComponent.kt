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
import edu.agh.susgame.front.model.graph.PathBuilder
import edu.agh.susgame.front.providers.interfaces.GameGraphProvider
import edu.agh.susgame.front.ui.Translation
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
    var pathBuilderState by remember { mutableStateOf(PathBuilder()) }

    val zoomState = remember {
        ZoomState(
            maxZoomIn = 2f,
            maxZoomOut = 0.5f,
            totalSize = mapState.mapSize,
        )
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .clipToBounds()
        .pointerInput(Unit) {
            detectTransformGestures { _, pan, zoom, _ ->
                zoomState.scale(zoom)
                zoomState.move(pan)
            }
        }
        .background(Color.Green)) {
        Box(
            modifier = Modifier.graphicsLayer(
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
                        // draw graph colors
                        drawLine(
                            color = edge.color, start = Offset(
                                startXY.position.x.dp.toPx(),
                                startXY.position.y.dp.toPx(),
                            ), end = Offset(
                                endXY.position.x.dp.toPx(),
                                endXY.position.y.dp.toPx(),
                            ), strokeWidth = 7f
                        )
                        val baseStart = Offset(
                            startXY.position.x.dp.toPx(), startXY.position.y.dp.toPx()
                        )
                        val baseEnd = Offset(
                            endXY.position.x.dp.toPx(), endXY.position.y.dp.toPx()
                        )
                        // draw players colors
                        edge.playersIdsUsingEdge.forEachIndexed { index, playerId ->
                            val offset = index * 10
                            mapState.players[playerId]?.color?.let {
                                drawLine(
                                    color = it,
                                    start = Offset(baseStart.x + offset, baseStart.y + offset),
                                    end = Offset(baseEnd.x + offset, baseEnd.y + offset),
                                    strokeWidth = 7f
                                )
                            }
                        }
                    }
                }
            }
            Column() {
                Text(zoomState.scaleValue().toString(), color = Color.Black)
                Text(pathToShow)
            }

            mapState.nodes.forEach { (key, node) ->
                Button(
                    modifier = Modifier
                        .offset(
                            x = (node.position.x - buttonWidth / 2).dp,
                            y = (node.position.y - buttonHeight / 2).dp,
                        )
                        .size(
                            width = buttonWidth.dp, height = buttonHeight.dp
                        ),
                    onClick = {
                        playerIdChangingPath?.let {
                            addNodeToPath(
                                nodeId = node.id,
                                pathBuilderState = pathBuilderState,
                                pathToShow = { newState -> pathToShow = newState },
                                playerId = playerIdChangingPath!!,
                                gameGraph = mapState
                            )
                        } ?: run {
                            inspectedNodeId = node.id
                        }
                    },

                    ) {
                    Text(
                        text = node.name + ":" + key.value, fontSize = 7.sp
                    )
                }
            }

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
                    pathBuilderState = pathBuilderState,
                    mapState = mapState
                )
            }
        }

        playerIdChangingPath?.let {
            Column() {
                Button(onClick = {
                    mapState.edges.forEach { (_, edge) -> edge.removePlayer(playerIdChangingPath!!) }
                    playerIdChangingPath = null
                    pathBuilderState = PathBuilder()
                    pathToShow = ""

                }) {
                    Text(Translation.Game.ABORT_PATH)
                }
                Button(onClick = {
                    playerIdChangingPath?.let {
                        if (pathBuilderState.isPathValid(serverId = mapState.serverId)) {
                            gameGraphProvider.changePlayerPath(
                                playerId = it, pathBuilder = pathBuilderState
                            )
                            playerIdChangingPath = null
                            pathBuilderState = PathBuilder()
                            pathToShow = ""

                        }
                    }
                }, enabled = playerIdChangingPath?.let {
                    pathBuilderState.isPathValid(serverId = mapState.serverId)
                } ?: false

                ) {
                    Text(Translation.Game.ACCEPT_PATH)
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
    pathBuilderState: PathBuilder,
    mapState: GameGraph
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
                        mapState.edges.forEach { (_, edge) -> edge.removePlayer(host.playerId) }
                        playerIdChangingPath(host.playerId)
                        pathBuilderState.addNodeToPath(nodeId = node.id)
                        pathToShow(pathBuilderState.getPathString())
                        onExit()
                    }) {
                        Text(Translation.Game.CHANGE_PATH)
                    }
                    mapState.paths[host.playerId]?.let { Text(it.getPathString()) }
                }
            }
        }
    }
}

private fun addNodeToPath(
    nodeId: NodeId,
    pathBuilderState: PathBuilder,
    pathToShow: (String) -> Unit,
    playerId: PlayerId,
    gameGraph: GameGraph
) {
    val edgeId = pathBuilderState.path.lastOrNull()?.let { lastNode ->
        gameGraph.getEdgeId(lastNode, nodeId)
    }

    edgeId?.let {
        if (pathBuilderState.isNodeValid(nodeId)) {
            pathBuilderState.addNodeToPath(nodeId)
            pathToShow(pathBuilderState.getPathString())
            gameGraph.edges[edgeId]?.addPlayer(playerId)
        }
    }

}