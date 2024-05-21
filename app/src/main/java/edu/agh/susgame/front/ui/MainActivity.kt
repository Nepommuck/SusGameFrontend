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
import edu.agh.susgame.front.providers.interfaces.AwaitingGamesProvider
import edu.agh.susgame.front.providers.interfaces.ServerMapProvider
import edu.agh.susgame.front.providers.mock.MockServerMapProvider
import edu.agh.susgame.front.providers.web.LocalWebConfig
import edu.agh.susgame.front.providers.web.WebAwaitingGamesProvider
import edu.agh.susgame.front.providers.web.rest.GamesRest
import edu.agh.susgame.front.ui.component.menu.navigation.MenuNavigationHostComponent
import edu.agh.susgame.front.ui.theme.PaddingL
import edu.agh.susgame.front.ui.theme.SusGameTheme


class MainActivity : ComponentActivity() {
    private val serverMapProvider: ServerMapProvider = MockServerMapProvider(
//        mockDelayMs = 1_000,
    )

    //    private val awaitingGamesProvider: AwaitingGamesProvider = MockAwaitingGamesProvider(
////        mockDelayMs = 1_000,
//    )
    private val gamesRest = GamesRest(webConfig = LocalWebConfig)
    private val awaitingGamesProvider: AwaitingGamesProvider = WebAwaitingGamesProvider(
        gamesRest
    )

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
                            awaitingGamesProvider,
                        )
                    }
                }
            }
        }
    }
}
