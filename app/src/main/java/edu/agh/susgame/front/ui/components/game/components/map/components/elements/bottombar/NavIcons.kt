package edu.agh.susgame.front.ui.components.game.components.map.components.elements.bottombar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.agh.susgame.R
import edu.agh.susgame.front.navigation.GameRoute
import edu.agh.susgame.front.ui.components.common.theme.PaddingXS

private val sizeDp = 50.dp

enum class ViewState {
    MAP,
    COMPUTER,
}

@Composable
fun NavIcons(gameNavController: NavController, viewState: ViewState) {
    var currentView by remember { mutableStateOf(viewState) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
    ) {

        Image(painter = painterResource(id = R.drawable.network),
            contentDescription = null,
            modifier = Modifier
                .size(sizeDp)
                .padding(PaddingXS)
                .graphicsLayer { alpha = if (currentView != ViewState.MAP) 0.5f else 1f }
                .clickable(enabled = currentView != ViewState.MAP) {
                    currentView = ViewState.MAP
                    gameNavController.navigate(GameRoute.Map.route)
                }
        )

        Image(painter = painterResource(id = R.drawable.button_console),
            contentDescription = null,
            modifier = Modifier
                .size(sizeDp)
                .padding(PaddingXS)
                .graphicsLayer { alpha = if (currentView != ViewState.COMPUTER) 0.5f else 1f }
                .clickable(enabled = currentView != ViewState.COMPUTER) {

                    currentView = ViewState.COMPUTER
                    gameNavController.navigate(GameRoute.Computer.route)

                })
    }
}