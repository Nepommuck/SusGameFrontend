package edu.agh.susgame.front.managers.state


enum class MiniGame {
    MiniGame1, MiniGame2
}

sealed class ComputerState {
    data object NothingOpened : ComputerState()

    data object ChatOpened : ComputerState()

    data class MiniGameOpened(val miniGame: MiniGame) : ComputerState()
}
