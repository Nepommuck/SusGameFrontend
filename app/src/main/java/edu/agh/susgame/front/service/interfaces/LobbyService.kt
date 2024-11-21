package edu.agh.susgame.front.service.interfaces

import edu.agh.susgame.dto.rest.model.GameMapEdgeDTO
import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.front.managers.LobbyManager
import java.util.concurrent.CompletableFuture

interface LobbyService {
    fun addLobbyManager(lobbyManager: LobbyManager)
    fun getAll(): CompletableFuture<Map<LobbyId, Lobby>>

    fun getById(lobbyId: LobbyId): CompletableFuture<Lobby?>
    fun getGameMap(lobbyId: LobbyId): CompletableFuture<List<GameMapEdgeDTO>>
    fun createNewGame(
        gameName: String,
        gamePin: String,
        maxNumberOfPlayers: Int,
        gameTime: Int,
    ): CompletableFuture<CreateNewGameResult>
}

sealed class CreateNewGameResult {
    data class Success(val lobbyId: LobbyId) : CreateNewGameResult()
    data object NameAlreadyExists : CreateNewGameResult()
    data object OtherError : CreateNewGameResult()
}