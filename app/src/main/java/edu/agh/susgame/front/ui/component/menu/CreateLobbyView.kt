package edu.agh.susgame.front.ui.component.menu

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import edu.agh.susgame.front.navigation.MenuRoute
import edu.agh.susgame.front.providers.interfaces.LobbiesProvider
import edu.agh.susgame.front.providers.interfaces.LobbiesProvider.CreateNewGameResult
import edu.agh.susgame.front.settings.Configuration
import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.ui.component.common.Header
import edu.agh.susgame.front.ui.theme.PaddingL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.roundToInt


private const val DEFAULT_PLAYERS_AMOUNT = 4
private const val DEFAULT_GAME_TIME = 10

@Composable
fun CreateLobbyView(
    lobbiesProvider: LobbiesProvider,
    navController: NavController,
) {
    var gameName by remember { mutableStateOf(Translation.CreateGame.DEFAULT_GAME_NAME) }
    var gamePin by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(value = false) }
    var selectedNumberOfPlayers by remember { mutableIntStateOf(DEFAULT_PLAYERS_AMOUNT) }
    var gameTime by remember { mutableIntStateOf(DEFAULT_GAME_TIME) }
    var isGamePinEnabled by remember { mutableStateOf(false) }
    Column(Modifier.padding(PaddingL)) {
        Column(
            Modifier
                .padding(PaddingL)
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Header(
                title = Translation.Menu.CREATE_GAME
            )

            // entering game name
            OutlinedTextField(
                label = {
                    Text(
                        text = Translation.CreateGame.ENTER_GAME_NAME + ": "
                    )
                },
                value = gameName,
                onValueChange = { gameName = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // entering game pin
            Row(
                Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedTextField(
                    label = {
                        Text(
                            Translation.CreateGame.ENTER_GAME_PIN
                        )
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        if (isGamePinEnabled) {
                            if (showPassword) {
                                IconButton(onClick = { showPassword = false }) {
                                    Icon(
                                        imageVector = Icons.Filled.Visibility,
                                        contentDescription = "hide_password"
                                    )
                                }
                            } else {
                                IconButton(
                                    onClick = { showPassword = true }) {
                                    Icon(
                                        imageVector = Icons.Filled.VisibilityOff,
                                        contentDescription = "hide_password"
                                    )
                                }
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    value = gamePin,
                    onValueChange = { if (it.length <= Configuration.MAX_PIN_LENGTH) gamePin = it },
                    singleLine = true,
                    enabled = isGamePinEnabled,
                    modifier = Modifier.weight(1f),
                )
                Checkbox(
                    checked = isGamePinEnabled,
                    onCheckedChange = {
                        isGamePinEnabled = it
                        gamePin = ""
                    }
                )
            }

            // entering maximal amount of players
            Row(
                Modifier
                    .padding(PaddingL)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "${Translation.CreateGame.AMOUNT_OF_PLAYERS}:"
                )
                Spacer(modifier = Modifier.width(PaddingL))
                Button(
                    onClick = { selectedNumberOfPlayers -= 1 },
                    enabled = selectedNumberOfPlayers > Configuration.MIN_PLAYERS_PER_GAME
                ) {
                    Text(
                        text = "-"
                    )
                }
                Text(
                    text = "$selectedNumberOfPlayers",
                    modifier = Modifier.padding(PaddingL),
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = { selectedNumberOfPlayers += 1 },
                    enabled = selectedNumberOfPlayers < Configuration.MAX_PLAYERS_PER_GAME
                ) {
                    Text(
                        text = "+"
                    )
                }
            }

            // entering game time
            Row(
                Modifier
                    .padding(PaddingL)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    modifier = Modifier.weight(0.2f),
                    text = "${Translation.CreateGame.GAME_TIME}:"
                )
                Text(
                    text = String.format(
                        locale = Locale.getDefault(),
                        format = "%02d",
                        gameTime
                    ) + " ${Translation.CreateGame.MINUTES} "
                )
                Spacer(
                    modifier = Modifier.width(PaddingL)
                )
                Slider(
                    modifier = Modifier.weight(0.3f),
                    value = gameTime.toFloat(),
                    onValueChange = { newValue ->
                        gameTime = newValue.toInt()
                    },
                    valueRange = Configuration.MIN_GAME_TIME..Configuration.MAX_GAME_TIME,
                    steps = (Configuration.MAX_GAME_TIME - Configuration.MIN_GAME_TIME - 1).roundToInt()
                )
            }

            // button to create new game
            Row(
                Modifier
                    .padding(PaddingL)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val context = LocalContext.current
                Button(onClick = {
                    createGameHandler(
                        gameName,
                        context,
                        lobbiesProvider,
                        navController,
                        gamePin,
                        selectedNumberOfPlayers,
                        gameTime
                    )
                }) {
                    Text(
                        text = Translation.Button.CREATE
                    )
                }
            }
        }

        // go_back button
        Button(onClick = {
            navController.navigate(MenuRoute.MainMenu.route)
        }) {
            Text(text = Translation.Button.GO_BACK)
        }
    }
}

private fun createGameHandler(
    gameName: String,
    androidContext: Context,
    provider: LobbiesProvider,
    navController: NavController,
    gamePin: String,
    numOfPlayers: Int,
    gameTime: Int,
) {
    if (gameName == "") Toast.makeText(
        androidContext,
        Translation.CreateGame.CREATE_NO_GAME_NAME,
        Toast.LENGTH_SHORT,
    ).show()
    else {
        provider.createNewGame(gameName, gamePin, numOfPlayers, gameTime)
            .thenAccept { creationResult ->
                val toastMessage = when (creationResult) {
                    is CreateNewGameResult.Success ->
                        Translation.CreateGame.CREATE_SUCCESS

                    CreateNewGameResult.NameAlreadyExists ->
                        Translation.CreateGame.CREATE_NAME_ALREADY_EXISTS

                    CreateNewGameResult.OtherError ->
                        Translation.CreateGame.CREATE_OTHER_ERROR
                }

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(
                        androidContext,
                        toastMessage,
                        Toast.LENGTH_SHORT,
                    ).show()

                    if (creationResult is CreateNewGameResult.Success) {
                        navController.navigate(
                            MenuRoute.Lobby.routeWithArgument(lobbyId = creationResult.lobbyId),
                        )
                    }
                }
            }
    }
}
