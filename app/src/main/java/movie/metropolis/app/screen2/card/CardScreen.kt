package movie.metropolis.app.screen2.card

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.colorspace.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import movie.metropolis.app.R
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.screen.profile.component.MembershipViewParameter
import movie.metropolis.app.screen2.card.component.CardContentBack
import movie.metropolis.app.screen2.card.component.CardContentFront
import movie.metropolis.app.screen2.card.component.FlippableCard
import movie.style.Barcode
import movie.style.layout.PreviewLayout
import movie.style.modifier.surface
import movie.style.modifier.vertical

private val CinemaCityColor = Color(0xFFE78838)

@Composable
fun CardScreen(
    membership: MembershipView,
    modifier: Modifier = Modifier,
    state: CardScreenState = remember { CardScreenState() },
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(state.background)
            .onSizeChanged { state.updateOffset(with(density) { it.height.toDp() }) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragCancel = { scope.launch { state.settle() } },
                    onDragEnd = { scope.launch { state.settle() } }
                ) { change, amount ->
                    change.consume()
                    val amount = if (amount < 0) 360 + amount else amount
                    state.rotation += amount
                    if (state.rotation > 360f)
                        state.rotation -= 360f
                }
            },
        contentAlignment = Alignment.Center
    ) {
        val logo = @Composable {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_cinemacity),
                contentDescription = null,
                colorFilter = ColorFilter.tint(CinemaCityColor)
            )
        }
        val name = @Composable {
            Text("CLUB")
        }
        FlippableCard(
            modifier = Modifier
                .offset(y = state.offset)
                .fillMaxWidth()
                .vertical(),
            rotation = state.rotation,
            key = membership.cardNumber,
            container = {
                Box(
                    modifier = Modifier.surface(
                        color = Color.Black,
                        shape = RoundedCornerShape(16.dp),
                        elevation = 16.dp,
                        shadowColor = CinemaCityColor
                    ),
                    propagateMinConstraints = true
                ) {
                    CompositionLocalProvider(LocalContentColor provides Color.White) {
                        it()
                    }
                }
            },
            front = {
                CardContentFront(logo = logo, name = name)
            },
            back = {
                CardContentBack(logo = logo, name = name, code = {
                    Barcode(
                        modifier = Modifier.background(Color.White),
                        code = membership.cardNumber,
                        format = BarcodeFormat.CODE_128
                    )
                })
            }
        )
    }
}

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

@Preview
@Composable
private fun CardScreenPreview() = PreviewLayout {
    val membership = remember { MembershipViewParameter().values.first() }
    val state = remember { CardScreenState().also { it.updateOffset(0.dp) } }
    CardScreen(membership, state = state)

}