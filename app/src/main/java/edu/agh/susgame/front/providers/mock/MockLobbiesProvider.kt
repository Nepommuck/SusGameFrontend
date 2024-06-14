package edu.agh.susgame.front.providers.mock

import androidx.compose.ui.graphics.Color
import edu.agh.susgame.front.Config
import edu.agh.susgame.front.model.Player
import edu.agh.susgame.front.model.PlayerId
import edu.agh.susgame.front.model.game.Lobby
import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.providers.interfaces.LobbiesProvider
import edu.agh.susgame.front.providers.interfaces.LobbiesProvider.CreateNewGameResult
import java.util.concurrent.CompletableFuture

class MockLobbiesProvider(mockDelayMs: Long? = null) : LobbiesProvider {
    private val delayMs = mockDelayMs ?: 0
    private var freeGameId: Int = 0
    private var freePlayerId: Int = 0
    private var currentLobbies: MutableMap<LobbyId, Lobby> = mutableMapOf()

    init {
        createCustomLobbies()
    }

    override fun getAll(): CompletableFuture<Map<LobbyId, Lobby>> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)
            currentLobbies.toMap()
        }

    override fun getById(lobbyId: LobbyId): CompletableFuture<Lobby?> =
        this.getAll().thenApply { allGames ->
            allGames[lobbyId]
        }

    override fun join(lobbyId: LobbyId, player: Player): CompletableFuture<Unit> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)

            currentLobbies[lobbyId]?.let {
                currentLobbies[lobbyId] = lobbyWithPlayerAdded(it, player)
            }
        }

    override fun leave(lobbyId: LobbyId, playerId: PlayerId): CompletableFuture<Unit> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)

            currentLobbies[lobbyId]?.let {
                currentLobbies[lobbyId] = lobbyWithPlayerRemoved(it, playerId)
            }
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
                )
            )
            CreateNewGameResult.Success(lobbyId)
        }

    /**
     * This function is only for testing, it shows logic behind creating new lobbies
     */
    private fun createCustomLobbies() {
        // game 1
        val player0 = Player(
            name = "Player_0",
            color = Color.Red,
            id = PlayerId(0)
        )
        val player1 = Player(
            name = "Player_1",
            color = Color.Green,
            id = PlayerId(1)
        )

        val lobbyIdValue1 = LobbyId(freeGameId++)
        currentLobbies.putIfAbsent(
            lobbyIdValue1, Lobby(
                id = lobbyIdValue1,
                name = "Gra dodana statycznie 1",
                maxNumOfPlayers = Config.gameConfig.playersPerGame.max,
                gameTime = 10,
            )
        )

        this.join(lobbyIdValue1, player0)
        this.join(lobbyIdValue1, player1)

        // game 2
        val player2 = Player(
            name = "Player_0",
            color = Color.Red,
            id = PlayerId(0)
        )
        val player3 = Player(
            name = "Player_1",
            color = Color.Green,
            id = PlayerId(1)
        )

        val lobbyIdValue2 = LobbyId(freeGameId++)
        currentLobbies.putIfAbsent(
            lobbyIdValue2, Lobby(
                id = lobbyIdValue2,
                name = "Gra dodana statycznie 2",
                maxNumOfPlayers = 4,
                gameTime = 5,
            )
        )
        this.join(lobbyIdValue2, player2)
        this.join(lobbyIdValue2, player3)
    }

    private fun getFreePlayerId(): PlayerId {
        return PlayerId(freePlayerId++)
    }

    private fun lobbyWithPlayerAdded(lobby: Lobby, player: Player): Lobby {
        val playerId = player.id ?: getFreePlayerId()
        player.id = playerId

        return lobby.copy(
            playersWaiting = lobby.playersWaiting + (playerId to player),
        )
    }

    private fun lobbyWithPlayerRemoved(lobby: Lobby, playerId: PlayerId): Lobby {
        lobby.playersWaiting[playerId]?.resetId()

        return lobby.copy(
            playersWaiting = lobby.playersWaiting - playerId,
        )
    }
}
