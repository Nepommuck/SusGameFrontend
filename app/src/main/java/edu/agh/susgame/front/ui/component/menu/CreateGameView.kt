package edu.agh.susgame.front.ui.component.menu

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
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
import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.ui.component.common.Header
import edu.agh.susgame.front.ui.component.menu.navigation.MenuRoute
import edu.agh.susgame.front.ui.theme.PaddingL

import java.util.Locale
import kotlin.math.roundToInt


private const val maxPinLength = 6
private const val maxPlayersAmount = 6
private const val minPlayersAmount = 2
private const val defaultPlayersAmount = 4
private const val minGameTime = 5f
private const val maxGameTime = 15f
private const val defaultGameTime = 10
@Composable
fun CreateGameView(
    navController: NavController,
) {
    var gameName by remember { mutableStateOf(Translation.CreateGame.DEFAULT_GAME_NAME) }
    var gamePIN by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(value = false) }
    var selectedNumber by remember { mutableIntStateOf(defaultPlayersAmount) }
    var gameTime by remember { mutableIntStateOf(defaultGameTime) }
    Column( Modifier.padding(PaddingL)) {
        Column(
            Modifier
                .padding(PaddingL)
                .weight(1f)
        ) {
            Header(title = Translation.Menu.CREATE_GAME)

            // entering game name
            OutlinedTextField(
                label = { Text(Translation.CreateGame.ENTER_GAME_NAME) },
                value = gameName,
                onValueChange = { gameName = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // entering game pin
            OutlinedTextField(
                label = { Text(Translation.CreateGame.ENTER_GAME_PIN) },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
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
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                value = gamePIN,
                onValueChange = { if (it.length <= maxPinLength) gamePIN = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // entering maximal amount of players
            Row(
                Modifier.padding(PaddingL).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = Translation.CreateGame.AMOUNT_OF_PLAYERS
                )
                Spacer(modifier = Modifier.width(PaddingL))
                Button(
                    onClick = { selectedNumber -= 1 },
                    enabled = selectedNumber > minPlayersAmount
                ) {
                    Text(text = "-")
                }
                Text(
                    text = "$selectedNumber",
                    modifier = Modifier.padding(PaddingL),
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = { selectedNumber += 1 },
                    enabled = selectedNumber < maxPlayersAmount
                ) {
                    Text(text = "+")
                }
            }

            // entering game time
            Row(
                Modifier.padding(PaddingL).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    modifier = Modifier.weight(0.2f),
                    text = Translation.CreateGame.GAME_TIME
                )
                Text(
                    text = String.format(
                        Locale.getDefault(),
                        "%02d",
                        gameTime
                    ) + Translation.CreateGame.MINUTES,
                )
                Spacer(modifier = Modifier.width(PaddingL))
                Slider(
                    modifier = Modifier.weight(0.3f),
                    value = gameTime.toFloat(),
                    onValueChange = { newValue ->
                        gameTime = newValue.toInt()
                    },
                    valueRange = minGameTime..maxGameTime,
                    steps = (maxGameTime- minGameTime-1).roundToInt()
                )
            }
            // button to create new game
            Row(
                Modifier.padding(PaddingL).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val context = LocalContext.current
                Button(onClick = {

                    if (gameName == "") Toast.makeText( context,Translation.Toasts.CREATE_NO_GAME_NAME, Toast.LENGTH_SHORT).show()
                    else Toast.makeText( context,Translation.Toasts.CREATE_SUCCESS, Toast.LENGTH_SHORT).show()
//                    navController.navigate(MenuRoute.MainMenu.route)
                }) {
                    Text(text = Translation.Button.CREATE)
                }
            }
        }
        Button(onClick = {
            navController.navigate(MenuRoute.MainMenu.route)
        }) {
            Text(text = Translation.Button.GO_BACK)
        }
    }
}

