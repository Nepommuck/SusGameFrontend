package edu.agh.susgame.front.providers.web

import edu.agh.susgame.front.model.Player
import edu.agh.susgame.front.model.PlayerId
import edu.agh.susgame.front.model.game.Lobby
import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.providers.interfaces.LobbiesProvider
import edu.agh.susgame.front.providers.interfaces.LobbiesProvider.CreateNewGameResult
import edu.agh.susgame.front.providers.web.rest.games.GamesRest
import edu.agh.susgame.front.providers.web.rest.games.model.CreateGameApiResult
import edu.agh.susgame.front.providers.web.rest.games.model.GetAllGamesApiResult
import edu.agh.susgame.front.providers.web.rest.games.model.GetGameApiResult
import edu.agh.susgame.front.providers.web.rest.model.LobbyApi
import java.util.concurrent.CompletableFuture


class WebLobbiesProvider(private val gamesRest: GamesRest) : LobbiesProvider {
    override fun getAll(): CompletableFuture<Map<LobbyId, Lobby>> =
        gamesRest.getAllGames().thenApply { response ->
            when (response) {
                GetAllGamesApiResult.Error ->
                    emptyMap()

                is GetAllGamesApiResult.Success ->
                    response
                        .lobbies
                        .map {
                            lobbyFromApiModel(it)
                        }.associateBy { it.id }
            }
        }

    override fun getById(lobbyId: LobbyId): CompletableFuture<Lobby?> =
        gamesRest.getGame(lobbyId).thenApply { response ->
            when (response) {
                GetGameApiResult.DoesNotExist -> null
                GetGameApiResult.OtherError -> null
                is GetGameApiResult.Success -> lobbyFromApiModel(response.lobby)
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

    // TODO GAME-59 Remove
    override fun join(lobbyId: LobbyId, player: Player): CompletableFuture<Unit> =
        CompletableFuture.supplyAsync { }

    // TODO GAME-59 Remove
    override fun leave(lobbyId: LobbyId, playerId: PlayerId): CompletableFuture<Unit> =
        CompletableFuture.supplyAsync { }

    companion object {
        private fun lobbyFromApiModel(lobby: LobbyApi): Lobby =
            Lobby(
                id = LobbyId(lobby.id),
                name = lobby.name,
                maxNumOfPlayers = lobby.maxNumberOfPlayers,
                gameTime = 10,
                // TODO GAME-59 After joining is implemented, remove this hardcoded adding `"The-player"`
                playersWaiting = (lobby.players + "The-player")
//                playersWaiting = lobby.players
                    .mapIndexed { index, playerNickname -> Pair(PlayerId(index), playerNickname) }
                    .associate { it.first to Player(it.second) }
                    .toMutableMap()
            )
    }
}
