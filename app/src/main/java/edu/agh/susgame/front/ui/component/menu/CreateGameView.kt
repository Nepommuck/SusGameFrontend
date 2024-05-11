package edu.agh.susgame.front.ui.component.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.ui.component.common.Header
import edu.agh.susgame.front.ui.component.menu.navigation.MenuRoute
import edu.agh.susgame.front.ui.theme.PaddingL

@Composable
fun CreateGameView(
    navController: NavController,
) {
    Column(Modifier.padding(PaddingL)) {
        Column(
            Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Header(title = Translation.Menu.CREATE_GAME)

            // TODO GAME-52
            Text(text = "TODO GAME-52")
        }
        Button(onClick = {
            navController.navigate(MenuRoute.MainMenu.route)
        }) {
            Text(text = Translation.Button.GO_BACK)
        }
    }
}
