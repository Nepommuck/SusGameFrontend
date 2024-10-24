package edu.agh.susgame.front.ui.components.menu.components.mainmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import edu.agh.susgame.front.navigation.MenuRoute
import edu.agh.susgame.front.Translation
import edu.agh.susgame.front.ui.components.common.Header
import edu.agh.susgame.front.ui.components.menu.components.mainmenu.elements.MainMenuButton
import edu.agh.susgame.front.ui.components.common.theme.PaddingL
import edu.agh.susgame.front.ui.components.common.theme.PaddingM

@Composable
fun MainMenuView(
    navController: NavController,
) {
    navController.navigate("${MenuRoute.Game.route}/0") // at start opens GameMap view, which helps in developing the map
    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Header(title = Translation.GAME_TITLE)

        Row(modifier = Modifier.padding(top = PaddingM)) {
            MainMenuButton(
                text = Translation.Menu.JOIN_GAME,
                onClick = {
                    navController.navigate(MenuRoute.SearchLobby.route)
                })

            Spacer(modifier = Modifier.width(PaddingL))

            MainMenuButton(
                text = Translation.Menu.CREATE_GAME,
                onClick = {
                    navController.navigate(MenuRoute.CreateLobby.route)
                })
        }
    }
}
