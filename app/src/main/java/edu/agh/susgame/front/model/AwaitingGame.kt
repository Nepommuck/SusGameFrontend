package edu.agh.susgame.front.model

data class AwaitingGame(
    val id: AwaitingGameId,
    val name: String,
    val description: String,
    val playersWaiting: Int,
) {
    data class AwaitingGameId(val value: Int)
}
