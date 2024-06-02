package edu.agh.susgame.front.providers.mock

import androidx.compose.ui.graphics.Color
import edu.agh.susgame.front.model.Player
import edu.agh.susgame.front.model.PlayerId
import edu.agh.susgame.front.model.game.Lobby
import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.providers.interfaces.LobbiesProvider
import edu.agh.susgame.front.settings.Configuration
import java.util.concurrent.CompletableFuture

class MockLobbiesProvider(mockDelayMs: Long? = null) : LobbiesProvider {
    private val delayMs = mockDelayMs ?: 0
    private var freeGameId: Int = 0
    private var freePlayerId: Int = 0
    private var currentLobbies: MutableMap<LobbyId, Lobby> = mutableMapOf()

    init {
        createCustomLobbies()
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
            currentLobbies[lobbyId]?.addPlayer(player, PlayerId(freePlayerId++))
        }

    override fun leave(lobbyId: LobbyId, playerId: PlayerId): CompletableFuture<Unit> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)
            currentLobbies[lobbyId]?.kickPlayer(playerId)
        }

    override fun createNewGame(
        gameName: String,
        gamePin: String,
        numOfPlayers: Int,
        gameTime: Int,
    ): CompletableFuture<LobbyId> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)
            val lobbyId = LobbyId(freeGameId++)
            currentLobbies.putIfAbsent(
                lobbyId,
                Lobby(
                    lobbyId,
                    gameName,
                    numOfPlayers,
                    gameTime,
                )
            )
            lobbyId
        }

    /**
     * This function is only for testing, it shows logic behind creating new lobbies
     */
    private fun createCustomLobbies() {
        // game 1
        val player0 = Player(
            name = "Player_0",
            color = Color.Red
        )
        val player1 = Player(
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
            )
        )

        currentLobbies[lobbyIdValue1]?.addPlayer(player0, PlayerId(freePlayerId++))
        currentLobbies[lobbyIdValue1]?.addPlayer(player1, PlayerId(freePlayerId++))


        // game 2
        val player2 = Player(
            name = "Player_0",
            color = Color.Red
        )
        val player3 = Player(
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
            )
        )
        currentLobbies[lobbyIdValue2]?.addPlayer(player2, PlayerId(freePlayerId++))
        currentLobbies[lobbyIdValue2]?.addPlayer(player3, PlayerId(freePlayerId++))
    }
}
