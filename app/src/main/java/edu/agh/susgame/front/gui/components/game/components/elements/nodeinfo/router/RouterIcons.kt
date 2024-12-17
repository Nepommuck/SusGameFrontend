package edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.router

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import edu.agh.susgame.R
import edu.agh.susgame.front.gui.components.common.graph.node.Router
import edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.Icons
import edu.agh.susgame.front.managers.GameManager

@Composable
fun RouterIcons(
    router: Router,
    gameManager: GameManager
) {
    Row(modifier = Modifier.fillMaxSize())
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icons(
                resourceId = R.drawable.upgrade,
                cost = router.upgradeCost,
                modifier = Modifier
                    .weight(2f)
                    .clickable(
                        enabled = canUpgrade(router, gameManager),
                        onClick = { gameManager.handleRouterUpgrade(router.id) })
                    .alpha(
                        if (canUpgrade(router, gameManager)) 1f else 0.4f
                    )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icons(
                resourceId = R.drawable.fix,
                cost = null,
                modifier = Modifier
                    .weight(2f)
                    .clickable(
                        enabled = !router.isWorking.value,
                        onClick = {
                            gameManager.handleRouterRepair(router.id)
                        }
                    )
                    .alpha(if (!router.isWorking.value) 1f else 0.2f)
            )
        }
    }
}

fun canUpgrade(router: Router, gameManager: GameManager): Boolean {
    val localPlayer = gameManager.playersById[gameManager.localPlayerId]
        ?: return false

    return localPlayer.tokens.intValue >= router.upgradeCost.intValue
}