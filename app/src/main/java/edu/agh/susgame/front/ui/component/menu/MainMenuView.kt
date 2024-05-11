package edu.agh.susgame.front.ui.component.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import edu.agh.susgame.front.ui.component.common.Header
import edu.agh.susgame.front.ui.component.menu.navigation.MenuRoute
import edu.agh.susgame.front.ui.theme.PaddingL
import edu.agh.susgame.front.ui.theme.PaddingM

@Composable
fun MainMenuView(
    navController: NavController,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Header(title = "Main menu")

        Row(modifier = Modifier.padding(PaddingM)) {
            Button(onClick = {
                navController.navigate(MenuRoute.SearchGame.route)
            }) {
                Text(text = "Search Game")
            }

            Spacer(modifier = Modifier.width(PaddingL))

            Button(onClick = {
                navController.navigate(MenuRoute.CreateGame.route)
            }) {
                Text(text = "Create Game")
            }
        }
    }
}
