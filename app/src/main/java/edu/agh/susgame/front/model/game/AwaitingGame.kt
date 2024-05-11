package edu.agh.susgame.front.model.game

import edu.agh.susgame.front.model.PlayerNickname

data class AwaitingGame(
    val id: GameId,
    val name: String,
    val playersWaiting: List<PlayerNickname>,
)
