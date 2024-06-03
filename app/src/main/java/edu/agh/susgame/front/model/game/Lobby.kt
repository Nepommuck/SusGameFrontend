package edu.agh.susgame.front.model.game

import edu.agh.susgame.front.model.Player
import edu.agh.susgame.front.model.PlayerId

data class LobbyId(val value: Int)
data class Lobby(
    val id: LobbyId,
    val name: String,
    val maxNumOfPlayers: Int,
    val gameTime: Int,
    val playersWaiting: Map<PlayerId, Player> = emptyMap(),
)
