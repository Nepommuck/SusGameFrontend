package edu.agh.susgame.front.ui.component.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.ui.component.common.Header
import edu.agh.susgame.front.ui.component.menu.navigation.MenuRoute
import edu.agh.susgame.front.ui.theme.PaddingL

private const val maxPinLength = 6
private const val maxPlayersAmount = 6
private const val minPlayersAmount = 2
@Composable
fun CreateGameView(
    navController: NavController,
) {
    var gameName by remember { mutableStateOf("") }
    var gamePIN by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(value = false) }
    var selectedNumber by remember { mutableIntStateOf(2) }
    var gameTime by remember { mutableStateOf(10) }
    Column( Modifier.padding(PaddingL)) {
        Column(
            Modifier
                .padding(PaddingL)
                .weight(1f)
        ) {
            Header(title = Translation.Menu.CREATE_GAME)

            // entering game name
            OutlinedTextField(
                label = { Text(Translation.Menu.ENTER_GAME_NAME) },
                value = gameName,
                onValueChange = { gameName = it},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // entering game pin
            OutlinedTextField(
                label = { Text(Translation.Menu.ENTER_GAME_PIN) },
                visualTransformation =  if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
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
                onValueChange = { if (it.length <= maxPinLength) gamePIN = it},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Row(){
                Column(verticalArrangement = Arrangement.SpaceEvenly){
                    Text(
                        text = "Liczba graczy:",
                    )
                    Text(
                        text = "Czas gry:",
                    )
                }
                Column(verticalArrangement = Arrangement.SpaceEvenly) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
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
                    Slider(
                        value = gameTime.toFloat(),
                        onValueChange = { newValue ->
                            gameTime = newValue.toInt()
                        },
                        valueRange = 1f..60f, // Zakres czasu gry od 1 do 60 minut
                        steps = 59,
                    )

                }
            }




            // TODO GAME-52
            Text(text = "TODO GAME-52")
        }
        Button(onClick = {
            navController.navigate(MenuRoute.MainMenu.route)
        }) {
            Text(text = Translation.Button.GO_BACK)
        }
    }
}

