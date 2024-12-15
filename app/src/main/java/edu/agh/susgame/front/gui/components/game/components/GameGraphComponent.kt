package edu.agh.susgame.front.gui.components.game.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.agh.susgame.R
import edu.agh.susgame.dto.socket.common.GameStatus
import edu.agh.susgame.front.gui.components.common.theme.PaddingS
import edu.agh.susgame.front.gui.components.common.util.Calculate
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.game.components.computer.ComputerComponent
import edu.agh.susgame.front.gui.components.game.components.elements.Background
import edu.agh.susgame.front.gui.components.game.components.elements.GameNet
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
        GameNet(gameManager = gameManager)
        UpperBarComp(gameManager = gameManager)

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
        ) {
//            if (gameState.isPathBeingChanged.value) {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(SIZE_DP)
//                            .padding(PaddingS)
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.cross),
//                            contentDescription = Translation.Game.ABORT_PATH,
//                            modifier = Modifier.clickable {
//                                gameManager.clearEdges(gameManager.localPlayerId)
//                                gameState.isPathBeingChanged.value = false
//                                gameManager.pathBuilder.reset()
//                            }
//                        )
//                    }
//                    Box(
//                        modifier = Modifier
//                            .size(SIZE_DP)
//                            .padding(PaddingS)
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.accept),
//                            contentDescription = Translation.Game.ACCEPT_PATH,
//                            modifier = Modifier
//                                .alpha(
//                                    Calculate.getAlpha(isPathValid)
//                                )
//                                .clickable(
//                                    enabled = isPathValid
//                                ) {
//                                    if (isPathValid) {
//                                        gameManager.handlePathChange()
//                                        gameState.isPathBeingChanged.value = false
//                                        gameState.currentlyInspectedNode.value = null
//                                    }
//                                }
//                        )
//                    }
//                }
//            }
        }

        gameState.currentlyInspectedNode.value
            ?.let { node ->
                NodeInfoComp(
                    node = node,
                    onExit = { gameState.currentlyInspectedNode.value = null },
//                    changingPath = { state -> gameState.isPathBeingChanged.value = state },
                    gameManager = gameManager,
                )
            }

        if (gameState.isComputerViewVisible.value) {
            ComputerComponent(gameService = gameService, gameManager = gameManager)
        }

        NavIcons(
            isComputerVisible = gameState.isComputerViewVisible,
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
