package edu.agh.susgame.front.managers

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerREST
import edu.agh.susgame.front.gui.components.common.util.ColorProvider
import edu.agh.susgame.front.gui.components.common.util.ParserDTO
import edu.agh.susgame.front.gui.components.common.util.player.PlayerLobby
import edu.agh.susgame.front.gui.components.common.util.player.PlayerStatus
import edu.agh.susgame.front.service.interfaces.LobbyService

class LobbyManager(
    val lobbyService: LobbyService,
    val id: LobbyId,
    val name: String,
//    val maxNumOfPlayers: Int,
//    val gameTime: Int,
) {
    val playersRest: MutableMap<PlayerId, PlayerREST> = mutableMapOf()
    val localPlayer: PlayerLobby = PlayerLobby()

    val playersMap: SnapshotStateMap<PlayerId, PlayerLobby> = mutableStateMapOf()
    val gameManager: MutableState<GameManager?> = mutableStateOf(null)
    val isGameReady: MutableState<Boolean> = mutableStateOf(false)

    val isColorBeingChanged: MutableState<Boolean> = mutableStateOf(false)

    val colorProvider: ColorProvider = ColorProvider()
    fun updateFromRest(lobby: Lobby) {
        lobby.playersWaiting.forEach {
            addPlayerRest(it)
        }
    }

    fun addLocalPlayer() {
        val localPlayerRest =
            PlayerREST(localPlayer.name, localPlayer.id, colorProvider.getUniqueRandomColor())
        addPlayerRest(localPlayerRest)
    }

    fun getPlayerStatus(id: PlayerId) = playersMap[id]?.status?.value

    fun addPlayerRest(player: PlayerREST) {
        playersMap[player.id] = PlayerLobby(
            name = player.nickname,
            id = player.id
        )
        playersRest[player.id] = player
    }

    fun countPlayers(): Int = playersMap.size

    fun getPlayerColor(id: PlayerId): MutableState<Color> =
        playersMap[id]?.color ?: mutableStateOf(Color.Gray)

    fun setPlayerColor(id: PlayerId, color: Color) {
        playersMap[id]?.color?.value = color
    }


    fun updatePlayerStatus(id: PlayerId, status: PlayerStatus) {
        playersMap[id]?.status?.value = status
    }

    fun removePlayer(playerId: PlayerId) {
        playersMap.remove(playerId)
        playersRest.remove(playerId)
    }

    fun getMapFromServer() {
        this.id.let { id ->
            lobbyService.getGameMap(id)
                .thenApply { gameMapDTO ->
                    if (gameMapDTO != null) {
                        this.gameManager.value = ParserDTO.gameMapDtoToGameManager(
                            gameMapDTO = gameMapDTO,
                            localPlayerId = this.localPlayer.id,
                            players = playersMap.values.toList()
                        )
                        isGameReady.value = true
                    } else {
                        println("Failed to update map from the server for gameMapId: $id and player: $this.localPlayer.id")
                    }
                }
        }
    }
}