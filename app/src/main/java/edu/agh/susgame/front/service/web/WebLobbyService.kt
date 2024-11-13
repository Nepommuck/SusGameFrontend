package edu.agh.susgame.front.service.web

import edu.agh.susgame.dto.rest.games.GamesRest
import edu.agh.susgame.dto.rest.games.model.CreateGameApiResult
import edu.agh.susgame.dto.rest.games.model.GetAllGamesApiResult
import edu.agh.susgame.dto.rest.games.model.GetGameApiResult
import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.front.service.interfaces.CreateNewGameResult
import edu.agh.susgame.front.service.interfaces.LobbyService
import edu.agh.susgame.front.ui.components.common.managers.LobbyManager
import java.util.concurrent.CompletableFuture


class WebLobbyService(private val gamesRest: GamesRest) : LobbyService {
    var lobbyManager: LobbyManager? = null

    override fun addLobbyManager(lobbyManager: LobbyManager){
        this.lobbyManager=lobbyManager
    }

    override fun getAll(): CompletableFuture<Map<LobbyId, Lobby>> =
        gamesRest.getAllGames().thenApply { response ->
            when (response) {
                GetAllGamesApiResult.Error ->
                    emptyMap()

                is GetAllGamesApiResult.Success ->
                    response
                        .lobbies
                        .associateBy { it.id }
            }
        }

    override fun getById(lobbyId: LobbyId): CompletableFuture<Lobby?> =
        gamesRest.getGame(lobbyId).thenApply { response ->
            when (response) {
                GetGameApiResult.DoesNotExist -> null
                GetGameApiResult.OtherError -> null
                is GetGameApiResult.Success -> response.lobby
            }

        }

    override fun createNewGame(
        gameName: String,
        gamePin: String,
        maxNumberOfPlayers: Int,
        gameTime: Int,
    ): CompletableFuture<CreateNewGameResult> =
        gamesRest.createGame(gameName, maxNumberOfPlayers, gamePin)
            .thenApply {
                when (it) {
                    is CreateGameApiResult.Success ->
                        CreateNewGameResult.Success(lobbyId = it.createdLobbyId)

                    CreateGameApiResult.NameAlreadyExists ->
                        CreateNewGameResult.NameAlreadyExists

                    CreateGameApiResult.OtherError ->
                        CreateNewGameResult.OtherError
                }
            }
}
