package edu.agh.susgame.front.ui.components.game.components.map.components.drawers

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.model.graph.GameGraph

@Composable
fun EdgeDrawer(gameGraph: GameGraph) {
    // Przechwycenie aktualnej gęstości ekranu
    val density = LocalDensity.current

    Canvas(modifier = Modifier.fillMaxSize()) {
        gameGraph.edges.forEach { (_, edge) ->

            val startXY = gameGraph.nodes[edge.firstNodeId]
            val endXY = gameGraph.nodes[edge.secondNodeId]

            if (startXY != null && endXY != null) {
                val startOffset = with(density) {
                    Offset(startXY.position.x.dp.toPx(), startXY.position.y.dp.toPx())
                }
                val endOffset = with(density) {
                    Offset(endXY.position.x.dp.toPx(), endXY.position.y.dp.toPx())
                }
                Log.d("Debug", "StartXY: ${Offset(startXY.position.x.dp.toPx(), startXY.position.y.dp.toPx()).x},${Offset(startXY.position.x.dp.toPx(), startXY.position.y.dp.toPx()).y}")


                drawLine(
                    color = edge.color,
                    start = startOffset,
                    end = endOffset,
                    strokeWidth = 7f
                )

                // Rysowanie kolorów graczy korzystających z krawędzi
                edge.playersIdsUsingEdge.forEachIndexed { index, playerId ->
                    gameGraph.players[playerId]?.colorHex?.let { hexColor ->
                        val playerOffset = index * 10

                        drawLine(
                            color = Color(hexColor),
                            start = Offset(
                                startOffset.x + playerOffset,
                                startOffset.y + playerOffset
                            ),
                            end = Offset(endOffset.x + playerOffset, endOffset.y + playerOffset),
                            strokeWidth = 7f
                        )
                    }
                }
            }
        }
    }
}
