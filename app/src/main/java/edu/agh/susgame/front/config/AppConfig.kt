package edu.agh.susgame.front.config

import kotlin.time.Duration

enum class ProviderType {
    MockLocal,
    Web,
}


interface AppConfig {
    val providers: ProviderType

    class WebConfig(
        val protocol: String,
        /**
         * NOTE: This value must be changed in `app/src/main/res/xml/network_security_config.xml` as well
         */
        val domain: String,
        val port: Int,

        val timeout: Duration,
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

    val gameConfig : GameConfig
}

