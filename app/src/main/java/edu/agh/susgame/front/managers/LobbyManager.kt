package edu.agh.susgame.front.managers

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.LobbyPin
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.front.gui.components.common.util.ColorProvider
import edu.agh.susgame.front.gui.components.common.util.ParserDTO
import edu.agh.susgame.front.gui.components.common.util.player.PlayerLobby
import edu.agh.susgame.front.gui.components.common.util.player.PlayerStatus
import edu.agh.susgame.front.managers.state.LobbyStateManager
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.GetGameDetailsResult.Success
import edu.agh.susgame.front.service.interfaces.LobbyService

class LobbyManager(
    val lobbyService: LobbyService,
    val gameService: GameService,
    val lobbyId: LobbyId,
    val lobbyPin: LobbyPin?,
    val name: String,
    val maxNumOfPlayers: Int,
) {
    val lobbyState = LobbyStateManager()

    // ATTRIBUTES - DEFAULT
    var localPlayerId: PlayerId? = null
    val playersMap: SnapshotStateMap<PlayerId, PlayerLobby> = mutableStateMapOf()
    val gameManager: MutableState<GameManager?> = mutableStateOf(null)

    // SERVICE MANAGEMENT - UPDATE FROM SERVER MESSAGES
    fun updateFromRest() {
        lobbyService
            .getLobbyDetails(lobbyId, lobbyPin)
            .thenApply { result ->
                when (result) {
                    is Success -> result.lobbyDetails.playersWaiting.forEach { player ->
                        updatePlayerJoins(
                            playerId = player.id,
                            nickname = player.nickname,
                            color = player.color?.let {
                                Color(it.decimalRgbaValue.toULong())
                            } ?: ColorProvider.DEFAULT_COLOR,
                            readiness = if (player.readiness) PlayerStatus.READY else PlayerStatus.NOT_READY
                        )
                    }

                    else -> {}
                }
            }
    }

    fun updatePlayerJoins(
        playerId: PlayerId,
        nickname: PlayerNickname,
        color: Color = ColorProvider.DEFAULT_COLOR,
        readiness: PlayerStatus = PlayerStatus.NOT_READY
    ) {
        playersMap[playerId] = PlayerLobby(
            id = playerId,
            name = nickname,
            color = mutableStateOf(color),
            status = mutableStateOf(readiness)
        )
        updateAllPlayersAreReadyValue()
    }

    fun updatePlayerColor(playerId: PlayerId, color: Color) {
        playersMap[playerId]?.color?.value = color
    }

    fun updatePlayerStatus(playerId: PlayerId, status: PlayerStatus) {
        playersMap[playerId]?.status?.value = status
        updateAllPlayersAreReadyValue()
    }

    fun updatePlayerLeaves(playerId: PlayerId) {
        playersMap.remove(playerId)
        updateAllPlayersAreReadyValue()
    }

    fun updateLocalPlayerId(playerId: PlayerId) {
        localPlayerId = playerId
    }

    // HANDLE GUI INPUT
    fun handleLocalPlayerJoin(nickname: PlayerNickname) {
        gameService.joinLobby(lobbyId, lobbyPin, nickname)
        lobbyState.hasPlayerJoined.value = true
        updateAllPlayersAreReadyValue()
    }

    fun handleLocalPlayerLeave() {
        localPlayerId?.let { gameService.sendLeavingRequest(it) }
        gameService.leaveLobby()
        lobbyState.hasPlayerJoined.value = false
    }

    fun handlePlayerColorChange(color: Color) {
        localPlayerId?.let {
            updatePlayerColor(it, color)
            gameService.sendPlayerChangeColor(
                playerId = it,
                color = color,
            )
            lobbyState.isColorBeingChanged.value = false
        }
    }

    fun handleLocalPlayerStatusChange() {
        localPlayerId?.let {
            val currentStatus = getPlayerStatus(it)
            val newStatus =
                if (currentStatus == PlayerStatus.READY) PlayerStatus.NOT_READY
                else PlayerStatus.READY
            gameService.sendChangePlayerReadinessRequest(it, newStatus)
            updatePlayerStatus(it, newStatus)
        }
        updateAllPlayersAreReadyValue()
    }

    // UTILS
    fun getNumberOfPlayers(): Int = playersMap.size

    fun loadMapFromServer() {
        this.lobbyId.let { id ->
            lobbyService.getGameMap(id)
                .thenApply { gameMapDTO ->
                    if (gameMapDTO != null) {
                        try {
                            this.gameManager.value = this.localPlayerId?.let {
                                ParserDTO.gameMapDtoToGameManager(
                                    gameService = gameService,
                                    gameMapDTO = gameMapDTO,
                                    localPlayerId = it,
                                    players = playersMap
                                )
                            }
                        } catch (e: Exception) {
                            println("Error updating gameManager: ${e.message}")
                        }
                        lobbyState.isGameMapReady.value = true
                    } else {
                        println("Failed to update map from the server for gameMapId: $id and player: $this.localPlayer.id")
                    }
                }
        }
    }

    // PRIVATE
    private fun getPlayerStatus(id: PlayerId) = playersMap[id]?.status?.value

    private fun updateAllPlayersAreReadyValue() {
        lobbyState.areAllPlayersReady.value = playersMap.values.all { player ->
            player.status.value == PlayerStatus.READY
        }
    }
}
