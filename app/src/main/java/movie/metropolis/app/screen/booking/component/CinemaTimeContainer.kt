package movie.metropolis.app.screen.booking.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.modifier.surface
import movie.style.shape.CompositeShape
import movie.style.shape.CutoutShape
import movie.style.theme.Theme

@Composable
fun CinemaTimeContainer(
    color: Color,
    contentColor: Color,
    name: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    poster: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    var nameSize by remember { mutableStateOf(DpSize.Zero) }
    val baseline = Theme.container.poster
    val cornerSize = baseline.topStart
    val shape = CompositeShape(nameSize) {
        setBaseline(baseline)
        addShape(
            shape = CutoutShape(cornerSize, CutoutShape.Orientation.BottomLeft),
            size = nameSize,
            alignment = Alignment.BottomStart,
            operation = PathOperation.Difference
        )
    }
    val containerColor = Theme.color.container.background
    Box(modifier = modifier.height(100.dp)) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .surface(containerColor, shape, 16.dp, color),
            propagateMinConstraints = true
        ) {
            poster()
        }
        Box(
            modifier = Modifier
                .onSizeChanged {
                    nameSize = with(density) { DpSize(it.width.toDp(), it.height.toDp()) }
                }
                .padding(top = 4.dp, end = 4.dp)
                .align(Alignment.BottomStart)
                .surface(color, CircleShape, 16.dp, color)
                .padding(8.dp, 4.dp)
        ) {
            ProvideTextStyle(Theme.textStyle.caption.copy(fontWeight = FontWeight.Medium)) {
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    name()
                }
            }
        }
    }
}


@Preview
@Composable
private fun CinemaTimeContainerPreview() = PreviewLayout {
    CinemaTimeContainer(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Black,
        contentColor = Color.White,
        name = { Text("Cinema City") }
    ) {
        Box(Modifier.background(Color.Blue))
    }
}