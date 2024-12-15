package edu.agh.susgame.front.service.web.rest

import com.google.gson.GsonBuilder
import edu.agh.susgame.dto.rest.games.GamesRest
import edu.agh.susgame.dto.rest.games.model.CreateGameApiResult
import edu.agh.susgame.dto.rest.games.model.GameCreationApiResponse
import edu.agh.susgame.dto.rest.games.model.GameCreationRequest
import edu.agh.susgame.dto.rest.games.model.GetAllGamesApiResult
import edu.agh.susgame.dto.rest.games.model.GetGameApiResult
import edu.agh.susgame.dto.rest.games.model.GetGameMapApiResult
import edu.agh.susgame.dto.rest.model.GameMapDTO
import edu.agh.susgame.dto.rest.model.LobbyDetails
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.LobbyPin
import edu.agh.susgame.dto.rest.model.LobbyRow
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
    private val gson = GsonBuilder().serializeNulls().create()

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
                val lobbies = gson.fromJson(
                    response.body?.string(),
                    Array<LobbyRow>::class.java,
                ).toList()

                GetAllGamesApiResult.Success(lobbies)
            }
        }

    override fun getGameDetails(
        gameId: LobbyId,
        gamePin: LobbyPin?,
    ): CompletableFuture<GetGameApiResult> =
        CompletableFuture.supplyAsync {
            val request = Request.Builder()
                .get()
                .url(
                    baseUrlBuilder()
                        .addPathSegment(gameId.value.toString())
                        .apply {
                            if (gamePin != null) {
                                addQueryParameter(name = "gamePin", value = gamePin.value)
                            }
                        }.build()
                ).build()

            val response = httpClient.newCall(request)
                .execute()

            when (response.code) {
                HttpURLConnection.HTTP_OK -> {
                    val lobbyDetails = gson.fromJson(
                        response.body?.string(),
                        LobbyDetails::class.java,
                    )
                    GetGameApiResult.Success(lobbyDetails)
                }

                GetGameApiResult.DoesNotExist.responseCode -> GetGameApiResult.DoesNotExist

                GetGameApiResult.InvalidPin.responseCode -> GetGameApiResult.InvalidPin

                else -> GetGameApiResult.OtherError
            }
        }

    override fun getGameMap(
        gameId: LobbyId,
    ): CompletableFuture<GetGameMapApiResult> = CompletableFuture.supplyAsync {
        val request = Request.Builder()
            .get()
            .url(
                baseUrlBuilder()
                    .addPathSegment("map")
                    .addPathSegment(gameId.value.toString())
                    .build()
            ).build()

        val response = httpClient.newCall(request)
            .execute()

        when (response.code) {
            HttpURLConnection.HTTP_OK -> GetGameMapApiResult.Success(
                gameMap = gson.fromJson(
                    response.body?.string(),
                    GameMapDTO::class.java,
                )
            )

            HttpURLConnection.HTTP_NOT_FOUND -> GetGameMapApiResult.GameDoesNotExist

            HttpURLConnection.HTTP_BAD_REQUEST -> GetGameMapApiResult.GameNotYetStarted

            else -> GetGameMapApiResult.OtherError
        }
    }

    override fun createGame(
        gameName: String,
        maxNumberOfPlayers: Int,
        gamePin: LobbyPin?,
    ): CompletableFuture<CreateGameApiResult> = CompletableFuture.supplyAsync {
        val gameCreationRequest = GameCreationRequest(gameName, maxNumberOfPlayers, gamePin)

        val body = gson.toJson(gameCreationRequest)
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
                val gameCreationApiResponse = gson.fromJson(
                    response.body?.string(),
                    GameCreationApiResponse::class.java,
                )
                CreateGameApiResult.Success(
                    createdLobbyId = gameCreationApiResponse.lobbyId,
                )
            }

            else ->
                CreateGameApiResult.OtherError
        }
    }
}
