package edu.agh.susgame.front.model.game

data class AwaitingGame(
    val id: GameId,
    val name: String,
    val playersWaiting: List<String>,
)
