package edu.agh.susgame.front.gui.components.game.components.drawers

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
import edu.agh.susgame.front.managers.GameManager
import kotlin.math.sqrt

private const val CIRCLE_RADIUS = 50f

@Composable
fun EdgeDrawer(gameManager: GameManager) {
    val density = LocalDensity.current


    Canvas(modifier = Modifier.fillMaxSize()) {
        gameManager.edgesList.forEach { edge ->

            val startXY = gameManager.nodesById[edge.firstNodeId]
            val endXY = gameManager.nodesById[edge.secondNodeId]

            if (startXY != null && endXY != null) {
                val startOffset = with(density) {
                    Offset((startXY.position.x.dp + 30.dp).toPx(), startXY.position.y.dp.toPx())
                }
                val endOffset = with(density) {
                    Offset((endXY.position.x.dp + 30.dp).toPx(), endXY.position.y.dp.toPx())
                }

                val dx = endOffset.x - startOffset.x
                val dy = endOffset.y - startOffset.y

                val length = sqrt(dx * dx + dy * dy)

                val unitX = dx / length
                val unitY = dy / length

                val offset = 50f

                val newStartOffset = Offset(
                    startOffset.x + unitX * offset,
                    startOffset.y + unitY * offset
                )

                val newEndOffset = Offset(
                    endOffset.x - unitX * offset,
                    endOffset.y - unitY * offset
                )

                // Rysuj główną linię
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

                // Rysuj linie graczy
                edge.playersIdsUsingEdge.forEachIndexed { index, playerId ->
                    gameManager.playersById[playerId]?.color?.let { color ->

                        val playerOffset = index * 10

                        val playerStartOffset = Offset(
                            newStartOffset.x + playerOffset,
                            newStartOffset.y + playerOffset
                        )

                        val playerEndOffset = Offset(
                            newEndOffset.x + playerOffset,
                            newEndOffset.y + playerOffset
                        )

                        val playerPath = Path().apply {
                            moveTo(playerStartOffset.x, playerStartOffset.y)
                            lineTo(playerEndOffset.x, playerEndOffset.y)
                        }

                        drawPath(
                            path = playerPath,
                            color = color.value,
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
