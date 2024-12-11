package edu.agh.susgame.front.gui.components.game.components.drawers

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import edu.agh.susgame.R
import edu.agh.susgame.front.gui.components.common.graph.node.Host
import edu.agh.susgame.front.gui.components.common.graph.node.Node
import edu.agh.susgame.front.gui.components.common.graph.node.Router
import edu.agh.susgame.front.gui.components.common.graph.node.Server
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.AssetsManager
import edu.agh.susgame.front.gui.components.game.components.elements.RouterBar
import edu.agh.susgame.front.managers.GameManager


private const val SCALE_FACTOR = 0.06f

@Composable
fun NodeDrawer(gameManager: GameManager) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val context = LocalContext.current
        gameManager.nodesList.forEach { node ->

            val imageResourceId = AssetsManager.nodeToResourceId(node)

            val density = LocalDensity.current

            val imageSize = getImageSize(context, imageResourceId)
            val width = with(density) { imageSize.width.dp.toPx() }
            val height = with(density) { imageSize.height.dp.toPx() }

            val positionX = with(density) { node.position.x.dp.toPx() }
            val positionY = with(density) { node.position.y.dp.toPx() }
            Column(modifier = Modifier
                .size(width = with(density) { width.toDp() } * SCALE_FACTOR + 60.dp,
                    height = with(density) { height.toDp() } * SCALE_FACTOR)
                .graphicsLayer(
                    translationX = positionX - (width * SCALE_FACTOR) / 2,
                    translationY = positionY - (height * SCALE_FACTOR) / 2
                )
            )
            {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = imageResourceId),
                        contentDescription = null,
                        colorFilter = getColorFilterForNode(node, gameManager),
                        modifier = Modifier
                            .weight(4f)
                            .clickable {
                                if (gameManager.gameState.isPathBeingChanged.value) {
                                    gameManager.addNodeToPathBuilder(node.id)
                                } else {
                                    gameManager.gameState.currentlyInspectedNode.value = node
                                }
                            },
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        when (node) {
                            is Host -> {
                                gameManager.playersById[node.playerId]?.name?.value?.let {
                                    Text(text = it, style = TextStyler.TerminalName, textAlign = TextAlign.Center)
                                }
                            }

                            is Router -> { RouterBar(router = node)
                            }

                            is Server -> { }
                        }
                    }
                }
            }
        }
    }
}

fun getImageSize(context: Context, resId: Int): IntSize {
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeResource(context.resources, resId, options)
    return IntSize(options.outWidth, options.outHeight)
}

fun getColorFilterForNode(
    node: Node,
    gameManager: GameManager
): ColorFilter? {
    return when (node) {
        is Host -> {
            val hostColor = gameManager.playersById[node.playerId]?.color?.value ?: Color.Gray
            ColorFilter.lighting(
                hostColor.copy(alpha = 0.8f),
                Color.Black
            )
        }

        is Router -> {
            val load =
                if (node.overheat.intValue > 0)
                    node.overheat.intValue.toFloat() / gameManager.criticalBufferOverheatLevel
                else
                    0f

            val routerColor =
                if (!node.isWorking.value) Color.Gray
                else Color(
                    red = (load * 255).toInt(),
                    green = ((1 - load) * 255).toInt(),
                    blue = 0,
                    alpha = (0.8f * 255).toInt()
                )
            ColorFilter.lighting(
                routerColor,
                Color.Black
            )
        }

        else -> null
    }
}