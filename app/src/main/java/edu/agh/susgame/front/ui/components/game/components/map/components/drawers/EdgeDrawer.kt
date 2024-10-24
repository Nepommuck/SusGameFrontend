package edu.agh.susgame.front.ui.components.game.components.map.components.drawers

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.model.graph.GameGraph

@Composable
fun EdgeDrawer(gameGraph: GameGraph) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        gameGraph.edges.forEach { (_, edge) ->

            val startXY = gameGraph.nodes[edge.firstNodeId]
            val endXY = gameGraph.nodes[edge.secondNodeId]
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
                    gameGraph.players[playerId]?.colorHex?.let { hexColor ->
                        drawLine(
                            color = Color(hexColor),
                            start = Offset(baseStart.x + offset, baseStart.y + offset),
                            end = Offset(baseEnd.x + offset, baseEnd.y + offset),
                            strokeWidth = 7f
                        )
                    }
                }
            }
        }
    }
}