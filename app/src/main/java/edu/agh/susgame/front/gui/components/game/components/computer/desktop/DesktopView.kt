package edu.agh.susgame.front.gui.components.game.components.computer.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import edu.agh.susgame.R
import edu.agh.susgame.dto.socket.ServerSocketMessage
import edu.agh.susgame.front.gui.components.game.components.computer.desktop.components.DesktopIconButton
import edu.agh.susgame.front.gui.components.game.components.computer.desktop.components.DesktopIconPlaceholder
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizQuestion
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.managers.state.util.ComputerState
import edu.agh.susgame.front.managers.state.util.MiniGame
import edu.agh.susgame.front.managers.state.util.QuizState


@Composable
fun DesktopView(gameManager: GameManager) {
    val gameState = gameManager.gameState
    val computerState = gameState.computerState
    val quizState = gameManager.quizManager.quizState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Blue),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            DesktopIconButton(
                painter = painterResource(id = R.drawable.computer_icon_tree),
                imageDescription = "icon-tree",
                onClick = {
                    computerState.value = ComputerState.MiniGameOpened(MiniGame.MiniGame1)
                    // TODO 114 Delete
                    gameManager.quizManager.enqueueNewQuestion(
                        QuizQuestion.fromDto(
                            ServerSocketMessage.QuizQuestionDTO(
                                questionId = -1,
                                question = "Czy masz rozum i godność człowieka?",
                                answers = listOf("Tak", "Nie", "Nie wiem", "Chyba"),
                                correctAnswer = 2,
                            )
                        ),
                    )
                },
            )

            DesktopIconButton(
                painter = painterResource(id = R.drawable.computer_icon_question),
                imageDescription = "icon-gear",
                onClick = {
                    if (quizState.value is QuizState.QuestionAvailable) {
                        computerState.value = ComputerState.QuizQuestionOpened
                    }
                },
                isVisible = { gameManager.quizManager.quizState.value is QuizState.QuestionAvailable }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            DesktopIconButton(
                painter = painterResource(id = R.drawable.computer_icon_envelope),
                imageDescription = "icon-envelope",
                onClick = { computerState.value = ComputerState.ChatOpened },
            )

            DesktopIconPlaceholder()
        }
    }
}
