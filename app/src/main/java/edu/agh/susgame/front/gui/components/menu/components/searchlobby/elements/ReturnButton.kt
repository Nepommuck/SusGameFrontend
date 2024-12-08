package edu.agh.susgame.front.gui.components.menu.components.searchlobby.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute

@Composable
fun ReturnButton(
    navController: NavController
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
//                .requiredSize(100.dp)
                .padding(16.dp)
                .align(Alignment.BottomStart)
        ) {
            Button(
                onClick = { navController.navigate(MenuRoute.MainMenu.route) },
                modifier = Modifier.requiredWidthIn(min = 120.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0),
                    disabledContainerColor = Color(0)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = Translation.Button.GO_BACK, style = TextStyler.TerminalM)
            }
        }
    }
}