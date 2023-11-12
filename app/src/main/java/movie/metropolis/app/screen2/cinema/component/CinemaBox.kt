package movie.metropolis.app.screen2.cinema.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.screen.cinema.component.CinemaViewParameter
import movie.metropolis.app.screen2.listing.component.RatingBox
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.modifier.LightSource
import movie.style.modifier.glow
import movie.style.modifier.surface
import movie.style.rememberPaletteImageState
import movie.style.shape.CompositeShape
import movie.style.shape.CutoutShape
import movie.style.theme.Theme

@Composable
fun CinemaBox(
    color: Color,
    contentColor: Color,
    name: @Composable () -> Unit,
    distance: @Composable () -> Unit,
    poster: @Composable () -> Unit,
    action: @Composable () -> Unit,
    onClick: () -> Unit,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    var distanceSize by remember { mutableStateOf(DpSize.Zero) }
    var nameSize by remember { mutableStateOf(DpSize.Zero) }
    val favoriteSize = DpSize(36.dp, 36.dp)
    val baseline = Theme.container.poster
    val cornerSize = baseline.topStart
    val shape = CompositeShape(distanceSize, nameSize) {
        setBaseline(baseline)
        addShape(
            shape = CutoutShape(cornerSize, CutoutShape.Orientation.TopRight),
            size = distanceSize,
            alignment = Alignment.TopEnd,
            operation = PathOperation.Difference
        )
        addShape(
            shape = CutoutShape(cornerSize, CutoutShape.Orientation.TopLeft),
            size = favoriteSize,
            alignment = Alignment.TopStart,
            operation = PathOperation.Difference
        )
        addShape(
            shape = CutoutShape(cornerSize, CutoutShape.Orientation.BottomLeft),
            size = nameSize,
            alignment = Alignment.BottomStart,
            operation = PathOperation.Difference
        )
    }
    val containerColor = Theme.color.container.background
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .surface(containerColor, shape, 16.dp, color)
                .clickable(onClick = onClick, role = Role.Image)
                .glow(shape, color),
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
                .glow(CircleShape, color, lightSource = LightSource.Top)
                .padding(8.dp, 4.dp)
        ) {
            ProvideTextStyle(Theme.textStyle.caption.copy(fontWeight = FontWeight.Medium)) {
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    name()
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .onSizeChanged {
                    distanceSize = with(density) { DpSize(it.width.toDp(), it.height.toDp()) }
                }
        ) {
            distance()
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.TopStart)
                .surface(color, CircleShape, 16.dp, color)
                .clickable(onClick = onActionClick)
                .glow(
                    CircleShape,
                    lightSource = LightSource.BottomRight,
                    color = contentColor,
                    width = 2.dp,
                    alpha = .4f
                )
                .padding(6.dp),
            propagateMinConstraints = true
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                action()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CinemaBoxPreview(
    @PreviewParameter(CinemaViewParameter::class)
    parameter: CinemaView
) = PreviewLayout {
    val state = rememberPaletteImageState(url = parameter.image)
    CinemaBox(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 128.dp),
        color = state.palette.color,
        contentColor = state.palette.textColor,
        name = { Text(parameter.name) },
        distance = {
            val distance = parameter.distance
            if (distance != null) RatingBox(
                color = state.palette.color,
                contentColor = state.palette.textColor,
                rating = { Text(distance) },
                offset = PaddingValues(start = 4.dp, bottom = 4.dp)
            )
        },
        poster = { Image(state) },
        action = { Icon(Icons.Rounded.LocationOn, null) },
        onClick = {},
        onActionClick = {}
    )
}