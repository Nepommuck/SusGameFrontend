package edu.agh.susgame.front.service.web.webservices

import edu.agh.susgame.dto.rest.games.GamesRest
import edu.agh.susgame.dto.rest.games.model.CreateGameApiResult
import edu.agh.susgame.dto.rest.games.model.GetAllGamesApiResult
import edu.agh.susgame.dto.rest.games.model.GetGameApiResult
import edu.agh.susgame.dto.rest.games.model.GetGameMapApiResult
import edu.agh.susgame.dto.rest.model.GameMapDTO
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.LobbyPin
import edu.agh.susgame.dto.rest.model.LobbyRow
import edu.agh.susgame.front.managers.LobbyManager
import edu.agh.susgame.front.service.interfaces.CreateNewGameResult
import edu.agh.susgame.front.service.interfaces.GetGameDetailsResult
import edu.agh.susgame.front.service.interfaces.LobbyService
import java.util.concurrent.CompletableFuture


class WebLobbyService(private val gamesRest: GamesRest) : LobbyService {
    override var lobbyManager: LobbyManager? = null

    override fun addLobbyManager(lobbyManager: LobbyManager) {
        this.lobbyManager = lobbyManager
    }

    override fun getAll(): CompletableFuture<Map<LobbyId, LobbyRow>> =
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

    override fun getLobbyDetails(
        lobbyId: LobbyId,
        lobbyPin: LobbyPin?,
    ): CompletableFuture<GetGameDetailsResult> =
        gamesRest.getGameDetails(lobbyId, lobbyPin).thenApply { response ->
            when (response) {
                is GetGameApiResult.Success -> GetGameDetailsResult.Success(response.lobby)
                GetGameApiResult.InvalidPin -> GetGameDetailsResult.InvalidPin
                else -> GetGameDetailsResult.OtherError
            }
        }

    override fun getGameMap(lobbyId: LobbyId): CompletableFuture<GameMapDTO?> =
        gamesRest.getGameMap(lobbyId).thenApply { response ->
            when (response) {
                is GetGameMapApiResult.Success -> response.gameMap
                else -> null
            }
        }

    override fun createNewGame(
        gameName: String,
        gamePin: String,
        maxNumberOfPlayers: Int,
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
