package edu.agh.susgame.front.gui.components.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.agh.susgame.dto.socket.common.GameStatus
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.game.components.computer.ComputerComponent
import edu.agh.susgame.front.gui.components.game.components.elements.Background
import edu.agh.susgame.front.gui.components.game.components.elements.GameNet
import edu.agh.susgame.front.gui.components.game.components.elements.LeftButtons
import edu.agh.susgame.front.gui.components.game.components.elements.NodeInfoComp
import edu.agh.susgame.front.gui.components.game.components.elements.UpperBarComp
import edu.agh.susgame.front.gui.components.game.components.elements.bottombar.NavIcons
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.service.interfaces.GameService

private val SIZE_DP = 50.dp

@Composable
internal fun GameGraphComponent(
    gameManager: GameManager,
    gameService: GameService,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        gameService.initGameManager(gameManager)
        gameService.sendStartGame()
    }

    val gameState = gameManager.gameState

    Background()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        if (!gameState.isComputerViewVisible.value) {
            GameNet(gameManager = gameManager)
            LeftButtons(gameManager.gameState)
            gameState.currentlyInspectedNode.value
                ?.let { node ->
                    NodeInfoComp(
                        node = node,
                        onExit = {
                            gameManager.cancelChangingPath()
                            gameManager.gameState.currentlyInspectedNode.value = null
                        },
                        gameManager = gameManager,
                    )
                }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight(0.7f)
                        .fillMaxWidth(0.98f)
                        .clip(RoundedCornerShape(50.dp))
                        .border(4.dp, Color.Black, RoundedCornerShape(50.dp))
                ) {
                    ComputerComponent(gameService = gameService, gameManager = gameManager)
                }
            }
        }
        UpperBarComp(gameManager = gameManager)

        NavIcons(
            isComputerVisible = gameState.isComputerViewVisible,
            gameManager = gameManager
        )

    }
    if (gameState.isMenuOpened.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .fillMaxSize(0.6f)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {

                Text(text = "MENU")
            }
        }
    }

    when (gameState.gameStatus.value) {
        GameStatus.FINISHED_WON, GameStatus.FINISHED_LOST -> {
            val message =
                if (gameState.gameStatus.value == GameStatus.FINISHED_WON) Translation.Game.YOU_WON
                else Translation.Game.YOU_LOST

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { navController.navigate(MenuRoute.FindGame.route) }) {
                    Text(message)
                }
            }
        }

        // Do nothing for other states
        else -> {}
    }
}
