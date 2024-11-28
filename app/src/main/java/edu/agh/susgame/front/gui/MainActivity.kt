package edu.agh.susgame.front.gui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import edu.agh.susgame.dto.rest.games.GamesRest
import edu.agh.susgame.front.config.AppConfig
import edu.agh.susgame.front.config.utils.ProviderType
import edu.agh.susgame.front.gui.components.common.theme.SusGameTheme
import edu.agh.susgame.front.gui.components.menu.navigation.MenuNavigationHost
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.LobbyService
import edu.agh.susgame.front.service.mock.MockGameService
import edu.agh.susgame.front.service.mock.MockLobbyService
import edu.agh.susgame.front.service.web.IpAddressProvider
import edu.agh.susgame.front.service.web.rest.GamesRestImpl
import edu.agh.susgame.front.service.web.webservices.WebGameService
import edu.agh.susgame.front.service.web.webservices.WebLobbyService


class MainActivity : ComponentActivity() {
    private data class Services(
        val lobbyService: LobbyService,
        val gameService: GameService,
    )

    private val ipAddressProvider: IpAddressProvider = IpAddressProvider()

    private val gamesRest: GamesRest = GamesRestImpl(
        webConfig = AppConfig.webConfig,
        ipAddressProvider = ipAddressProvider
    )

    private val services = when (AppConfig.providers) {
        ProviderType.LOCAL -> {
            val mockLobbiesProvider = MockLobbyService(mockDelayMs = 0)
            Services(
                mockLobbiesProvider,
                MockGameService(mockLobbiesProvider),
            )
        }

        ProviderType.WEB -> Services(
            WebLobbyService(gamesRest),
            WebGameService(AppConfig.webConfig, ipAddressProvider),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SusGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    Box {
                        MenuNavigationHost(
                            navController,
                            services.lobbyService,
                            services.gameService,
                            ipAddressProvider,
                        )
                    }
                }
            }
        }
    }
}
