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
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizQuestion
import edu.agh.susgame.front.managers.state.GameStateManager
import edu.agh.susgame.front.managers.state.util.ComputerState
import edu.agh.susgame.front.managers.state.util.MiniGame
import edu.agh.susgame.front.managers.state.util.QuizAnswerState
import edu.agh.susgame.front.managers.state.util.QuizState


@Composable
fun DesktopView(gameState: GameStateManager) {
    val computerState = gameState.computerState

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
                    gameState.quizState.value = QuizState.QuestionAvailable(
                        question = QuizQuestion.fromDto(
                            ServerSocketMessage.QuizQuestionDTO(
                                questionId = -1,
                                question = "Czy masz rozum i godność człowieka?",
                                answers = listOf("Tak", "Nie", "Nie wiem", "Chyba"),
                                correctAnswer = 2,
                            )
                        ),
                        answerState = QuizAnswerState.NotAnswered,
                    )
                },
            )

            DesktopIconButton(
                painter = painterResource(id = R.drawable.computer_icon_question),
                imageDescription = "icon-gear",
                onClick = {
                    if (gameState.quizState.value is QuizState.QuestionAvailable) {
                        computerState.value = ComputerState.QuizQuestionOpened
                    }
                },
                isVisible = { gameState.quizState.value is QuizState.QuestionAvailable }
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

//            DesktopIconPlaceholder()

            DesktopIconButton(
                painter = painterResource(id = R.drawable.computer_icon_tree),
                imageDescription = "icon-tree",
                onClick = {
                    gameState.quizState.value = QuizState.QuestionNotAvailable
                },
            )
        }
    }
}
