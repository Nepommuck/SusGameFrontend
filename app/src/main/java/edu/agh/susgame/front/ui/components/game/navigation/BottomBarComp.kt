package edu.agh.susgame.front.ui.components.game.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import edu.agh.susgame.front.ui.components.common.util.BottomNavigationItem

@Composable
fun BottomBarComponent(navController: NavHostController) {
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