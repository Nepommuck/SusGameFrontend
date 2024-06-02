package edu.agh.susgame.front

import edu.agh.susgame.front.util.AppConfig
import edu.agh.susgame.front.util.ProviderType
import kotlin.time.Duration.Companion.seconds

object Config : AppConfig {
    override val providers = ProviderType.Web

    override val webConfig = AppConfig.WebConfig(
        protocol = "http",
        // NOTE: This value must be changed in `app/src/main/res/xml/network_security_config.xml` as well
        domain = "192.168.0.105",
        port = 8080,

        timeout = 5.seconds,
    )
}
