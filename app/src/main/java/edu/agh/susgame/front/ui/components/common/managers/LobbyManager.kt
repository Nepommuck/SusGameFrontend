package edu.agh.susgame.front.ui.components.common.managers

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.dto.rest.model.PlayerREST

enum class PlayerStatus {
    NOT_READY,
    READY,
    CONNECTING
}

data class PlayerLobby(val name: PlayerNickname, val status: MutableState<PlayerStatus>)


class LobbyManager(
    var id: LobbyId? = null,
    var name: String? = null,
    var maxNumOfPlayers: Int? = null,
    var gameTime: Int? = null,
    var playersMap: MutableMap<PlayerId, PlayerLobby> = mutableMapOf(),
    var localId: PlayerId? = PlayerId(0)

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

    fun addPlayerRest(player: PlayerREST) {
        val playerLobby =
            PlayerLobby(name = player.nickname, status = mutableStateOf(PlayerStatus.NOT_READY))
        playersMap[player.id] = playerLobby
    }

    fun setId(id: PlayerId) {
        this.localId = id
    }

    fun updatePlayerStatus(id: PlayerId, status: PlayerStatus) {
        playersMap[id]?.status?.value = status
    }

    fun deletePlayer(player: PlayerREST) {
        playersMap.remove(player.id)
    }
}