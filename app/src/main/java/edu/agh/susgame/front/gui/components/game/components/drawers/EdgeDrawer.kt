package edu.agh.susgame.front.gui.components.game.components.drawers

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.managers.GameManager
import kotlin.math.sqrt

private val OFFSET_BOX: Dp = 30.dp
private val OFFSET_LABEL: Dp = 6.dp


@Composable
fun EdgeDrawer(gameManager: GameManager) {
    val density = LocalDensity.current
    Box(modifier = Modifier.fillMaxSize()) {
        gameManager.edgesList.forEach { edge ->

            val startXY = gameManager.nodesById[edge.firstNodeId]
            val endXY = gameManager.nodesById[edge.secondNodeId]

            if (startXY != null && endXY != null) {
                val startOffset = with(density) {
                    Offset(
                        (startXY.position.x.dp + OFFSET_BOX).toPx(),
                        startXY.position.y.dp.toPx()
                    )
                }
                val endOffset = with(density) {
                    Offset((endXY.position.x.dp + OFFSET_BOX).toPx(), endXY.position.y.dp.toPx())
                }

                val dx = endOffset.x - startOffset.x
                val dy = endOffset.y - startOffset.y

                val length = sqrt(dx * dx + dy * dy)

                val unitX = dx / length
                val unitY = dy / length

                val offset = 70f

                val newStartOffset = Offset(
                    startOffset.x + unitX * offset,
                    startOffset.y + unitY * offset
                )

                val newEndOffset = Offset(
                    endOffset.x - unitX * offset,
                    endOffset.y - unitY * offset
                )


                val path = Path().apply {
                    moveTo(newStartOffset.x, newStartOffset.y)
                    lineTo(newEndOffset.x, newEndOffset.y)
                }
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawPath(
                        path = path,
                        color = Color.Gray,
                        style = Stroke(
                            width = 5f,
                            pathEffect = PathEffect.dashPathEffect(
                                intervals = floatArrayOf(10f, 7f),
                                phase = 0f,
                            )
                        )
                    )
                }

                val leftX = (newStartOffset.x / density.density).dp
                val upY = (newEndOffset.y / density.density).dp

                val rightX = (newEndOffset.x / density.density).dp
                val downY = (newStartOffset.y / density.density).dp

                val middleX = ((leftX + rightX) / 2) - OFFSET_LABEL
                val middleY = (downY + upY) / 2 - OFFSET_LABEL


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
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawPath(
                                path = playerPath,
                                color = color.value,
                                style = Stroke(
                                    width = 7f,
                                    pathEffect = PathEffect.dashPathEffect(
                                        intervals = floatArrayOf(10f, 7f),
                                        phase = 0f
                                    )
                                )
                            )
                        }
                    }
                }
                if (gameManager.gameState.areEdgesBandwidthShown.value) {
                    Row(
                        modifier = Modifier
                            .offset(middleX, middleY)
                            .background(Color.Black, shape = RoundedCornerShape(8.dp))
                            .height(20.dp),
                        horizontalArrangement = Arrangement.Absolute.Left,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
//                            text = "${edge.packetsTransported.intValue}/${edge.bandwidth}",
                            text = "${edge.bandwidth}",
                            style = TextStyler.TerminalM,
                            textAlign = TextAlign.Center,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        // CODE TO UPDATE EDGES -> delete it if it will not be used
//                        Image(
//                            painter = painterResource(id = R.drawable.plus),
//                            contentDescription = null,
//                            modifier = Modifier
//                                .clickable(
//                                    onClick = {
//                                        gameManager.handleEdgeUpdate(edgeId = edge.id)
//                                    }
//                                )
//                                .padding(2.dp)
//                        )
//                        Text(
//                            text = "${edge.upgradeCost.intValue}",
//                            style = TextStyler.TerminalS,
//                            textAlign = TextAlign.Center,
//                        )

                    }
                }
            }
        }
    }
}
