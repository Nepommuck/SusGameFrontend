package edu.agh.susgame.front.gui.components.game.components.computer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.gui.components.game.components.computer.chat.ChatColors
import edu.agh.susgame.front.gui.components.game.components.computer.chat.ChatView
import edu.agh.susgame.front.gui.components.game.components.computer.desktop.DesktopView
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizQuestionView
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.managers.state.util.ComputerState
import edu.agh.susgame.front.managers.state.util.QuizState
import edu.agh.susgame.front.service.interfaces.GameService


@Composable
fun ComputerComponent(
    gameService: GameService,
    gameManager: GameManager
) {
    val computerState = gameManager.gameState.computerState

    Row(
        modifier = Modifier
            .background(ChatColors.BACKGROUND)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            DesktopView(gameManager.gameState)
        }
        Box(modifier = Modifier.weight(1f)) {
            when (computerState.value) {
                ComputerState.NothingOpened ->
                    Box {}

                ComputerState.ChatOpened ->
                    ChatView(gameService, gameManager)

                is ComputerState.MiniGameOpened -> Box {
                    val miniGame = (computerState.value as ComputerState.MiniGameOpened).miniGame
                    Text("Minigame $miniGame")
                }

                is ComputerState.QuizQuestionOpened -> Box {
                    when (val quizState = gameManager.gameState.quizState.value) {
                        QuizState.QuestionNotAvailable ->
                            computerState.value = ComputerState.NothingOpened

                        is QuizState.QuestionAvailable -> QuizQuestionView(
//                            quizState = remember { mutableStateOf(quizState) },
                            quizQuestion = quizState.question,
                            answerState = quizState.answerState,
                            quizState = gameManager.gameState.quizState,
                        )
                    }
                }
            }
        }
    }
}
