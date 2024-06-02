package edu.agh.susgame.front.providers.interfaces

import edu.agh.susgame.front.model.Player
import edu.agh.susgame.front.model.PlayerId
import edu.agh.susgame.front.model.game.Lobby
import edu.agh.susgame.front.model.game.LobbyId
import java.util.concurrent.CompletableFuture

interface LobbiesProvider {
    sealed class CreateNewGameResult {
        data class Success(val lobbyId: LobbyId) : CreateNewGameResult()
        data object NameAlreadyExists : CreateNewGameResult()
        data object OtherError : CreateNewGameResult()
    }

    fun getAll(): CompletableFuture<MutableMap<LobbyId, Lobby>>

    fun getById(lobbyId: LobbyId): CompletableFuture<Lobby?>

    // TODO GAME-59 Remove
    fun join(lobbyId: LobbyId, player: Player): CompletableFuture<Unit>

    // TODO GAME-59 Remove
    fun leave(lobbyId: LobbyId, playerId: PlayerId): CompletableFuture<Unit>

    fun createNewGame(
        gameName: String,
        gamePin: String,
        maxNumberOfPlayers: Int,
        gameTime: Int,
    ): CompletableFuture<CreateNewGameResult>

}
