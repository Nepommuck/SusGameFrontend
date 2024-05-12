package edu.agh.susgame.front.ui.component.menu

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import edu.agh.susgame.front.model.game.AwaitingGame.Companion.freeId
import edu.agh.susgame.front.model.game.GameId
import edu.agh.susgame.front.providers.interfaces.AwaitingGamesProvider
import edu.agh.susgame.front.settings.Configuration
import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.ui.component.common.Header
import edu.agh.susgame.front.ui.component.menu.navigation.MenuRoute
import edu.agh.susgame.front.ui.theme.PaddingL
import edu.agh.susgame.front.model.PlayerNickname
import java.util.Locale
import kotlin.math.roundToInt


private const val defaultPlayersAmount = 4
private const val defaultGameTime = 10

@Composable
fun CreateGameView(
    awaitingGamesProvider: AwaitingGamesProvider,
    navController: NavController,
) {
    var gameName by remember { mutableStateOf(Translation.CreateGame.DEFAULT_GAME_NAME) }
    var gamePIN by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(value = false) }
    var selectedNumberOfPlayers by remember { mutableIntStateOf(defaultPlayersAmount) }
    var gameTime by remember { mutableIntStateOf(defaultGameTime) }
    var checkedBool by remember { mutableStateOf(false) }
    Column(Modifier.padding(PaddingL)) {
        Column(
            Modifier
                .padding(PaddingL)
                .weight(1f)
                .verticalScroll(ScrollState(0))
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
                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedTextField(
                    label = {
                        Text(
                            Translation.CreateGame.ENTER_GAME_PIN
                        )
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        if (checkedBool) {
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
                    value = gamePIN,
                    onValueChange = { if (it.length <= Configuration.MaxPinLength) gamePIN = it },
                    singleLine = true,
                    enabled = checkedBool,
                    modifier = Modifier.weight(1f),

                )
                Checkbox(
                    checked = checkedBool,
                    onCheckedChange = {
                        checkedBool = it
                        gamePIN = ""
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
                    enabled = selectedNumberOfPlayers > Configuration.MinPlayersAmount
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
                    enabled = selectedNumberOfPlayers < Configuration.MaxPlayersPerGame
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
                    valueRange = Configuration.MinGameTime..Configuration.MaxGameTime,
                    steps = (Configuration.MaxGameTime - Configuration.MinGameTime - 1).roundToInt()
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
                    createGameHandler(gameName, context, awaitingGamesProvider, navController, gamePIN, selectedNumberOfPlayers,gameTime)
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


private fun createGameHandler(gameName: String, context: Context, provider: AwaitingGamesProvider, navController: NavController, gamePin: String, numOfPlayers: Int, gameTime: Int) {
    if (gameName == "") Toast.makeText(
        context,
        Translation.CreateGame.CREATE_NO_GAME_NAME,
        Toast.LENGTH_SHORT
    ).show()
    else {
        val newId = freeId++
        provider.createNewGame(GameId(newId), gameName, gamePin, numOfPlayers, gameTime)
        Toast.makeText(
            context,
            Translation.CreateGame.CREATE_SUCCESS,
            Toast.LENGTH_SHORT
        ).show()
        navController.navigate("${MenuRoute.AwaitingGame.route}/$newId")
    }

}



