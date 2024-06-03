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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.agh.susgame.front.model.graph.GameGraph
import edu.agh.susgame.front.model.graph.Node
import edu.agh.susgame.front.ui.theme.PaddingM
import edu.agh.susgame.front.ui.util.ZoomState

private const val buttonWidth = 70
private const val buttonHeight = 30

@Composable
internal fun GameGraphComponent(mapState: GameGraph) {
    val changeNodeInfoVisibility = remember { mutableStateOf(false) }
    val nodeToShow = remember { mutableIntStateOf(0) }
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
            Canvas(modifier = Modifier.fillMaxSize()) {// it will be improved, just testing how drawing works
                mapState.edges.forEach { (_, edge) ->

                    val startXY = mapState.nodes[edge.firstNodeId]
                    val endXY = mapState.nodes[edge.secondNodeId]
                    if (startXY != null && endXY != null) {
                        drawLine(
                            color = Color.Black,
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
                    }
                }
            }

            Text(zoomState.scaleValue().toString(), color = Color.Black)
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
                        changeNodeInfoVisibility.value = true
                        nodeToShow.intValue = node.id
                    },
                ) {
                    Text(
                        text = node.name + ":" + key,
                        fontSize = 7.sp
                    )
                }
            }
        }
        if (changeNodeInfoVisibility.value) {
            mapState.nodes[nodeToShow.intValue]?.let { ShowInfo(node = it,changeNodeInfoVisibility) }
        }
    }
}


@Composable
private fun ShowInfo(node: Node, showBox: MutableState<Boolean>) {
    Box(
        modifier = Modifier
            .background(Color.Cyan)
            .padding(PaddingM),
        ) {
        Row() {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(node.getInfo())
            }
            Button(onClick = { showBox.value = false }) {
                Text("X")
            }
        }
    }
}

