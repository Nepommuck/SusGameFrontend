package edu.agh.susgame.front.ui.component.game.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.model.graph.GameGraph
import edu.agh.susgame.front.ui.util.ZoomState

@Composable
internal fun ServerMapComponent(mapState: GameGraph) {
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
            .background(Color.Red)
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
                mapState.edges.forEach { (key, edge) ->

                    val startXY = mapState.nodes[edge.firstNodeId]
                    val endXY = mapState.nodes[edge.secondNodeId]
                    if (startXY != null && endXY != null) {
                        drawLine(
                            color = Color.Black,
                            start = Offset(
                                startXY.position.x.toFloat(),
                                startXY.position.y.toFloat()
                            ),
                            end = Offset(endXY.position.x.toFloat(), endXY.position.y.toFloat()),
                            strokeWidth = 2f
                        )
                    }
                }

            }
            Column {
                Text(zoomState.scaleValue().toString(), color = Color.Black)
                mapState.nodes.forEach { (key, node) ->
                    Box(
                        modifier = Modifier.offset(node.position.x.dp, node.position.y.dp)
                    ) {
                        Button(onClick = { /* Do something */ }) {
                            Text(text = node.name + ":" + key)
                        }
                    }
                }
            }
        }
    }
}
