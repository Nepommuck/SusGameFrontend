package edu.agh.susgame.front.gui.components.menu.components.createlobby

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import edu.agh.susgame.R
import edu.agh.susgame.dto.rest.model.LobbyPin
import edu.agh.susgame.front.gui.components.common.theme.Header
import edu.agh.susgame.front.gui.components.common.theme.MenuBackground
import edu.agh.susgame.front.gui.components.common.theme.MenuButton
import edu.agh.susgame.front.gui.components.common.theme.PaddingM
import edu.agh.susgame.front.gui.components.common.theme.PaddingXL
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.menu.components.createlobby.elements.CreateGameComp
import edu.agh.susgame.front.gui.components.menu.components.createlobby.elements.GameNameComp
import edu.agh.susgame.front.gui.components.menu.components.createlobby.elements.GamePinComp
import edu.agh.susgame.front.gui.components.menu.components.createlobby.elements.PlayersNumberComp
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute
import edu.agh.susgame.front.service.interfaces.LobbyService


private const val DEFAULT_PLAYERS_AMOUNT = 2

@Composable
fun CreateLobbyView(
    lobbyService: LobbyService,
    navController: NavController,
) {
    var gameName by remember { mutableStateOf(Translation.CreateGame.DEFAULT_GAME_NAME) }
    var showPassword by remember { mutableStateOf(value = false) }
    var selectedNumberOfPlayers by remember { mutableIntStateOf(DEFAULT_PLAYERS_AMOUNT) }
    var isGamePinEnabled by remember { mutableStateOf(false) }
    var howManyPinEnabled by remember { mutableIntStateOf(0) }
    var gamePinInputValue by remember { mutableStateOf("") }

    val audioId = when (selectedNumberOfPlayers) {
        2 -> R.raw.uncanny_2
        3 -> R.raw.uncanny_3
        4 -> R.raw.uncanny_4
        5 -> R.raw.uncanny_5
        6 -> R.raw.uncanny_6
        else -> {
            R.raw.uncanny_2
        }
    }

    val context = LocalContext.current

    LaunchedEffect(selectedNumberOfPlayers) {
        if (howManyPinEnabled > 15) {
            val mediaPlayer = MediaPlayer.create(context, audioId)

            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.prepareAsync()
            }

            mediaPlayer.start()
        }

    }

    MenuBackground()

    Column(
        Modifier
            .fillMaxSize()
            .padding(PaddingM),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            title = Translation.Menu.CREATE_GAME, style = TextStyler.TerminalXL
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.9f)
                        .padding(PaddingXL)
                        .weight(3f),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GameNameComp(
                        gameName = gameName,
                        onGameNameChange = { gameName = it }
                    )
                    GamePinComp(
                        gamePin = gamePinInputValue,
                        showPassword = showPassword,
                        isGamePinEnabled = isGamePinEnabled,
                        onGamePinChange = { gamePinInputValue = it },
                        onShowPasswordChange = { showPassword = it },
                        onGamePinEnabledChange = {
                            isGamePinEnabled = it
                            howManyPinEnabled += 1
                        },
                    )
                    PlayersNumberComp(
                        numOfPlayers = selectedNumberOfPlayers,
                        onGameTimeChange = { selectedNumberOfPlayers = it }
                    )
                    CreateGameComp(
                        gameName = gameName,
                        gamePin = if (isGamePinEnabled) LobbyPin(gamePinInputValue) else null,
                        selectedNumberOfPlayers = selectedNumberOfPlayers,
                        lobbyService = lobbyService,
                        navController = navController
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val resourceId = when (selectedNumberOfPlayers) {
                        2 -> R.drawable.uncanny2
                        3 -> R.drawable.uncanny3
                        4 -> R.drawable.uncanny4
                        5 -> R.drawable.uncanny5
                        6 -> R.drawable.uncanny6
                        else -> {
                            R.drawable.uncanny2
                        }
                    }

//                    val audioId = when (selectedNumberOfPlayers) {
//                        2 -> R.raw.uncanny_2
//                        3 -> R.raw.uncanny_3
//                        4 -> R.raw.uncanny_4
//                        5 -> R.raw.uncanny_5
//                        6 -> R.raw.uncanny_6
//                        else -> {
//                            R.raw.uncanny_2
//                        }
//                    }
//                    val mediaPlayer = MediaPlayer.create(LocalContext.current, audioId)
//                    mediaPlayer.start()


                    if (howManyPinEnabled > 15) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(id = resourceId),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }
    MenuButton(
        text = Translation.Button.GO_BACK,
        onClick = { navController.navigate(MenuRoute.MainMenu.route) },
        alignment = Alignment.BottomStart
    )
}