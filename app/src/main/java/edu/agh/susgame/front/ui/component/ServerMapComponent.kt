package edu.agh.susgame.front.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.map.ServerMapProvider
import edu.agh.susgame.front.ui.util.ZoomState

@Composable
fun ServerMapComponent(serverMapProvider: ServerMapProvider) {
    val mapState = serverMapProvider.getServerMapState()
    val zoomState = remember {
        ZoomState(
            maxZoomIn = 2f,
            maxZoomOut = 0.5f,
            totalSize = mapState.value.mapSize,
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clipToBounds()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    zoomState.scale(zoom)
                    zoomState.move(pan)
                }
            }
            .background(Color.Red)
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer(
                    scaleX = zoomState.scaleValue(),
                    scaleY = zoomState.scaleValue(),
                    translationX = zoomState.translationX(),
                    translationY = zoomState.translationY(),
                    clip = false
                )
            //  TODO Make this box bigger than the outer one
            //   .size(1200.dp)
//                .background(Color.Red)
        ) {
            Column {
                Text(zoomState.scaleValue().toString(), color = Color.Black)
                mapState.value.serves.forEach { server ->
                    Box(
                        modifier = Modifier.offset(server.position.x.dp, server.position.y.dp)
                    ) {
                        Button(onClick = { /* Do something */ }) {
                            Text(text = server.name)
                        }
                    }
                }
            }
        }
    }
}
