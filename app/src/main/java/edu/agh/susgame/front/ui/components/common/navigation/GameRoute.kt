package edu.agh.susgame.front.ui.components.common.navigation

sealed class GameRoute(val route: String) {
    data object Map : GameRoute("game-map")

    data object Computer : GameRoute("game-computer")
}
