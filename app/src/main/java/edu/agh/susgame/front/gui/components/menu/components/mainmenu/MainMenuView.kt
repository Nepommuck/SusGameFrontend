package edu.agh.susgame.front.gui.components.menu.components.mainmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.agh.susgame.front.gui.components.common.theme.Header
import edu.agh.susgame.front.gui.components.common.theme.MenuBackground
import edu.agh.susgame.front.gui.components.common.theme.PaddingL
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
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
    MenuBackground()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {

        Spacer(modifier = Modifier.weight(0.5f))
        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
            Header(title = Translation.Menu.GAME_TITLE, style = TextStyler.TerminalXXL)
        }

        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
            IpAddressInput(ipAddressProvider)
        }

        Row(modifier = Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
            Spacer(modifier = Modifier.weight(0.6f))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                        contentAlignment = Alignment.Center
            ) {
                MainMenuButton(
                    text = Translation.Menu.JOIN_GAME,
                    ipAddressProvider = ipAddressProvider,
                    onClick = {
                        navController.navigate(MenuRoute.SearchLobby.route)
                    })
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                        contentAlignment = Alignment.Center
            )
            {
                MainMenuButton(
                    text = Translation.Menu.CREATE_GAME,
                    ipAddressProvider = ipAddressProvider,
                    onClick = {
                        navController.navigate(MenuRoute.CreateLobby.route)
                    }
                )
            }
            Spacer(modifier = Modifier.weight(0.6f))
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }
}
