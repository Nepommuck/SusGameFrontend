package edu.agh.susgame.front

import edu.agh.susgame.front.util.AppConfig
import edu.agh.susgame.front.util.ProviderType
import kotlin.time.Duration.Companion.seconds

object Config : AppConfig {
    override val providers = ProviderType.MockLocal

    override val webConfig = AppConfig.WebConfig(
        protocol = "http",
        // NOTE: This value must be changed in `app/src/main/res/xml/network_security_config.xml` as well
        domain = "192.168.0.105",
        port = 8080,
        timeout = 5.seconds,
    )

    override val gameConfig = AppConfig.GameConfig(
        maxPinLength = 6,
        gameTimeMinutes = AppConfig.GameConfig.GameTimeMinutes(
            min = 5,
            max = 15
        ),
        playersPerGame = AppConfig.GameConfig.PlayersPerGame(
            min = 2,
            max = 6
        ),
    )
}
