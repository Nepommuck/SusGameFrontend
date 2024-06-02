package edu.agh.susgame.front.providers.mock

import androidx.compose.ui.graphics.Color
import edu.agh.susgame.front.model.Player
import edu.agh.susgame.front.model.game.Lobby
import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.model.game.PlayerId
import edu.agh.susgame.front.providers.interfaces.LobbiesProvider
import edu.agh.susgame.front.providers.interfaces.LobbiesProvider.CreateNewGameResult
import edu.agh.susgame.front.settings.Configuration
import java.util.concurrent.CompletableFuture

class MockLobbiesProvider(mockDelayMs: Long? = null) : LobbiesProvider {
    private val delayMs = mockDelayMs ?: 0
    private var freeGameId: Int = 0
    private var currentLobbies: MutableMap<LobbyId, Lobby> = mutableMapOf()


    override fun createCustomLobbies() {
        // game 1
        val player0 = Player(
            id = PlayerId(0),
            name = "Player_0",
            color = Color.Red
        )
        val player1 = Player(
            id = PlayerId(1),
            name = "Player_1",
            color = Color.Green
        )

        val lobbyIdValue1 = LobbyId(freeGameId++)
        currentLobbies.putIfAbsent(
            lobbyIdValue1, Lobby(
                id = lobbyIdValue1,
                name = "Gra dodana statycznie 1",
                maxNumOfPlayers = Configuration.MaxPlayersPerGame,
                gameTime = 10,
                gamePin = "game pin",
            )
        )

        currentLobbies[lobbyIdValue1]?.addPlayer(player0)
        currentLobbies[lobbyIdValue1]?.addPlayer(player1)


        // game 2
        val player2 = Player(
            id = PlayerId(0),
            name = "Player_0",
            color = Color.Red
        )
        val player3 = Player(
            id = PlayerId(1),
            name = "Player_1",
            color = Color.Green
        )

        val lobbyIdValue2 = LobbyId(freeGameId++)
        currentLobbies.putIfAbsent(
            lobbyIdValue2, Lobby(
                id = lobbyIdValue2,
                name = "Gra dodana statycznie 2",
                maxNumOfPlayers = 4,
                gameTime = 5,
                gamePin = null,
            )
        )
        currentLobbies[lobbyIdValue2]?.addPlayer(player2)
        currentLobbies[lobbyIdValue2]?.addPlayer(player3)
    }

    override fun getAll(): CompletableFuture<MutableMap<LobbyId, Lobby>> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)
            currentLobbies
        }

    override fun getById(lobbyId: LobbyId): CompletableFuture<Lobby?> =
        this.getAll().thenApply { allGames ->
            allGames[lobbyId]
        }

    override fun join(lobbyId: LobbyId, player: Player): CompletableFuture<Unit> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)
            currentLobbies[lobbyId]?.addPlayer(player)
        }

    override fun leave(lobbyId: LobbyId, playerId: PlayerId): CompletableFuture<Unit> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)
            currentLobbies[lobbyId]?.kickPlayer(playerId)
        }

    override fun createNewGame(
        gameName: String,
        gamePin: String,
        maxNumberOfPlayers: Int,
        gameTime: Int,
    ): CompletableFuture<CreateNewGameResult> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)
            val lobbyId = LobbyId(freeGameId++)
            currentLobbies.putIfAbsent(
                lobbyId,
                Lobby(
                    lobbyId,
                    gameName,
                    maxNumberOfPlayers,
                    gameTime,
                    gamePin,
                )
            )
            CreateNewGameResult.Success(lobbyId)
        }
}
