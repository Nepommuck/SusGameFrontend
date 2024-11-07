package edu.agh.susgame.front.ui.components.game.components.map.components.drawers

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.ui.graph.GameMapFront
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private const val CIRCLE_RADIUS = 50f

@Composable
fun EdgeDrawer(gameMapFront: GameMapFront) {
    val density = LocalDensity.current

    Canvas(modifier = Modifier.fillMaxSize()) {
        gameMapFront.edges.forEach { (_, edge) ->

            val startXY = gameMapFront.nodes[edge.firstNodeId]
            val endXY = gameMapFront.nodes[edge.secondNodeId]

            if (startXY != null && endXY != null) {
                val startOffset = with(density) {
                    Offset(startXY.position.x.dp.toPx(), startXY.position.y.dp.toPx())
                }
                val endOffset = with(density) {
                    Offset(endXY.position.x.dp.toPx(), endXY.position.y.dp.toPx())
                }

                val dx = endOffset.x - startOffset.x
                val dy = endOffset.y - startOffset.y


                val angle = atan2(dy, dx)


                val newStartOffset = Offset(
                    startOffset.x + cos(angle) * CIRCLE_RADIUS,
                    startOffset.y + sin(angle) * CIRCLE_RADIUS
                )

                val newEndOffset = Offset(
                    endOffset.x - cos(angle) * CIRCLE_RADIUS,
                    endOffset.y - sin(angle) * CIRCLE_RADIUS
                )

                val path = Path().apply {
                    moveTo(newStartOffset.x, newStartOffset.y)
                    lineTo(newEndOffset.x, newEndOffset.y)
                }

                drawPath(
                    path = path,
                    color = Color.Gray,
                    style = Stroke(
                        width = 5f,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(10f, 7f),
                            0f
                        )
                    )
                )

                edge.playersIdsUsingEdge.forEachIndexed { index, playerId ->
                    gameMapFront.players[playerId]?.colorHex?.let { hexColor ->
                        val playerOffset = index * 10


                        val playerPath = Path().apply {
                            moveTo(startOffset.x + playerOffset, startOffset.y + playerOffset)
                            lineTo(endOffset.x + playerOffset, endOffset.y + playerOffset)
                        }


                        drawPath(
                            path = playerPath,
                            color = Color(hexColor),
                            style = Stroke(
                                width = 7f,
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(10f, 7f),
                                    0f
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}
