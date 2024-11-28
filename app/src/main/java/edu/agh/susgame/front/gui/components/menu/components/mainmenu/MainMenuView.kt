package edu.agh.susgame.front.gui.components.menu.components.mainmenu

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.agh.susgame.front.gui.components.common.theme.Header
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.menu.components.mainmenu.elements.IpAddressInput
import edu.agh.susgame.front.gui.components.menu.components.mainmenu.elements.MainMenuButton
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute
import edu.agh.susgame.front.service.web.IpAddressProvider

@Composable
fun MainMenuView(
    navController: NavController,
    ipAddressProvider: IpAddressProvider,
) {
//    navController.navigate("${MenuRoute.Game.route}/0") // at start opens GameMap view, which helps in developing the map
    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Header(title = Translation.GAME_TITLE)

        IpAddressInput(ipAddressProvider)

        Row(modifier = Modifier.padding(top = 40.dp)) {
            MainMenuButton(
                text = Translation.Menu.JOIN_GAME,
                ipAddressProvider = ipAddressProvider,
                onClick = {
                    navController.navigate(MenuRoute.SearchLobby.route)
                })

            Spacer(modifier = Modifier.width(70.dp))

            MainMenuButton(
                text = Translation.Menu.CREATE_GAME,
                ipAddressProvider = ipAddressProvider,
                onClick = {
                    navController.navigate(MenuRoute.CreateLobby.route)
                }
            )
        }
    }
}
