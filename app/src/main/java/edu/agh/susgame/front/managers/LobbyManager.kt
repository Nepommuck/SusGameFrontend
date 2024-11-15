package edu.agh.susgame.front.managers

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerREST
import edu.agh.susgame.front.ui.components.common.util.player.PlayerLobby
import edu.agh.susgame.front.ui.components.common.util.player.PlayerStatus


class LobbyManager(
    var id: LobbyId? = null,
    var name: String? = null,
    var maxNumOfPlayers: Int? = null,
    var gameTime: Int? = null,
    var playersMap: SnapshotStateMap<PlayerId, PlayerLobby> = mutableStateMapOf(),
    var localId: PlayerId? = PlayerId(5)

) {
    fun updateFromRest(lobby: Lobby) {
        this.id = lobby.id
        this.name = lobby.name
        this.maxNumOfPlayers = lobby.maxNumOfPlayers
        this.gameTime = lobby.gameTime
        lobby.playersWaiting.forEach() {
            addPlayerRest(it)
        }
    }
    fun getPlayerStatus(id: PlayerId) = playersMap[id]?.status?.value

    fun addPlayerRest(player: PlayerREST) {
        playersMap[player.id] = PlayerLobby(
            name = player.nickname,
            status = mutableStateOf(PlayerStatus.NOT_READY)
        )
    }

    fun getHowManyPlayersinLobby() = playersMap.size

    fun setId(id: PlayerId) {
        this.localId = id
    }

    fun updatePlayerStatus(id: PlayerId, status: PlayerStatus) {
        playersMap[id]?.status?.value = status
    }

    fun deletePlayer(playerId: PlayerId) {
        playersMap.remove(playerId)
    }
}