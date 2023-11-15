package movie.metropolis.app.screen.card

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.colorspace.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CardScreenState {

    var rotation by mutableFloatStateOf(0f)
    var blur by mutableStateOf(0.dp)
        private set
    var background by mutableStateOf(Color.Transparent)
        private set
    var offset by mutableStateOf(Int.MAX_VALUE.dp)
        private set
    private var closedOffset by mutableStateOf(Int.MAX_VALUE.dp)
    val isOpen: Boolean
        get() = offset != closedOffset

    fun updateOffset(offset: Dp) {
        if (closedOffset == Int.MAX_VALUE.dp) {
            this.offset = offset
            this.closedOffset = offset
        }
    }

    suspend fun settle() {
        val to = when (rotation % 360) {
            in 90f..<270f -> 180f
            in 270f..360f -> 360f
            else -> 0f
        }
        animate(rotation, to, animationSpec = tween(700)) { it, _ ->
            rotation = it
        }
    }

    suspend fun close() {
        coroutineScope {
            launch {
                animate(offset.value, closedOffset.value, animationSpec = tween(1000)) { it, _ ->
                    offset = it.dp
                }
            }
            launch {
                animate(rotation, rotation + 180f, animationSpec = tween(700)) { it, _ ->
                    rotation = it
                }
                rotation = 0f
            }
            launch {
                animate(blur.value, 0f, animationSpec = tween(700)) { it, _ ->
                    blur = it.dp
                }
            }
            launch {
                animate(
                    typeConverter = Color.VectorConverter(ColorSpaces.Srgb),
                    background,
                    Color.Transparent,
                    animationSpec = tween(1000)
                ) { it, _ ->
                    background = it
                }
            }
        }
    }

    suspend fun open() {
        coroutineScope {
            launch {
                animate(offset.value, 0f, animationSpec = tween(700)) { it, _ ->
                    offset = it.dp
                }
            }
            launch {
                animate(blur.value, 16f, animationSpec = tween(700)) { it, _ ->
                    blur = it.dp
                }
            }
            launch {
                animate(180f, 360f, animationSpec = tween(1400)) { it, _ ->
                    rotation = it
                }
                rotation %= 360f
            }
            launch {
                animate(
                    typeConverter = Color.VectorConverter(ColorSpaces.Srgb),
                    background,
                    Color.Black.copy(alpha = .5f),
                    animationSpec = tween(700)
                ) { it, _ ->
                    background = it
                }
            }
        }
    }

}