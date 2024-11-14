package edu.agh.susgame.front

import edu.agh.susgame.front.utils.AppConfig
import edu.agh.susgame.front.utils.AppConfig.*
import edu.agh.susgame.front.utils.AppConfig.GameConfig.*
import edu.agh.susgame.front.utils.ProviderType
import kotlin.time.Duration.Companion.seconds

object Config : AppConfig {
    override val providers = ProviderType.MockLocal

    override val webConfig = WebConfig(
        domain = "192.168.1.26",

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
