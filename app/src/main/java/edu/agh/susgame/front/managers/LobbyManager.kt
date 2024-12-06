package edu.agh.susgame.front.managers

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.front.gui.components.common.util.ParserDTO
import edu.agh.susgame.front.gui.components.common.util.player.PlayerLobby
import edu.agh.susgame.front.gui.components.common.util.player.PlayerStatus
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.LobbyService

class LobbyManager(
    val lobbyService: LobbyService,
    val gameService: GameService,
    val lobbyId: LobbyId,
    val name: String,
//    val maxNumOfPlayers: Int,
//    val gameTime: Int,
) {
    // ATTRIBUTES - DEFAULT
    var localPlayerId: PlayerId? = null
    val playersMap: SnapshotStateMap<PlayerId, PlayerLobby> = mutableStateMapOf()
    val gameManager: MutableState<GameManager?> = mutableStateOf(null)
    val isGameReady: MutableState<Boolean> = mutableStateOf(false)
    val isColorBeingChanged: MutableState<Boolean> = mutableStateOf(false)

    // SERVICE MANAGEMENT - UPDATE FROM SERVER MESSAGES
    fun updateFromRest() {
        lobbyService.getById(lobbyId).thenApply { lobby ->
            lobby?.playersWaiting?.forEach { player ->
                updateAddPlayer(
                    playerId = player.id,
                    nickname = player.nickname,
                    color = Color(player.color),
                    readiness = if (player.readiness) PlayerStatus.READY else PlayerStatus.NOT_READY
                )
            }
            println(lobby?.playersWaiting)
        }
    }

    fun updateAddPlayer(
        playerId: PlayerId,
        nickname: PlayerNickname,
        color: Color = Color.Red,
        readiness: PlayerStatus = PlayerStatus.NOT_READY
    ) {
        playersMap[playerId] = PlayerLobby(
            id = playerId,
            name = nickname,
            color = mutableStateOf(color),
            status = mutableStateOf(readiness)
        )
    }

    fun updatePlayerColor(id: PlayerId, color: Color) {
        playersMap[id]?.color?.value = color
    }

    fun updatePlayerStatus(id: PlayerId, status: PlayerStatus) {
        playersMap[id]?.status?.value = status
    }

    fun updateRemovePlayer(playerId: PlayerId) {
        playersMap.remove(playerId)
    }

    // HANDLE GUI INPUT
    fun handleLocalPlayerJoin(nickname: PlayerNickname) {
        gameService.joinLobby(lobbyId, nickname)
    }

    fun handleLocalPlayerLeave() {
        localPlayerId?.let { gameService.sendLeavingRequest(it) }
        gameService.leaveLobby()
    }
    fun handlePlayerColorChange(color: Color){
        localPlayerId?.let{
            updatePlayerColor(it, color)
            gameService.sendPlayerChangeColor(
                playerId = it,
                color = color.value
            )
            isColorBeingChanged.value = false
        }
    }

    fun handleLocalPlayerStatusChange() {
        localPlayerId?.let {
            val currentStatus = getPlayerStatus(it)
            val newStatus =
                if (currentStatus == PlayerStatus.READY) PlayerStatus.NOT_READY else PlayerStatus.READY
            gameService.sendChangePlayerReadinessRequest(it, newStatus)
            updatePlayerStatus(it, newStatus)
        }
    }

    // UTILS
    fun getNumberOfPlayers(): Int = playersMap.size

    fun loadMapFromServer() {
        this.lobbyId.let { id ->
            lobbyService.getGameMap(id)
                .thenApply { gameMapDTO ->
                    if (gameMapDTO != null) {
                        this.gameManager.value = this.localPlayerId?.let {
                            ParserDTO.gameMapDtoToGameManager(
                                gameMapDTO = gameMapDTO,
                                localPlayerId = it,
                                players = playersMap.values.toList()
                            )
                        }
                        isGameReady.value = true
                    } else {
                        println("Failed to update map from the server for gameMapId: $id and player: $this.localPlayer.id")
                    }
                }
        }
    }

    // PRIVATE
    private fun getPlayerStatus(id: PlayerId) = playersMap[id]?.status?.value
}