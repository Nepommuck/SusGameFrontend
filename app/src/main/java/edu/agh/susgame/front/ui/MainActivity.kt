package edu.agh.susgame.front.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import edu.agh.susgame.front.Config
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.LobbyService
import edu.agh.susgame.front.service.interfaces.ServerMapProvider
import edu.agh.susgame.front.service.mock.MockGameService
import edu.agh.susgame.front.service.mock.MockLobbyService
import edu.agh.susgame.front.service.mock.MockServerMapProvider
import edu.agh.susgame.front.service.web.WebGameService
import edu.agh.susgame.front.service.web.WebLobbyService
import edu.agh.susgame.front.service.web.rest.games.GamesRest
import edu.agh.susgame.front.ui.component.menu.navigation.MenuNavigationHostComponent
import edu.agh.susgame.front.ui.theme.PaddingL
import edu.agh.susgame.front.ui.theme.SusGameTheme
import edu.agh.susgame.front.util.ProviderType


class MainActivity : ComponentActivity() {
    private data class Services(
        val lobbyService: LobbyService,
        val webGameService: GameService,
        val serverMapProvider: ServerMapProvider,
    )

    private val gamesRest = GamesRest(webConfig = Config.webConfig)

    private val services = when (Config.providers) {
        ProviderType.MockLocal -> {
            val mockLobbiesProvider = MockLobbyService(mockDelayMs = 1_000)
            Services(
                mockLobbiesProvider,
                MockGameService(mockLobbiesProvider),
                MockServerMapProvider(),
            )
        }

        ProviderType.Web -> Services(
            WebLobbyService(gamesRest),
            WebGameService(webConfig = Config.webConfig),
            MockServerMapProvider(),
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

                    Box(modifier = Modifier.padding(PaddingL)) {
                        MenuNavigationHostComponent(
                            navController,
                            services.serverMapProvider,
                            services.lobbyService,
                            services.webGameService,
                        )
                    }
                }
            }
        }
    }
}
