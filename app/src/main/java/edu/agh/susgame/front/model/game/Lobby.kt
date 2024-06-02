package edu.agh.susgame.front.model.game

import edu.agh.susgame.front.model.Player
import edu.agh.susgame.front.model.PlayerId

data class LobbyId(val value: Int)
class Lobby(
    val id: LobbyId,
    val name: String,
    val maxNumOfPlayers: Int,
    val gameTime: Int,
    val playersWaiting: MutableMap<PlayerId, Player> = mutableMapOf(),
) {
    fun addPlayer(player: Player, playerId: PlayerId) {
        playersWaiting.putIfAbsent(playerId, player)
        player.id = playerId
    }

    fun kickPlayer(playerId: PlayerId) {
        playersWaiting[playerId]?.resetId()
        playersWaiting.remove(playerId)
    }
}
