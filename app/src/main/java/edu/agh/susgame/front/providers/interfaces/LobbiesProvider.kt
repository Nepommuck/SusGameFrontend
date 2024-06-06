package edu.agh.susgame.front.providers.interfaces

import edu.agh.susgame.front.model.game.Lobby
import edu.agh.susgame.front.model.game.LobbyId
import java.util.concurrent.CompletableFuture

interface LobbiesProvider {
    sealed class CreateNewGameResult {
        data class Success(val lobbyId: LobbyId) : CreateNewGameResult()
        data object NameAlreadyExists : CreateNewGameResult()
        data object OtherError : CreateNewGameResult()
    }

    fun getAll(): CompletableFuture<Map<LobbyId, Lobby>>

    fun getById(lobbyId: LobbyId): CompletableFuture<Lobby?>

    fun createNewGame(
        gameName: String,
        gamePin: String,
        maxNumberOfPlayers: Int,
        gameTime: Int,
    ): CompletableFuture<CreateNewGameResult>
}
