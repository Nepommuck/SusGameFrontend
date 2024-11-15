package edu.agh.susgame.front.config

import edu.agh.susgame.front.config.AppConfig.GameConfig
import edu.agh.susgame.front.config.AppConfig.GameConfig.GameTimeMinutes
import edu.agh.susgame.front.config.AppConfig.GameConfig.PlayersPerGame
import edu.agh.susgame.front.config.AppConfig.WebConfig
import kotlin.time.Duration.Companion.seconds

object Config : AppConfig {
    override val providers = ProviderType.Web

    override val webConfig = WebConfig(
        domain = "192.168.1.13",

        protocol = "http",
        port = 8080,
        timeout = 5.seconds,
    )

    override val gameConfig = GameConfig(
        maxPinLength = 6,
        gameTimeMinutes = GameTimeMinutes(
            min = 5,
            max = 15
        ),
        playersPerGame = PlayersPerGame(
            min = 2,
            max = 6
        ),
    )
}
