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
import edu.agh.susgame.front.providers.interfaces.GameService
import edu.agh.susgame.front.providers.interfaces.LobbiesProvider
import edu.agh.susgame.front.providers.interfaces.ServerMapProvider
import edu.agh.susgame.front.providers.mock.MockLobbiesProvider
import edu.agh.susgame.front.providers.mock.MockServerMapProvider
import edu.agh.susgame.front.providers.web.socket.WebGameService
import edu.agh.susgame.front.providers.web.WebLobbiesService
import edu.agh.susgame.front.providers.web.rest.games.GamesRest
import edu.agh.susgame.front.ui.component.menu.navigation.MenuNavigationHostComponent
import edu.agh.susgame.front.ui.theme.PaddingL
import edu.agh.susgame.front.ui.theme.SusGameTheme
import edu.agh.susgame.front.util.ProviderType


class MainActivity : ComponentActivity() {
    private val gamesRest = GamesRest(webConfig = Config.webConfig)

    private val lobbiesProvider: LobbiesProvider = when (Config.providers) {
        ProviderType.MockLocal -> MockLobbiesProvider(mockDelayMs = 1_000)
        ProviderType.Web -> WebLobbiesService(
            gamesRest
        )
    }

    private val serverMapProvider: ServerMapProvider = MockServerMapProvider()

    private val webGameService: GameService = WebGameService(webConfig = Config.webConfig)

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
                            serverMapProvider,
                            lobbiesProvider,
                            webGameService,
                        )
                    }
                }
            }
        }
    }
}
