package movie.metropolis.app.screen.booking.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.style.layout.PreviewLayout

@Suppress("InfiniteTransitionLabel", "InfinitePropertiesLabel")
@Composable
fun EmptyComponent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val transition = rememberInfiniteTransition()
        val offset by transition.animateValue(
            initialValue = 0.dp,
            targetValue = -(32).dp,
            typeConverter = Dp.VectorConverter,
            animationSpec = infiniteRepeatable(tween(1000), repeatMode = RepeatMode.Reverse)
        )
        val width by transition.animateValue(
            initialValue = 48.dp,
            targetValue = 56.dp,
            typeConverter = Dp.VectorConverter,
            animationSpec = infiniteRepeatable(tween(1000), repeatMode = RepeatMode.Reverse)
        )
        val height by transition.animateValue(
            initialValue = 2.dp,
            targetValue = 1.dp,
            typeConverter = Dp.VectorConverter,
            animationSpec = infiniteRepeatable(tween(1000), repeatMode = RepeatMode.Reverse)
        )
        val rotation by transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing))
        )
        Icon(
            modifier = Modifier
                .size(48.dp)
                .offset(y = offset)
                .rotate(rotation),
            painter = painterResource(id = R.drawable.ic_tumbleweed),
            contentDescription = null
        )
        Box(Modifier.height(2.dp)) {
            Box(
                Modifier
                    .width(width)
                    .height(height)
                    .background(LocalContentColor.current)
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun EmptyComponentPreview() = PreviewLayout {
    EmptyComponent()
}