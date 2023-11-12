package movie.metropolis.app.screen2.movie.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.screen.listing.component.DefaultPosterAspectRatio
import movie.metropolis.app.screen2.listing.component.RatingBox
import movie.style.layout.PreviewLayout
import movie.style.modifier.LightSource
import movie.style.modifier.glow
import movie.style.modifier.surface
import movie.style.shape.CompositeShape
import movie.style.shape.CutoutShape
import movie.style.theme.Theme

@Composable
fun LargeMoviePoster(
    color: Color,
    contentColor: Color,
    order: @Composable () -> Unit,
    rating: @Composable () -> Unit,
    onOrderClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    var cutoutSize by remember { mutableStateOf(DpSize.Zero) }
    val actionSize = DpSize(56.dp, 56.dp)
    val baseline = Theme.container.poster
    val cornerSize = baseline.topStart
    val shape = CompositeShape(cutoutSize) {
        setBaseline(baseline)
        addShape(
            shape = CutoutShape(cornerSize, CutoutShape.Orientation.BottomRight),
            size = cutoutSize,
            alignment = Alignment.BottomEnd,
            operation = PathOperation.Difference
        )
        if (onOrderClick != null) addShape(
            shape = CutoutShape(cornerSize, CutoutShape.Orientation.TopLeft),
            size = actionSize,
            alignment = Alignment.TopStart,
            operation = PathOperation.Difference
        )
    }
    val containerColor = Theme.color.container.background
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .surface(containerColor, shape, 16.dp, color)
                .glow(shape, color)
                .aspectRatio(DefaultPosterAspectRatio),
            propagateMinConstraints = true
        ) {
            content()
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .onSizeChanged {
                    cutoutSize = with(density) { DpSize(it.width.toDp(), it.height.toDp()) }
                }
        ) {
            rating()
        }
        if (onOrderClick != null) Box(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.TopStart)
                .surface(color, Theme.container.button, 16.dp, color)
                .clickable(onClick = onOrderClick, role = Role.Button)
                .glow(Theme.container.button, contentColor, lightSource = LightSource.BottomRight)
                .padding(12.dp),
            propagateMinConstraints = true
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                order()
            }
        }
    }
}

@Composable
fun LargeRatingBox(
    color: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    rating: @Composable () -> Unit,
) = RatingBox(
    modifier = modifier,
    color = color,
    contentColor = contentColor,
    textStyle = Theme.textStyle.title,
    padding = PaddingValues(16.dp, 6.dp),
    offset = PaddingValues(top = 8.dp, start = 8.dp),
    shape = Theme.container.poster,
    rating = rating
)

@Preview(showBackground = true)
@Composable
private fun LargeMoviePosterPreview() = PreviewLayout {
    LargeMoviePoster(
        color = Color.Blue,
        contentColor = Color.White,
        order = { Icon(Icons.Rounded.ShoppingCart, null) },
        rating = {
            LargeRatingBox(
                color = Color.Blue,
                contentColor = Color.White
            ) {
                Text("86%")
            }
        },
        onOrderClick = {}
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color.Blue)
        )
    }
}