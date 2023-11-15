package movie.metropolis.app.screen.purchase.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import kotlinx.coroutines.android.awaitFrame
import movie.style.layout.PreviewLayout
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Rotation
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Size
import java.util.concurrent.TimeUnit

@Composable
fun Confetti(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val parties = remember { festive() }
    Box(
        modifier = modifier,
        propagateMinConstraints = true
    ) {
        content()
        var isVisible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            awaitFrame()
            isVisible = true
        }
        if (isVisible) KonfettiView(modifier = Modifier.fillMaxSize(), parties = parties)
    }
}

private fun festive(): List<Party> {
    val party = Party(
        speed = 50f,
        maxSpeed = 70f,
        damping = 0.9f,
        angle = Angle.TOP,
        spread = 45,
        size = listOf(Size.SMALL, Size.MEDIUM, Size.LARGE),
        timeToLive = 5000L,
        rotation = Rotation(),
        colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
        emitter = Emitter(duration = 500, TimeUnit.MILLISECONDS).max(60),
        position = Position.Relative(0.5, 1.0),
        fadeOutEnabled = false
    )

    return listOf(
        party,
        party.copy(
            speed = 45f,
            maxSpeed = 65f,
            spread = 10,
            emitter = Emitter(duration = 500, TimeUnit.MILLISECONDS).max(20),
        ),
        party.copy(
            speed = 50f,
            maxSpeed = 70f,
            spread = 120,
            emitter = Emitter(duration = 500, TimeUnit.MILLISECONDS).max(80),
        ),
        party.copy(
            speed = 65f,
            maxSpeed = 80f,
            spread = 10,
            emitter = Emitter(duration = 500, TimeUnit.MILLISECONDS).max(20),
        )
    )
}

@Preview
@Composable
private fun ConfettiPreview() = PreviewLayout {
    Confetti(modifier = Modifier.fillMaxSize()) {
        Box {}
    }
}
