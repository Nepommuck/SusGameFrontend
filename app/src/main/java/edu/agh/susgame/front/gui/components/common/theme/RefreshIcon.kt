package edu.agh.susgame.front.gui.components.common.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.agh.susgame.R
import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.front.service.interfaces.LobbyService

@Composable
fun RefreshIcon(
    onRefreshClicked: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .requiredSize(110.dp)
                .padding(40.dp)
                .align(Alignment.TopEnd)
                .clickable {
                    onRefreshClicked()
                }
        ) {
            Image(
                painter = painterResource(R.drawable.power),
                contentDescription = "Loading Animation",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.5f),
//                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}