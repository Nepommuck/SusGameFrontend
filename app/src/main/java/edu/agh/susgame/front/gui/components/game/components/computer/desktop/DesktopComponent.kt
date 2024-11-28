package edu.agh.susgame.front.gui.components.game.components.computer.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun DesktopComponent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Blue)
    ) {
        Button(
            onClick = {},
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 50.dp, y = 50.dp),
        ) {
            Text("Icon-1")
        }

        Button(
            onClick = {},
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 150.dp, y = 150.dp)
        ) {
            Text("Icon-2")
        }

        Button(
            onClick = {},
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 150.dp, y = 50.dp)
        ) {
            Text("Icon-3")
        }
    }
}