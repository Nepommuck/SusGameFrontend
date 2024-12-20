package edu.agh.susgame.front.gui.components.game.components.elements.bottombar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.agh.susgame.R
import edu.agh.susgame.front.gui.components.common.theme.PaddingXS
import edu.agh.susgame.front.gui.components.common.util.Calculate
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.managers.state.util.QuizState

private val SIZE_DP = 50.dp

@Composable
fun NavIcons(
    isComputerVisible: MutableState<Boolean>,
    gameManager: GameManager
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
    ) {

        Image(painter = painterResource(id = R.drawable.network),
            contentDescription = null,
            modifier = Modifier
                .size(SIZE_DP)
                .padding(PaddingXS)
                .alpha(Calculate.getAlpha(!isComputerVisible.value))
                .clickable {
                    isComputerVisible.value = false
                }
        )

        Image(
            painter = painterResource(id = R.drawable.button_console),
            contentDescription = null,
            modifier = Modifier
                .size(SIZE_DP)
                .padding(PaddingXS)
                .alpha(Calculate.getAlpha(isComputerVisible.value))
                .clickable {
                    isComputerVisible.value = true
                },
            colorFilter = if (gameManager.quizManager.quizState.value is QuizState.QuestionAvailable) {
                ColorFilter.lighting(
                    multiply = Color(0xFFFFA500),
                    add = Color.Black
                )
            } else {
                null
            }
        )
    }
}
