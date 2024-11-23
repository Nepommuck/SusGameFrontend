package edu.agh.susgame.front.config.utils

interface Configuration {
    val providers: ProviderType

    class WebConfig(
        val protocol: String,
        val defaultIpAddress: String?,
        val port: Int,
    )

    val webConfig: WebConfig

    class GameConfig(
        val maxPinLength: Int,
        val gameTimeMinutes: GameTimeMinutes,
        val playersPerGame: PlayersPerGame,
    ) {
        data class GameTimeMinutes(
            val min: Int,
            val max: Int,
        )

        data class PlayersPerGame(
            val min: Int,
            val max: Int,
        )
    }

    val gameConfig: GameConfig
}

