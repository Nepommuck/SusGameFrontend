package edu.agh.susgame.front.ui.component

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.providers.interfaces.AppSettingsProvider
import edu.agh.susgame.front.providers.interfaces.AwaitingGamesProvider
import edu.agh.susgame.front.providers.interfaces.ServerMapProvider
import edu.agh.susgame.front.providers.mock.MockAppSettingsProvider
import edu.agh.susgame.front.providers.mock.MockAwaitingGamesProvider
import edu.agh.susgame.front.providers.mock.MockServerMapProvider
import edu.agh.susgame.front.ui.component.menu.navigation.NavBar
import edu.agh.susgame.front.ui.theme.SusGameTheme


class MainActivity : ComponentActivity() {
    private val serverMapProvider: ServerMapProvider = MockServerMapProvider()
    private val appSettingsProvider: AppSettingsProvider = MockAppSettingsProvider()
    private val awaitingGamesProvider: AwaitingGamesProvider = MockAwaitingGamesProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SusGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavBar(serverMapProvider, appSettingsProvider, awaitingGamesProvider)
                }
            }
        }
    }
}
