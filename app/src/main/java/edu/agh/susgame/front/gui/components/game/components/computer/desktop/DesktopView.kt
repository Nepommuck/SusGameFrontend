package edu.agh.susgame.front.gui.components.game.components.computer.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import edu.agh.susgame.R
import edu.agh.susgame.front.gui.components.game.components.computer.desktop.components.DesktopIconButton
import edu.agh.susgame.front.gui.components.game.components.computer.desktop.components.DesktopIconPlaceholder
import edu.agh.susgame.front.gui.components.game.components.elements.Background
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.managers.state.util.ComputerState
import edu.agh.susgame.front.managers.state.util.MiniGame
import edu.agh.susgame.front.managers.state.util.QuizState


@Composable
fun DesktopView(gameManager: GameManager) {
    val gameState = gameManager.gameState
    val computerState = gameState.computerState
    val quizState = gameManager.quizManager.quizState

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Background()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
//            DesktopIconButton(
//                painter = painterResource(id = R.drawable.computer_icon_tree),
//                imageDescription = "icon-tree",
//                onClick = {
//                    computerState.value = ComputerState.MiniGameOpened(MiniGame.MiniGame1)
//                },
//
//            )
            DesktopIconButton(
                painter = painterResource(id = R.drawable.computer_icon_envelope),
                imageDescription = "icon-envelope",
                onClick = { computerState.value = ComputerState.ChatOpened },
            )

            DesktopIconButton(
                painter = painterResource(id = R.drawable.computer_icon_question),
                imageDescription = "icon-question",
                onClick = {
                    if (quizState.value is QuizState.QuestionAvailable) {
                        computerState.value = ComputerState.QuizQuestionOpened
                    }
                },
                isVisible = { quizState.value is QuizState.QuestionAvailable }
            )

        }
    }
}
