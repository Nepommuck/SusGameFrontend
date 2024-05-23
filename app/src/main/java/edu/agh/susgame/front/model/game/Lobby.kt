package edu.agh.susgame.front.model.game

import edu.agh.susgame.front.model.Player

class Lobby(
    val id: LobbyId,
    val name: String,
    val maxNumOfPlayers: Int,
    val gameTime: Int,
    val gamePin: String?, // probably not safe here, should be hidden in the future
    val playersWaiting: MutableMap<PlayerId, Player> = mutableMapOf(),
    private var freePlayerId: Int = 0,
) {
    fun addPlayer(player: Player) {
        val newPlayerId = PlayerId(freePlayerId++)
        playersWaiting.putIfAbsent(newPlayerId, player)
        player.id = newPlayerId
    }

    fun kickPlayer(playerId: PlayerId) {
        playersWaiting[playerId]?.resetId()
        playersWaiting.remove(playerId)
    }
}
