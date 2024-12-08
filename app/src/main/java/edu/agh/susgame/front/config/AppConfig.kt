package edu.agh.susgame.front.config

import edu.agh.susgame.front.config.utils.Configuration
import edu.agh.susgame.front.config.utils.Configuration.GameConfig
import edu.agh.susgame.front.config.utils.Configuration.GameConfig.GameTimeMinutes
import edu.agh.susgame.front.config.utils.Configuration.GameConfig.PlayersPerGame
import edu.agh.susgame.front.config.utils.Configuration.WebConfig
import edu.agh.susgame.front.config.utils.ProviderType

object AppConfig : Configuration {
    override val providers = ProviderType.WEB

    override val webConfig = WebConfig(
        // Very useful for development purposes
        defaultIpAddress = "192.168.1.26",
        // defaultIpAddress = null,

        protocol = "http",
        port = 8080,
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
