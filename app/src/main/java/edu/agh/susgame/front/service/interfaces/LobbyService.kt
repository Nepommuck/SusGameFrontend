package edu.agh.susgame.front.service.interfaces

import edu.agh.susgame.dto.rest.model.GameMapDTO
import edu.agh.susgame.dto.rest.model.LobbyDetails
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.LobbyPin
import edu.agh.susgame.dto.rest.model.LobbyRow
import edu.agh.susgame.front.managers.LobbyManager
import java.util.concurrent.CompletableFuture

interface LobbyService {
    val lobbyManager: LobbyManager?

    fun addLobbyManager(lobbyManager: LobbyManager)

    fun getAll(): CompletableFuture<Map<LobbyId, LobbyRow>>

    fun getLobbyDetails(
        lobbyId: LobbyId,
        lobbyPin: LobbyPin?,
    ): CompletableFuture<GetGameDetailsResult>

    fun getGameMap(lobbyId: LobbyId): CompletableFuture<GameMapDTO?>

    fun createNewGame(
        gameName: String,
        gamePin: LobbyPin?,
        maxNumberOfPlayers: Int,
    ): CompletableFuture<CreateNewGameResult>
}

sealed class CreateNewGameResult {
    data class Success(val lobbyId: LobbyId) : CreateNewGameResult()
    data object NameAlreadyExists : CreateNewGameResult()
    data object OtherError : CreateNewGameResult()
}

sealed class GetGameDetailsResult {
    data class Success(val lobbyDetails: LobbyDetails) : GetGameDetailsResult()
    data object InvalidPin : GetGameDetailsResult()
    data object OtherError : GetGameDetailsResult()
}
