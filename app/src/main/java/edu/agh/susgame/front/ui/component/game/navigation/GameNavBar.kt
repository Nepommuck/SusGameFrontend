package edu.agh.susgame.front.ui.component.game.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import edu.agh.susgame.front.model.game.GameId
import edu.agh.susgame.front.providers.interfaces.ServerMapProvider
import edu.agh.susgame.front.ui.theme.PaddingL


@Composable
private fun BottomBar(navController: NavHostController) {
    var navigationSelectedItem by remember {
        mutableIntStateOf(0)
    }

    NavigationBar {
        BottomNavigationItem.allMenuItems
            .forEachIndexed { index, navigationItem ->
                NavigationBarItem(
                    selected = index == navigationSelectedItem,
                    label = {
                        Text(navigationItem.label)
                    },
                    icon = {
                        Icon(
                            navigationItem.icon,
                            contentDescription = navigationItem.label,
                        )
                    },
                    onClick = {
                        navigationSelectedItem = index
                        navController.navigate(navigationItem.route)
                        //    TODO Those lines were taken from an internet example and are
                        //     designed to somehow keep the state. We may need them in the
                        //     future, but for now they were doing more harm
                        //  {
                        //    popUpTo(navController.graph.findStartDestination().id) {
                        //        saveState = true
                        //    }
                        //    launchSingleTop = true
                        //    restoreState = true
                        //  }
                    }
                )
            }
    }
}

@Composable
fun GameNavBar(
    gameId: GameId?,
    serverMapProvider: ServerMapProvider,
) {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = PaddingL),
        bottomBar = {
            BottomBar(navController)
        }
    ) { padding ->
        GameNavigationHost(
            gameId,
            padding,
            navController,
            serverMapProvider,
        )
    }
}
