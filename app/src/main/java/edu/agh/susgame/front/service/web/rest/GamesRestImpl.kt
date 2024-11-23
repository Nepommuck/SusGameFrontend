package edu.agh.susgame.front.service.web.rest

import com.google.gson.Gson
import edu.agh.susgame.dto.rest.games.GamesRest
import edu.agh.susgame.dto.rest.games.model.CreateGameApiResult
import edu.agh.susgame.dto.rest.games.model.GameCreationApiResponse
import edu.agh.susgame.dto.rest.games.model.GameCreationRequest
import edu.agh.susgame.dto.rest.games.model.GetAllGamesApiResult
import edu.agh.susgame.dto.rest.games.model.GetGameApiResult
import edu.agh.susgame.dto.rest.games.model.GetGameMapApiResult
import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.front.config.utils.Configuration.WebConfig
import edu.agh.susgame.front.service.web.IpAddressProvider
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.HttpURLConnection
import java.util.concurrent.CompletableFuture


class GamesRestImpl(
    override val webConfig: WebConfig,
    override val ipAddressProvider: IpAddressProvider
) : GamesRest, AbstractRest(endpointName = "games") {

    override fun getAllGames(): CompletableFuture<GetAllGamesApiResult> =
        CompletableFuture.supplyAsync {
            val request = Request.Builder()
                .get()
                .url(baseUrlBuilder().build())
                .build()

            val response = httpClient.newCall(request)
                .execute()

            if (!response.isSuccessful)
                GetAllGamesApiResult.Error
            else {
                val lobbies = Gson().fromJson(
                    response.body?.string(),
                    Array<Lobby>::class.java,
                ).toList()

                GetAllGamesApiResult.Success(lobbies)
            }
        }

    override fun getGame(gameId: LobbyId): CompletableFuture<GetGameApiResult> =
        CompletableFuture.supplyAsync {
            val request = Request.Builder()
                .get()
                .url(
                    baseUrlBuilder()
                        .addPathSegment(gameId.value.toString())
                        .build()
                ).build()

            val response = httpClient.newCall(request)
                .execute()

            when (response.code) {
                HttpURLConnection.HTTP_NOT_FOUND -> GetGameApiResult.DoesNotExist

                HttpURLConnection.HTTP_OK -> {
                    val lobby = Gson().fromJson(
                        response.body?.string(),
                        Lobby::class.java,
                    )
                    GetGameApiResult.Success(lobby)
                }

                else -> GetGameApiResult.OtherError
            }
        }

    override fun getGameMap(gameId: LobbyId): CompletableFuture<GetGameMapApiResult> {
        TODO("Not yet implemented")
    }

    override fun createGame(
        gameName: String,
        maxNumberOfPlayers: Int,
        gamePin: String?,
    ): CompletableFuture<CreateGameApiResult> = CompletableFuture.supplyAsync {
        val gameCreationRequest = GameCreationRequest(gameName, maxNumberOfPlayers, gamePin)

        val body = Gson().toJson(gameCreationRequest)
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .post(body)
            .url(baseUrlBuilder().build())
            .build()

        val response = httpClient.newCall(request)
            .execute()

        when (response.code) {
            HttpURLConnection.HTTP_CONFLICT ->
                CreateGameApiResult.NameAlreadyExists

            HttpURLConnection.HTTP_CREATED -> {
                val gameCreationApiResponse = Gson().fromJson(
                    response.body?.string(),
                    GameCreationApiResponse::class.java,
                )
                CreateGameApiResult.Success(
                    createdLobbyId = LobbyId(gameCreationApiResponse.gameId),
                )
            }

            else ->
                CreateGameApiResult.OtherError
        }
    }
}
